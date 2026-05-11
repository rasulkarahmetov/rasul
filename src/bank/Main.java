package bank;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class Main extends JFrame {
    BankService bank;
    JTextField name = new JTextField(), phone = new JTextField(), amount = new JTextField();
    DefaultTableModel model;
    boolean balanceHidden = false;
    RoundButton btnAdd, btnDep, btnWith, btnCal, btnInterest, btnHistory, btnStats, btnSettings, btnHide;

    Color BG = new Color(20, 20, 26), SIDE = new Color(30, 30, 40), ACCENT = new Color(130, 87, 229);

    public Main(BankService bank) {
        this.bank = bank; bank.load();
        setTitle("ProBank Evolution 💎 Premium");
        setSize(1200, 850); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG); setLayout(new BorderLayout());

        // МЕНЮ
        JPanel side = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        side.setPreferredSize(new Dimension(240, 0)); side.setBackground(SIDE);
        side.setBorder(new EmptyBorder(20, 10, 10, 10));

        JLabel logo = new JLabel("💎 PRO BANK");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22)); logo.setForeground(ACCENT);
        side.add(logo);

        btnAdd = createMenuBtn("➕ Новый счет", ACCENT);
        btnDep = createMenuBtn("💰 Пополнение", new Color(60, 60, 80));
        btnWith = createMenuBtn("💸 Снять", new Color(60, 60, 80));
        btnCal = createMenuBtn("📅 График выплат", new Color(46, 204, 113));
        btnInterest = createMenuBtn("📈 % Начислить", new Color(46, 204, 113));
        btnHistory = createMenuBtn("📜 История", new Color(50, 100, 150));
        btnStats = createMenuBtn("📊 Статистика", new Color(150, 100, 50));
        btnHide = createMenuBtn("👁️ Скрыть баланс", new Color(100, 100, 100));
        btnSettings = createMenuBtn("⚙ Настройки", new Color(80, 80, 100));

        side.add(btnAdd); side.add(btnDep); side.add(btnWith); side.add(btnCal);
        side.add(btnInterest); side.add(btnHistory); side.add(btnStats); side.add(btnHide); side.add(btnSettings);
        add(side, BorderLayout.WEST);

        // ВЕРХ
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10)); top.setOpaque(false);
        styleField(name, 120); styleField(phone, 120); styleField(amount, 80);
        top.add(lbl("Имя:")); top.add(name); top.add(lbl("Тел:")); top.add(phone); top.add(lbl("Сумма:")); top.add(amount);
        add(top, BorderLayout.NORTH);

        // ТАБЛИЦА
        model = new DefaultTableModel(new String[]{"КЛИЕНТ", "ТЕЛЕФОН", "БАЛАНС", "ТИП"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(45); table.setBackground(SIDE); table.setForeground(Color.WHITE);
        JScrollPane sp = new JScrollPane(table); sp.setBorder(null); sp.getViewport().setBackground(BG);
        add(sp, BorderLayout.CENTER);

        // ЛОГИКА
        btnAdd.addActionListener(e -> {
            try {
                String n = name.getText(); String p = phone.getText(); double am = Double.parseDouble(amount.getText());
                Object[] ops = {"Savings", "Credit"};
                int res = JOptionPane.showOptionDialog(this, "Тип?", "Выбор", 0, 3, null, ops, ops[0]);
                if (res == 0) bank.add(new SavingsAccount(n, p, am));
                else if (res == 1) {
                    double r = Double.parseDouble(JOptionPane.showInputDialog("Годовых %:"));
                    int m = Integer.parseInt(JOptionPane.showInputDialog("Месяцев:"));
                    bank.add(new CreditAccount(n, p, -am, r, m));
                }
                bank.log("Создан счет: " + n); refreshTable("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Ошибка!"); }
        });

        btnHide.addActionListener(e -> { balanceHidden = !balanceHidden; refreshTable(""); });

        btnDep.addActionListener(e -> {
            Account a = bank.find(name.getText());
            if (a != null) {
                double v = Double.parseDouble(amount.getText()); a.deposit(v);
                bank.log("Депозит: " + a.getOwner() + " +" + v); bank.save(); refreshTable("");
            }
        });

        btnCal.addActionListener(e -> {
            Account a = bank.find(name.getText());
            if (a instanceof CreditAccount) {
                CreditAccount c = (CreditAccount) a;
                double total = Math.abs(c.getBalance()) * (1 + (c.getRate()/100.0 * c.getMonths()/12.0));
                JOptionPane.showMessageDialog(this, "К оплате: " + String.format("%.2f", total) + " ₽\nДата: " + LocalDate.now().plusMonths(c.getMonths()));
            }
        });

        btnHistory.addActionListener(e -> {
            JTextArea area = new JTextArea(20, 40); for (String h : bank.history) area.append(h + "\n");
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "История", 1);
        });

        btnStats.addActionListener(e -> {
            double t = 0; for (Account a : bank.accounts) t += a.getBalance();
            JOptionPane.showMessageDialog(this, "Капитал: " + String.format("%.2f", t) + " ₽");
        });

        btnInterest.addActionListener(e -> {
            String v = JOptionPane.showInputDialog("Процент:");
            if(v != null) { bank.applyInterest(Double.parseDouble(v)); refreshTable(""); }
        });

        btnSettings.addActionListener(e -> {
            JDialog d = new JDialog(this, "Settings", true); d.setSize(300, 200); d.setLayout(new FlowLayout());
            JTextField l = new JTextField(bank.currentUser.login, 15), p = new JTextField(bank.currentUser.password, 15);
            JButton s = new JButton("Save"); s.addActionListener(al -> { bank.updateUserInfo(l.getText(), p.getText()); d.dispose(); });
            d.add(new JLabel("Login:")); d.add(l); d.add(new JLabel("Pass:")); d.add(p); d.add(s); d.setVisible(true);
        });

        refreshTable("");
    }

    private void refreshTable(String f) {
        model.setRowCount(0);
        for (Account a : bank.accounts) {
            String bal = balanceHidden ? "****" : String.format("%.2f", a.getBalance());
            if (a.getOwner().toLowerCase().contains(f.toLowerCase()))
                model.addRow(new Object[]{a.getOwner(), a.getPhone(), bal, a.getType()});
        }
    }
    private RoundButton createMenuBtn(String t, Color c) {
        RoundButton b = new RoundButton(t, 20); b.setPreferredSize(new Dimension(210, 40));
        b.setBackground(c); b.setFont(new Font("Segoe UI", Font.PLAIN, 13)); return b;
    }
    private void styleField(JTextField f, int w) {
        f.setPreferredSize(new Dimension(w, 35)); f.setBackground(SIDE); f.setForeground(Color.WHITE); f.setBorder(null);
    }
    private JLabel lbl(String t) { JLabel l = new JLabel(t); l.setForeground(Color.GRAY); return l; }
}