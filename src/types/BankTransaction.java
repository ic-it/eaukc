package src.types;

public class BankTransaction extends ABankTransaction {
    public BankTransaction(int id, User userFrom, User userTo, int amount, int time, String comment)
    {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = amount;
        this.time = time;
        this.comment = comment;
    }
}
