package src.db;

import java.util.ArrayList;

import src.types.BankTransaction;
import src.types.User;

public interface IUsersT {

    public User newUser(String name, int money);

    public User getUser(int id);
    public User getUser(String code);
    public void saveUser(User user);
    public User getRandUser();

    public BankTransaction sendMoney(User userFrom, User userTo, int amount, String comment);
    public BankTransaction sendMoney(User userFrom, User userTo, int amount);

    public ArrayList<BankTransaction> getUserBankTransactions(User user);
    public ArrayList<User> getUsers();
}
