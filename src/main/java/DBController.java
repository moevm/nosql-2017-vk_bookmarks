import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.regex;

public class DBController {
    private static String strCollPeople = "people";
    private static String strCollPhoto = "photo";
    private static String strCollVideo = "video";
    private static String strCollPosts = "posts";
    private static final String databaseName = "bookmarks";

    private static MongoClient mongoClient = new MongoClient("localhost", 27017);
    private static MongoDatabase mongoDB = mongoClient.getDatabase(databaseName);

    public static void removeAll(){
        for(String name : mongoDB.listCollectionNames()){
            mongoDB.getCollection(name).drop();
        }
    }

    public static void dbAddUsers(List<UserMin> users){
        try {
            MongoCollection<Document> coll = mongoDB.getCollection(strCollPeople);
            for (UserMin user : users) {
                Document doc = new Document("_id", user.getId())
                        .append("name", user.getFirstName())
                        .append("surname", user.getLastName());
                coll.insertOne(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dbAddPosts(List<WallpostFull> posts){
        try {
            MongoCollection<Document> coll = mongoDB.getCollection(strCollPosts);
            for (WallpostFull post : posts) {
                Document doc = new Document("_id", post.getId())
                        .append("text", post.getText())
                        .append("ownerid", post.getOwnerId());
                coll.insertOne(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dbAddPhotos(List<Photo> photos){
        try {
            MongoCollection<Document> coll = mongoDB.getCollection(strCollPhoto);
            for (Photo photo : photos) {
                Document doc = new Document("_id", photo.getId())
                        .append("text", photo.getText())
                        .append("ownerid", photo.getOwnerId());
                coll.insertOne(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dbAddVideos(List<Video> videos){
        try {
            MongoCollection<Document> coll = mongoDB.getCollection(strCollVideo);
            for (Video video : videos) {
                Document doc = new Document("_id", video.getId())
                        .append("text", video.getDescription())
                        .append("ownerid", video.getOwnerId());
                coll.insertOne(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Document> getPeople(){
        List<Document> list = new LinkedList<Document>();
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPeople).find().iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getPeople(String request){
        List<Document> list = new LinkedList<Document>();
        String pattern = ".*" + request + ".*";
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPeople).find(regex("name", pattern)).iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getPhoto(){
        List<Document> list = new LinkedList<Document>();
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPhoto).find().iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getPhoto(String request){
        List<Document> list = new LinkedList<Document>();
        String pattern = ".*" + request + ".*";
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPhoto).find(regex("text", pattern)).iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getPost(){
        List<Document> list = new LinkedList<Document>();
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPosts).find().iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getPost(String request){
        List<Document> list = new LinkedList<Document>();
        String pattern = ".*" + request + ".*";
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPosts).find(regex("text", pattern)).iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getVideo(){
        List<Document> list = new LinkedList<Document>();
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollVideo).find().iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }

    public static List<Document> getVideo(String request){
        List<Document> list = new LinkedList<Document>();
        String pattern = ".*" + request + ".*";
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollVideo).find(regex("text", pattern)).iterator();
        try {
            while(cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }
}
