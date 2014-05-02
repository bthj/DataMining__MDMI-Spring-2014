package is.bthj.itu.datamining.flicksters;

import is.bthj.itu.datamining.flicksters.data.FlickrPhotos;

public class KMeanClusterFactory {

	@SuppressWarnings("unchecked")
	public static <T> KMeanCluster<T> createKMeanClusterWithSeedObject( T tuple ) {
		KMeanCluster<T> cluster;
		if( tuple instanceof FlickrPhotos ) {
			cluster = (KMeanCluster<T>) new KMeanClusterOfFlickrPhotos();
			cluster.addToCluster(tuple);
		} else {
			cluster = null;
		}
		return cluster;
	}
}
