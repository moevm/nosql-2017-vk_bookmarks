  import com.mongodb.*;
  import com.mongodb.client.MongoCollection;
  import com.mongodb.client.MongoCursor;
  import com.mongodb.client.MongoDatabase;
  import org.bson.Document;

  public class test {
      public static void main(String[] args) {
          MongoClient mongoClient = new MongoClient("localhost", 27017);
          MongoDatabase db = mongoClient.getDatabase("mydb");


          MongoCollection coll = db.getCollection("testCollection");

          Document doc = new Document("name", "MongoDB")
                  .append("type", "database")
                  .append("count", 1)
                  .append("info", new Document("x", 123123123).append("y", 345));
          coll.insertOne(doc);

          MongoCursor cursor = coll.find().iterator();
          while (cursor.hasNext()) {
              System.out.println(cursor.next());

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