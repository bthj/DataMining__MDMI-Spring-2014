package is.bthj.itu.datamining.clustering;

import is.bthj.itu.datamining.preprocessing.data.DataCollectionQuestionaire;

import java.util.ArrayList;
import java.util.List;


public class KMeanCluster {

	private List<DataCollectionQuestionaire> clusterMembers;
	
	public KMeanCluster() {
		
		this.clusterMembers = new ArrayList<DataCollectionQuestionaire>();
	}

	
	/**
	 * Add data object (tuple) to this cluster
	 */
	public void addToCluster( DataCollectionQuestionaire dataObject ) {
		
		clusterMembers.add( dataObject );
	}
	
	/**
	 * Remove data object from this cluster
	 */
	public void removeFromCluster( DataCollectionQuestionaire dataObject ) {
		
		clusterMembers.remove( dataObject );
	}
	
	/**
	 * Compute the mean of the all the objects' values in this cluster
	 */
	public float getClusterMean() {
		float totalMemberValues = 0;
		for( DataCollectionQuestionaire oneTuple : clusterMembers ) {
			
			totalMemberValues += KMeans.getTupleValue( oneTuple );
		}
		return totalMemberValues / clusterMembers.size();
	}
	
	/**
	 * Checks if a given tuple is in a given cluster
	 */
	public boolean containsObject( DataCollectionQuestionaire dataObject ) {
		
		return clusterMembers.contains(dataObject);
	}
	
	
	public int size() {
		return clusterMembers.size();
	}
	
	List<DataCollectionQuestionaire> getClusterMembers() {
		return this.clusterMembers;
	}
	
	
	
	// Note:  Based on code provided in a Data Mining Lab. 
	@Override
	public String toString() {
		String toPrintString = "-----------------------------------CLUSTER START------------------------------------------" + System.getProperty("line.separator");
		
		for(DataCollectionQuestionaire i : this.clusterMembers)
		{
			toPrintString += i.toString() + System.getProperty("line.separator");
		}
		toPrintString += "-----------------------------------CLUSTER END-------------------------------------------" + System.getProperty("line.separator");
		
		return toPrintString;
	}
}
