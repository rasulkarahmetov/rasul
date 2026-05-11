package bank;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        BankService bank = new BankService();
        setTitle("ProBank Login");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel();
        p.setBackground(new Color(20, 20, 26));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("PRO BANK LOGIN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(0.5f);

        JTextField log = new JTextField();
        style(log);
        JPasswordField pas = new JPasswordField();
        style(pas);

        JButton btn = new JButton("ВОЙТИ");
        btn.setMaximumSize(new Dimension(400, 45));
        btn.setBackground(new Color(130, 87, 229));
        btn.setForeground(Color.WHITE);
        btn.setAlignmentX(0.5f);

        // КНОПКА ВОССТАНОВЛЕНИЯ ПАРОЛЯ
        JButton btnForgot = new JButton("Забыли пароль?");
        btnForgot.setForeground(Color.GRAY);
        btnForgot.setBorder(null);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setAlignmentX(0.5f);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (bank.authenticate(log.getText(), new String(pas.getPassword()))) {
                new Main(bank).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Неверный логин или пароль!");
            }
        });

        btnForgot.addActionListener(e -> {
            String secret = JOptionPane.showInputDialog(this, "Введите секретное слово :");
            if ("admin".equals(secret)) {
                if (!bank.users.isEmpty()) {
                    User u = bank.users.get(0);
                    JOptionPane.showMessageDialog(this, "Логин: " + u.login + "\nПароль: " + u.password);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Неверное слово!");
            }
        });

        p.add(title); p.add(Box.createRigidArea(new Dimension(0, 30)));
        p.add(log); p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(pas); p.add(Box.createRigidArea(new Dimension(0, 20)));
        p.add(btn); p.add(Box.createRigidArea(new Dimension(0, 15)));
        p.add(btnForgot);
        add(p);
    }

    private void style(JTextField f) {
        f.setMaximumSize(new Dimension(400, 40));
        f.setBackground(new Color(38, 40, 55));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}