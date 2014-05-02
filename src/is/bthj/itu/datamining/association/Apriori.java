package is.bthj.itu.datamining.association;

import is.bthj.itu.datamining.preprocessing.CSVFileReader;
import is.bthj.itu.datamining.preprocessing.QuestionairePreProcessor;
import is.bthj.itu.datamining.preprocessing.data.DataCollectionQuestionaire;
import is.bthj.itu.datamining.preprocessing.enums.ProgrammingLanguages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Apriori {

	/*
	 * Stores frequent item sets for level the Apriori algorithm goes through.
	 * When generating frequent item sets for one level, those from the previous level
	 * are useful for detecting if a candidate has infrequent k-1 subsets,
	 * as can be seen in the method hasInfrequentSubset below.
	 */
	Map<Integer, Map<ItemStringSet, Integer>> levelsOfFrequentItemSets;
	
	
	public static void main(String[] args) {
		
		Apriori apriori = new Apriori();
		
		// Frequency / association analysis for the _Favorite programming languages_ survey data:
		String[][] transactions = apriori.getFavoriteProgrammingLanguagesCleanedAndSorted();
		List<ItemStringSet> distinctItems = apriori.getAllDistinctProgrammingLanguages();
		
		// Textbook data (table 6.1) testing:
//		String[][] transactions = apriori.getTextBookTransactionalData();
//		List<ItemStringSet> distinctItems = apriori.getAllDistinctTextbookItems();
		
		apriori.apriori( transactions, distinctItems, 2, 70 );
	}
	
	
	/*
	 * Default constructor.
	 */
	public Apriori() {
		levelsOfFrequentItemSets = new HashMap<Integer, Map<ItemStringSet,Integer>>();
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	///// Apriori frequent item sets generation algorithms
	///// - organization into methods below based on code provided with Lab assignments.
	
	/**
	 * Find frequent itemsets within the given support threshold,  
	 * and generate association rules from those frequent itemsets.
	 * @param transactions Transactional data to find frequent itemsets in
	 * @param distinctItems All distinct attribute values occurring in the dataset, ordered.
	 * @param supportThreshold Mininum support theshold that the frequent itemsets must support.
	 * @param minimumConfidencePercentage Minimum confidence threshold for generated association rules.
	 * @return
	 */
    public List<ItemStringSet> apriori( 
    		String[][] transactions, List<ItemStringSet> distinctItems, 
    		int supportThreshold, int minimumConfidencePercentage ) {
        int k = 0;
        
        Map<ItemStringSet, Integer> frequentItemSets = 
        		generateFrequentItemSetsLevel1( transactions, distinctItems, supportThreshold );
        levelsOfFrequentItemSets.put( k+1, frequentItemSets );
        
        for( k = 1; frequentItemSets.size() > 0; k++ ) {
            System.out.print( "Finding frequent itemsets of length " + (k + 1) + "…" );
            frequentItemSets = generateFrequentItemSets( supportThreshold, transactions, frequentItemSets );

            levelsOfFrequentItemSets.put( k+1, frequentItemSets );

            System.out.println( " found " + frequentItemSets.size() );
        }
        // Now k-1 indexes to the table of frequent itemsets in levelsOfFrequentItemSets
        frequentItemSets = levelsOfFrequentItemSets.get( --k );
        
        // Print the found frequent itemsets satisfying the given support theshold:
        printFrequentItemSets( frequentItemSets, supportThreshold );
        
        // Create and print association rules from the frequent itemsets:
        printAssociationRules( frequentItemSets, minimumConfidencePercentage );


        return new ArrayList<ItemStringSet>(frequentItemSets.keySet());
    }
  

    private Map<ItemStringSet, Integer> generateFrequentItemSets( 
    		int supportThreshold, String[][] transactions,
    		Map<ItemStringSet, Integer> lowerLevelItemSets ) {
        
    	// Generate candidate itemsets from the lower level itemsets
    	List<ItemStringSet> candidates = 
    			new ArrayList<ItemStringSet>();
    	for( ItemStringSet firstSet : lowerLevelItemSets.keySet() ) {
    		for( ItemStringSet secondSet : lowerLevelItemSets.keySet() ) {
    			ItemStringSet candidate = joinSets( firstSet, secondSet );
    			
    			if( null != candidate && 
    					! hasInfrequentSubset( candidate, supportThreshold ) ) {
    				
    				candidates.add( candidate );
    			}
    		}
    	}

        /*
         * Check the support for all candidates and add only those
         * that have enough support to the set
         */
    	Map<ItemStringSet, Integer> frequentItemSets = getFrequentItemSetsFromCandidates(
				supportThreshold, transactions, candidates);
        return frequentItemSets;
    }

	private Map<ItemStringSet, Integer> getFrequentItemSetsFromCandidates(
			int supportThreshold, String[][] transactions,
			List<ItemStringSet> candidates) {
		
		Map<ItemStringSet, Integer> frequentItemSets = new TreeMap<ItemStringSet, Integer>();
    	for( ItemStringSet oneSet : candidates ) {
    	
    		int support = countSupport( oneSet, transactions );
    		if( support >= supportThreshold ) {
    			
    			frequentItemSets.put( oneSet, support );
    		}
    	}
		return frequentItemSets;
	}
	
	
    private Map<ItemStringSet, Integer> generateFrequentItemSetsLevel1( 
    		String[][] transactions, List<ItemStringSet> distinctItems, int supportThreshold ) {
    	
        return getFrequentItemSetsFromDistinctAttributeValues( supportThreshold, transactions, distinctItems );
    }
	
	private Map<ItemStringSet, Integer> getFrequentItemSetsFromDistinctAttributeValues( 
			int supportThreshold, String[][] transactions, List<ItemStringSet> distinctItems) {
		
		// TODO: A bit of an overkill to have ItemStringSet instances for single string values
		// 		but done for consistency - might be worth refactoring..
		Map<ItemStringSet, Integer> frequentItemSets = new TreeMap<ItemStringSet, Integer>();
		for( ItemStringSet oneAttributeValue : distinctItems ) {

			int support = countSupport( oneAttributeValue, transactions );
			if( support >= supportThreshold ) {
    			
    			frequentItemSets.put( oneAttributeValue, support );
    		}
		}
		return frequentItemSets;
	}
    
	
    private boolean hasInfrequentSubset( ItemStringSet candidate, int supportThreshold ) {
		boolean hasInfrequentSubset = false;
		
		KSubsets kSubsets = new KSubsets( candidate );
		int subsetSize = candidate.size() - 1;
		Map<ItemStringSet, Integer> frequentSubsets = levelsOfFrequentItemSets.get(subsetSize);

		for( ItemStringSet oneSubset : kSubsets.getKSubsets( subsetSize ) ) {

			if( null == frequentSubsets.get(oneSubset) ) {
				// we've got one subset that isn't frequent
				hasInfrequentSubset = true;
				break;
			} else {
				String debug = "delme";
			}
		}
		return hasInfrequentSubset;
	}
	
	
	private ItemStringSet joinSets( ItemStringSet first, ItemStringSet second ) {
		// we're joining for pass k = setSize+1 
		if( first.size() == second.size() ) {
			ItemStringSet joinedSet = null;
			
			// let's check if the first k-2 items match
			boolean firstKminus2Match = 
				first.getItemSubset(0, first.size()-1).equals( 
						second.getItemSubset(0, second.size()-1));
			// and if the last item of first (k-1) is less than
			// the last item of second
			boolean lastInLexicographicalOrder = 
					first.getItem(first.size()-1).compareTo(
							second.getItem(second.size()-1)) < 0;
			if( firstKminus2Match && lastInLexicographicalOrder ) {
				/*
				 * where l_1 in L_k-1, l_2 in L_k-1
				 * we have met the condition:
				 * l_1[1] = l_2[1] ^ l_1[2] = l_2[2] ^ ... ^
				 * l_1[k-2] = l_2[k-2] ^ l_1[k-1] < l_2[k-1]
				 */
				joinedSet = new ItemStringSet(
						first.getSubset(0, first.size()), 
						second.getSubset(second.size()-1, second.size()) );
			}
			return joinedSet;
		} else {
			return null;
		}
	}
	
	
	private int countSupport( ItemStringSet itemSet, String[][] transactions ) {
		
		// Assume that items in ItemStringSets and transactions are unique
		// and all with the same (natural) order
		
		int supportCount = 0;
		for( String[] oneTransaction : transactions ) {
			if( oneTransaction.length >= itemSet.size() ) {

				boolean supportForAttributes = false;;
				for( String oneAttributeValue : itemSet.getSet() ) {
					supportForAttributes = false;
					for( String oneTransactionAttributeValue : oneTransaction ) {
						if( oneTransactionAttributeValue.equals(oneAttributeValue) ) {
							supportForAttributes = true;
							break;
						}
					}
					if( ! supportForAttributes ) {
						// we failed to find support in this tuple for one attribute value
						// and so we can stop searching
						break;
					}
				}
				if( supportForAttributes ) {
					supportCount++;
				}
			}
		}
		return supportCount;
	}
	
    private void printFrequentItemSets( 
    		Map<ItemStringSet, Integer> frequentItemSets, int minimumSupport) {
    	
    	System.out.println( "***Frequent itemsets with minimum support: " + minimumSupport );
    	for( ItemStringSet onesSet : frequentItemSets.keySet() ) {
    		System.out.println( Arrays.toString(onesSet.getSet()) );
    	}
    }
	
	
	
	///////////////////////////////////////////////////////////////////////////
	///// Association rules methods
	
	private void printAssociationRules( 
			Map<ItemStringSet, Integer> frequentItemSets, int minimumConfidencePercentage ) {
		
		System.out.println( "***Association rules with minimum conficence = " + minimumConfidencePercentage + "%" );
		
		for( ItemStringSet oneFrequentItemSet : frequentItemSets.keySet() ) {
			
			List<ItemStringSet> frequentItemSubsets = 
					getAllNonemptySubsets(oneFrequentItemSet);
			for( ItemStringSet oneFrequentItemSubset : frequentItemSubsets ) {
				
				ItemStringSet complementarySet = 
						getComplementarySubsetFromSuperset(
								oneFrequentItemSet, oneFrequentItemSubset);
				
				// As in P( B | A ):
				// B
				int eventProbability = levelsOfFrequentItemSets.get(
						oneFrequentItemSubset.size()+complementarySet.size()).get( // support_count( A U B )
								new ItemStringSet(oneFrequentItemSubset, complementarySet) ); 
				
				// A
				int conditionProbability = levelsOfFrequentItemSets.get( // support_count( A )
						oneFrequentItemSubset.size()).get(oneFrequentItemSubset);
				
				
				float confidence = (float) eventProbability / conditionProbability;
				int confidencePercentage = Math.round(confidence*100);
				
				if( confidencePercentage >= minimumConfidencePercentage ) {
					
					System.out.println( oneFrequentItemSubset + " => " + complementarySet + 
							", confidence = " + eventProbability + "/" + conditionProbability 
							+ " = " + confidencePercentage + "%" );
				}
			}
		}
	}
	
	private List<ItemStringSet> getAllNonemptySubsets( ItemStringSet frequentItemSet ) {
		
		List<ItemStringSet> subsets = new ArrayList<ItemStringSet>();
		
		KSubsets kSubsets = new KSubsets( frequentItemSet );
		
		for( int i = frequentItemSet.size() - 1; i > 0; i-- ) {
			
			subsets.addAll( kSubsets.getKSubsets(i) );
		}
		return subsets;
	}
	
	private ItemStringSet getComplementarySubsetFromSuperset( 
			ItemStringSet superset, ItemStringSet subset ) {
		
		String[] complement = new String[ superset.size() - subset.size() ];
		int complementIndex = 0;
		for( String oneSupersetAttribute : superset.getSet() ) {
			boolean subsetAttribInSuperset = false;
			for( String oneSubsetAttribute : subset.getSet() ) {
				if( oneSubsetAttribute.equals(oneSupersetAttribute) ) {
					subsetAttribInSuperset = true;
					break;
				}
			}
			if( false == subsetAttribInSuperset ) {
				complement[complementIndex++] = oneSupersetAttribute;
			}
		}
		return new ItemStringSet( complement );
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	///// data methods
	
	
	///// Methods specific to the Favorite programming languages survey attribute
	
    public List<ItemStringSet> getAllDistinctProgrammingLanguages() {
    	// we're focusing on the preferred programming languages attribute here
    	// so let's get all the distinct values from the corresponding enum
    	// TODO: Handle references to various attributes of different types in a more general way.
    	List<ItemStringSet> programmingLanguageNames = new ArrayList<ItemStringSet>();
    	for( ProgrammingLanguages oneLang : ProgrammingLanguages.values() ) {
    		programmingLanguageNames.add( new ItemStringSet(new String[]{oneLang.name()}) );
    	}
    	return programmingLanguageNames;
    }
    
    public String[][] getFavoriteProgrammingLanguagesCleanedAndSorted() {
    	
    	String[][] data;
    	String[][] transactions = null;
		try {
			data = CSVFileReader.read( "data_mining_2014_dataset.csv", ";", false );
		
			QuestionairePreProcessor preProcessor = new QuestionairePreProcessor();
			List<DataCollectionQuestionaire> cleanedQuestionaireTuples = 
					preProcessor.getCleanedQuestionaires( data );
			
			transactions = new String[cleanedQuestionaireTuples.size()][];
			
			for( int i=0; i < cleanedQuestionaireTuples.size(); i++ ) {
				
				transactions[i] = 
						cleanedQuestionaireTuples.get(i).getFavoriteProgrammingLanguages();
			}
			
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		
		return transactions;
    }
    
    
    ///// Methods specific to textbook example data.
    
    /**
     * Data from Table 6.1 in the textbook, Data Mining Concepts and Techniques, 3rd edition
     * - for validation
     * @return Table 6.1 data from textbook
     */
    public String[][] getTextBookTransactionalData() {
    	
    	String [][] textbookData = new String[9][];
    	
    	textbookData[0] = new String[] {"I1", "I2", "I5"};
    	textbookData[1] = new String[] {"I2", "I4"};
    	textbookData[2] = new String[] {"I2", "I3"};
    	textbookData[3] = new String[] {"I1", "I2", "I4"};
    	textbookData[4] = new String[] {"I1", "I3"};
    	textbookData[5] = new String[] {"I2", "I3"};
    	textbookData[6] = new String[] {"I1", "I3"};
    	textbookData[7] = new String[] {"I1", "I2", "I3", "I5"};
    	textbookData[8] = new String[] {"I1", "I2", "I3"};
    	
    	return textbookData;
    }
    
    public List<ItemStringSet> getAllDistinctTextbookItems() {
    	
    	List<ItemStringSet> table6_1items = new ArrayList<ItemStringSet>();
    	for( String oneItem : new String[] {"I1", "I2", "I3", "I4", "I5"} ) {
    		
    		table6_1items.add( new ItemStringSet(new String[]{oneItem}) );
    	}
    	return table6_1items;
    }
}
