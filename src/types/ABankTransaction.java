package src.types;


/**
 * Abstract class for bank transactions
 */
public abstract class ABankTransaction {
    public int id;
    public AUser userFrom;
    public AUser userTo;
    public int amount;
    public int time;
    public String comment;

    
    /** 
     * This is the magical method for transferring to a string 
     * 
     * @return String
     */
    public String toString()
    {
        return "BankTransaction[id: " + id + ", userFromID: " + userFrom.id + ", userToID: " + userTo.id + ", amount: " + amount + ", time: " + time + ", comment: " + comment + "]";
    }
}
