package is.bthj.itu.datamining.flicksters;

import is.bthj.itu.datamining.flicksters.data.FlickrPhotos;
import is.bthj.itu.datamining.flicksters.data.FlickrPhotosPreprocessor;

import java.util.List;

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
		
		SquareErrorsSummator<FlickrPhotos> errorSummator = new SquareErrorsSummator<FlickrPhotos>();
		
		List<FlickrPhotos> flickrPhotos = FlickrPhotosPreprocessor.getCPHphotos();
		
		int k = 5;
		KMeans<FlickrPhotos> kMeans = new KMeans<>();
		
		List<KMeanCluster<FlickrPhotos>> clusters = kMeans.kMeansPartition( k, flickrPhotos );
		
		float sumOfSquaredError = errorSummator.getSumOfSquaredError(clusters);
		
		System.out.println( "Error for k = " + k + ": " + sumOfSquaredError );
	}
}
