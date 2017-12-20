import com.mongodb.DBObject;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    ScrollPane peopleScrollPane = new ScrollPane();
    @FXML
    TextField peopleTextField;

    @FXML
    Tab postsTab = new Tab();
    @FXML
    ScrollPane postsScrollPane = new ScrollPane();
    @FXML
    TextField postTextField;
    @FXML
    TextField postAdvanced;

    @FXML
    Tab videoTab = new Tab();

    @FXML
    TextField videoTextField;
    @FXML
    ScrollPane videoScrollPane = new ScrollPane();
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

        List<Document> list = DBController.getPeople();

        if(list.size() == 0) {
            peopleScrollPane.setContent(new Label("Nothing to show"));
            return;
        }

        VBox tempVb = new VBox();

        for (Document curr : list){
            String id = curr.getInteger("_id").toString();
            String username = curr.getString("username");
            Label text = new Label(username);
            Hyperlink hyperlink = new Hyperlink("user: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/id" + id));

            tempVb.getChildren().addAll(hyperlink, text, new Label("\n"));
        }
        peopleScrollPane.setContent(tempVb);
    }

    @FXML
    public void peopleFind(){

        List<Document> list = DBController.getPeople(peopleTextField.getText());

        if(list.size() == 0) {
            peopleScrollPane.setContent(new Label("Nothing to show"));
            return;
        }

        VBox tempVb = new VBox();

        for (Document curr : list){
            String id = curr.getInteger("_id").toString();
            String username = curr.getString("username");
            Label text = new Label(username);
            Hyperlink hyperlink = new Hyperlink("user: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/id" + id));

            tempVb.getChildren().addAll(hyperlink, text, new Label("\n"));
        }
        peopleScrollPane.setContent(tempVb);
    }

    @FXML
    public void postShowAll(){
        List<Document> list = DBController.getPost();
        if(list.size() == 0) {
            postsScrollPane.setContent(new Label("Nothing to show"));
            return;
        }
        VBox tempVb = new VBox();

        for (Document curr : list){
            String fromId = curr.getInteger("fromid").toString();
            String id = curr.getInteger("_id").toString();
            Label text = new Label(curr.getString("text"));
            Hyperlink hyperlink = new Hyperlink("Post: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/fave?section=likes_posts&w=wall" + fromId + "_" + id));

            tempVb.getChildren().addAll(hyperlink, text, new Label("\n\n"));
        }
        postsScrollPane.setContent(tempVb);
    }

    @FXML
    public void postAdvancedClicked(){
        List<Document> list = DBController.getPostAttachments(postAdvanced.getText());
        if(list.size() == 0) {
            postsScrollPane.setContent(new Label("Nothing to show"));
            return;
        }

        VBox tempVb = new VBox();

        for (Document curr : list){
            String attachmentsStr = curr.getString("attachments");
            if (attachmentsStr.length() < 2) continue;
            String fromId = curr.getInteger("fromid").toString();
            String id = curr.getInteger("_id").toString();
            Label text = new Label(curr.getString("text"));
            Hyperlink hyperlink = new Hyperlink("Post: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/fave?section=likes_posts&w=wall" + fromId + "_" + id));
            Label attachments = new Label(attachmentsStr);

            tempVb.getChildren().addAll(hyperlink, text, new Label("Music:"), attachments, new Label("\n\n"));
        }
        postsScrollPane.setContent(tempVb);
    }

    @FXML
    public void postFind(){
        List<Document> list = DBController.getPost(postTextField.getText());
        if(list.size() == 0) {
            postsScrollPane.setContent(new Label("Nothing to show"));
            return;
        }

        VBox tempVb = new VBox();

        for (Document curr : list){
            String fromId = curr.getInteger("fromid").toString();
            String id = curr.getInteger("_id").toString();
            Label text = new Label(curr.getString("text"));
            Hyperlink hyperlink = new Hyperlink("Post: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/fave?section=likes_posts&w=wall" + fromId + "_" + id));

            tempVb.getChildren().addAll(hyperlink, text, new Label("\n\n"));
        }
        postsScrollPane.setContent(tempVb);
    }

    @FXML
    public void videoShowAll(){

        List<Document> list = DBController.getVideo();
        if(list.size() == 0) {
            videoScrollPane.setContent(new Label("Nothing to show"));
            return;
        }

        VBox tempVb = new VBox();

        for (Document curr : list){
            String fromId = curr.getInteger("ownerid").toString();
            String id = curr.getInteger("_id").toString();
            Label text = new Label(curr.getString("text"));
            Hyperlink hyperlink = new Hyperlink("Video: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/fave?section=likes_video&z=video" + fromId + "_" + id));

            tempVb.getChildren().addAll(hyperlink, text, new Label("\n\n"));
        }
        videoScrollPane.setContent(tempVb);
    }

    @FXML
    public void videoFind(){

        List<Document> list = DBController.getVideo(videoTextField.getText());
        if(list.size() == 0) {
            videoScrollPane.setContent(new Label("Nothing to show"));
            return;
        }

        VBox tempVb = new VBox();

        for (Document curr : list){
            String fromId = curr.getInteger("ownerid").toString();
            String id = curr.getInteger("_id").toString();
            Label text = new Label(curr.getString("text"));
            Hyperlink hyperlink = new Hyperlink("Video: " + curr.getInteger("_id").toString());
            hyperlink.setOnAction(event -> getHostServices().showDocument("https://vk.com/fave?section=likes_video&z=video" + fromId + "_" + id));

            tempVb.getChildren().addAll(hyperlink, text, new Label("\n\n"));
        }
        videoScrollPane.setContent(tempVb);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("vkapi.fxml"));
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("VK bookmarks");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
