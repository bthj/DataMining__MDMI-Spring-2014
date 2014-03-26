package is.bthj.itu.datamining.preprocessing.data;
import is.bthj.itu.datamining.preprocessing.enums.BooleanSynonyms;

import java.util.Date;


public class DataCollectionQuestionaire {
	
	/*
	 * This class was initially created for a data cleaning lab excersise, 
	 * fields are commented out that won't be used for the individual assignment 
	 */
	

	int age;
//	Date dateOfBirth;
	float programmingSkill; // 1 - 10
	float yearsAtUniversiy;
	String operatingSystemPreference;
	String[] favoriteProgrammingLanguages;
//	int englishFluency;  // 45 - 69
//	String favoriteAnimal;  // choice of Elephant, Zebra, Aspargus
	BooleanSynonyms moreMountainsShouldBeInDenmark;
	BooleanSynonyms fedUpWithWinter;
//	int randomIntegerBetween1and10;
//	float randomRealNumberBetween0and1;
//	float randomRealIndependentNumberBetween0and1;
//	String canteenFoodReview;
	String favoriteColour;
//	boolean knowsWhatANeuralNetworkIs; // neural and vector machine are the same
//	boolean knowsWhatASupportVectorMachineIs; // ...question in 2013
//	boolean knowsWhatSQLis;
//	String favoriteSQLserver;
//	boolean knowsWhatAPrioriAlgorithmIs;
//	float squareRootOf44523675;
//	String noorShakerHometown;
//	String therbForttGlag;
//	int howManyPlanetsInTheSolarSystem;
//	int missingFibonacciNumber;
//	String nameOfGivenSequence;
	
	
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public float getProgrammingSkill() {
		return programmingSkill;
	}
	public void setProgrammingSkill(float programmingSkill) {
		this.programmingSkill = programmingSkill;
	}
	public float getYearsAtUniversiy() {
		return yearsAtUniversiy;
	}
	public void setYearsAtUniversiy(float yearsAtUniversiy) {
		this.yearsAtUniversiy = yearsAtUniversiy;
	}
	
	public String getOperatingSystemPreference() {
		return operatingSystemPreference;
	}
	public void setOperatingSystemPreference(String operatingSystemPreference) {
		this.operatingSystemPreference = operatingSystemPreference;
	}
	public String[] getFavoriteProgrammingLanguages() {
		return favoriteProgrammingLanguages;
	}
	public void setFavoriteProgrammingLanguages(
			String[] favoriteProgrammingLanguages) {
		this.favoriteProgrammingLanguages = favoriteProgrammingLanguages;
	}

	public BooleanSynonyms getMoreMountainsShouldBeInDenmark() {
		return moreMountainsShouldBeInDenmark;
	}
	public void setMoreMountainsShouldBeInDenmark(
			BooleanSynonyms moreMountainsShouldBeInDenmark) {
		this.moreMountainsShouldBeInDenmark = moreMountainsShouldBeInDenmark;
	}
	public BooleanSynonyms getFedUpWithWinter() {
		return fedUpWithWinter;
	}
	public void setFedUpWithWinter(BooleanSynonyms fedUpWithWinter) {
		this.fedUpWithWinter = fedUpWithWinter;
	}
	
	public String getFavoriteColour() {
		return favoriteColour;
	}
	public void setFavoriteColour(String favoriteColour) {
		this.favoriteColour = favoriteColour;
	}

}
