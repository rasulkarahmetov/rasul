package bank;
import java.io.*;
import java.util.*;

public class BankService {
    public ArrayList<Account> accounts = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<String> history = new ArrayList<>(); // Добавили список истории
    public User currentUser;

    public BankService() {
        loadUsers();
        loadHistory();
        if (users.isEmpty()) users.add(new User("admin", "1234"));
    }

    public boolean authenticate(String l, String p) {
        for (User u : users) if (u.login.equals(l) && u.password.equals(p)) { currentUser = u; return true; }
        return false;
    }

    public void log(String message) {
        String entry = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        ) + " | " + message;
        history.add(entry);
        saveHistory(entry);
    }

    public void applyInterest(double rate) {
        for (Account a : accounts) {
            if (a instanceof SavingsAccount) {
                double bonus = a.getBalance() * (rate / 100);
                a.deposit(bonus);
                log("Начислен %: " + a.getOwner() + " (+" + String.format("%.2f", bonus) + ")");
            }
        }
        save();
    }

    public void updateUserInfo(String nl, String np) {
        if (currentUser != null) { currentUser.login = nl; currentUser.password = np; saveUsers(); }
    }

    public void add(Account a) { accounts.add(a); save(); }
    public Account find(String n) {
        for (Account a : accounts) if (a.getOwner().equalsIgnoreCase(n.trim())) return a;
        return null;
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter("data.txt")) {
            for (Account a : accounts) {
                if (a instanceof CreditAccount) {
                    CreditAccount c = (CreditAccount) a;
                    pw.println("CREDIT," + c.getOwner() + "," + c.getPhone() + "," + c.getBalance() + "," + c.getRate() + "," + c.getMonths());
                } else {
                    pw.println("SAVINGS," + a.getOwner() + "," + a.getPhone() + "," + a.getBalance());
                }
            }
        } catch (Exception e) {}
    }

    public void load() {
        File f = new File("data.txt"); if (!f.exists()) return;
        accounts.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p[0].equals("CREDIT")) accounts.add(new CreditAccount(p[1], p[2], Double.parseDouble(p[3]), Double.parseDouble(p[4]), Integer.parseInt(p[5])));
                else accounts.add(new SavingsAccount(p[1], p[2], Double.parseDouble(p[3])));
            }
        } catch (Exception e) {}
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter("users.txt")) {
            for (User u : users) pw.println(u.login + "," + u.password);
        } catch (Exception e) {}
    }

    private void loadUsers() {
        File f = new File("users.txt"); if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l; while ((l = br.readLine()) != null) { String[] p = l.split(","); users.add(new User(p[0], p[1])); }
        } catch (Exception e) {}
    }

    private void saveHistory(String entry) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("history.txt", true))) { pw.println(entry); } catch (Exception e) {}
    }

    private void loadHistory() {
        File f = new File("history.txt"); if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l; while ((l = br.readLine()) != null) history.add(l);
        } catch (Exception e) {}
    }
}