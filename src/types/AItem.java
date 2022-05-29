package src.types;

import java.util.ArrayList;

public abstract class AItem {
    public int id;
    public AUser owner;
    public String name;
    public String info;

    
    /** 
     * This is the magical method for transferring to a string
     * 
     * @return String
     */
    public String toString()
    {
        return "User[id: " + id + ", owner: " + owner + ", name: " + name + ", info: " + info + "]";
    }

    
    /** 
     * Sell item to buyer
     * 
     * @param buyer
     * @param amount
     * @return AItemsTransaction
     */
    public AItemsTransaction sell(AUser buyer, int amount)
    {
        return null;
    }

    /**
     * Save item to database
     */
    public void commit() {}

    
    /** 
     * Learn all the translations of the subject
     * 
     * @return ArrayList<ItemsTransaction>
     */
    public ArrayList<ItemsTransaction> getTransactions()
    {
        return null;
    }
}
