package is.bthj.itu.datamining.clustering;

import is.bthj.itu.datamining.FileWritingHelper;
import is.bthj.itu.datamining.preprocessing.CSVFileReader;
import is.bthj.itu.datamining.preprocessing.QuestionairePreProcessor;
import is.bthj.itu.datamining.preprocessing.data.DataCollectionQuestionaire;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KMeans {

	public static void main(String[] args) {
		// create clusters with different numbers of partitions
		
		KMeans kMeans = new KMeans();
		String[][] data;
		try {
			
			data = CSVFileReader.read( "data_mining_2014_dataset.csv", false );
			
			QuestionairePreProcessor preProcessor = new QuestionairePreProcessor();
			List<DataCollectionQuestionaire> questionaireTuples = 
					preProcessor.getCleanedQuestionaires( data );
			
			
			/*
			 * let's try a range of k partitions and print the resulting 
			 * sums of square errors.
			 */
			Map<Integer, Float> kVSsumOfSquaredErrors = new TreeMap<Integer, Float>(); // collection to save to file, for plotting
			int onePartitionSizeRepetition = 10;
			for( int k = 2; k <= 10; k++ ) {
			
				// let's repeat each partitioning size k and take an average
				
				float averageSumOfSquareErrors = 
						kMeans.getAverageOfSquareErrorSumsForKPartitions(
								onePartitionSizeRepetition, k, questionaireTuples);
				
				kVSsumOfSquaredErrors.put(k, averageSumOfSquareErrors);
				
				System.out.println( "Average of " + onePartitionSizeRepetition 
						+ " sums of square errors for partition size k = " + k 
						+ ": " + averageSumOfSquareErrors );
				
				FileWritingHelper.writeMapToFile( kVSsumOfSquaredErrors, "kVSsumOfSquaredErrors__k_2-10.tab" );
				
				
				// the following to partition only once per partion size:
				
				//List<KMeanCluster> clusters = kMeans.kMeansPartition( k, questionaireTuples );
				
				// kMeans.printSumOfSquaredErrors( k, clusters );
				
				// kMeans.printClusters( k, clusters );
			}
			
		} catch ( IOException e ) {
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	
	/**
	 * Compute k-means clusters.
	 * This implementation is dependent on the DataCollectionQuestionaire type:
	 * TODO: Use generics instead of depending on a specific data type.
	 */
	
	public List<KMeanCluster> kMeansPartition( int k, List<DataCollectionQuestionaire> data) {
		
		List<KMeanCluster> clusters = initializeClusters( k, data );
		
		boolean noChange = false;
		while( false == noChange ) {
			
			noChange = true;
			
			for( DataCollectionQuestionaire oneTuple : data ) {
				
				boolean oneChange = assignTupleToCluster( oneTuple, clusters );
				
				if( oneChange ) {
					noChange = false;
				}
			}
		}
		
		return clusters;
	}
	
	/**
	 * Compute partitioning n times 
	 * and return the average of the resulting summed square errors. 
	 */
	public float getAverageOfSquareErrorSumsForKPartitions( 
			int n, int k, List<DataCollectionQuestionaire> data ) {
		
		float errorSumOfSums = 0;
		
		for( int i = 0; i < n; i++ ) {
			
			List<KMeanCluster> partition = kMeansPartition( k, data );
			errorSumOfSums += getSquaredErrorOfKMeansPartition( partition );
		}
		return errorSumOfSums / n;
	}
	
	public void printClusters( int k, List<KMeanCluster> clusters ) {
		
		System.out.println( "Clusters for k = " + k );
		for( KMeanCluster oneCluster : clusters ) {
			System.out.println( oneCluster );
		}
	}
	
	public void printSumOfSquaredErrors( int k, List<KMeanCluster> clusters ) {
		
		float sumOfSquaredErrors = getSquaredErrorOfKMeansPartition(clusters);
		System.out.println( "Error for k = " + k + ": " + sumOfSquaredErrors );
	}
	
	/**
	 * For each object in each cluster, square the distance from the object
	 * to its cluster center, and sum the distances.
	 * 
	 * @param partition List of clusters forming the k-Means partition
	 * @return
	 */
	public float getSquaredErrorOfKMeansPartition( List<KMeanCluster> partition ) {
		float squaredErrorSum = 0;
		
		for( KMeanCluster oneCluster : partition ) {
			
			float oneClusterMean = oneCluster.getClusterMean();
			
			for( DataCollectionQuestionaire oneObject : oneCluster.getClusterMembers() ) {
				
				squaredErrorSum = (float)
						Math.pow( getTupleEuclideanDistanceToValue(oneObject, oneClusterMean), 2 );
			}
		}
		return squaredErrorSum;
	}
	
	
	
	/**
	 * Arbritrarily choose k objects from D as the initial cluster centers
	 */
	private List<KMeanCluster> initializeClusters( int k, List<DataCollectionQuestionaire> data ) {
		List<KMeanCluster> clusters = new ArrayList<KMeanCluster>();
		
		for( DataCollectionQuestionaire oneSeedObject : getKRandomObjectsFromDataset(k, data) ) {
			
			KMeanCluster oneCluster = new KMeanCluster();
			oneCluster.addToCluster( oneSeedObject );
			clusters.add( oneCluster );
		}
		return clusters;
	}
	
	/**
	 * Get k random tuples from the data set 
	 */
	private List<DataCollectionQuestionaire> getKRandomObjectsFromDataset( 
			int k, List<DataCollectionQuestionaire> data ) {
		
		// let's mix up the order of elements
		Collections.shuffle( data );
		// and then return (a view to) the k first
		return data.subList( 0, k );
	}
	
	
	private boolean assignTupleToCluster( 
			DataCollectionQuestionaire oneTuple, List<KMeanCluster> clusters ) {
		
		boolean clusterChange = false;
		
		int lowestDistanceClusterIndex = Integer.MAX_VALUE;
		int tupleClusterIndex = Integer.MIN_VALUE; // index to the cluster that contains this tuple, if any
		float lowestDistanceToClusterMean = Float.MAX_VALUE;
		for( int i = 0; i < clusters.size(); i++ ) {
			
			// TODO: For efficiency we should compute the cluster means once per rearrangement pass!
			float currentClusterMeanTupleDistance = 
					getTupleEuclideanDistanceToValue( oneTuple, clusters.get(i).getClusterMean() );
			if( currentClusterMeanTupleDistance < lowestDistanceToClusterMean ) {
				lowestDistanceToClusterMean = currentClusterMeanTupleDistance;
				lowestDistanceClusterIndex = i;
			}
			
			// let's check if this tuple is already in the cluster we're currently looking at
			if( clusters.get(i).containsObject(oneTuple) ) {
				// and then save the index to this cluster
				tupleClusterIndex = i;
			}
		}
		// lowestDistanceClusterIndex now points to the cluster whose mean this tuple is closest to
		if( lowestDistanceClusterIndex != tupleClusterIndex ) {
			// the tuple isn't already in it's currently closest cluster
			// so let's put the tuple in to it, where it now belongs
			clusters.get(lowestDistanceClusterIndex).addToCluster( oneTuple );
			// the cluster we put this tuple into is not the same it was previously in, if any
			clusterChange = true;
			
			if( tupleClusterIndex != Integer.MIN_VALUE ) {
				// this tuple was already in another cluster, let's remove it from there
				clusters.get(tupleClusterIndex).removeFromCluster(oneTuple );
			}
		}
		
		return clusterChange;
	}
	 
	
	
	/**
	 * Get Euclidean distance from tuple's value to a given value 
	 */
	private float getTupleEuclideanDistanceToValue( DataCollectionQuestionaire tuple, float value ) {
		
		// it's unnecessary to use float instead of double in this project, 
		// but there might be some performance gain using that lower range data type...
		
		return (float) Math.sqrt( Math.pow( value - KMeans.getTupleValue(tuple), 2) );
	}
	
	
	/**
	 * Compute one data object's representative (normalized) value, 
	 * to be used for calculating means.
	 * 
	 */
	public static float getTupleValue( DataCollectionQuestionaire tuple ) {
		
		return tuple.getAge();
	}

}
