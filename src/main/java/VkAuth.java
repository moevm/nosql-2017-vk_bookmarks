import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.responses.SearchResponse;
import com.vk.api.sdk.queries.users.UserField;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class VkAuth extends Application {
    private Label userName;
    private TextField userTextField;
    private Button submitBtn;

    private static String toSearch;
    private static String tokenUrl;
    private static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    private static final int APP_ID = 6279038;
    private static final String CLIENT_SECRET = "S1br1PrMDxCCG4Mi8xpo";
    private static final String PERMISSIONS = "friends, wall, photos,messages";
    private static final String VK_AUTH_URL = "https://oauth.vk.com/authorize?" +
            "client_id=" + APP_ID + "&" +
            "display=page&" +
            "redirect_uri=" + REDIRECT_URL + "&" +
            "scope=" + PERMISSIONS + "&" +
            "response_type=code&" +
            "v=5.69";

    public static void main(String[] args) {
        DBController.initializeDb();
        launch(VkAuth.class);
    }

    private SearchResponse invokeApiUsers(String toMatch){
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        try {
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URL, tokenUrl)
                    .execute();

            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
            return vk.users().
                    search(actor).
                    q(toMatch).
                    fields(UserField.PHOTO_MAX_ORIG, UserField.ABOUT, UserField.STATUS).
                    execute();

        } catch (ApiException|ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start(Stage stage) throws Exception{

        final VBox vBox = new VBox();

        final WebView authWebView = new WebView();
        final WebEngine engine = authWebView.getEngine();
        engine.load(VK_AUTH_URL);

        userName = new Label("Type search keywords here:");
        userName.setVisible(false);
        userName.setStyle("-fx-font-size: 2em; -fx-border-width: 2px;");

        userTextField = new TextField();
        userTextField.setVisible(false);
        userTextField.setMinSize(200,50);
        userTextField.setMaxSize(200,50);

        submitBtn = new Button("Search");
        submitBtn.setVisible(false);
        submitBtn.setStyle("-fx-font-size: 2em; -fx-border-width: 2px; -fx-background-color: #00ff0f; ");
        submitBtn.setMinSize(200,50);

        vBox.getChildren().addAll(userName, userTextField, submitBtn, authWebView);
        
        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                toSearch = userTextField.getText();
                SearchResponse response = invokeApiUsers(toSearch);
                if(response == null | response.getCount() < 1) System.out.println("No results");
                else {
                    DBController.dbAddUsers(response.getItems());
                    System.out.println(toSearch);
                    for (UserFull user : response.getItems()) {
                        System.out.println(user.getFirstName() + " " + user.getLastName());
                        Label temp = new Label(user.getFirstName() + " " + user.getLastName());
                        vBox.getChildren().add(temp);

                    }
                }
            }
        });


        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();

        engine.locationProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue.startsWith(REDIRECT_URL)){
                    tokenUrl = newValue;
                    tokenUrl = tokenUrl.substring(REDIRECT_URL.length()+6);
                    authWebView.setVisible(false);
                    userName.setVisible(true);
                    userTextField.setVisible(true);
                    submitBtn.setVisible(true);
                }
            }
        });

    }


}