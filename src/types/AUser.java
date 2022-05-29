package src.types;

import java.util.ArrayList;

public abstract class AUser {
    public int id;
    public String name;
    public int money;
    public String code;

    
    /** 
     * This is the magical method for transferring to a string
     * 
     * @return String
     */
    public String toString()
    {
        return "User[id: " + id + ", name: " + name + ", money: " + money + ", code: " + code + "]";
    }

    
    /** 
     * Send Money to user
     * 
     * @param amount
     * @param userTo
     * @param comment
     * @return ABankTransaction
     */
    public ABankTransaction sendMoney(int amount, AUser userTo, String comment) 
    {
        return null;
    }

    
    /** 
     * Send Money to user
     * 
     * @param amount
     * @param userTo
     * @return ABankTransaction
     */
    public ABankTransaction sendMoney(int amount, AUser userTo)
    {
        return null;
    }

    /**
     * Save user
     */
    public void commit() { }

    /**
     * Update user from database
     */
    public void update() { }

    
    /** 
     * Get user BankTransactions
     * 
     * @return ArrayList<BankTransaction>
     */
    public ArrayList<BankTransaction> getBankTransactions() 
    {
        return null;
    }

    
    /** 
     * Get user items
     * 
     * @return ArrayList<Item>
     */
    public ArrayList<Item> getItems() 
    {
        return null;
    }
}
