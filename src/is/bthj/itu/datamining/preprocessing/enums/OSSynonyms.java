package is.bthj.itu.datamining.preprocessing.enums;

public enum OSSynonyms {

	Windows( "win", "windows" ),
	OSX( "mac", "osx", "os x"),
	Linux( "linux", "unix", "ubuntu", "debian", "redhat", "fedora", "gentoo"),
	ChromeOS( "chrome" );
	
	private String[] synonyms;
	
	private OSSynonyms( String... osSynonyms ) {
		this.synonyms = osSynonyms;
	}
	
	public static OSSynonyms getMatchingOS( String osSynonym ) {
		OSSynonyms returnedSynonym = null;
		osSynonym = osSynonym.toLowerCase().trim();
		enumLoop:
		for( OSSynonyms osEnum : OSSynonyms.values() ) {
			for( String oneSynonym : osEnum.synonyms ) {
				if( osSynonym.contains( oneSynonym ) ) {
					returnedSynonym = osEnum;
					break enumLoop;
				}
			}
		}
		return returnedSynonym;
	}
}
