package src.GUIScenes;

// import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import src.Globals;
import src.db.ItemsT;
import src.types.BankTransaction;
import src.types.Item;
import src.utils.GetSet;
import src.utils.UtilsFunc;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;





public class MenuSceneController  implements Initializable
{
    private boolean windowIsOpen = true;


    public class PlayerItemsModel 
    {
        private Item item;

        public PlayerItemsModel(Item item) {
            this.item = item;
        }

        public String getName() { return item.name; }
        // public void setName(String name) { this.name = new SimpleStringProperty(name); }
        public String getInfo() { return item.info; }
        // public void setInfo(String info) { this.info = new SimpleStringProperty(info); }
    }
    
    public class PlayerTransactionsModel
    {
        public BankTransaction bt;

        public PlayerTransactionsModel(BankTransaction bt) {
            this.bt = bt;
        }

        public String getFrom() { return bt.userFrom.name; }
        public String getTo() { return bt.userTo.name; }
        public String getAmount() { return new Integer(bt.amount).toString(); }
        public String getComment() { return bt.comment; }
    }

    public class AuctionsModel 
    {
        private Item item;

        public AuctionsModel(Item item) {
            this.item = item;
        }

        public String getName() { return item.name; }
        public String getInfo() { return item.info; }
        public String toString() { return item.name; }
    }
    


    @FXML
    private Label playerName;
    @FXML
    private Label playerMoney;
    @FXML
    private Label playerCode;
    @FXML
    private Label messageLabel;
    
    @FXML
    private TableView<PlayerItemsModel> playerItemsTable;
    @FXML
    private TableColumn<PlayerItemsModel, String> itemNameCol;
    @FXML
    private TableColumn<PlayerItemsModel, String> itemInfoCol;

    @FXML
    private TableView<PlayerTransactionsModel> transactionTable;
    @FXML
    private TableColumn<PlayerTransactionsModel, String> transactionFromCol;
    @FXML
    private TableColumn<PlayerTransactionsModel, String> transactionToCol;
    @FXML
    private TableColumn<PlayerTransactionsModel, String> transactionAmountCol;
    @FXML
    private TableColumn<PlayerTransactionsModel, String> transactionCommentCol;


    @FXML
    private ListView<AuctionsModel> auctionsList;


    private ObservableList<AuctionsModel> auctionsModel = FXCollections.observableArrayList();
    private ObservableList<PlayerItemsModel> playerItemsModel = FXCollections.observableArrayList();
    private ObservableList<PlayerTransactionsModel> playerTransactionsModel = FXCollections.observableArrayList();


    public void changeUserButtonClick(ActionEvent event) throws IOException
    {
        windowIsOpen = false;
        Stage stage = (Stage) playerItemsTable.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./LoginScene.fxml")));
        stage.setScene(scene);
    }

    
    public void viewItemButtonClick(ActionEvent event) throws IOException
    {

        if (playerItemsTable.getSelectionModel().isEmpty())
        {
            messageLabel.setText("Message: Select item to sell it");
            return;
        }
        PlayerItemsModel pim = playerItemsTable.getSelectionModel().getSelectedItem();

        Globals.itemView.set(pim.item);

        Globals.initSceneArguments.set( () -> { 
            GetSet<Boolean> a = new GetSet<>(true);
            return a; 
        });

        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./ViewItemScene.fxml")));
        stage.setScene(scene);
        stage.show();
    }

    
    public void viewAuctionItemClick(ActionEvent event) throws IOException
    {
        if (auctionsList.getSelectionModel().isEmpty())
        {
            messageLabel.setText("Message: Select item to sell it");
            return;
        }
        AuctionsModel pim = auctionsList.getSelectionModel().getSelectedItem();

        Globals.itemView.set(pim.item);

        Globals.initSceneArguments.set( () -> { 
            GetSet<Boolean> a = new GetSet<>(false);
            return a;
        });

        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./ViewItemScene.fxml")));
        stage.setScene(scene);
        stage.show();
    }

    
    public void joinClick(ActionEvent event) throws IOException
    {
        int initialCost = UtilsFunc.getRandomNumber(10, 10000);
        AuctionsModel pim = auctionsList.getSelectionModel().getSelectedItem();
        
        // Globals.windowCallback.get().call();
        Globals.initialCost.set(initialCost);
        Globals.userCanTakePartInAuction.set(true);
        Globals.auctionItem.set(Globals.itemView.get());
        Globals.auctionItem.set(pim.item);

        windowIsOpen = false;
        Stage stage = (Stage)auctionsList.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./GameScene.fxml")));
        stage.setScene(scene);
        // stage.show();
    }

    
    public void createItemButtonClick(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./CreateItemScene.fxml")));
        stage.setScene(scene);
        stage.show();
    }

