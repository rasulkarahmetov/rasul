package bank;

import java.io.*;
import java.util.ArrayList;

public class BankService {

    public ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<String> transactions = new ArrayList<>();

    // добавить счет
    public void addAccount(Account acc) {
        accounts.add(acc);
    }

    // найти счет
    public Account findAccount(String owner) {
        for (Account acc : accounts) {
            if (acc.getOwner().equals(owner)) {
                return acc;
            }
        }
        return null;
    }

    // пополнение
    public void deposit(String owner, double amount) {
        Account acc = findAccount(owner);
        if (acc != null) {
            acc.deposit(amount);
            transactions.add("Пополнение: " + owner + " +" + amount);
        }
    }

    // снятие
    public void withdraw(String owner, double amount) {
        Account acc = findAccount(owner);
        if (acc != null) {
            acc.withdraw(amount);
            transactions.add("Снятие: " + owner + " -" + amount);
        }
    }

    // ===== ПЕРЕВОД =====
    public void transfer(String from, String to, double amount) {
        Account accFrom = findAccount(from);
        Account accTo = findAccount(to);

        if (accFrom != null && accTo != null) {
            accFrom.withdraw(amount);
            accTo.deposit(amount);

            transactions.add("Перевод: " + from + " -> " + to + " : " + amount);
        }
    }

    // ===== ИСТОРИЯ =====
    public void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("История пуста");
            return;
        }

        for (String t : transactions) {
            System.out.println(t);
        }
    }

    // ===== ПОКАЗ СЧЕТОВ =====
    public void showAccounts() {
        for (Account acc : accounts) {
            System.out.println(acc.getOwner() + " | " + acc.getBalance() + " | " + acc.getType());
        }
    }

    // ===== SAVE =====
    public void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("data.txt"))) {
            for (Account acc : accounts) {
                pw.println(acc.getOwner() + "," + acc.getBalance() + "," + acc.getType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===== LOAD =====
    public void loadData() {
        File file = new File("data.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String name = parts[0];
                double balance = Double.parseDouble(parts[1]);
                String type = parts[2];

                if (type.equals("Savings")) {
                    accounts.add(new SavingsAccount(name, balance));
                } else {
                    accounts.add(new CreditAccount(name, balance));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}