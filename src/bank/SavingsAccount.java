package bank;

public class SavingsAccount extends Account {
    // Теперь конструктор принимает 3 параметра и передает их в Account
    public SavingsAccount(String owner, String phone, double balance) {
        super(owner, phone, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    @Override
    public String getType() {
        return "Savings";
    }
}