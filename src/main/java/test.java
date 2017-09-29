  import com.mongodb.*;

public class test {
    public static void main(String[] args) {

        try {
            MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));

            DB db = mongoClient.getDB("mydb");

            DBCollection coll = db.getCollection("testCollection");
            mongoClient.setWriteConcern(WriteConcern.JOURNALED);

            BasicDBObject doc = new BasicDBObject("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("info", new BasicDBObject("x", 123123123).append("y", 345));
            coll.insert(doc);

        DBCursor cursor = coll.find();
        try{
            while(cursor.hasNext()){
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }


        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
    }

}

/*
mongo
show dbs
use mydb
show collections
db.testCollection.find()
*/