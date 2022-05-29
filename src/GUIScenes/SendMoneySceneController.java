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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import src.Globals;
import src.db.UsersT;
import src.types.User;

public class SendMoneySceneController implements Initializable
{
    public class ComboBoxUser {
        public User user;
    
        public String getName() {
            return user.name;
        }
    
        public ComboBoxUser(User user) 
        {
            this.user = user;
        }
    }

    @FXML
    private TextField amountTextField;   
    @FXML
    private ComboBox<ComboBoxUser> selectUserComboBox;
    @FXML 
    private TextArea commentTextArea;

    
    public void sendButtonClick(ActionEvent event) throws IOException
    {
        if (selectUserComboBox.getSelectionModel().isEmpty() || amountTextField.getText().trim().isEmpty())
            return;
        
        if (Integer.parseInt(amountTextField.getText()) > Globals.player.get().money)
        {
            Globals.message.set("Have no Money");
            Globals.updateWindow.get().call();
        }
        else
        {
            if (commentTextArea.getText().trim().isEmpty())
                Globals.player.get().sendMoney(Integer.parseInt(amountTextField.getText()), selectUserComboBox.getSelectionModel().getSelectedItem().user);
            else
                Globals.player.get().sendMoney(Integer.parseInt(amountTextField.getText()), selectUserComboBox.getSelectionModel().getSelectedItem().user, commentTextArea.getText());
            Globals.message.set("Sent");
            Globals.updateWindow.get().call();
        }
        
        // Stage stage = (Stage)amountTextField.getScene().getWindow();
        // Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./MenuScene.fxml")));
        // stage.setScene(scene);

        Stage stage = (Stage)amountTextField.getScene().getWindow();
        stage.close();

    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        System.out.println(this);
        amountTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*")) 
                {
                    amountTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        selectUserComboBox.setConverter(new StringConverter<ComboBoxUser>() {
            @Override
            public String toString(ComboBoxUser object) {
                return object.getName();
            }
        
            @Override
            public ComboBoxUser fromString(String string) {
                return null;
            }
        });

        ObservableList<ComboBoxUser> comboboxUsers = FXCollections.observableArrayList();
        for (User u: UsersT.getInstance().getUsers())
        {
            comboboxUsers.add(new ComboBoxUser(u));
        }


        selectUserComboBox.setItems(comboboxUsers);
    }
        
}
