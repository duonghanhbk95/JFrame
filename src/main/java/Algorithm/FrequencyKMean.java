/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ConnectDB.ConnectionDB;
import ConnectDB.MyConstants;
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
public class FrequencyKMean {

	public int NUM_CLUSTERS_FREQUENCY = 0;
	public List<Point> frequency_points;
	public final List<Cluster> frequency_clusters;

	public static int iterationFrequency = 0;

	public FrequencyKMean() {
		this.frequency_clusters = new ArrayList();
		this.frequency_points = new ArrayList();
	}

	public Point chooseCentroidFre(List<Point> points, List<Cluster> clusters, int id) {
		Point centroid1 = null;
		double max = Double.MIN_VALUE;

		if (clusters.isEmpty()) {
			centroid1 = frequency_points.get(id);
			return centroid1;

		}
		int j = 1;
		for (Point point : points) {
			int i = 1;
			System.out.println("point " + j + ":" + j);
			double sum = 0;
			for (Cluster cluster : clusters) {
				sum += Point.distanceFrequency(cluster.getFrequencyCentroid(), point);
				System.out.println("sum " + i + ": " + sum);
				i++;
			}
			if (sum > max) {
				max = sum;

				System.out.println("max " + max);
				centroid1 = point;
			}
			j++;
		}
		return centroid1;
	}

	// init frequency points
	private void initFrequencyPoints(Cursor cursor) {
		try {
			frequency_points = Point.getFrequencyPoints(cursor);
			frequency_clusters.clear();

			for (int i = 0; i < NUM_CLUSTERS_FREQUENCY; i++) {
				Cluster cluster = new Cluster(i);
				Point centroid = frequency_points.get(i);

				cluster.setFrequencyCentroid(centroid);
				frequency_clusters.add(cluster);
			}
		} catch (Exception e) {
			System.out.println("points < num_clusters_frequency");
		}

	}

	private void calculate() {
		boolean finish = false;
		int iteration = 0;
		while (!finish) {
			// Clear cluster state
			clearClusters();

			List<Point> lastCentroids = getCentroids();

			// Assign points to the closer cluster
			assignCluster();

			// Calculate new centroids.
			calculateFrequencyCentroids();

			plotFrequencyClusters();
			iterationFrequency++;
			

			List<Point> currentCentroids = getCentroids();

			double distance = 0;
			for (int i = 0; i < lastCentroids.size(); i++) {
				distance += Point.distanceFrequency(lastCentroids.get(i), currentCentroids.get(i));

				System.out.println("distance" + distance);
			}
			System.out.println("#################");
			System.out.println("Iteration: " + iterationFrequency);
			System.out.println("Centroid distances: " + distance);

			if (distance == 0) {
				finish = true;

			}
		}
		plotFrequencyClusters();
	}

	private List getCentroids() {
		List centroids = new ArrayList(NUM_CLUSTERS_FREQUENCY);
		for (Cluster cluster : frequency_clusters) {
			Point aux = cluster.getFrequencyCentroid();
			Point point = new Point(aux.getNumGoal(), aux.getNumTask(), aux.getNumQuality(), aux.getNumResource(),
					aux.getNumLink());
			centroids.add(point);
		}
		return centroids;
	}

	private void clearClusters() {
		frequency_clusters.forEach((cluster) -> {
			cluster.clearFrequency();
		});
	} // ok!

	private void assignCluster() {
		double max = Double.MAX_VALUE;
		double min = max;
		int cluster = 0;
		double distance = 0.0;

		for (Point point : frequency_points) {
			min = max;
			for (int i = 0; i < NUM_CLUSTERS_FREQUENCY; i++) {
				Cluster c = frequency_clusters.get(i);
				distance = Point.distanceFrequency(point, c.getFrequencyCentroid());
				if (distance < min) {
					min = distance;
					cluster = i;
				}
			}
			point.setCluster(cluster);
			frequency_clusters.get(cluster).addFrequencyPoints(point);
		}
	}

	private void calculateFrequencyCentroids() {
		for (Cluster cluster : frequency_clusters) {

			double SumGoal = 0;
			double SumTask = 0;
			double SumQuality = 0;
			double SumResource = 0;
			double SumLink = 0;

			List<Point> list = cluster.getFrequencyPoints();

			int n_points = list.size();

			for (Point p : list) {
				SumGoal += p.getNumGoal();
				SumTask += p.getNumTask();
				SumQuality += p.getNumQuality();
				SumResource += p.getNumResource();
				SumLink += p.getNumLink();
			}

			if (n_points > 0) {
				double newGoal = SumGoal / n_points;
				double newTask = SumTask / n_points;
				double newQuality = SumQuality / n_points;
				double newResource = SumResource / n_points;
				double newLink = SumLink / n_points;

				cluster.setFrequencyCentroid(new Point(newGoal, newTask, newQuality, newResource, newLink));

			}
			System.out.println("List============ " + list);
		}

	}

