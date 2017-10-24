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
    private int id_model;
    

    public void insert(String path, DBCollection original) throws UnknownHostException {

        //drop all collection
        

        // Insert Document 1
        ReadJSON js = new ReadJSON();
        JSONArray array = js.readFolder(path);

        id_model = original.find().count() + 1;

        for (Object obj : array) {
            BasicDBObject dbObject = (BasicDBObject) JSON.parse(obj.toString());
            dbObject.append("id_model", id_model);
            original.insert(dbObject);
            id_model++;
        }

        original.createIndex("id_model");

    }
}
