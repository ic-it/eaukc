package src.GUIScenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import src.Globals;
import src.db.UsersT;
import src.types.User;

public class SelectUserSceneController implements Initializable
{
    @FXML
    private ListView<String> usersListView;
    private UsersT ut = UsersT.getInstance();


    public void loginButtonClick(ActionEvent event) throws IOException
    {
        if (usersListView.getSelectionModel().getSelectedIndex() == -1)
            return;
        
        Globals.player.set(
            ut.getUser(usersListView.getSelectionModel().getSelectedIndex() + 1)
        );

        Stage stage = (Stage)usersListView.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("./MenuScene.fxml")));
        stage.setScene(scene);
    }


    public void cancelButtonClick(ActionEvent event) throws IOException
    {
        Stage stage = (Stage)usersListView.getScene().getWindow();
        Parent createUser = FXMLLoader.load(getClass().getResource("./LoginScene.fxml"));
        Scene scene = new Scene(createUser);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        ObservableList<String> items = FXCollections.observableArrayList();
        
        for (User u : ut.getUsers())
        {
            items.add("Name: " + u.name + "; Code: "+ u.code + "; Money: " + u.money);
        }
        usersListView.setItems(items);
    }
}
