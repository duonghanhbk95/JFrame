/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QueryDB.getData;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Vector {
   
    public void createCollectionVector(DBCollection original, DBCollection vector) {

        DBCursor iCursor = original.find();

        int i = 1;

        Grouping group = new Grouping();

        while (iCursor.hasNext()) {

            Type type = new Type();
            List listType = new ArrayList();

            DBObject dbObj = (DBObject) iCursor.next();

//            System.out.println("sizeGoal " + type.getGroup(dbObj, "istar.Goal").size());
            listType = group.getVector(type.getGroup(dbObj, "istar.Goal"), type.getGroup(dbObj, "istar.Task"),
                    type.getGroup(dbObj, "istar.Quality"), type.getGroup(dbObj, "istar.Resource"));

            BasicDBObject vectorField = new BasicDBObject();
            vectorField.put("id_model", i);

            // creating field meaning_vector
            BasicDBObject meaning_vector = new BasicDBObject();
            meaning_vector.append("goal", listType.get(0)).append("task", listType.get(1));
            meaning_vector.append("quality", listType.get(2)).append("resource", listType.get(3));
            vectorField.put("meaning_vector", meaning_vector);

            // creating field frequency_vector
            BasicDBObject frequency_vector = new BasicDBObject();
            frequency_vector.append("numGoal", type.getGroup(dbObj, "istar.Goal").size());
            frequency_vector.append("numTask", type.getGroup(dbObj, "istar.Task").size());
            frequency_vector.append("numQuality", type.getGroup(dbObj, "istar.Quality").size());
            frequency_vector.append("numResource", type.getGroup(dbObj, "istar.Resource").size());
            frequency_vector.append("numLink", type.Link(dbObj));

            vectorField.put("frequency_vector", frequency_vector);

            //insert values into collection vector
            vector.insert(vectorField);

            i++;
        }

        vector.createIndex("meaning_id");
        vector.createIndex("frequency_id");

    }
}
