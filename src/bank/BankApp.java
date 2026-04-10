package bank;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BankApp extends JFrame {

    private final BankService bank = new BankService();

    private final JTextField nameField = new JTextField();
    private final JTextField amountField = new JTextField();

    private final DefaultTableModel model;

    public BankApp() {

        setTitle("🏦 Bank System PRO");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        bank.loadData();

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Данные"));

        topPanel.add(new JLabel("Имя:"));
        topPanel.add(new JLabel("Сумма:"));
        topPanel.add(new JLabel("Поиск:"));

        topPanel.add(nameField);
        topPanel.add(amountField);
        JTextField searchField = new JTextField();
        topPanel.add(searchField);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{"Имя", "Баланс", "Тип"}, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttons = new JPanel(new GridLayout(2, 4, 10, 10));

        JButton createBtn = new JButton("Создать");
        JButton depositBtn = new JButton("Пополнить");
        JButton withdrawBtn = new JButton("Снять");
        JButton transferBtn = new JButton("Перевод");
        JButton interestBtn = new JButton("Проценты");
        JButton deleteBtn = new JButton("Удалить");
        JButton showBtn = new JButton("Обновить");
        JButton historyBtn = new JButton("История");

        buttons.add(createBtn);
        buttons.add(depositBtn);
        buttons.add(withdrawBtn);
        buttons.add(transferBtn);
        buttons.add(interestBtn);
        buttons.add(deleteBtn);
        buttons.add(showBtn);
        buttons.add(historyBtn);

        add(buttons, BorderLayout.SOUTH);

        // ===== ACTIONS =====

        createBtn.addActionListener(_ -> {
            String name = nameField.getText();

            String[] options = {"Savings", "Credit"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Выберите тип счета",
                    "Тип",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                bank.addAccount(new SavingsAccount(name, 0));
            } else if (choice == 1) {
                bank.addAccount(new CreditAccount(name, 0));
            }

            refresh();
        });

        depositBtn.addActionListener(_ -> {
            bank.deposit(nameField.getText(), Double.parseDouble(amountField.getText()));
            refresh();
        });

        withdrawBtn.addActionListener(_ -> {
            bank.withdraw(nameField.getText(), Double.parseDouble(amountField.getText()));
            refresh();
        });

        transferBtn.addActionListener(_ -> {
            String from = nameField.getText();
            String to = JOptionPane.showInputDialog(this, "Кому перевести:");
            double amount = Double.parseDouble(amountField.getText());

            bank.transfer(from, to, amount);
            refresh();
        });

        interestBtn.addActionListener(_ -> {
            String name = nameField.getText();
            Account acc = bank.findAccount(name);

            if (acc instanceof SavingsAccount) {
                String rateStr = JOptionPane.showInputDialog(this, "Процент (например 0.05 = 5%)");
                double rate = Double.parseDouble(rateStr);

                ((SavingsAccount) acc).addInterest(rate);
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Не Savings счет");
            }
        });

        deleteBtn.addActionListener(_ -> {
            Account acc = bank.findAccount(nameField.getText());
            if (acc != null) {
                bank.accounts.remove(acc);
                refresh();
            }
        });

        showBtn.addActionListener(_ -> refresh());

        historyBtn.addActionListener(_ -> bank.showTransactions());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                bank.saveData();
            }
        });

        refresh();
    }

    private void refresh() {
        model.setRowCount(0);

        for (Account acc : bank.accounts) {
            model.addRow(new Object[]{
                    acc.getOwner(),
                    acc.getBalance(),
                    acc.getType()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankApp().setVisible(true));
    }
}