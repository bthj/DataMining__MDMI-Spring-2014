package is.bthj.itu.datamining.flicksters.execution;

import is.bthj.itu.datamining.flicksters.data.ClusterCentroid;
import is.bthj.itu.datamining.flicksters.data.FlickrPhotoCluster;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * For an array of cluster groups, 
 * partition all the cluster centroids, 
 * where each centroid has a weight according to count of members in that cluster. 
 * @author bthj
 *
 */
public class PartitionClusterCentroids {

	// TODO: filter out centroids with NaN values
	
	public static void main(String[] args) throws IOException {
		int k = 0;
		if( args.length != 1 ) {
			System.err.println("An integer parameter for the value of k is needed, \n" + 
								"indicating the cluster partition files to find centroids from.");
			System.exit(0);
		} else {
			k = Integer.parseInt(args[0]);
		}
		
		
		final ObjectMapper mapper = new ObjectMapper();
		
		// let's find all cluster group files, read the info from them into a list of ClusterCentroid:
	    final PathMatcher matcher = 
	    		FileSystems.getDefault().getPathMatcher("glob:./"+FilenameConstants.clusterGroupPrefix+"*");
	    Files.walkFileTree(Paths.get("./"), new SimpleFileVisitor<Path>() {
	    	
	    	List<ClusterCentroid> clusterCentroids = new ArrayList<ClusterCentroid>();
	    	
	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
	            if (matcher.matches(file)) {
	                System.out.println(file);
	                
	                // read from one cluster group json file:
	                //@SuppressWarnings("unchecked")
					Map<String, FlickrPhotoCluster> flickrPhotoClusterGroup = 
	                		mapper.readValue(new File(file.toString()), 
	                				new TypeReference<Map<String, FlickrPhotoCluster>>() { } );
	                
	                // collect all centroids from that cluster group
	                for( FlickrPhotoCluster oneCluster : flickrPhotoClusterGroup.values() ) {
	                	
	                	ClusterCentroid clusterCentroid = new ClusterCentroid();
//	                	clusterCentroid.setCentroid( oneCluster.getCentroid() );
	                	clusterCentroid.setCentroidLatitude( oneCluster.getCentroidLatitude() );
	                	clusterCentroid.setCentroidLongitude( oneCluster.getCentroidLongitude() );
	                	clusterCentroid.setMemberCount( oneCluster.getMemberCount() );
	                	clusterCentroids.add( clusterCentroid );
	                }
	                
	                
	                
	            }
	            return FileVisitResult.CONTINUE;
	        }
	        
	        // Print each directory visited.
	        @Override
	        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
	            System.out.format("Directory: %s%n", dir);
	            
	            System.out.println("Collected #" + clusterCentroids.size() + " centroids");
	            
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
	            return FileVisitResult.CONTINUE;
	        }
	    });
	}
}
