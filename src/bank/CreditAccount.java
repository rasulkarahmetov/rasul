package bank;
import java.time.LocalDate;

public class CreditAccount extends Account {
    private double rate;
    private int months; // Заменили days на months
    private LocalDate startDate;

    public CreditAccount(String owner, String phone, double balance, double rate, int months) {
        super(owner, phone, balance);
        this.rate = rate;
        this.months = months;
        this.startDate = LocalDate.now();
    }

    public double getRate() { return rate; }
    public int getMonths() { return months; }
    public LocalDate getStartDate() { return startDate; }

    @Override
    public void withdraw(double amount) { balance -= amount; }
    @Override
    public String getType() { return "Credit"; }
}