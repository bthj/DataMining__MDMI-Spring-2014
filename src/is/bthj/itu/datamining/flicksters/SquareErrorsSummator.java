package is.bthj.itu.datamining.flicksters;

import is.bthj.itu.datamining.FileWritingHelper;
import is.bthj.itu.datamining.flicksters.data.FlickrPhotos;
import is.bthj.itu.datamining.flicksters.data.FlickrPhotosPreprocessor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SquareErrorsSummator<T> {
	
	/**
	 * As on page 368 of Data Mining Concepts and Techniques, 3rd edition.
	 */
	public float getSumOfSquaredError( List<KMeanCluster<T>> clusters ) {
		float sumOfSquaredError = 0;
		for( KMeanCluster<T> oneCluster : clusters ) {
			sumOfSquaredError += oneCluster.getSumOfSquaredDistancesToMean();
		}
		return sumOfSquaredError;
	}
	

	public static void main(String[] args) {
		int kFrom, kTo;
		if( args.length == 2 ) {
			kFrom = Integer.parseInt(args[0]);
			kTo = Integer.parseInt(args[1]);
		} else {
			kFrom = 2;
			kTo = 10;
		}
		
		SquareErrorsSummator<FlickrPhotos> errorSummator = new SquareErrorsSummator<FlickrPhotos>();
		
		List<FlickrPhotos> flickrPhotos = FlickrPhotosPreprocessor.getCPHphotos();
		
		
		KMeans<FlickrPhotos> kMeans = new KMeans<>();
		// collection of sums of squared errors, for different values of k, to save to file for plotting:
		Map<Integer, Float> kVSsumOfSquaredErrors = new TreeMap<Integer, Float>(); 
		for( int k = kFrom; k <= kTo; k++ ) {
			
			List<KMeanCluster<FlickrPhotos>> clusters = kMeans.kMeansPartition( k, flickrPhotos );
			
			float sumOfSquaredError = errorSummator.getSumOfSquaredError(clusters);
			
			kVSsumOfSquaredErrors.put(k, sumOfSquaredError);
			
			String resultString = "Error for k = " + k + ": " + sumOfSquaredError;
			System.out.println( resultString );
			FileWritingHelper.writeStringToFile(
					resultString, 
					"FlickrPhotos__kVSsumOfSquaredErrors__k_2-10__"+(new java.util.Date().getTime())+".txt" );
		}
		
		FileWritingHelper.writeMapToFile( 
				kVSsumOfSquaredErrors, 
				"FlickrPhotos__kVSsumOfSquaredErrors__k_2-10__"+(new java.util.Date().getTime())+".tab" );
	}
}
