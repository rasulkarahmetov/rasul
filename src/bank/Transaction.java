package bank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String text;
    private String time;

    public Transaction(String text) {
        this.text = text;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String toString() { return "[" + time + "] " + text; }
}