package is.bthj.itu.datamining.flicksters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;
import is.bthj.itu.datamining.flicksters.data.FlickrPhotos;

public class KMeanClusterOfFlickrPhotos extends KMeanCluster<FlickrPhotos> {
	
	private LatLng clusterMean;
	
	private Map<String, UTMRef> tupleUTMRef;
	
	public KMeanClusterOfFlickrPhotos() {
		
		this.clusterMembers = new ArrayList<FlickrPhotos>();
		this.tupleUTMRef = new HashMap<String, UTMRef>();
	}
	
	@Override
	public float getClusterMean() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getTupleDistanceToClusterMean(FlickrPhotos tuple) {
		float distance = 0;
		
		if( null == clusterMean ) updateClusterMean();
			
		LatLng tupleLatLng = new LatLng(tuple.getLatitude(), tuple.getLongitude());
		distance = (float) clusterMean.distance(tupleLatLng);
		
		return distance;
	}
	
	
	/**
	 * Add data object (tuple) to this cluster
	 */
	@Override
	public void addToCluster( FlickrPhotos dataObject ) {
		
		clusterMembers.add( dataObject );
		
		clusterMean = null;  // we'll lazily update the cluster mean when needed
	}
	
	/**
	 * Remove data object from this cluster
	 */
	@Override
	public void removeFromCluster( FlickrPhotos dataObject ) {
		
		clusterMembers.remove( dataObject );
		
		clusterMean = null;  // we'll lazily update the cluster mean when needed
	}
	
	@Override
	public float getSumOfSquaredDistancesToMean() {
		float squaredDistancesSum = 0;
		for( FlickrPhotos oneTuple : clusterMembers ) {
			squaredDistancesSum += getTupleDistanceToClusterMean(oneTuple);
		}
		return squaredDistancesSum;
	}
	
	
	private void updateClusterMean() {
		
		// TODO: converting to UTM on the fly may be too expensive!
		// 	we might instead want to do something like:  http://www.geomidpoint.com/calculation.html
		double eastingSums = 0, northingSums = 0;
		char latZone = '\u0000';
		int lngZone = 0;
		for( FlickrPhotos oneTuple : clusterMembers ) {
			
			LatLng tupleLatLng = new LatLng(oneTuple.getLatitude(), oneTuple.getLongitude());
			
			UTMRef tupleUTM = tupleUTMRef.get(oneTuple.getPhotoId());
			if( null == tupleUTM ) {
				tupleUTM = tupleLatLng.toUTMRef();
				tupleUTMRef.put(oneTuple.getPhotoId(), tupleUTM);
			}
			
			eastingSums += tupleUTM.getEasting();
			northingSums += tupleUTM.getNorthing();
			
			latZone = tupleUTM.getLatZone();
			lngZone = tupleUTM.getLngZone();
		}
		int totalMembers = clusterMembers.size();
		// we'll use the last tupleUTM zone info and hope we're not spanning UTM zones!
		UTMRef midpointUTM = new UTMRef(
				eastingSums / totalMembers, northingSums / totalMembers, 
				latZone, lngZone);
		
		clusterMean = midpointUTM.toLatLng();
	}

}
