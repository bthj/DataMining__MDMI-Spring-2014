package is.bthj.itu.datamining.preprocessing;
import is.bthj.itu.datamining.preprocessing.data.DataCollectionQuestionaire;
import is.bthj.itu.datamining.preprocessing.enums.BasicColorNames;
import is.bthj.itu.datamining.preprocessing.enums.BooleanSynonyms;
import is.bthj.itu.datamining.preprocessing.enums.OSSynonyms;
import is.bthj.itu.datamining.preprocessing.enums.ProgrammingLanguages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.List;

public class QuestionairePreProcessor {

	public List<DataCollectionQuestionaire> getCleanedQuestionaires( String[][] data ) {
		
		List<DataCollectionQuestionaire> questionaries = 
				new ArrayList<DataCollectionQuestionaire>();
		
		for( int i=0; i < data.length; i++ ) {
			DataCollectionQuestionaire oneQuestionaire = new DataCollectionQuestionaire();
			String[] oneTuple = data[i];
			
			String ageAnswer = oneTuple[0];
			String programmingSkillAnswer = oneTuple[1];
			String yearsAtUniversityAnswer = oneTuple[2];
			String operatingSystemPreferenceAnswer = oneTuple[3];
			String favoriteProgrammingLanguagesAnswer = oneTuple[4];
			String moreMountainsAnswer = oneTuple[7];
			String fedUpWithWinterAnswer = oneTuple[8];
			String favoriteColorAnswer = oneTuple[13];
			
			
			oneQuestionaire.setAge( getAgeNormalized(ageAnswer) );
			
			oneQuestionaire.setProgrammingSkill( 
					getProgrammingSkillNormalized(programmingSkillAnswer) );
			
			oneQuestionaire.setYearsAtUniversiy( 
					getYearsAtUniversity(yearsAtUniversityAnswer) );
			
			oneQuestionaire.setOperatingSystemPreference( 
					getPreferedOSNormalized(operatingSystemPreferenceAnswer) );
			
			oneQuestionaire.setFavoriteProgrammingLanguages(
					getFavoriteProgrammingLanguagesNormalized(favoriteProgrammingLanguagesAnswer) );
			
			oneQuestionaire.setMoreMountainsShouldBeInDenmark( 
					getBooleanAnswer(moreMountainsAnswer) );
			oneQuestionaire.setFedUpWithWinter( 
					getBooleanAnswer(fedUpWithWinterAnswer) );
			
			oneQuestionaire.setFavoriteColour( getColorNameMatchingAnswer(favoriteColorAnswer) );
			
			if( ! shouldIgnoreTuple(oneQuestionaire) ) {
				
				// let's just use tuples that have valid values for *all* attributes
				
				questionaries.add(oneQuestionaire);	
			}
		}
		
		return questionaries;
	}
	
	
	/*
	 * "Normalize" favorite color answers by comparing them to a list 
	 * of color names and return the closes match, if any.
	 * First we try exact matches, then partial substring matches.
	 */
	private String getColorNameMatchingAnswer( String colorAnswer ) {
		String colorName = null;
		colorAnswer = colorAnswer.toLowerCase().trim();
		for( BasicColorNames e : BasicColorNames.values() ) {
			if( e.name().toLowerCase().equals(colorAnswer) ) {
				colorName = e.name();
			}
		}
		if( null == colorName ) {
			// no exact match, let's loop gain looking for partial matches
			for( BasicColorNames e : BasicColorNames.values() ) {
				if( colorAnswer.indexOf( e.name().toLowerCase() ) > -1 ) {
					colorName = e.name();
				}
			}
		}
		if( null == colorName ) {
			// we've found no match, so lets return the empty string
			colorName = "-";
		}
		
		return colorName;
	}
	
	/*
	 * Normalize prefered OS answer by choosing an OS enum that has values
	 * that best match what we find within the answer
	 */
	private String getPreferedOSNormalized( String osAnswer ) {
		OSSynonyms preferedOS = null;
		
		String osAnswerWords = osAnswer.toLowerCase().trim();  //.split("\\s+");
		
		//for( String oneWord : osAnswerWords ) {
			
			preferedOS = OSSynonyms.getMatchingOS( osAnswerWords );
			
			//if( null != preferedOS ) break;
		//}
		
		return null != preferedOS ? preferedOS.name() : "-";
	}
	
	
	/*
	 * Return an array based on answer, with programming language names
	 * spelled according to an enum list.
	 */
	private String[] getFavoriteProgrammingLanguagesNormalized( String progAnswer ) {
		List<String> returnedLanguages = new ArrayList<String>();
		
		String[] givenLangs = progAnswer.split(",");
		for( String oneLang : givenLangs ) {
			ProgrammingLanguages progLang = 
					ProgrammingLanguages.getProgrammingLanguage(oneLang);
			if( null == progLang ) {
				// let's try to split the given language on spaces and see
				// if the parts match anything
				String[] langParts = oneLang.split("\\s+");
				for( String oneLangPart : langParts ) {
					progLang = ProgrammingLanguages.getProgrammingLanguage(oneLangPart);
					if( null != progLang ) {
						returnedLanguages.add( progLang.name() );		
					}
				}
			} else {
				returnedLanguages.add( progLang.name() );
			}
		}
		// let's have consistent ordering - we might have the ProgrammingLanguages' enum
		// dictate the ordering, but we'll use the strings' natural ordering for now:
		Collections.sort( returnedLanguages );
		
		return (String[]) returnedLanguages.toArray(new String[returnedLanguages.size()]);
	}
	
	
	
