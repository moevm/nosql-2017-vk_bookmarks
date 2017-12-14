import com.mongodb.DBObject;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.List;

public class UI extends Application {
    @FXML
    TabPane mainTabPane = new TabPane();
    @FXML
    Tab peopleTab = new Tab();
    @FXML
    VBox peopleVBox;
    @FXML
    TextField peopleTextField;

    @FXML
    Tab photoTab = new Tab();
    @FXML
    VBox photosVBox;
    @FXML
    TextField photoTextField;

    @FXML
    Tab postsTab = new Tab();
    @FXML
    VBox postVBox;
    @FXML
    TextField postTextField;

    @FXML
    Tab videoTab = new Tab();
    @FXML
    VBox videoVBox;
    @FXML
    TextField videoTextField;

    @FXML
    Tab linksTab = new Tab();
    @FXML
    Tab authTab = new Tab();
    @FXML
    WebView authWebView;

    @FXML
    public void authorizeRequired(){
        WebEngine engine = authWebView.getEngine();
        VkAuth.invokeRequest(engine);
    }

    @FXML
    public void peopleShowAll(){
        peopleVBox.getChildren().clear();
        List<Document> list = DBController.getPeople();
        if(list.size() == 0) {
            peopleVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            peopleVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void peopleFind(){
        peopleVBox.getChildren().clear();
        List<Document> list = DBController.getPeople(peopleTextField.getText());
        System.out.println(peopleTextField.getText());
        if(list.size() == 0) {
            peopleVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            peopleVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void photoShowAll(){
        photosVBox.getChildren().clear();
        List<Document> list = DBController.getPhoto();
        if(list.size() == 0) {
            photosVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            photosVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void photoFind(){
        photosVBox.getChildren().clear();
        List<Document> list = DBController.getPhoto(photoTextField.getText());
        if(list.size() == 0) {
            photosVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            photosVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void postShowAll(){
        postVBox.getChildren().clear();
        List<Document> list = DBController.getPost();
        if(list.size() == 0) {
            postVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            postVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void postFind(){
        postVBox.getChildren().clear();
        List<Document> list = DBController.getPost(postTextField.getText());
        if(list.size() == 0) {
            postVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            postVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void videoShowAll(){
        videoVBox.getChildren().clear();
        List<Document> list = DBController.getVideo();
        if(list.size() == 0) {
            videoVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            videoVBox.getChildren().add(temp);

        }
    }

    @FXML
    public void videoFind(){
        videoVBox.getChildren().clear();
        List<Document> list = DBController.getVideo(videoTextField.getText());
        if(list.size() == 0) {
            videoVBox.getChildren().add(new Label("Nothing to show"));
            return;
        }

        for (Document curr : list){
            Label temp = new Label(curr.toString());
            videoVBox.getChildren().add(temp);

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("vkapi.fxml"));

        Scene scene = new Scene(root, 900, 700);
        stage.setTitle("VK bookmarks");
        stage.setScene(scene);
        stage.show();
    }
}
