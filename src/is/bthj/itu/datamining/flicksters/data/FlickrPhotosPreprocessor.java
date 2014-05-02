package is.bthj.itu.datamining.flicksters.data;

import is.bthj.itu.datamining.preprocessing.CSVFileReader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FlickrPhotosPreprocessor {

	public static List<FlickrPhotos> getCPHphotos() {
		
		List<FlickrPhotos> flickrPhotos = new ArrayList<FlickrPhotos>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String[][] data = CSVFileReader.read( "CPHpics.csv", ",", false );
			
			for( int i=0; i < data.length; i++ ) {
				
				FlickrPhotos oneFlickrPhoto = new FlickrPhotos();
				oneFlickrPhoto.setPhotoId( data[i][0] );
				oneFlickrPhoto.setOwnerId( data[i][1] );
				oneFlickrPhoto.setTags( data[i][2] );
				oneFlickrPhoto.setDateTaken( dateFormatter.parse(data[i][3]) );
				oneFlickrPhoto.setLatitude( Float.parseFloat( data[i][4] ) );
				oneFlickrPhoto.setLongitude( Float.parseFloat( data[i][5] ) );
				
				flickrPhotos.add( oneFlickrPhoto );
			}
			
		} catch (IOException | ParseException e) {

			System.err.println(e.getLocalizedMessage());
		}
		
		return flickrPhotos;
	}
}
