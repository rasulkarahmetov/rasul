package bank;

public class SavingsAccount extends Account {

    public SavingsAccount(String owner, double balance) {
        super(owner, balance);
    }

    // теперь процент вводится снаружи
    public void addInterest(double rate) {
        balance = balance + (balance * rate);
    }

    @Override
    public String getType() {
        return "Savings";
    }
}