package is.bthj.itu.datamining.flicksters.kmeans;

import uk.me.jstott.jcoord.LatLng;
import is.bthj.itu.datamining.flicksters.data.ClusterCentroid;

public class KMeanClusterOfClusterCentroids extends KMeanCluster<ClusterCentroid, LatLng> {
	
	private LatLng clusterMean;

	@Override
	public float getTupleDistanceToClusterMean(ClusterCentroid tuple) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LatLng getClusterMean() {

		return clusterMean;
	}

	@Override
	public float getSumOfSquaredDistancesToMean() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateClusterMean() {
		// TODO Auto-generated method stub
		
	}

}
