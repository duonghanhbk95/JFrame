/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.List;

/**
 * insert cluster into DB
 *
 * @author Hanh Nguyen
 */
public class CollectionCentroid {

    public void insertMeaningCentroid(List<Cluster> clusters, DBCollection centroid) {
       
        for (int i = 0; i < clusters.size(); i++) {
            BasicDBObject dbObject = new BasicDBObject("meaning_id", i + 1);
            BasicDBObject centroidField = new BasicDBObject();
            centroidField.append("goal", clusters.get(i).getMeaningCentroid().getGoal());
            centroidField.append("task", clusters.get(i).getMeaningCentroid().getTask());
            centroidField.append("quality", clusters.get(i).getMeaningCentroid().getQuality());
            centroidField.append("resource", clusters.get(i).getMeaningCentroid().getResource());
            dbObject.append("meaning_centroid", centroidField);
            
            dbObject.append("numberOfCluster2", 0);
            centroid.insert(dbObject);

        }
    }
    
    

    public void insertFrequencyCentroid(Cluster meaning_cluster, List<Cluster> frequency_clusters, DBCollection centroid) {

       
        
        // find meaning_centroid
        BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
        DBCursor newCursor = centroid.find(whereVector.add("meaning_id", meaning_cluster.meaning_id + 1).get());
        DBCursor oldCursor = centroid.find(whereVector.add("meaning_id", meaning_cluster.meaning_id + 1).get());
        
        // creating new centroid
        BasicDBObject oldCentroid = (BasicDBObject)oldCursor.next();
        BasicDBObject newCentroid = (BasicDBObject) newCursor.next();
        BasicDBObject newCentroidSet = new BasicDBObject();
        
        // creating field
        for (int i = 0; i < frequency_clusters.size(); i++) {
            int num = i + 1;
            BasicDBObjectBuilder fre_centroid = new BasicDBObjectBuilder();
           
            fre_centroid.add("numGoal", frequency_clusters.get(i).getFrequencyCentroid().getNumGoal());
            fre_centroid.add("numTask", frequency_clusters.get(i).getFrequencyCentroid().getNumTask());
            fre_centroid.add("numQuality", frequency_clusters.get(i).getFrequencyCentroid().getNumQuality());
            fre_centroid.add("numResource", frequency_clusters.get(i).getFrequencyCentroid().getNumResource());
            fre_centroid.add("numLink", frequency_clusters.get(i).getFrequencyCentroid().getNumLink());
            fre_centroid.add("frequency_id",frequency_clusters.get(i).frequency_id+1);
            
            newCentroid.append("frequency_centroid " + num, fre_centroid.get());

        }
        newCentroid.append("numberOfCluster2", frequency_clusters.size());
        // update centroid
        centroid.save(newCentroid);
//        newCentroidSet.put("$set", newCentroid);
//        
//        
//        
//        
//        centroid.update(oldCentroid, newCentroidSet);
    }
}
