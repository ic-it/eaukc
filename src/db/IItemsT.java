package src.db;

import java.util.ArrayList;

import src.types.Item;
import src.types.ItemsTransaction;
import src.types.User;

public interface IItemsT {
    
    public void saveItem(Item item);
    public Item newItem(User user, String name, String info);
    public Item getItem(int id);
    public Item getRandItem();
    public ArrayList<Item> getUserItems(User u);
    public ItemsTransaction sellItem(Item item, User buyer, int amount);
    public ArrayList<ItemsTransaction> getItemTransactions(Item item);

    public default boolean isItemsT() 
    {
        return false;
    }
}
