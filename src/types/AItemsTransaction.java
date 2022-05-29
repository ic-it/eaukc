package src.types;

public abstract class AItemsTransaction 
{
    public int id;
    public AItem item;
    public ABankTransaction bankTransaction;

    
    /** 
     * This is the magical method for transferring to a string
     * 
     * @return String
     */
    public String toString()
    {
        return "ItemsTransaction[id: " + id + ", item: " + item + ", bankTransaction: " + bankTransaction + "]";
    }
}
