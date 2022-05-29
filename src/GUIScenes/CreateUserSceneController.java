package src.GUIScenes;

import java.io.IOException;
import java.net.URL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.Constants;
import src.Globals;
import src.db.UsersT;

import java.util.ResourceBundle;


public class CreateUserSceneController implements Initializable
{
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField moneyTextField;

    private UsersT ut = UsersT.getInstance();
    
    
    public void createButtonClick(ActionEvent event) throws IOException
    {
        try
        {
            Integer.parseInt(moneyTextField.getText());
        } 
        catch (Exception e)
        {
            return;
        }
        Globals.player.set(
            ut.newUser(
                nameTextField.getText(), 
                Integer.parseInt(moneyTextField.getText())
            )
        );

        Stage stage = (Stage)moneyTextField.getScene().getWindow();
        Parent createUser = FXMLLoader.load(getClass().getResource("./MenuScene.fxml"));
        Scene scene = new Scene(createUser);
        stage.setScene(scene);
    }

    public void cancelButtonClick(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)moneyTextField.getScene().getWindow();
        Parent createUser = FXMLLoader.load(getClass().getResource("./LoginScene.fxml"));
        Scene scene = new Scene(createUser);
        stage.setScene(scene);
    }
    

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        moneyTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                if (!newValue.matches("\\d*")) 
                {
                    moneyTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}
