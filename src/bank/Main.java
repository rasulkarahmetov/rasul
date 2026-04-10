package bank;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankService bank = new BankService();

        bank.loadData();

        while (true) {
            System.out.println("\n--- БАНК ---");
            System.out.println("1. Создать счет");
            System.out.println("2. Показать счета");
            System.out.println("3. Пополнить");
            System.out.println("4. Снять");
            System.out.println("5. Перевод");
            System.out.println("6. История");
            System.out.println("7. Начислить проценты");
            System.out.println("0. Выход");

            int choice = sc.nextInt();

            try {

                if (choice == 1) {
                    System.out.print("Имя: ");
                    String name = sc.next();

                    System.out.println("1 - Savings, 2 - Credit");
                    int type = sc.nextInt();

                    if (type == 1) {
                        bank.addAccount(new SavingsAccount(name, 0));
                    } else {
                        bank.addAccount(new CreditAccount(name, 0));
                    }

                } else if (choice == 2) {
                    for (Account acc : bank.accounts) {
                        System.out.println(acc.getOwner() + " | " + acc.getBalance() + " | " + acc.getType());
                    }

                } else if (choice == 3) {
                    System.out.print("Имя: ");
                    String name = sc.next();
                    System.out.print("Сумма: ");
                    double amount = sc.nextDouble();

                    bank.deposit(name, amount);

                } else if (choice == 4) {
                    System.out.print("Имя: ");
                    String name = sc.next();
                    System.out.print("Сумма: ");
                    double amount = sc.nextDouble();

                    bank.withdraw(name, amount);

                } else if (choice == 5) {
                    System.out.print("От кого: ");
                    String from = sc.next();

                    System.out.print("Кому: ");
                    String to = sc.next();

                    System.out.print("Сумма: ");
                    double amount = sc.nextDouble();

                    bank.transfer(from, to, amount);

                } else if (choice == 6) {
                    bank.showTransactions();

                } else if (choice == 7) {
                    System.out.print("Имя: ");
                    String name = sc.next();

                    Account acc = bank.findAccount(name);

                    if (acc instanceof SavingsAccount) {
                        System.out.print("Введите процент (например 0.05 = 5%): ");
                        double rate = sc.nextDouble();

                        ((SavingsAccount) acc).addInterest(rate);
                        System.out.println("Проценты начислены");
                    } else {
                        System.out.println("Это не savings счет");
                    }

                } else if (choice == 0) {
                    bank.saveData();
                    System.out.println("Данные сохранены. Выход...");
                    break;
                }

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}