/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
import QueryDB.getData.Comparation;
import QueryDB.getData.Vector;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanh Nguyen
 */
public class Kmean{
//Number of Clusters. This metric should be related to the number of points

    // connect db
    public ConnectionDB connect = new ConnectionDB();
    public DBCollection vector = connect.connect(MyConstants.VECTOR_COLLECTION_NAME);
    public DBCollection centroid = connect.connect(MyConstants.CENTROID_COLLECTION_NAME);
    public DBCollection original = connect.connect(MyConstants.ORIGINAL_MODEL_NAME);
    public DBCollection cluster = connect.connect(MyConstants.CLUSTER_COLLECTION_NAME);
   
 
    public int NUM_CLUSTERS_MEANING = 0;
    public List<Point> meaning_points;

    public final List<Cluster> meaning_clusters;
    public static int iterationMeaning = 0;
    public Kmean() {
        this.meaning_points = new ArrayList();
        this.meaning_clusters = new ArrayList();
    }

    public Point chooseCentroidMeaning(List<Point> points, List<Cluster> clusters, Comparation sml, int id) {
        Point centroid1 = null;
        float min = Float.MAX_VALUE;

        if (clusters.isEmpty()) {

            centroid1 = meaning_points.get(id);
            return centroid1;

        }
        int j = 1;
        for (Point point : points) {
            int i = 1;
            System.out.println("point " + j + ":" + j);
            float sum = 0;
            for (Cluster cluster : clusters) {
                sum += sml.compare(cluster.getMeaningCentroid().getGoal(), point.getGoal()) + sml.compare(cluster.getMeaningCentroid().getQuality(), point.getQuality())
                        + sml.compare(cluster.getMeaningCentroid().getResource(), point.getResource()) + sml.compare(cluster.getMeaningCentroid().getTask(), point.getTask());
                System.out.println("sum " + i + ": " + sum);
                i++;

            }
            if (sum < min) {
                min = sum;

                System.out.println("min " + min);
                centroid1 = point;
            }
            j++;
        }

        return centroid1;
    }
//init meaning points
    Comparation sml = new Comparation();

    public void initMeaningPoints(Cursor cursor) {

        meaning_points = Point.getMeaningPoints(cursor);

        //Create Clusters
        //Set Centroids
        for (int i = 0; i < NUM_CLUSTERS_MEANING; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid2 = chooseCentroidMeaning(meaning_points, meaning_clusters, sml, i);
            cluster.setMeaningCentroid(centroid2);
            meaning_clusters.add(cluster);

        }

        plotMeaningClusters();
        System.out.println("----------------------------------");
        System.out.println("init");
        System.out.println("----------------------------------");
    }

    private void plotMeaningClusters() {
        for (int i = 0; i < NUM_CLUSTERS_MEANING; i++) {
            Cluster c = meaning_clusters.get(i);
            c.plotMeaningCluster();
        }
    } //ok!

    public void calculate() {
        boolean finish = false;
        

        // Add in new data, one at a time, recalculating centroids with each new one. 
        while (!finish) {
            //Clear cluster state
            clearClusters();

            List<Point> lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateMeaningCentroids();

            plotMeaningClusters();
            iterationMeaning++;

            List<Point> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            float finishState = 0;
            for (int i = 0; i < lastCentroids.size(); i++) {
                if (currentCentroids.get(i).getGoal().equals(lastCentroids.get(i).getGoal())
                        && currentCentroids.get(i).getTask().equals(lastCentroids.get(i).getTask())
                        && currentCentroids.get(i).getQuality().equals(lastCentroids.get(i).getQuality())
                        && currentCentroids.get(i).getResource().equals(lastCentroids.get(i).getResource())) {
                    finishState++;

                }
            }
            System.out.println("#################");
            System.out.println("Iteration: " + iterationMeaning);
            System.out.println("Centroid distances: " + finishState);

            if (finishState == NUM_CLUSTERS_MEANING) {
                finish = true;

            }
        }

        plotMeaningClusters();
    }

    private void clearClusters() {
        meaning_clusters.forEach((cluster) -> {
            cluster.clearMeaning();
        });
    } //ok!

    private List getCentroids() {
        List centroids = new ArrayList(NUM_CLUSTERS_MEANING);
        for (Cluster cluster : meaning_clusters) {
            Point aux = cluster.getMeaningCentroid();
            Point point = new Point(aux.getGoal(), aux.getTask(), aux.getQuality(), aux.getResource());
            centroids.add(point);
        }
        return centroids;
    }//ok!

