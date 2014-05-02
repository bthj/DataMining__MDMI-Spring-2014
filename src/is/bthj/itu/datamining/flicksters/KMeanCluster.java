package is.bthj.itu.datamining.flicksters;

import java.util.List;
import java.util.Set;

public abstract class KMeanCluster<T> {

	protected Set<T> clusterMembers;


	/**
	 * Compute the mean of the all the objects' values in this cluster
	 */ 
	public abstract float getClusterMean();
	
	public abstract float getTupleDistanceToClusterMean( T tuple );
	
	
	/**
	 * Add data object (tuple) to this cluster
	 */
	public abstract void addToCluster( T dataObject );
	
	/**
	 * Remove data object from this cluster
	 */
	public abstract void removeFromCluster( T dataObject );
	
	
	/**
	 * For computation of sum of squared error
	 */
	public abstract float getSumOfSquaredDistancesToMean();
	
	
	/**
	 * Checks if a given tuple is in a given cluster
	 */
	public boolean containsObject( T dataObject ) {
		
		return clusterMembers.contains(dataObject);
	}
	
	
	public int size() {
		return clusterMembers.size();
	}
	
	Set<T> getClusterMembers() {
		return this.clusterMembers;
	}
	
 
	@Override
	public String toString() {
		String toPrintString = "-----------------------------------CLUSTER START------------------------------------------" + System.getProperty("line.separator");
		
		for( T i : this.clusterMembers ) {
			toPrintString += i.toString() + System.getProperty("line.separator");
		}
		toPrintString += "-----------------------------------CLUSTER END-------------------------------------------" + System.getProperty("line.separator");
		
		return toPrintString;
	}
}
