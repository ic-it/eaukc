package src.types;

import java.util.ArrayList;
import src.db.IUsersT;
import src.db.ItemsT;


public class User extends AUser
{
    IUsersT ut;

    public User(int id, String name, int money, String code, IUsersT ut)
    {
        this.id = id;
        this.name = name;
        this.money = money;
        this.code = code;
        
        this.ut = ut;
    }

    public void update()
    {
        AUser u = ut.getUser(id);
        name     = u.name;
        money    = u.money;
        code     = u.code;
    }

    public void commit()
    {
        ut.saveUser(this);
    }

    
    /** 
     * @param amount
     * @param userTo
     * @param comment
     * @return ABankTransaction
     */
    public ABankTransaction sendMoney(int amount, AUser userTo, String comment)
    {
        if (money < amount)
            return null;

        money -= amount;
        userTo.money += amount;

        this.commit();
        userTo.commit();
        
        return ut.sendMoney(this, (User)userTo, amount, comment);
    }
    
    
    /** 
     * @param amount
     * @param userTo
     * @return ABankTransaction
     */
    public ABankTransaction sendMoney(int amount, AUser userTo)
    {
        return sendMoney(amount, userTo, "SEND MONEY");
    }

    
    /** 
     * @return ArrayList<BankTransaction>
     */
    public ArrayList<BankTransaction> getBankTransactions()
    {
        return ut.getUserBankTransactions(this);
    }
    
    
    /** 
     * @return ArrayList<Item>
     */
    public ArrayList<Item> getItems()
    {
        return ItemsT.getInstance().getUserItems(this);
    }
}
