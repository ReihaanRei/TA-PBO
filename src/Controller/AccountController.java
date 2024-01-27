package Controller;

import java.util.ArrayList;

import Controller.libs.*;
import Controller.libs.Transaction.TransactionType;
import Model.Data.*;
import Node.*;
import View.*;

public class AccountController {

    ModelAccount modelAccount = new ModelAccount();
    AccountView menu = new AccountView();
    IO io = new IO();

    Account user;
    HistoryHandler historyHandler;

    public void userAuth() {
        Auth auth = new Auth();
        while (user == null) {
            int selectedOption = menu.userAuth();
            switch (selectedOption) {
                case 1:
                    auth.register(modelAccount);
                    break;
                case 2:
                    user = auth.login(modelAccount);
                    historyHandler = new HistoryHandler(user, modelAccount);
                    if (user.getName() == null) {
                        String name = io.getUserInput("Masukkan Nama Anda : ");
                        while (name.isEmpty()) {
                            name = io.getUserInput("Masukkan Nama Anda : ");
                        }
                        user.setName(name);
                        modelAccount.updateAccount(user);
                    }
                    if (user.getAddress() == null) {
                        String address = io.getUserInput("Masukkan Alamat Anda : ");
                        while (address.isEmpty()) {
                            address = io.getUserInput("Masukkan Alamat Anda : ");
                        }
                        user.setAddress(address);
                        modelAccount.updateAccount(user);
                    }
                    if (user.getEmail() == null) {
                        String email = io.getUserInput("Masukkan Email Anda : ");
                        while (email.isEmpty()) {
                            email = io.getUserInput("Masukkan Email Anda : ");
                        }
                        user.setEmail(email);
                        modelAccount.updateAccount(user);
                    }
                    userMenu();
                    break;
                case 3:
                    return;
                default:
                    break;
            }
        }
    }

    public void userMenu() {
        while (true) {
            menu.userWallet(user);
            int selectedOption = menu.mainMenu();
            switch (selectedOption) {
                case 1:
                    topUp();
                    break;
                case 2:
                    transfer();
                    break;
                case 3:
                    payment();
                    break;
                case 4:
                    history();
                    break;
                case 5:
                    profile();
                    break;
                case 6:
                    user = null;
                    historyHandler = null;
                    return;
                default:
                    break;
            }
        }
    }

    public void topUp() {
        io.print("TOPUP");
        double amount = Double.parseDouble(io.getUserInput("Masukkan nominal topup : "));

        PaymentMethod payment = new PaymentMethod();
        menu.Bank(payment.getModelBank().getListBank());
        int bank = Integer.parseInt(io.getUserInput("Pilih Bank : "));
        if (bank > 0 && bank <= payment.getModelBank().getListBank().size()) {

            System.out.println(
                    "Virtual Account : " + payment.getModelBank().getBank(bank - 1).getCode() + user.getPhone());

            String accept = io.getUserInput("Apakah anda yakin ingin melanjutkan? (Y/N) ");
            if (accept.equals("Y") || accept.equals("y")) {
                Transaction topUp = new Transaction(String.valueOf(user.getHistory().getListTransaction().size() + 1),
                        amount);
                topUp.topUp();

                historyHandler.execute(topUp);
            }
        } else {
            io.print("Bank Tidak Tersedia");
        }
    }

    public void transfer() {
        io.print("TRANSFER");
        String phone;
        int userid;
        do {
            phone = io.getUserInput("Masukkan penerima : ");
            userid = modelAccount.searchAccount(phone);
            if (userid != -1) {
                double amount = Double.parseDouble(io.getUserInput("Masukkan nominal transfer : "));
                Balance balance = new Balance(user);

                if (!balance.validateBalance(amount)) {
                    io.print("Saldo tidak mencukupi");
                    continue;
                }

                Transaction sender = new Transaction(String.valueOf(user.getHistory().getListTransaction().size() + 1),
                        amount);
                sender.transferSender();
                historyHandler.execute(sender);

                Transaction receiver = new Transaction(
                        String.valueOf(modelAccount.getAccount(userid).getHistory().getListTransaction().size() + 1),
                        amount);
                receiver.transferReceiver();
                HistoryHandler his = new HistoryHandler(modelAccount.getAccount(userid), modelAccount);
                his.execute(receiver);
            } else {
                io.print("Nomor telepon tidak ditemukan");
            }
        } while (userid == -1);
    }

