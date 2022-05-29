package src.types;

import java.util.ArrayList;

import src.db.IItemsT;
import src.db.UsersT;

public class Item extends AItem
{
    IItemsT it;

    public Item(int id, int owner_id, String name, String info, IItemsT it)
    {
        this.id = id;
        this.owner = UsersT.getInstance().getUser(owner_id);
        this.name = name;
        this.info = info;

        this.it = it;
    }

    
    /** 
     * @param buyer
     * @param amount
     * @return AItemsTransaction
     */
    public AItemsTransaction sell(User buyer, int amount)
    {
        return it.sellItem(this, buyer, amount);
    }

    public void commit()
    {
        it.saveItem(this);
    }

    
    /** 
     * @return ArrayList<ItemsTransaction>
     */
    public ArrayList<ItemsTransaction> getTransactions()
    {
        return it.getItemTransactions(this);
    }

}
