import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.fave.responses.GetPhotosResponse;
import com.vk.api.sdk.objects.fave.responses.GetPostsResponse;
import com.vk.api.sdk.objects.fave.responses.GetUsersResponse;
import com.vk.api.sdk.objects.fave.responses.GetVideosResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.WallpostFull;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebEngine;
import java.util.List;


public class VkAuth{
    private static boolean requestConfirmed = false;
    private static String tokenUrl;
    private static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    private static final int APP_ID = 6279038;
    private static final String CLIENT_SECRET = "S1br1PrMDxCCG4Mi8xpo";
    private static final String PERMISSIONS = "friends";
    private static final String VK_AUTH_URL = "https://oauth.vk.com/authorize?" +
            "client_id=" + APP_ID + "&" +
            "display=page&" +
            "redirect_uri=" + REDIRECT_URL + "&" +
            "scope=" + PERMISSIONS + "&" +
            "response_type=code&" +
            "v=5.69";


    public static void invokeRequest(WebEngine engine){
        if(engine == null) return;
        engine.load(VK_AUTH_URL);

        engine.locationProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue.startsWith(REDIRECT_URL) && !requestConfirmed){
                    DBController.removeAll();
                    tokenUrl = newValue;
                    tokenUrl = tokenUrl.substring(REDIRECT_URL.length()+6);

                    TransportClient transportClient = HttpTransportClient.getInstance();
                    VkApiClient vk = new VkApiClient(transportClient);
                    try {
                        UserAuthResponse authResponse = vk.oauth()
                                .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URL, tokenUrl)
                                .execute();

                        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());

                        GetUsersResponse usersResponse = vk.fave().
                                getUsers(actor).
                                execute();
                        List<UserMin> users = usersResponse.getItems();
                        DBController.dbAddUsers(users);

                        GetPostsResponse response = vk.fave().
                                getPosts(actor).
                                execute();
                        List<WallpostFull> posts = response.getItems();
                        DBController.dbAddPosts(posts);

                        GetPhotosResponse photosResponse = vk.fave().
                                getPhotos(actor).
                                execute();
                        List<Photo> photos = photosResponse.getItems();
                        DBController.dbAddPhotos(photos);

                        GetVideosResponse videosResponse = vk.fave().
                                getVideos(actor).
                                execute();
                        List<Video> videos = videosResponse.getItems();
                        DBController.dbAddVideos(videos);


                        requestConfirmed = true;
                    } catch (ApiException|ClientException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}