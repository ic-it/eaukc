package src.game;

import src.Globals;
import src.db.UsersT;
import src.types.User;
import src.utils.GetSet;
import src.utils.UtilsFunc;

/**
 * Bot Factory
 */
public class BotFactory 
{

    public BotFactory() {}

    
    /** 
     * @param bot
     * @param raiseTheRate
     * @return Bot
     */
    public Bot cloneBot(Bot bot, RaiseTheRate raiseTheRate)
    {
        Class<? extends Bot> clone = bot.getClass();
        try {
            return (Bot) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lambda for RaiseTheRate
     * This is the link between the stage and the bot 
     */
    public abstract interface RaiseTheRate
    {
        public abstract void raise(int rate, User user);
    }
    
    /**
     * General description of the bot
     */
    public abstract class Bot
    {
        public GetSet<Integer> rate;
        public GetSet<Boolean> auctionIsActive;
        public GetSet<User> user;
        
        /**
         * Method for the Observer
         * @param rate
         */
        public void updateCurrentRate(int rate)
        {
            this.rate.set(rate);
        }

        /**
         * Methods for the Observer
         */
        public void stopBot()
        {
            auctionIsActive.set(false);
        }

        public Integer wantsToRaiseTheRate()
        {
            return null;
        }
    }

    /**
     *  PlainBot
     *  A new thread is created when the bot starts
     */
    class PlainBot extends Bot
    {
        public PlainBot(RaiseTheRate raiseTheRate)
        {
            rate = new GetSet<>(0);
            auctionIsActive = new GetSet<>(true);
            do
                user = new GetSet<>(UsersT.getInstance().getRandUser());
            while (!Globals.addUserToAuction(user.get()));

            new Thread() {
                public void run()
                {
                    while (auctionIsActive.get())
                    {
                        Integer newRate = wantsToRaiseTheRate();
                        if (newRate != null)
                        {
                            raiseTheRate.raise(newRate, user.get());
                        }
                        
                        try
                        {
                            Thread.sleep(100*UtilsFunc.getRandomNumber(1, 10));
                        } catch (InterruptedException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        @Override
        public Integer wantsToRaiseTheRate()
        {
            if (rate.get() >= user.get().money/4 || UtilsFunc.getRandomNumber(1, 10) != 2)
                return null;
            return UtilsFunc.getRandomNumber(rate.get(), rate.get()*2);
        }
    }

    /**
     * AggressiveBot
     */
    class AggressiveBot extends PlainBot 
    {
        public AggressiveBot(RaiseTheRate raiseTheRate)
        {
            super(raiseTheRate);
        }
        
        @Override
        public Integer wantsToRaiseTheRate()
        {
            if (rate.get() >= user.get().money/2 || UtilsFunc.getRandomNumber(1, 5) != 2)
                return null;
            
            return rate.get() + UtilsFunc.getRandomNumber(5, user.get().money/4);
        }
    }

    /**
     * RestrainedBot
     */
    class RestrainedBot extends PlainBot
    {
        public RestrainedBot(RaiseTheRate raiseTheRate)
        {
            super(raiseTheRate);
        }

        @Override
        public Integer wantsToRaiseTheRate()
        {
            if (rate.get() >= user.get().money/3 || UtilsFunc.getRandomNumber(1, 10) != 2)
                return null;
            
            return rate.get() + UtilsFunc.getRandomNumber(5, user.get().money/4);
        }
    }

    /**
     * PassiveBot
     */
    class PassiveBot extends PlainBot 
    {
        public PassiveBot(RaiseTheRate raiseTheRate)
        {
            super(raiseTheRate);
        }

        @Override
        public Integer wantsToRaiseTheRate()
        {
            if (rate.get() >= user.get().money/8 || UtilsFunc.getRandomNumber(1, 10) != 2)
                return null;
            
            return rate.get() + UtilsFunc.getRandomNumber(5, user.get().money/8);
        }
    }

    public class TypeErrorException extends Exception {  
        public TypeErrorException(String errorMessage) {  
            super(errorMessage);  
        }  
    }  
    
    /** 
     * A method for creating a bot 
     * 
     * @param botType
     * @param raiseTheRate Function from the scene
     * @return Bot
     */
    public Bot getBot(BotType botType, RaiseTheRate raiseTheRate) throws TypeErrorException
    {
        switch (botType)
        {
            case Aggressive:
                return new AggressiveBot(raiseTheRate);
            
            case Restrained:
                return new RestrainedBot(raiseTheRate);
            
            case Plain:
                return new PlainBot(raiseTheRate);
            
            case Passive:
                return new PassiveBot(raiseTheRate);
            
            default:
                throw new TypeErrorException("Unsupported BotType");
                // return null;
        }
    }

}
