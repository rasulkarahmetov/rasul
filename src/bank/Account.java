package bank;

public abstract class Account {
    protected String owner;
    protected String phone; // Новое поле
    protected double balance;

    public Account(String owner, String phone, double balance) {
        this.owner = owner;
        this.phone = phone;
        this.balance = balance;
    }

    public String getOwner() { return owner; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }
    public void deposit(double amount) { if (amount > 0) balance += amount; }
    public abstract void withdraw(double amount);
    public abstract String getType();
}