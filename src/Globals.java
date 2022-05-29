package src;

import java.util.ArrayList;

import src.types.Item;
import src.types.User;
import src.utils.GetSet;

/**
 * global variables that are used to communicate between scenes
 */
public class Globals
{
    /**
     * interface for lambda
     */
    public interface VoidCall {
        public void call();
    }

    /**
     * interface for lambda
     */
    public interface GetSetCall {
        public GetSet call();
    }

    public static GetSet<User> player = new GetSet<>();
    public static GetSet<Integer> tempId = new GetSet<>();
    public static GetSet<String> message = new GetSet<>();
    public static GetSet<Item> itemView = new GetSet<>();
    public static GetSet<Item> auctionItem = new GetSet<>();
    public static GetSet<VoidCall> windowCallback = new GetSet<>();
    public static GetSet<VoidCall> updateWindow = new GetSet<>();
    public static GetSet<GetSetCall> initSceneArguments = new GetSet<>();
    public static GetSet<Integer> initialCost = new GetSet<>();
    public static GetSet<Boolean> userCanTakePartInAuction = new GetSet<>();
    public static GetSet<String> dbFile = new GetSet<>();

    public static GetSet<ArrayList<User>> usersInAuction = new GetSet<>(new ArrayList<User>());

    public static void clearUsersInAuction()
    {
        usersInAuction.set(new ArrayList<User>());
    }

    
    /** 
     * Is there a user in the auction
     * 
     * @param user User to check
     * @return boolean is user in the auction has already
     */
    public static boolean usersAtTheAuction(User user)
    {
        for (User u: usersInAuction.get())
            if (user.id == u.id || player.get() == null || u.id == player.get().id)
                return true;
        return false;
    }

    
    /** 
     * Add a user to an auction and at the same time check if he is in it
     * 
     * @param user User to add
     * @return boolean is user in the auction has already
     */
    public static boolean addUserToAuction(User user)
    {
        if (usersAtTheAuction(user))
            return false;
        
        usersInAuction.get().add(user);
        return true;
    }

}