	private void plotFrequencyClusters() {
		for (int i = 0; i < NUM_CLUSTERS_FREQUENCY; i++) {
			Cluster c = frequency_clusters.get(i);
			c.plotFrequencyCluster();
		}
	} // ok!

	public void insertFrequency_id(List<Cluster> frequency_clusters, Cluster meaning_cluster, DBCollection vector) {

		for (Cluster cluster : frequency_clusters) {
			BasicDBObjectBuilder whereVector1 = BasicDBObjectBuilder.start();
			whereVector1.add("meaning_id", meaning_cluster.meaning_id + 1);
			for (int i = 0; i < cluster.getFrequencyPoints().size(); i++) {

				BasicDBObjectBuilder whereVector2 = BasicDBObjectBuilder.start();

				whereVector2.add("numGoal", cluster.frequency_points.get(i).getNumGoal());
				whereVector2.add("numTask", cluster.frequency_points.get(i).getNumTask());
				whereVector2.add("numQuality", cluster.frequency_points.get(i).getNumQuality());
				whereVector2.add("numResource", cluster.frequency_points.get(i).getNumResource());
				whereVector2.add("numLink", cluster.frequency_points.get(i).getNumLink());

				whereVector1.add("frequency_vector", whereVector2.get());

				// insert field frequency_id into vector collection
				DBCursor cursor = vector.find(whereVector1.get());
				while (cursor.hasNext()) {
					BasicDBObject obj = (BasicDBObject) cursor.next();
					obj.append("frequency_id", cluster.getFrequencyId() + 1);
					vector.save(obj);
				}

				//
				// vector.update(vector.find(whereVector1.get()).next(),
				// obj.append("frequency_id", cluster.getFrequencyId() + 1));
			}

		}

	}

	ConnectionDB connect = new ConnectionDB();
	DBCollection model = connect.connect(MyConstants.ORIGINAL_MODEL_NAME);
	DBCollection clusterCol = connect.connect(MyConstants.CLUSTER_COLLECTION_NAME);

	public void insertClusterCol(Cluster meaning_cluster, Cluster frequency_cluster, DBCursor cursor) {

		// find model which has id_model the same field into vector collection
		while (cursor.hasNext()) {
			BasicDBObjectBuilder whereModel = BasicDBObjectBuilder.start();
			whereModel.add("id_model", cursor.next().get("id_model"));
			DBCursor cursor2 = model.find(whereModel.get());

			BasicDBObject values = (BasicDBObject) cursor2.next();
			values.append("meaning_id", meaning_cluster.meaning_id + 1).append("frequency_id",
					frequency_cluster.frequency_id + 1);
			clusterCol.insert(values);

		}

	}

	// clustering frequency

	public void executeFrequency(DBCursor cursor) {

		initFrequencyPoints(cursor);

		calculate();

	}
	// clustering level 2 based on frequency