    private void assignCluster() {
        float max;

        int cluster = 0;
        float distance = 0;
        int count = 0;
        for (Point point : meaning_points) {

            max = -4;
            for (int i = 0; i < NUM_CLUSTERS_MEANING; i++) {
                Cluster c = meaning_clusters.get(i);
                System.out.println("point" + "[" + count + "]:" + point + "\n");
                System.out.println("centroid" + "[" + i + "]:" + c.getMeaningCentroid() + "\n");

                distance = Point.distanceMeaning(point, c.getMeaningCentroid());

                System.out.println("distance" + "[" + i + "]:" + distance);
                if (distance >= max) {
                    max = distance;
                    cluster = i;

                    System.out.println("max:" + max + "\n");
                }

            }
            count++;
            point.setCluster(cluster);
            meaning_clusters.get(cluster).addMeaningPoints(point);

        }
        plotMeaningClusters();

    }

    private void calculateMeaningCentroids() {
        Comparation Sml = new Comparation();
        List emp = new ArrayList();

        int i = 0;
        for (Cluster cluster : meaning_clusters) {
            List<Point> list = cluster.getMeaningPoints();

            Representation r = new Representation();
            emp = r.represent(list, Sml);

            cluster.setMeaningCentroid(new Point(emp.get(0).toString(), emp.get(1).toString(), emp.get(2).toString(), emp.get(3).toString()));

            i++;
        }

    }

    public void insertMeaning_id(List<Cluster> meaning_clusters, DBCollection vector) {

        for (Cluster cluster : meaning_clusters) {

            for (int i = 0; i < cluster.getMeaningPoints().size(); i++) {

                BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
                whereVector.push("meaning_vector");
                whereVector.add("goal", cluster.meaning_points.get(i).getGoal());
                whereVector.add("task", cluster.meaning_points.get(i).getTask());
                whereVector.add("quality", cluster.meaning_points.get(i).getQuality());
                whereVector.add("resource", cluster.meaning_points.get(i).getResource());

                //insert field meaning_id into vector collection
                DBCursor cursor = vector.find(whereVector.get());
                while (cursor.hasNext()) {
                    BasicDBObject obj = (BasicDBObject) cursor.next();
                    obj.append("meaning_id", cluster.getMeaningId() + 1);
                    vector.save(obj);
                }

                System.out.println("-------------------------");
            }
        }
    }
    public void insertClusterColMeaning(List<Cluster> meaning_clusters, DBCollection vector, DBCollection model, DBCollection clusterCol) {
    	for(Cluster cluster : meaning_clusters) {
    		BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
            whereVector.add("meaning_id", cluster.meaning_id + 1);
            DBCursor cursorVector = vector.find(whereVector.get());
            while(cursorVector.hasNext()) {
            	BasicDBObjectBuilder whereModel = BasicDBObjectBuilder.start();
                whereModel.add("id_model", cursorVector.next().get("id_model"));
                DBCursor cursor2 = model.find(whereModel.get());

                BasicDBObject values = (BasicDBObject) cursor2.next();
                values.append("meaning_id", cluster.meaning_id + 1);
                clusterCol.insert(values);
            }
    	}
    	
    }
    
    // clustering level 1 based on meaning
    public void execute1(DBCollection centroid, DBCollection vector) {
        long startVectorCol = System.currentTimeMillis();
        Vector vectorCol = new Vector();
        vectorCol.createCollectionVector(original, vector);
        long endVectorCol = System.currentTimeMillis();
     
        long startinit = System.currentTimeMillis();
        DBCursor cursorMeaning = vector.find();
        initMeaningPoints(cursorMeaning);
        long endinit = System.currentTimeMillis();
        
        long startCalculate = System.currentTimeMillis();
        calculate();
        long endCalculate = System.currentTimeMillis();
        
      
        
        long startinsertMeaning = System.currentTimeMillis();
        insertMeaning_id(meaning_clusters, vector);
        long endinsertMeaning = System.currentTimeMillis();
       
        // creating table centroid
        long startCentroid = System.currentTimeMillis();
        CollectionCentroid db = new CollectionCentroid();
        db.insertMeaningCentroid(meaning_clusters, centroid);
        long endCentroid = System.currentTimeMillis();
        
        System.out.println("timeVector " + (endVectorCol - startVectorCol));
        System.out.println("timeInit " + (endinit - startinit));
        System.out.println("timeCalculate " + (endCalculate - startCalculate));
        
        System.out.println("timeinsertMeaning " + (endinsertMeaning - startinsertMeaning));
        System.out.println("timeInsertCentroid " + (endCentroid - startCentroid));
        System.out.println("total Tiem " + (endCentroid - startVectorCol));
    }

//    public static void main(String[] args) {
//       java.awt.EventQueue.invokeLater(() -> {
//           new JFrameStart().setVisible(true);
//       });
//    }
}
