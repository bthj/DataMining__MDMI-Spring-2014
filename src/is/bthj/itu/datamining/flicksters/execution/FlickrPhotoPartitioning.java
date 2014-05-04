package is.bthj.itu.datamining.flicksters.execution;

import is.bthj.itu.datamining.flicksters.data.FlickrPhotoCluster;
import is.bthj.itu.datamining.flicksters.data.FlickrPhoto;
import is.bthj.itu.datamining.flicksters.data.FlickrPhotosPreprocessor;
import is.bthj.itu.datamining.flicksters.kmeans.KMeanCluster;
import is.bthj.itu.datamining.flicksters.kmeans.KMeans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.me.jstott.jcoord.LatLng;

/**
 * Organizes Flickr photo tuples into k partitions
 * by using the k-means method.
 * Prints out the partitioning result to a JSON file.
 * Keeps on creating cluster partitions until the execution is interrupted.
 * 
 * @author bthj
 *
 */

public class FlickrPhotoPartitioning {
	
	private static final Set<String> tagsStopList = new HashSet<String>();
	static {
		tagsStopList.add("");
		tagsStopList.add("copenhagen");
		tagsStopList.add("denmark");
		tagsStopList.add("københavn");
		tagsStopList.add("europe");
		tagsStopList.add("danmark");
		tagsStopList.add("copenhague");
		tagsStopList.add("kopenhagen");
		tagsStopList.add("nordic");
		tagsStopList.add("scandinavia");
		tagsStopList.add("northerneurope");
	}
	
	private static String findMostFrequentTagInFlickrPhotos( 
			List<FlickrPhoto> flickrPhotos) {
		String mostFrequentTag = null;
		
		Map<String, Integer> tagCounts = new HashMap<String, Integer>();
		
		for( FlickrPhoto onePhoto : flickrPhotos ) {
			String[] tags = onePhoto.getTags().split("\\s+");
			for( String oneTag : tags ) {
				if( ! tagsStopList.contains(oneTag) && 
						! oneTag.matches("[^\\s]*:[^\\s]*=[^\\s]*") &&
						! oneTag.matches("[0-9]+") ) {
					Integer oneTagCount = tagCounts.get(oneTag);
					if( null == oneTagCount ) {
						tagCounts.put(oneTag, 1);
					} else {
						tagCounts.put(oneTag, oneTagCount + 1);
					}	
				}
			}
		}
		
		Map.Entry<String, Integer> maxEntry = null;
		for( Map.Entry<String, Integer> oneEntry : tagCounts.entrySet() ) {
			
			if( maxEntry == null || oneEntry.getValue().compareTo(maxEntry.getValue()) > 0 ) {
				maxEntry = oneEntry;
			}
		}
		if( null != maxEntry ) {
			// let's return the first key with the max value (there might be more than one)
			mostFrequentTag = maxEntry.getKey();
		}
		
		return mostFrequentTag;
	}
	
	private static void writeFlickrPhotoClusterGroupToJSONFile( 
			List<KMeanCluster<FlickrPhoto, LatLng>> clusters, String fileName ) {
		
		Map<String, FlickrPhotoCluster> flickrPhotoClusterGroup = 
				new LinkedHashMap<String, FlickrPhotoCluster>();
		
		int clusterCount = 0;
		for( KMeanCluster<FlickrPhoto, LatLng> oneKmeansCluster : clusters ) {
			
			FlickrPhotoCluster photoCluster = new FlickrPhotoCluster();
			photoCluster.setCentroid( oneKmeansCluster.getClusterMean() );
			photoCluster.setMeberCount( oneKmeansCluster.getClusterMembers().size() );
			
			List<FlickrPhoto> flickrPhotosInCluster =
					new ArrayList<FlickrPhoto>(oneKmeansCluster.getClusterMembers());
			//photoCluster.setTuples( flickrPhotosInCluster );
			photoCluster.setMostFrequentTag(
					findMostFrequentTagInFlickrPhotos(flickrPhotosInCluster) );
			
			flickrPhotoClusterGroup.put( "Cluster"+ ++clusterCount, photoCluster );
		}
		
		// see https://github.com/FasterXML/jackson-databind/
		ObjectMapper mapper = new ObjectMapper();
		
			
			try {
				mapper.writeValue( new File(fileName), flickrPhotoClusterGroup );
				
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
			

	}

	public static void main(String[] args) {
		String dataFilename = null;
		int k = 0;
		if( args.length != 2 ) {
			System.err.println(
					"A parameter with the data file name and a parameter with an integer value for k is required.");
			System.exit(0);
		} else {
			dataFilename = args[0];
			k = Integer.parseInt(args[1]);
		}
		
		List<FlickrPhoto> flickrPhotos = FlickrPhotosPreprocessor.getFlickrPhotos(dataFilename);
		
		KMeans<FlickrPhoto, LatLng> kMeans = new KMeans<FlickrPhoto, LatLng>();
		while( true ) {
			String kMeansClusterGroupFileName = 
					"kMeansClusterGroupForK"+k+"__"+(new java.util.Date().getTime())+".json";
			System.out.println("Starting on: " + kMeansClusterGroupFileName );
			
			List<KMeanCluster<FlickrPhoto, LatLng>> clusters = 
					kMeans.kMeansPartition( k, flickrPhotos );
			
			writeFlickrPhotoClusterGroupToJSONFile(
					clusters, 
					kMeansClusterGroupFileName );
			
			System.out.println("Wrote: " + kMeansClusterGroupFileName);
		}
	}
}
