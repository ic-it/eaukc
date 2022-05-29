package src.GUIScenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBElement.GlobalScope;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.Globals;
import src.db.ItemsT;

public class CreateItemSceneController implements Initializable
{
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea infoTextArea;
    
    
    public void createButtonClick(ActionEvent event) throws IOException
    {
        if (nameTextField.getText().length() == 0 || infoTextArea.getText().length() == 0)
        {
            Globals.message.set("Fill in the input fields");
        } else
        {
            ItemsT.getInstance().newItem(Globals.player.get(), nameTextField.getText(), infoTextArea.getText());
            Globals.message.set("");
        }

        Globals.updateWindow.get().call();

        Stage stage = (Stage)nameTextField.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
    }
}
