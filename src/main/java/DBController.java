import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vk.api.sdk.objects.audio.AudioFull;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.video.Video;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.regex;

public class DBController {
    private static String strCollPeople = "people";
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
                        .append("username", user.getFirstName() + " " + user.getLastName());
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
                List<WallpostAttachment> attachments = post.getAttachments();

                if(attachments!=null) {
                    StringBuilder tempAudio = new StringBuilder();
                    tempAudio.delete(0, tempAudio.length());
                    for (WallpostAttachment temp : attachments) {
                        if (temp == null) continue;
                        AudioFull audio = temp.getAudio();
                        if (audio == null) continue;
                         tempAudio.append(audio.getArtist()).append(" - ").append(audio.getTitle()).append("\n");
                    }

                Document doc = new Document("_id", post.getId())
                        .append("text", post.getText())
                        .append("ownerid", post.getOwnerId())
                        .append("fromid", post.getFromId())
                        .append("attachments", tempAudio.toString());
                coll.insertOne(doc);
                }
                else {
                    Document doc = new Document("_id", post.getId())
                            .append("text", post.getText())
                            .append("ownerid", post.getOwnerId())
                            .append("fromid", post.getFromId())
                            .append("attachments", "");
                    coll.insertOne(doc);
                }
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
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPeople).find(regex("username", pattern)).iterator();
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

    public static List<Document> getPostAttachments(String request){
        List<Document> list = new LinkedList<Document>();
        String pattern = ".*" + request + ".*";
        MongoCursor<Document> cursor = mongoDB.getCollection(strCollPosts).find(regex("attachments", pattern)).iterator();
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
