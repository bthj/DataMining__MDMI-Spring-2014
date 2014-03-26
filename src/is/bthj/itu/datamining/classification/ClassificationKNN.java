package is.bthj.itu.datamining.classification;

import is.bthj.itu.datamining.preprocessing.CSVFileReader;
import is.bthj.itu.datamining.preprocessing.QuestionairePreProcessor;
import is.bthj.itu.datamining.preprocessing.data.DataCollectionQuestionaire;
import is.bthj.itu.datamining.preprocessing.enums.BooleanSynonyms;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class ClassificationKNN {

	public static void main( String[] args ) {
		
		String[][] data;
		try {
			data = CSVFileReader.read( "data_mining_2014_dataset.csv", false );
		
			QuestionairePreProcessor preProcessor = new QuestionairePreProcessor();
			List<DataCollectionQuestionaire> questionaireTuples = 
					preProcessor.getCleanedQuestionaires( data );
			
			Map<Integer, Float> kVSaccuracy = new TreeMap<Integer, Float>();
			for( int k=1; k <= 30; k++ ) {
				
				float accuracyForK = testAccuracyWithKNN( questionaireTuples, k );
				
				kVSaccuracy.put( k, accuracyForK );
				
				System.out.println("K: " + k + ", accuracy: " + accuracyForK);
			}
			
			//writeKandAccuracyToFile( kVSaccuracy, "kVSaccuracy__all_attributes.tab" );
			//writeKandAccuracyToFile( kVSaccuracy, "kVSaccuracy__color_attribute.tab" );
			//writeKandAccuracyToFile( kVSaccuracy, "kVSaccuracy__age_attribute.tab" );
			//writeKandAccuracyToFile( kVSaccuracy, "kVSaccuracy__operating_system_attribute.tab" );
			writeKandAccuracyToFile( kVSaccuracy, "kVSaccuracy__age_programmingSkill_yearsAtUni_attribute.tab" );
			
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	private static void writeKandAccuracyToFile( Map<Integer, Float> kVSaccuracy, String fileName ) {
		
		try {
		
			FileWriter writer = new FileWriter( fileName );
			
			for( Map.Entry<Integer, Float> entryPoint : kVSaccuracy.entrySet() ) {
				
				writer.append( entryPoint.getKey().toString() ).append('\t');
				writer.append( entryPoint.getValue().toString() ).append('\n');
			}
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
//		StringBuilder kAxis = new StringBuilder("k <- [");
//		StringBuilder accuracyAxis = new StringBuilder("accuracy <- [");
//		for( Map.Entry<Integer, Float> entryPoint : kVSaccuracy.entrySet() ) {
//	    	kAxis.append( entryPoint.getKey() ).append(",");
//	    	accuracyAxis.append( entryPoint.getValue() ).append(",");
//	    }
//		kAxis.append("]");
//		accuracyAxis.append("]");
//		System.out.println( kAxis.toString() );
//		System.out.println( accuracyAxis.toString() );
//		System.out.println( "plot( k, accuracy )" ); 
	}
	
	
	// Hardcoded target attribute:  Do you think there should be more mountains in Denmark?
	// ...yes, choice of target attribute should dynamically configurable (TODO)
	
	
	static float testAccuracyWithKNN( 
			List<DataCollectionQuestionaire> questionaireTuples, int k ) {
		
		float correctClassificationCount = 0;
		List<DataCollectionQuestionaire> testSet = getTestSet(questionaireTuples);
		for( DataCollectionQuestionaire oneTuple : testSet ) {
			
			List<DataCollectionQuestionaire> kNearestNeighbours = 
					getKNearestTrainingNeighboursToTuple(
							oneTuple, k, getTrainingSet(questionaireTuples) );
			
			// TODO: Find the dominant target value.
			BooleanSynonyms dominantTargetValue = 
					getDominantAnswerToMountains( kNearestNeighbours );
			// TODO: Evaluate correctness of dominant target value...
			if( dominantTargetValue == oneTuple.getMoreMountainsShouldBeInDenmark() ) {
				correctClassificationCount++;
			}
		}
		float correctClassificationPercentage = correctClassificationCount / testSet.size();
		return correctClassificationPercentage;
	}
	
	// Not-so-very-generic method...
	static BooleanSynonyms getDominantAnswerToMountains( 
			List<DataCollectionQuestionaire> kNearestNeighbours ) {
		
		BooleanSynonyms targetValue = null;
		
		int yesCount = 0, noCount = 0;
		for( DataCollectionQuestionaire tuple : kNearestNeighbours ) {
			if( tuple.getMoreMountainsShouldBeInDenmark() == BooleanSynonyms.Yes ) {
				yesCount++;
			} else if( tuple.getMoreMountainsShouldBeInDenmark() == BooleanSynonyms.No ) {
				noCount++;
			}
			if( yesCount > noCount ) {
				targetValue = BooleanSynonyms.Yes;
			} else if( noCount > yesCount ) {
				targetValue = BooleanSynonyms.No;
			} else { // we have a tie, let's choose the target value randomly
				if( new Random().nextBoolean() ) {
					targetValue = BooleanSynonyms.Yes;
				} else {
					targetValue = BooleanSynonyms.No;
				}
			}
		}
		
		return targetValue;
	}
	
	
	
	static List<DataCollectionQuestionaire> getKNearestTrainingNeighboursToTuple(
			DataCollectionQuestionaire tuple, int k, List<DataCollectionQuestionaire> trainingSet) {
		
		List<DataCollectionQuestionaire> nearestNeighbours = new ArrayList<DataCollectionQuestionaire>();

		// Using TreeMultiMap to allow more than one entry with the same key,
		// so the distance-tuple map is the same size as the training set
		List<DistantQuestionaireTuple> neighbours = 
				new ArrayList<DistantQuestionaireTuple>(); // Sorted queue
		for( DataCollectionQuestionaire oneNeighbour : trainingSet ) {
			double distanceToOneNeighbour = distanceBetweenTwoTuples( tuple, oneNeighbour );
			
			neighbours.add( new DistantQuestionaireTuple(distanceToOneNeighbour, oneNeighbour) );
		}
		
		// let's sort using the custom comparator in DistantQuestionaireTuple
		Collections.sort( neighbours );
		
		return getKFirstFromDistanceQueue( k, neighbours );
	}
	

	static double distanceBetweenTwoTuples( 
			DataCollectionQuestionaire tuple, DataCollectionQuestionaire oneNeighbour ) {
		
		double sumOfSquaredDistances = 0;
		
		// instead of having a list of enums (attrbute list) as in the
		// mushroom lab excerize, that can be looped over, we'll hand pick
		// attributes here, as they are a mix of nominal and numerical values.
		
		sumOfSquaredDistances += distanceBetweenNumericValues(
				tuple.getAge(), oneNeighbour.getAge() );
		
		sumOfSquaredDistances += distanceBetweenNumericValues(
				tuple.getProgrammingSkill(), oneNeighbour.getProgrammingSkill() );
		
		sumOfSquaredDistances += distanceBetweenNumericValues(
				tuple.getYearsAtUniversiy(), oneNeighbour.getYearsAtUniversiy());
//		
//		sumOfSquaredDistances += distanceBetweenNominalValues(
//				tuple.getOperatingSystemPreference(), oneNeighbour.getOperatingSystemPreference() );
//		
//		sumOfSquaredDistances += distanceBetweenNominalValues(
//				tuple.getFedUpWithWinter().name(), oneNeighbour.getFedUpWithWinter().name() );
//		
//		sumOfSquaredDistances += distanceBetweenNominalValues(
//				tuple.getFavoriteColour(), oneNeighbour.getFavoriteColour() );
		
		return Math.sqrt( sumOfSquaredDistances );
	}
	
	static int distanceBetweenNominalValues( String nominal1, String nominal2 ) {
		// in fact unnecessary to square the value, which is either 0 or 1
		// - done here for somewhat more clarity.
		
		return (int) Math.pow((nominal1.equals(nominal2) ? 0 : 1), 2 );
	}
	static double distanceBetweenNumericValues( double numeric1, double numeric2 ) {
		
		return Math.pow(Math.abs(numeric1 - numeric2), 2);
	}
	
	
	private static List<DataCollectionQuestionaire> getTrainingSet( 
			List<DataCollectionQuestionaire> questionaireTuples ) {
		
		int endIndex = getTrainingTestPivotIndex(questionaireTuples);
		
		return questionaireTuples.subList(0, endIndex);
	}
	private static List<DataCollectionQuestionaire> getTestSet( 
			List<DataCollectionQuestionaire> questionaireTuples ) {
		
		int startIndex = getTrainingTestPivotIndex(questionaireTuples);
		
		return questionaireTuples.subList(startIndex, questionaireTuples.size());
	}
	private static int getTrainingTestPivotIndex( 
			List<DataCollectionQuestionaire> questionaireTuples ) {
		
		return (int)Math.floor( questionaireTuples.size() * (2.0 / 3.0) ) - 1;
	}
	
	
	static List<DataCollectionQuestionaire> getKFirstFromDistanceQueue( 
			int k, List<DistantQuestionaireTuple> neighbours ) {
		
		List<DataCollectionQuestionaire> nearestKNeighbours = new ArrayList<DataCollectionQuestionaire>();
		
		int i = 0;
		for( DistantQuestionaireTuple tuple : neighbours ) {
			if( i++ == k ) {
				break;
			} else {
				nearestKNeighbours.add( tuple.getTuple() );
			}
		}
		
		return nearestKNeighbours;
	}
	
	
	/*
	 * A sortable class that holds a quiestionaire tuple and it's distance
	 * (to a given tuple).  Sorts on the distance.
	 */
	static class DistantQuestionaireTuple implements Comparable<DistantQuestionaireTuple> {
		
		double distance;
		DataCollectionQuestionaire tuple;
		
		public DistantQuestionaireTuple( double distance, DataCollectionQuestionaire tuple ) {
			this.distance = distance;
			this.tuple = tuple;
		}
		
		public int compareTo( DistantQuestionaireTuple other ) {
			
			return Double.compare( this.distance, other.getDistance() );
		}

		public double getDistance() {
			return distance;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		public DataCollectionQuestionaire getTuple() {
			return tuple;
		}
		public void setTuple(DataCollectionQuestionaire tuple) {
			this.tuple = tuple;
		}
	}
}
