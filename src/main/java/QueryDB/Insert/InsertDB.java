/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.Insert;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
import ReadFile.ReadJSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import org.json.simple.JSONArray;

/**
 *
 * @author Hanh Nguyen
 */
public class InsertDB {

    ConnectionDB connect = new ConnectionDB();
    DBCollection centroid = connect.connect(MyConstants.CENTROID_COLLECTION_NAME);
    DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);
    DBCollection original = connect.connect(MyConstants.ORIGINAL_MODEL_NAME);
    DBCollection cluster = connect.connect(MyConstants.CLUSTER_COLLECTION_NAME);

    public void insert(String path) throws UnknownHostException {

        //drop all collection
        centroid.drop();
        cluster.drop();
        original.drop();
        vector.drop();

        // Insert Document 1
        ReadJSON js = new ReadJSON();
        JSONArray array = js.readFolder(path);

        int i = 1;

        for (Object obj : array) {
            BasicDBObject dbObject = (BasicDBObject) JSON.parse(obj.toString());
            dbObject.append("id_model", i);
            original.insert(dbObject);
            i++;
        }

        original.createIndex("id_model");

    }
}