    public void payment() {
        io.print("PAYMENT");
        int selectedOption = menu.paymentMenu();
        Balance duwit = new Balance(user);
        switch (selectedOption) {
            case 1:
                String phone = io.getUserInput("Masukkan nomor telepon : ");
                int[] pulsa = { 5000, 10000, 20000, 50000, 100000 };
                int pulsaSelector = menu.pulsa(pulsa);
                double amount = pulsa[pulsaSelector - 1];
                if (amount != 0 && duwit.validateBalance(amount)) {
                    Transaction tumbas = new Transaction(
                            String.valueOf(user.getHistory().getListTransaction().size() + 1), amount);
                    historyHandler.execute(tumbas);
                    tumbas.payment();
                } else if (!duwit.validateBalance(amount)) {
                    io.print("Saldo tidak mencukupi");
                }
                break;
            case 2:
                String meteran = io.getUserInput("Masukkan nomor meteran : ");
                int[] jumlah = { 20000, 50000, 100000, 200000, 1000000 };
                int plnSelector = menu.tokenListrik(jumlah);
                amount = jumlah[plnSelector - 1];
                if (amount != 0 && duwit.validateBalance(amount)) {
                    Transaction tombwas = new Transaction(
                            String.valueOf(user.getHistory().getListTransaction().size() + 1), amount);
                    io.print("Kode token : " + tombwas.payment(meteran));
                    historyHandler.execute(tombwas);
                } else if (!duwit.validateBalance(amount)) {
                    io.print("Saldo tidak mencukupi");
                }
                break;
        }
    }

    public void history() {
        io.print("HISTORY");
        int selectedOption = menu.historyMenu();
        ArrayList<Transaction> listTransaction = new ArrayList<>();

        switch (selectedOption) {
            case 1:
                menu.userHistory(user.getHistory().getListTransaction());
                break;
            case 2:
                listTransaction = filterTransactionsByType(user.getHistory().getListTransaction(),
                        TransactionType.INCOME);
                menu.userHistory(listTransaction);
                break;
            case 3:
                listTransaction = filterTransactionsByType(user.getHistory().getListTransaction(),
                        TransactionType.OUTCOME);
                menu.userHistory(listTransaction);
                break;
            case 4:
                return;
            default:
                break;
        }
    }

    private ArrayList<Transaction> filterTransactionsByType(ArrayList<Transaction> transactions, TransactionType type) {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals(type)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    // public void confirm() {
    // io.
    // }

    public void profile() {
        int selectedOption = menu.profileMenu();
        do {
            menu.userProfile(user);
            selectedOption = menu.profileMenu();
            int editOption = menu.editProfileMenu();
            switch (editOption) {
                case 1:
                    String nama;
                    boolean konfirmasi;
                    do {
                        nama = io.getUserInput("Masukkan nama baru : ");
                        konfirmasi = io.getUserInput("Ketik Y untuk mengkonfirmasi : ").equalsIgnoreCase("Y");
                    } while (!konfirmasi);
                    user.setName(nama);
                    io.print("Nama berhasil diubah");
                    break;
                case 2:
                    String address;
                    konfirmasi = false;
                    do {
                        address = io.getUserInput("Masukkan alamat baru : ");
                        konfirmasi = io.getUserInput("Ketik Y untuk mengkonfirmasi : ").equalsIgnoreCase("Y");
                    } while (!konfirmasi);
                    user.setAddress(address);
                    io.print("Alamat berhasil diubah");
                    break;
                case 3:
                    String email;
                    konfirmasi = false;
                    do {
                        email = io.getUserInput("Masukkan email baru : ");
                        konfirmasi = io.getUserInput("Ketik Y untuk mengkonfirmasi : ").equalsIgnoreCase("Y");
                    } while (!konfirmasi);
                    user.setEmail(email);
                    io.print("Email berhasil diubah");
                    break;
                case 4:
                    String pass = io.getPasswordInput("Masukkan password baru : ");
                    String verif;
                    int tryAttempt;
                    while (pass.length() < 8 || !containLetterAndDigit(pass)) {
                        io.print("password harus lebih dari 8 karakter dan terdiri dari huruf dan angka");
                        pass = io.getPasswordInput("Masukkan password baru : ");
                    }

                    do {
                        verif = io.getPasswordInput("Repeat password : ");
                        tryAttempt = +1;
                    } while(!verif.equals(pass) || tryAttempt<4);
                    if (verif.equals(pass)) {
                        user.setPassword(pass);
                        io.print("Password berhasil diubah");
                    } else{
                        io.print("Gak jadi ganti password, lu-nya bingung");
                    }
                    break;
            }
        } while (selectedOption == 1);
        modelAccount.commitData();
    }
    
    private boolean containLetterAndDigit(String pass) {
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char x : pass.toCharArray()) {
            if (Character.isLetter(x)) {
                hasLetter = true;
            } else if (Character.isDigit(x)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                break;
            }
        }
        return hasLetter && hasDigit;
    }

}