	/*
	 * Return Yes or No, possibly inferred from their synonyms
	 */
	private BooleanSynonyms getBooleanAnswer( String writtenAnswer ) {
		
		BooleanSynonyms booleanAnswer = BooleanSynonyms.getBooleanAnswer(writtenAnswer);
		
		if( null == booleanAnswer ) {
			return null;
		} else {
			return booleanAnswer;
		}
		
	}
	
	
	/*
	 * If we find age as an integer equal or above 18 
	 * and below or equal too 120, return it or -1 otherwise.
	 */
	private int getAgeNormalized( String ageAnswer ) {
		
		int age = getIntegerValue( ageAnswer );
		
		if( age < 18 || age > 120 ) age = Integer.MIN_VALUE;
		
		return age;
	}
	
	/*
	 * Get programming skill as a value between 1 and 10 inclusive.
	 * If we find a numeric value below 1, set it to 1,
	 * if we find a numeric value above 10, set it to 10,
	 * if we don't find a numeric value, return -1 . 
	 */
	private float getProgrammingSkillNormalized( String progSkillAnswer ) {
		
		float skill = getFloatValue( progSkillAnswer );
		
		if( skill < 1 && skill != -Float.MAX_VALUE ) {
			skill = 1;
		} else if( skill > 10 ) {
			skill = 10;
		}
		
		return skill;
	}
	
	/*
	 * Get years at university as a numeric value,
	 * without any normalization
	 */
	private float getYearsAtUniversity( String uniAnswer ) {
		
		return getFloatValue( uniAnswer );
	}
	
	
	/*
	 * Remove all non-digit characters, split into words on spaces
	 * and see if we can parse an integer from one of the words.
	 */
	private int getIntegerValue( String answer ) {
		int value = Integer.MIN_VALUE;
		
		for( String oneWord : getDigitsSplit(answer) ) {
			
			try {
				value = Integer.parseInt( oneWord );
			} catch (Exception e) {
				value = Integer.MIN_VALUE;
			}
			if( value > 0 ) break;
		}
		return value;
	}
	
	/*
	 * Remove all non-digit characters, split into words on spaces
	 * and see if we can parse an integer from one of the words.
	 */
	private float getFloatValue( String answer ) {
		float value = -Float.MAX_VALUE;
		
		for( String oneWord : getDigitsSplit(answer) ) {
			
			try {
				value = Float.parseFloat( oneWord );
			} catch (Exception e) {
				value = -Float.MAX_VALUE;
			}
			if( value > 0 ) break;
		}
		return value;
	}
	
	/*
	 * Get string split into words no spaces,
	 * with non-digit characters removed.  
	 * Additionally, the ½ symbol is replaced with .5
	 */
	private String[] getDigitsSplit( String answer ) {
		
		return answer.replaceAll("\u00bd", ".5") // replace the ½ symbol with .5
				.replaceAll(",",".") // replace commas with dots, as we interpret fractions with dots
				.replaceAll("[^\\d.]", "") // remove all non-digit characters 
				.split("\\s+")  // and split into words on spaces 
				;
	}
	
	
	
	private boolean shouldIgnoreTuple( DataCollectionQuestionaire oneQuestionaire ) {
		boolean ignore = false;
		
		// TODO: assess the validity of the data
		if( oneQuestionaire.getAge() == Integer.MIN_VALUE ||
			oneQuestionaire.getProgrammingSkill() == -Float.MAX_VALUE ||
			oneQuestionaire.getYearsAtUniversiy() == -Float.MAX_VALUE ||
			oneQuestionaire.getOperatingSystemPreference().equals("-") ||
			oneQuestionaire.getFavoriteProgrammingLanguages().length == 0 ||
			oneQuestionaire.getMoreMountainsShouldBeInDenmark() == null ||
			oneQuestionaire.getFedUpWithWinter() == null ||
			oneQuestionaire.getFavoriteColour().equals("-") ) {
				
				ignore = true;
			}
		
		return ignore;
	}
}
