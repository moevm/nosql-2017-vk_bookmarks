import com.mongodb.*;
import com.vk.api.sdk.objects.users.UserFull;
import java.util.List;

public class DBController {

    static MongoClient mongoClient;
    static DB db;
    static DBCollection usersColl;

    public static void initializeDb(){
        try {
            String strCollUsers = "users";
            mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
            db = mongoClient.getDB("mydb");
            db.getCollection(strCollUsers).drop();

        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void dbAddUsers(List<UserFull> users){
        try {
            DBCollection coll = db.getCollection("users");
            mongoClient.setWriteConcern(WriteConcern.JOURNALED);
            for (UserFull user : users) {
                BasicDBObject doc = new BasicDBObject("_id", user.getId())
                        .append("name", user.getFirstName())
                        .append("surname", user.getLastName())
                        .append("status", user.getStatus());
                coll.insert(doc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
