package is.bthj.itu.datamining.flicksters.data;

import java.util.List;

import uk.me.jstott.jcoord.LatLng;

public class FlickrPhotoCluster {

	private LatLng centroid;
	private int meberCount;
	private String mostFrequentTag;
	private List<FlickrPhoto> tuples;
	
	
	
	public LatLng getCentroid() {
		return centroid;
	}
	public void setCentroid(LatLng centroid) {
		this.centroid = centroid;
	}
	public int getMeberCount() {
		return meberCount;
	}
	public void setMeberCount(int meberCount) {
		this.meberCount = meberCount;
	}
	public String getMostFrequentTag() {
		return mostFrequentTag;
	}
	public void setMostFrequentTag(String mostFrequentTag) {
		this.mostFrequentTag = mostFrequentTag;
	}
	public List<FlickrPhoto> getTuples() {
		return tuples;
	}
	public void setTuples(List<FlickrPhoto> tuples) {
		this.tuples = tuples;
	}

}
