package src.types;

public class ItemsTransaction extends AItemsTransaction
{
    public ItemsTransaction(int id, Item item, BankTransaction bankTransaction)
    {
        this.id = id;
        this.item = item;
        this.bankTransaction = bankTransaction;
    }
}
