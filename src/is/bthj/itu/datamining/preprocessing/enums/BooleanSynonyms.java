package is.bthj.itu.datamining.preprocessing.enums;

public enum BooleanSynonyms {

	Yes( "yes", "all right", "alright", "very well", "of course", "by all means", "sure", "definitely",
			"certainly", "absolutely", "indeed", "affirmative", "in the affirmative", "agreed", 
			"roger", "aye", "aye aye", "yeah", "yah", "yep", "yup", "uh-huh", "okay", "OK", 
			"okey-dokey", "okey-doke", "righto", "righty-ho", "surely", "acha", "yea" ),

	No( "no", "no indeed", "absolutely not", "most certainly not", "of course not", 
			"under no circumstances", "by no means", "not at all", "negative", "never", 
			"not really", "no thanks", "nae", "nope", "nah", "not on your life", "no way", 
			"no fear", "not on your nelly", "no siree", "naw", "nay" );
	
	private String[] synonyms;
	
	private BooleanSynonyms( String... boolSynonyms ) {
		this.synonyms = boolSynonyms;
	}
	
	public static BooleanSynonyms getBooleanAnswer( String boolAnswer ) {
		BooleanSynonyms returnedSynonym = null;
		
		boolAnswer = boolAnswer.toLowerCase();
		
		enumLoop:
		for( BooleanSynonyms oneBool : BooleanSynonyms.values() ) {
			
			for( String oneBoolSynonym : oneBool.synonyms ) {
				
				if( boolAnswer.contains(oneBoolSynonym) ) {
					returnedSynonym = oneBool;
					break enumLoop;
				}
			}
		}
		
		return returnedSynonym;
	}
	
	public String toString() {
		return this.name();
	}
}
