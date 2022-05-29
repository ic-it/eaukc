package src.db;

import java.util.ArrayList;
import java.util.Date;
import java.sql.SQLException;

import src.types.BankTransaction;
import src.types.User;
import src.utils.GetSet;
import src.utils.RandomString;


public class UsersT implements IUsersT
{
    private static UsersT instance = null;
    private DataBase db;

    private UsersT()
    {
        db = DataBase.getInstance();
    }

    
    /** 
     * Singleton for Users Table
     * 
     * @return UsersT
     */
    public static UsersT getInstance()
    {
        if (instance == null)
            instance = new UsersT();
        return instance;
    }


    /**
     * new user
     * 
     * @param name
     * @param money
     * @return User
     */
    public User newUser(String name, int money)
    {
        GetSet<Integer> time = new GetSet<Integer>((int) (new Date().getTime()/1000));
        GetSet<Integer> newUserId = new GetSet<Integer>();

        db.executeQuery("INSERT INTO users (name, money, code) VALUES (?, ?, ?) RETURNING id", ps -> {
            try 
            {
                ps.setString(1, name);
                ps.setInt(2, money);
                ps.setString(3, new RandomString(6).nextString());
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                newUserId.set(rs.getInt(1));
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        
        db.executeUpdate("INSERT INTO bank_transactions (user_from_id, user_to_id, amount, time, comment) " +
                         "VALUES (-1, ?, ?, ?, \"NEW USER\")", ps -> {
            try 
            {
                ps.setInt(1, newUserId.get());
                ps.setInt(2, money);
                ps.setInt(3, time.get());
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        });
        return getUser(newUserId.get());
    }

    
    /** 
     * get user by code
     * @param code
     * @return User
     */
    public User getUser(String code)
    {
        GetSet<Integer> userId = new GetSet<Integer>();
        db.executeQuery("SELECT id FROM users WHERE code = ?;", ps -> {
            try 
            {
                ps.setString(1, code);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                userId.set(rs.getInt(1));
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return getUser(userId.get());
    }

    
    /** 
     * get user by id
     * 
     * @param id
     * @return User
     */
    public User getUser(int id)
    {
        if (id == -1)
            return new User(-1, "ADMIN", -1, "#0", this);
        GetSet<User> user = new GetSet<User>();
        db.executeQuery("SELECT name, money, code FROM users WHERE id = ?;", ps -> {
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
                user.set(
                    new User(id, rs.getString(1), rs.getInt(2), rs.getString(3), this)
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return user.get();
    }

    
    /** 
     * get random user
     * 
     * @return User
     */
    public User getRandUser()
    {
        GetSet<User> user = new GetSet<User>();
        db.executeQuery("SELECT id, name, money, code FROM users ORDER BY random() LIMIT 1;", ps -> {}, rs -> {
            try 
            {
                user.set(
                    new User(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), this)
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return user.get();
    }

    
    /** 
     * save user
     * @param user
     */
    public void saveUser(User user)
    {
        db.executeUpdate("UPDATE users " + 
                         "SET name = ?, money = ?, code = ?" +
                         "WHERE id = ?;", ps -> {
            try 
            {
                // user.name
                ps.setString(1, user.name);
                ps.setInt(2, user.money);
                ps.setString(3, user.code);
                ps.setInt(4, user.id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        });
    }

    
    /** 
     * send money 
     * 
     * @param userFrom
     * @param userTo
     * @param amount
     * @param comment
     * @return BankTransaction
     */
    public BankTransaction sendMoney(User userFrom, User userTo, int amount, String comment)
    {
        GetSet<Integer> transactionId = new GetSet<Integer>();
        GetSet<Integer> time = new GetSet<Integer>((int) (new Date().getTime()/1000));

        db.executeQuery("INSERT INTO bank_transactions (user_from_id, user_to_id, amount, time, comment)" +
                        "VALUES (?, ?, ?, ?, ?) RETURNING id", ps -> {
            try 
            {
                ps.setInt(1, userFrom.id);
                ps.setInt(2, userTo.id);
                ps.setInt(3, amount);
                ps.setInt(4, time.get());
                ps.setString(5, comment);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                transactionId.set(rs.getInt(1));
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });
        return new BankTransaction(transactionId.get(), userFrom, userTo, amount, time.get(), comment);
    }

    
    /** 
     * send money
     * 
     * @param userFrom
     * @param userTo
     * @param amount
     * @return BankTransaction
     */
    public BankTransaction sendMoney(User userFrom, User userTo, int amount)
    {
        return sendMoney(userFrom, userTo, amount, "SEND MONEY");
    }

    
    /** 
     * get all users
     * 
     * @return ArrayList<User>
     */
    public ArrayList<User> getUsers()
    {
        ArrayList<User> users = new ArrayList<>();

        db.executeQuery("SELECT id, name, money, code FROM users;", ps -> {}, rs -> {
            try 
            {
                users.add(
                    new User(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), this)
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });

        return users;
    }

    
    /** 
     * Get user bank transactions
     * 
     * @param user
     * @return ArrayList<BankTransaction>
     */
    public ArrayList<BankTransaction> getUserBankTransactions(User user)
    {
        ArrayList<BankTransaction> bt = new ArrayList<>();
        String sql = "SELECT bank_transactions.id, bank_transactions.user_from_id, bank_transactions.user_to_id, bank_transactions.amount, bank_transactions.time, bank_transactions.comment " +
                    "FROM bank_transactions "+
                    "INNER JOIN users ON users.id = bank_transactions.user_from_id OR users.id = bank_transactions.user_to_id " + 
                    "WHERE users.id = ?;";
        db.executeQuery(sql, ps -> {
        try 
        {
            ps.setInt(1, user.id);
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }, rs -> {
            try 
            {
                bt.add(
                    new BankTransaction(
                        rs.getInt(1), 
                        getUser(rs.getInt(2)), // != -1 ? rs.getInt(2) : user.id
                        getUser(rs.getInt(3)), // != -1 ? rs.getInt(3) : user.id
                        rs.getInt(4), 
                        rs.getInt(5), 
                        rs.getString(6)
                    )
                );
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
            return 0;
        });

        return bt;
    }
}