	public void execute2(List<Cluster> meaning_clusters, DBCollection vector, DBCollection centroid) {

		CollectionCentroid db = new CollectionCentroid();

		 for (Cluster cluster : meaning_clusters) {
		 BasicDBObjectBuilder whereVector1 = BasicDBObjectBuilder.start();
		 DBCursor cursor = vector.find(whereVector1.add("meaning_id",
		 cluster.meaning_id + 1).get());
		
		 executeFrequency(cursor);
		
		
		 insertFrequency_id(frequency_clusters, cluster, vector);
		 db.insertFrequencyCentroid(cluster, frequency_clusters, centroid);
		
		 BasicDBObjectBuilder whereVector2 = BasicDBObjectBuilder.start();
		 whereVector2.add("meaning_id", cluster.meaning_id + 1);
		 for (int i = 0; i < frequency_clusters.size(); i++) {
		
		 whereVector2.add("frequency_id", frequency_clusters.get(i).frequency_id + 1);
		
		 DBCursor cursorVector = vector.find(whereVector2.get());
		
		 insertClusterCol(cluster, frequency_clusters.get(i), cursorVector);
		
		 }
		
		
		 }
		
		//cluster for frequency test
//		for (int j = 1; j <= 15; j++) {
//			BasicDBObjectBuilder whereVector = BasicDBObjectBuilder.start();
//			DBCursor point = vector.find(whereVector.add("meaning_id", j).get());
//			
//			
//			System.out.println(j);
//			if (point.count() > 15) {
//				executeFrequency(point);
//				
//				for (Cluster cluster : frequency_clusters) {
//					BasicDBObjectBuilder whereVector1 = BasicDBObjectBuilder.start();
//					whereVector1.add("meaning_id", j);
//					for (int i = 0; i < cluster.getFrequencyPoints().size(); i++) {
//
//						BasicDBObjectBuilder whereVector2 = BasicDBObjectBuilder.start();
//
//						whereVector2.add("numGoal", cluster.frequency_points.get(i).getNumGoal());
//						whereVector2.add("numTask", cluster.frequency_points.get(i).getNumTask());
//						whereVector2.add("numQuality", cluster.frequency_points.get(i).getNumQuality());
//						whereVector2.add("numResource", cluster.frequency_points.get(i).getNumResource());
//						whereVector2.add("numLink", cluster.frequency_points.get(i).getNumLink());
//
//						whereVector1.add("frequency_vector", whereVector2.get());
//
//						
//						DBCursor cursor = vector.find(whereVector1.get());
//						while (cursor.hasNext()) {
//							BasicDBObject obj = (BasicDBObject) cursor.next();
//							obj.append("frequency_id", cluster.getFrequencyId() + 1);
//							vector.save(obj);
//						}
//
//						
//					}
//
//				}
//				  // find meaning_centroid
//		        BasicDBObjectBuilder Vector = BasicDBObjectBuilder.start();
//		        DBCursor newCursor = centroid.find(Vector.add("meaning_id", j).get());
//		        DBCursor oldCursor = centroid.find(Vector.add("meaning_id", j).get());
//		        
//		        // creating new centroid
//		        BasicDBObject oldCentroid = (BasicDBObject)oldCursor.next();
//		        BasicDBObject newCentroid = (BasicDBObject) newCursor.next();
//		        BasicDBObject newCentroidSet = new BasicDBObject();
//		        
//		        // creating field
//		        for (int i = 0; i < frequency_clusters.size(); i++) {
//		            int num = i + 1;
//		            BasicDBObjectBuilder fre_centroid = new BasicDBObjectBuilder();
//		           
//		            fre_centroid.add("numGoal", frequency_clusters.get(i).getFrequencyCentroid().getNumGoal());
//		            fre_centroid.add("numTask", frequency_clusters.get(i).getFrequencyCentroid().getNumTask());
//		            fre_centroid.add("numQuality", frequency_clusters.get(i).getFrequencyCentroid().getNumQuality());
//		            fre_centroid.add("numResource", frequency_clusters.get(i).getFrequencyCentroid().getNumResource());
//		            fre_centroid.add("numLink", frequency_clusters.get(i).getFrequencyCentroid().getNumLink());
//		            fre_centroid.add("frequency_id",frequency_clusters.get(i).frequency_id+1);
//		            
//		            newCentroid.append("frequency_centroid " + num, fre_centroid.get());
//
//		        }
//		        
//		        System.out.println(frequency_clusters.size());
//		        newCentroid.append("numberOfCluster2", frequency_clusters.size());
//		       
//		        centroid.save(newCentroid);
//		         BasicDBObjectBuilder whereVector2 = BasicDBObjectBuilder.start();
//				 whereVector2.add("meaning_id",j);
//				 for (int i = 0; i < frequency_clusters.size(); i++) {
//
//					whereVector2.add("frequency_id", frequency_clusters.get(i).frequency_id + 1);
//
//					DBCursor cursorVector = vector.find(whereVector2.get());
//
//					
//					while (cursorVector.hasNext()) {
//						BasicDBObjectBuilder whereModel = BasicDBObjectBuilder.start();
//						whereModel.add("id_model", cursorVector.next().get("id_model"));
//						DBCursor cursor2 = model.find(whereModel.get());
//
//						BasicDBObject values = (BasicDBObject) cursor2.next();
//						values.append("meaning_id", j).append("frequency_id",frequency_clusters.get(i).frequency_id + 1);
//						clusterCol.insert(values);
//
//					}
//
//				
//				 }
//		    
//			}
//
//		}

	}
}
