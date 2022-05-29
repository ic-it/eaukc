package src;


import src.db.ItemsT;
import src.db.UsersT;
import src.types.BankTransaction;
import src.types.Item;
import src.types.User;
import src.utils.Input;

/**
 * console test
 */
public class CUI {
    abstract interface UserState
    {
        public abstract void call();
    }

    private User user;
    private UsersT ut;
    private ItemsT it;
    public UserState state;

    CUI()
    {
        ut = UsersT.getInstance();
        it = ItemsT.getInstance();
        user = null;
        state = this::loginState;
    }

    public void loginState()
    {
        if ( Input.I("Create user(1) or select user (2): ") == 1 )
            createUser();
        else
            selectUser();
        System.out.println("Selected user: " + user);
        state = this::menuState;
    }

    public void menuState()
    {
        System.out.println("0. Exit");
        System.out.println("1. Show all users");
        System.out.println("2. Send money");
        System.out.println("3. Create Item");
        System.out.println("4. Get Items");
        System.out.println("5. Sell Item");
        int action = Input.I("Select action: ");
        switch (action)
        {
            case 0: 
                state = null;
                break;
            case 1: 
                printUsers();
                break;
            case 2:
                sendMoney();
                break;
            case 3:
                createItem();
                break;
            case 4:
                printUserItems();
                break;
            case 5:
                state = this::sellItem;
                break;
            default:
                
        }
    }


    public void printUsers()
    {
        for (User u: ut.getUsers())
            System.out.println(u);
    }

    public void printUserItems()
    {
        for (Item i: user.getItems())
            System.out.println(i);
    }

    public void selectUser()
    {
        printUsers();
        user = ut.getUser(Input.S("Select User(code): "));
    }

    public void createUser()
    {
        user = ut.newUser(Input.S("Name: "), Input.I("Money: "));
    }

    public void createItem()
    {
        it.newItem(user, Input.S("Name: "), Input.S("Info: "));
    }

    public void sendMoney()
    {
        printUsers();
    
        String uc = Input.S("Select User(code): ");
        int am = Input.I("Amount: ");

        BankTransaction bt = (BankTransaction) user.sendMoney(am, ut.getUser(uc));
        System.out.println(bt);
    }

    public void sellItem()
    {
        printUserItems();
        Item item = it.getItem(Input.I("Select Item Id: "));
        printUsers();
        User buyer = ut.getUser(Input.S("Select Buyer Code: "));
        int am = Input.I("Money amount: ");

        item.sell(buyer, am);
        state = this::menuState;
    }

    public void launch()
    {
        while (state != null)
        {
            state.call();
        }
    }

    // public static void main(String[] args) {
    //     CUI cui = new CUI();
    //     cui.launch();
    // }


}
