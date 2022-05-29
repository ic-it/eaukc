package src.GUIScenes;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import src.Constants;
import src.Globals;

import java.util.ResourceBundle;


public class LoginSceneController implements Initializable
{
    @FXML
    private Label versionLabel;

    @FXML
    private Button newUserButton;
    

    public void newUserButtonClick(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)newUserButton.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./CreateUserScene.fxml")));
        stage.setScene(scene);
    }

    public void selectUserButtonClick(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)newUserButton.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./SelectUserScene.fxml")));
        stage.setScene(scene);
    }

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        if (Globals.dbFile.get() == null)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Database Chooser");
            File file;
            do 
            {
                file = fileChooser.showOpenDialog(new Stage());
            } while (file == null);
            Globals.dbFile.set("jdbc:sqlite:" + file.getPath());
        }
        versionLabel.setText("Version: " + Constants.version);
    }
}
