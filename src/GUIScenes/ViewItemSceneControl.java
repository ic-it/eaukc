package src.GUIScenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import src.Globals;
import src.types.Item;
import src.types.ItemsTransaction;

public class ViewItemSceneControl implements Initializable
{
    public class ItemTransactionModel
    {
        public ItemsTransaction it;

        public ItemTransactionModel(ItemsTransaction it) {
            this.it = it;
        }

        public String getFrom() { return it.bankTransaction.userTo.name; }
        public String getTo() { return it.bankTransaction.userFrom.name; }
        public String getAmount() { return new Integer(it.bankTransaction.amount).toString(); }
    }

    @FXML
    public Label itemName;
    @FXML
    public Label itemInfo;
    @FXML
    public Label itemOwner;
    @FXML
    public Button sellItemButton;
    @FXML
    public TextField initialCostTextField;

    private boolean playerCanSellItem = false;

    @FXML
    private TableView<ItemTransactionModel> itemTransactionTable;
    @FXML
    private TableColumn<ItemTransactionModel, String> itemTransactionFromCol;
    @FXML
    private TableColumn<ItemTransactionModel, String> itemTransactionToCol;
    @FXML
    private TableColumn<ItemTransactionModel, String> itemTransactionAmountCol;

    private ObservableList<ItemTransactionModel> itemTransactionModel = FXCollections.observableArrayList();


    @FXML
    public void sellItemButtonClick(ActionEvent event) throws IOException
    {
        if (initialCostTextField.getText().length() == 0)
            return;
        
        try
        {
            Integer.parseInt(initialCostTextField.getText());
        } 
        catch (Exception e)
        {
            return;
        }
        Globals.initialCost.set(Integer.parseInt(initialCostTextField.getText()));
        Globals.userCanTakePartInAuction.set(!playerCanSellItem);
        Globals.windowCallback.get().call();
        if (playerCanSellItem)
        {
            Globals.auctionItem.set(Globals.itemView.get());

            Stage stage = (Stage)sellItemButton.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./GameScene.fxml")));
            stage.setScene(scene);
        }
        else
            System.out.println("Can not");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        itemName.setText("Name: " + Globals.itemView.get().name.replace("\n", " "));
        itemInfo.setText("Info: " + Globals.itemView.get().info.replace("\n", " "));
        itemOwner.setText("Owner:" + Globals.itemView.get().owner.name.replace("\n", " "));

        itemTransactionToCol.setCellValueFactory(new PropertyValueFactory<>("To"));
        itemTransactionFromCol.setCellValueFactory(new PropertyValueFactory<>("From"));
        itemTransactionAmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));

        for (ItemsTransaction t: Globals.itemView.get().getTransactions())
            itemTransactionModel.add(new ItemTransactionModel(t));
        
        itemTransactionTable.setItems(itemTransactionModel);
        
        playerCanSellItem = (boolean) Globals.initSceneArguments.get().call().get();
        sellItemButton.setDisable(!playerCanSellItem);
        initialCostTextField.setDisable(!playerCanSellItem);

        

        initialCostTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*")) 
                {
                    initialCostTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
