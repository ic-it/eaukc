package src.GUIScenes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.Globals;
import src.game.BotFactory;
import src.game.BotType;
import src.game.BotFactory.TypeErrorException;
import src.types.User;
import src.utils.GetSet;
import src.utils.UtilsFunc;

public class GameSceneController implements Initializable
{
    private int currentRate;
    private User currentBuyer;
    private int account;
    private ArrayList<BotFactory.Bot> bots;



    @FXML
    private Label currentRateLabel;
    @FXML
    private TextField newRateTextFeild;
    @FXML
    private Button raiseButtonClick;
    @FXML
    private Label adminAccount;
    @FXML
    private TextArea logsTextArea;
    @FXML
    private Button quitButton;
    @FXML
    private Label messageLabel;
    @FXML
    private ListView<String> membersListView;

    void updateMessageLabel(String msg)
    {
        messageLabel.setText("Messgae: " + msg);
    }
    
    @FXML
    public void quitButtonClick(ActionEvent event) throws IOException 
    {
        Globals.clearUsersInAuction();
        Stage stage = (Stage)quitButton.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./MenuScene.fxml")));
        stage.setScene(scene);
    }


    @FXML
    public void viewItemButtonClick(ActionEvent event) throws IOException
    {
        Globals.itemView.set(Globals.auctionItem.get());

        Globals.initSceneArguments.set( () -> { 
            GetSet<Boolean> a = new GetSet<>(false);
            return a; 
        });

        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./ViewItemScene.fxml")));
        stage.setScene(scene);
        stage.show();
    }

    
    @FXML
    public void raiseButton(ActionEvent event) throws IOException
    {
        int newRate;
        try
        {
            newRate = Integer.parseInt(newRateTextFeild.getText());
        } 
        catch (Exception e)
        {
            updateMessageLabel("No money, no honey XD");
            return;
        }

        if (newRateTextFeild.getText().length() == 0)
            return;

        if (newRate <= currentRate)
        {
            updateMessageLabel("The rate must be higher than the current");
            return;
        }

        if (newRate > Globals.player.get().money)
        {
            updateMessageLabel("No money, no honey XD");
            return;
        }

        raiseTheRate(newRate, Globals.player.get());

    }


    void raiseTheRate(int rate, User user)
    {
        if (rate <= this.currentRate || rate > user.money)
            return;
        
        System.out.println(rate + ": " + user);
        logsTextArea.appendText("User " + user.name + "#" + user.code + " raises " + (rate - this.currentRate) +  ". Price now: " + rate + "\n");
        
        this.currentRate = rate;
        this.currentBuyer = user;

        Platform.runLater(new Runnable()
        {
            public void run() 
            {
                updateCurrentRate();
            }
        });


        for (BotFactory.Bot bot: bots)
            bot.updateCurrentRate(this.currentRate);

        account = 5;
        Platform.runLater(new Runnable()
        {
            public void run() 
            {
                adminAccount.setText(""+account);
            }
        });
    }

    public void updateCurrentRate()
    {
        currentRateLabel.setText("The current rate: " + currentRate);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        quitButton.setDisable(true);
        currentRate = Globals.initialCost.get();
        currentBuyer = (User)Globals.auctionItem.get().owner;
        account = 5;

        updateCurrentRate();

        if (Globals.userCanTakePartInAuction.get() == null)
        {
            throw new Error("Error");
        }

        boolean userCanTakePartInAuction = (boolean) Globals.userCanTakePartInAuction.get();

        newRateTextFeild.setDisable(!userCanTakePartInAuction);
        raiseButtonClick.setDisable(!userCanTakePartInAuction);

        newRateTextFeild.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*"))
                {
                    newRateTextFeild.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Globals.clearUsersInAuction();

        bots = new ArrayList<>();
        
        int ba = UtilsFunc.getRandomNumber(4, 10);
        while (ba --> 0)
        {
            BotFactory bf = new BotFactory();
            
            if (UtilsFunc.getRandomNumber(1, 5) == 2)
            {
                try
                {
                    bots.add(bf.getBot(BotType.Plain, this::raiseTheRate));
                }
                catch (TypeErrorException e)
                {
                    
                }
                continue;
            }
            if (UtilsFunc.getRandomNumber(1, 5) == 2)
            {
                try
                {
                    bots.add(bf.getBot(BotType.Aggressive, this::raiseTheRate));
                }
                catch (TypeErrorException e)
                {
                    
                }
                continue;
            }
            if (UtilsFunc.getRandomNumber(1, 5) == 2)
            {
                try
                {
                    bots.add(bf.getBot(BotType.Restrained, this::raiseTheRate));
                }
                catch (TypeErrorException e)
                {
                    
                }
                continue;
            }
            if (UtilsFunc.getRandomNumber(1, 10) == 2)
            {
                try
                {
                    bots.add(bf.getBot(BotType.Passive, this::raiseTheRate));
                }
                catch (TypeErrorException e)
                {
                    
                }
                continue;
            }
        }

        for (BotFactory.Bot bot: bots)
            bot.updateCurrentRate(this.currentRate);

        for (BotFactory.Bot bot: bots)
        {
            System.out.println(bot.getClass().getName() + ": " + bot.user);
        }

        ObservableList<String> members = FXCollections.observableArrayList();

        for (BotFactory.Bot bot: bots)
        {
            members.add("Name: " + bot.user.get().name + "; Code: "+ bot.user.get().code + "; Money: " + bot.user.get().money);
        }

        membersListView.setItems(members);


        new Thread()
        {
            public void run()
            {
                while (account != 0) 
                {
                    Platform.runLater(new Runnable()
                    {
                        public void run() 
                        {
                            adminAccount.setText(""+account);
                        }
                    });
                    
                    if (account == 0)
                        break;
                    
                    try
                    {
                        Thread.sleep(1000*(6-account));
                    } catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                    account -= 1;
                }

                /// QUIT code

                for (BotFactory.Bot bot: bots)
                    bot.stopBot();

                if (currentBuyer.id != Globals.auctionItem.get().owner.id)
                {
                    logsTextArea.appendText("User " + currentBuyer.name + "#" + currentBuyer.code + " bought an item for " + currentRate + "\n");
                    Globals.auctionItem.get().sell(currentBuyer, currentRate);
                } else
                {
                    logsTextArea.appendText("No one wanted to buy the item\n");
                }
                quitButton.setDisable(false);

                // Globals.auctionItem.set(null);
                /// QUIT code
            }
        }.start();
    }
    
}
