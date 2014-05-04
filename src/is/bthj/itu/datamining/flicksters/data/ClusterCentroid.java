package is.bthj.itu.datamining.flicksters.data;

import uk.me.jstott.jcoord.LatLng;

public class ClusterCentroid {

	private LatLng centroid;
	private int memberCount;
	
	
	public LatLng getCentroid() {
		return centroid;
	}
	public void setCentroid(LatLng centroid) {
		this.centroid = centroid;
	}
	public int getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
	
}
