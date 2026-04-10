package bank;

public class CreditAccount extends Account {

    public CreditAccount(String owner, double balance) {
        super(owner, balance);
    }

    @Override
    public void withdraw(double amount) {
        balance -= amount; // можно уходить в минус
    }

    @Override
    public String getType() {
        return "Credit";
    }
}