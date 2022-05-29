package src.db;

import java.sql.SQLException;
import java.util.ArrayList;

import src.types.Item;
import src.types.ItemsTransaction;
import src.types.User;
import src.utils.GetSet;
import src.types.BankTransaction;

public class ItemsT implements IItemsT
{
    private static ItemsT instance = null;
    private DataBase db;
    private UsersT ut;

    private ItemsT()
    {
        db = DataBase.getInstance();
        ut = UsersT.getInstance();
    }

    
    /** 
     * Singleton for Items Tablse
     * @return ItemsT
     */
    public static ItemsT getInstance()
    {
        if (instance == null)
            instance = new ItemsT();
        return instance;
    }

    
    /** 
     * Create new item 
     * 
     * @param user
     * @param name
     * @param info
     * @return Item
     */
    public Item newItem(User user, String name, String info)
    {
        GetSet<Integer> newItemId = new GetSet<Integer>();

        db.executeQuery("INSERT INTO items (owner_id, name, info) VALUES (?, ?, ?) RETURNING id", ps -> {
            try 
            {
                ps.setInt(1, user.id);
                ps.setString(2, name);
                ps.setString(3, info);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                newItemId.set(rs.getInt(1));
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        
        db.executeUpdate("INSERT INTO items_transactions (item_id, bank_transaction_id) " +
                         "VALUES (?, -1)", ps -> {
            try 
            {
                ps.setInt(1, newItemId.get());
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        });
        return getItem(newItemId.get());
    }


    /** 
     * Get item by id
     * 
     * @param id
     * @return Item
     */
    public Item getItem(int id)
    {
        GetSet<Item> item = new GetSet<Item>();
        db.executeQuery("SELECT owner_id, name, info FROM items WHERE id = ?;", ps -> {
            try 
            {
                ps.setInt(1, id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                item.set(
                    new Item(id, rs.getInt(1), rs.getString(2), rs.getString(3), this)
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return item.get();
    }

    
    /** 
     * Get random item
     * 
     * @return Item
     */
    public Item getRandItem()
    {
        GetSet<Item> item = new GetSet<Item>();
        db.executeQuery("SELECT owner_id, name, info FROM items ORDER BY random() LIMIT 1;", ps -> {}, rs -> {
            try 
            {
                item.set(
                    new Item(rs.getInt(1), rs.getInt(1), rs.getString(2), rs.getString(3), this)
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return item.get();
    }

    
    /** 
     * Save item
     * 
     * @param item
     */
    public void saveItem(Item item)
    {
        db.executeUpdate("UPDATE items " + 
                         "SET owner_id = ?, name = ?" +
                         "WHERE id = ?;", ps -> {
            try 
            {
                ps.setInt(1, item.owner.id);
                ps.setString(2, item.name);
                ps.setInt(3, item.id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        });
    }

    
    /** 
     * Send item
     * 
     * @param bTransaction
     * @param item
     * @return ItemsTransaction
     */
    public ItemsTransaction sendItem(BankTransaction bTransaction, Item item)
    {
        GetSet<ItemsTransaction> newTr = new GetSet<>();
        db.executeQuery("INSERT INTO items_transactions (item_id, bank_transaction_id) " +
                         "VALUES (?, ?) RETURNING id", ps -> {
            try 
            {
                ps.setInt(1, item.id);
                ps.setInt(2, bTransaction.id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try {
                newTr.set(
                    new ItemsTransaction(rs.getInt(1), item, bTransaction)
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
        return newTr.get();
    }

    
    /** 
     * Sell item (send cash and send item)
     * 
     * 
     * @param item
     * @param buyer
     * @param amount
     * @return ItemsTransaction
     */
    public ItemsTransaction sellItem(Item item, User buyer, int amount)
    {
        BankTransaction bt = (BankTransaction) buyer.sendMoney(amount, item.owner, "SELL: " + item.name);

        if (bt == null)
            return null;
        
        ItemsTransaction it = sendItem(bt, item);

        item.owner = buyer;
        item.commit();
        return it;
    }

    
    /** 
     * Get user items
     * 
     * @param u
     * @return ArrayList<Item>
     */
    public ArrayList<Item> getUserItems(User u)
    {
        ArrayList<Item> items = new ArrayList<>();

        db.executeQuery("SELECT id, owner_id, name, info FROM items WHERE owner_id = ?;", ps -> {
            try 
            {
                ps.setInt(1, u.id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                items.add(
                    new Item(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), this)
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return items;
    }

    
    /** 
     * Get item trnsactions 
     * 
     * @param item
     * @return ArrayList<ItemsTransaction>
     */
    public ArrayList<ItemsTransaction> getItemTransactions(Item item)
    {

        ArrayList<ItemsTransaction> it = new ArrayList<>();
        String sql =    "SELECT bank_transactions.id, bank_transactions.user_from_id, bank_transactions.user_to_id, bank_transactions.amount, bank_transactions.time, bank_transactions.comment, items_transactions.id " +
                        "FROM items_transactions " +
                        "INNER JOIN bank_transactions ON bank_transactions.id = items_transactions.bank_transaction_id " +
                        "WHERE items_transactions.item_id = ?;";
        db.executeQuery(sql, ps -> {
        try 
        {
            ps.setInt(1, item.id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                it.add(
                    new ItemsTransaction(
                        rs.getInt(7),
                        item,
                        new BankTransaction(
                            rs.getInt(1), 
                            ut.getUser(rs.getInt(2)),
                            ut.getUser(rs.getInt(3)),
                            rs.getInt(4), 
                            rs.getInt(5), 
                            rs.getString(6)
                        )
                    )
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });

        return it;
    }
}
