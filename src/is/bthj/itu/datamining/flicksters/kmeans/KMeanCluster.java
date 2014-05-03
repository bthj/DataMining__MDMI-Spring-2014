package is.bthj.itu.datamining.flicksters.kmeans;

import java.util.Set;

import uk.me.jstott.jcoord.LatLng;

public abstract class KMeanCluster<T, M> {

	protected Set<T> clusterMembers;

	
	public abstract float getTupleDistanceToClusterMean( T tuple );
	
	public abstract M getClusterMean();
	
	/**
	 * For computation of sum of squared error
	 */
	public abstract float getSumOfSquaredDistancesToMean();
	
	/**
	 * Update cluster mean value, based on its current set of members
	 */
	public abstract void updateClusterMean();
	
	
	/**
	 * Add data object (tuple) to this cluster
	 */
	public void addToCluster( T dataObject ) {
		
		clusterMembers.add( dataObject );
	}
	
	/**
	 * Remove data object from this cluster
	 */
	public void removeFromCluster( T dataObject ) {
		
		clusterMembers.remove( dataObject );
	}
	
	/**
	 * Checks if a given tuple is in a given cluster
	 */
	public boolean containsObject( T dataObject ) {
		
		return clusterMembers.contains(dataObject);
	}
	
	
	public int size() {
		return clusterMembers.size();
	}
	
	public Set<T> getClusterMembers() {
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
