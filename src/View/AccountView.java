package View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Node.*;

public class AccountView {
    private IO io;

    public AccountView() {
        this.io = new IO();
    }

    public int userAuth() {
        String title = "E-Wallet KopiPaste";
        String[] Menu = { "Register", "Login", "Keluar" };
        int selectedOption = io.getMenuUserInput(title, Menu);
        io.cls();
        return selectedOption;
    }

    public int mainMenu() {
        String title = "HOME";
        String[] Menu = { "TopUp", "Transfer", "Payment", "History", "Profile", "Logout" };
        int selectedOption = io.getMenuUserInput(title, Menu);
        io.cls();
        return selectedOption;
    }

    public int paymentMenu() {
        String title = "PAYMENT";
        String[] Menu = { "PULSA", "TOKEN LISTRIK", "Back" };
        int selectedOption = io.getMenuUserInput(title, Menu);
        io.cls();
        return selectedOption;
    }

    public int pulsa(int[] amount) {
        String title = "PULSA";
        List<String> menu = Arrays.stream(amount)
                .mapToObj(i -> "Rp. " + i)
                .collect(Collectors.toList());
        menu.add("Cancel");
        int selectedOption = io.getMenuUserInput(title, menu.toArray(new String[0]));
        io.cls();
        return selectedOption;
    }

    public int tokenListrik(int[] amount) {
        String title = "TOKEN LISTRIK";
        List<String> menu = Arrays.stream(amount)
                .mapToObj(i -> "Rp. " + i)
                .collect(Collectors.toList());
        menu.add("Cancel");
        int selectedOption = io.getMenuUserInput(title, menu.toArray(new String[0]));
        io.cls();
        return selectedOption;
    }

    public void userWallet(Account user) {
        String leftAlignFormat = "| %-30s |%n";

        System.out.format("+--------------------------------+%n");
        System.out.format(leftAlignFormat, io.centerText(user.getPhone(), 30));
        System.out.format(leftAlignFormat, io.centerText(user.getName(), 30));
        System.out.format(leftAlignFormat, io.centerText("Rp. " + String.valueOf(user.getWallet().getBalance()), 30));
    }

    public void userHistory(ArrayList<Transaction> listTransaction) {
        String leftAlignFormat = "| %-30s |%n";

        System.out.format("+--------------------------------+%n");
        for (Transaction transaction : listTransaction) {
            System.out.format(leftAlignFormat, transaction.getId(), 30);
            System.out.format(leftAlignFormat, transaction.getAmount(), 30);
            System.out.format(leftAlignFormat, transaction.getTime(), 30);
            System.out.format(leftAlignFormat, transaction.getAction(), 30);
            System.out.format(leftAlignFormat, transaction.getType(), 30);
            System.out.format("+--------------------------------+%n");
            if (transaction.getName() != null) {
                System.out.format(leftAlignFormat, transaction.getName(), 30);
            }
        }

    }

    public int historyMenu() {
        String title = "HISTORY";
        String[] Menu = { "Lihat Semua History", "Lihat History Masuk", "Lihat History Keluar", "Back" };
        int selectedOption = io.getMenuUserInput(title, Menu);
        io.cls();
        return selectedOption;
    }

    public void userProfile(Account user) {
        String leftAlignFormat = "| %-30s |%n";
        System.out.format("+--------------------------------+%n");
        System.out.format(leftAlignFormat, io.centerText("Nama :" + user.getName(), 30));
        System.out.format(leftAlignFormat, io.centerText("Alamat: " + user.getAddress(), 30));
        System.out.format(leftAlignFormat, io.centerText("Email: " + user.getEmail(), 30));
        System.out.format(leftAlignFormat, io.centerText("Rp. " + String.valueOf(user.getWallet().getBalance()), 30));
        System.out.format("+--------------------------------+%n");
    }

    public int profileMenu() {
        String title = "PROFILE";
        String[] Menu = { "Edit Profile", "Cancel" };
        int selectedOption = io.getMenuUserInput(title, Menu);
        io.cls();
        return selectedOption;
    }

    public int editProfileMenu() {
        String title = "EDIT PROFILE";
        String[] Menu = { "Name", "Address", "Email", "Password", "Back" };
        int selectedOption = io.getMenuUserInput(title, Menu);
        io.cls();
        return selectedOption;
    }

    public void Bank(ArrayList<Bank> listBank) {
        for (Bank bank : listBank) {
            System.out.println((listBank.indexOf(bank) + 1) + ". " + bank.getCode() + " - " + bank.getName());
        }
    }
}