    public void updateMessage()
    {
        messageLabel.setText(Globals.message.get());
    }

    public void updateTables()
    {

        playerTransactionsModel.clear();
        playerItemsModel.clear();

        playerItemsTable.refresh();
        transactionTable.refresh();


        transactionToCol.setCellValueFactory(new PropertyValueFactory<>("To"));
        transactionFromCol.setCellValueFactory(new PropertyValueFactory<>("From"));
        transactionAmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        transactionCommentCol.setCellValueFactory(new PropertyValueFactory<>("Comment"));

        for (BankTransaction t: Globals.player.get().getBankTransactions())
            playerTransactionsModel.add(new PlayerTransactionsModel(t));

        transactionTable.setItems(playerTransactionsModel);

        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        itemInfoCol.setCellValueFactory(new PropertyValueFactory<>("Info"));
        
        for (Item i: Globals.player.get().getItems())
            playerItemsModel.add(new PlayerItemsModel(i));
        
        playerItemsTable.setItems(playerItemsModel);
    }

    
    public void sendMoneyButtonClick(ActionEvent event) throws IOException
    {

        windowIsOpen = false;
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./SendMoneyScene.fxml")));
        stage.setScene(scene);
        stage.show();
    }


    public void quit(ActionEvent event) throws IOException
    {
        windowIsOpen = false;
        Platform.exit();
        System.exit(0);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        playerName.setText("Name: " + Globals.player.get().name);
        playerCode.setText("Code: " + Globals.player.get().code);
        playerMoney.setText("Money: " + Globals.player.get().money);

        if (Globals.message.get() != null)
        {
            messageLabel.setText("Message: " + Globals.message.get());
            Globals.message.set(null);
        }
        else 
        {
            messageLabel.setText("");
        }

        playerItemsTable.setRowFactory( tv -> {
            TableRow<PlayerItemsModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {

                    try {

                        PlayerItemsModel pim = playerItemsTable.getSelectionModel().getSelectedItem();

                        Globals.itemView.set(pim.item);

                        Globals.initSceneArguments.set( () -> { 
                            GetSet<Boolean> a = new GetSet<>(true);
                            return a; 
                        });

                        Stage stage = new Stage();
                        Scene scene;
                        scene = new Scene(FXMLLoader.load(getClass().getResource("./ViewItemScene.fxml")));
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });

        updateTables();

        Globals.updateWindow.set(() -> {
            this.updateMessage();
            this.updateTables();
        });

        Globals.windowCallback.set(() -> {
            Stage stage = (Stage) this.messageLabel.getScene().getWindow();
            stage.close();
        });


        new Thread()
        {
            public void run()
            {
                while (windowIsOpen)
                {
                    Platform.runLater(new Runnable()
                    {
                        public void run() 
                        {
                            auctionsModel.clear();
                            int x = 5;
                            while (x --> 0)
                            {
                                Item randItem = ItemsT.getInstance().getRandItem();
                                while (randItem.owner.id == Globals.player.get().id)
                                    randItem = ItemsT.getInstance().getRandItem();
                                auctionsModel.add(new AuctionsModel(randItem));
                            }
                            auctionsList.setItems(auctionsModel);
                        }
                    });

                    try
                    {
                        Thread.sleep(1000*UtilsFunc.getRandomNumber(10, 30));
                    } catch (InterruptedException ex)
                    {
                        ex.printStackTrace();
                    }
                }

                /// QUIT code
                /// QUIT code
            }
        }.start();
    }
}
