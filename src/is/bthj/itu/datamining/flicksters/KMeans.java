package is.bthj.itu.datamining.flicksters;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KMeans<T> {

	/**
	 * Compute k-means clusters.
	 * This implementation is dependent on the DataCollectionQuestionaire type:
	 */
	public List<KMeanCluster<T>> kMeansPartition( int k, List<T> data) {
		
		List<KMeanCluster<T>> clusters = initializeClusters( k, data );
		
		boolean noChange = false;
		int clusterIterationCount = 0;
		while( false == noChange ) {
			
			System.out.println( "Clustering iteration #" + ++clusterIterationCount );
			
			noChange = true;
			
			int tupleCount = 0;
			for( T oneTuple : data ) {
				if( tupleCount % 1000 == 0 ) System.out.println("Processing tuple #" + tupleCount);
				
				boolean oneChange = assignTupleToCluster( oneTuple, clusters );
				
				if( oneChange ) {
					noChange = false;
				}
				tupleCount++;
			}
		}
		
		return clusters;
	}
	
	/**
	 * Arbritrarily choose k objects from D as the initial cluster centers
	 */
	private List<KMeanCluster<T>> initializeClusters( int k, List<T> data ) {
		List<KMeanCluster<T>> clusters = new ArrayList<KMeanCluster<T>>();
		
		for( T oneSeedObject : getKRandomObjectsFromDataset(k, data) ) {

			clusters.add( KMeanClusterFactory.createKMeanClusterWithSeedObject(oneSeedObject) );
		}
		return clusters;
	}
	
	/**
	 * Get k random tuples from the data set 
	 */
	private List<T> getKRandomObjectsFromDataset( 
			int k, List<T> data ) {
		
		// let's mix up the order of elements
		Collections.shuffle( data );
		// and then return (a view to) the k first
		return data.subList( 0, k );
	}
	
	
	private boolean assignTupleToCluster( T oneTuple, List<KMeanCluster<T>> clusters ) {
		
		boolean clusterChange = false;
		
		int lowestDistanceClusterIndex = Integer.MAX_VALUE;
		int tupleClusterIndex = Integer.MIN_VALUE; // index to the cluster that contains this tuple, if any
		float lowestDistanceToClusterMean = Float.MAX_VALUE;
		for( int i = 0; i < clusters.size(); i++ ) {
			
			// TODO: For efficiency we should compute the cluster means once per rearrangement pass!
			float currentClusterMeanTupleDistance = 
					// getTupleEuclideanDistanceToValue( oneTuple, clusters.get(i).getClusterMean() );
					clusters.get(i).getTupleDistanceToClusterMean(oneTuple);
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
}
