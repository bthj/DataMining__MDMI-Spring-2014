package is.bthj.itu.datamining.preprocessing.data;
import is.bthj.itu.datamining.preprocessing.enums.BooleanSynonyms;

import java.util.Arrays;
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
	
	
	@Override
	/**
	 * Used to determine whether two DataCollectionQuestionaire objects are equal
	 */
	public boolean equals( Object o ) {
		if( !(o instanceof DataCollectionQuestionaire) ) {
			return false;
		}
		
		return this.age == ((DataCollectionQuestionaire) o).getAge() &&
				this.programmingSkill == ((DataCollectionQuestionaire) o).getProgrammingSkill() &&
				this.yearsAtUniversiy == ((DataCollectionQuestionaire) o).getYearsAtUniversiy() &&
				this.operatingSystemPreference.equals( ((DataCollectionQuestionaire) o).getOperatingSystemPreference() ) &&
				Arrays.equals( this.favoriteProgrammingLanguages, ((DataCollectionQuestionaire) o).getFavoriteProgrammingLanguages() ) &&
				this.moreMountainsShouldBeInDenmark == ((DataCollectionQuestionaire) o).getMoreMountainsShouldBeInDenmark() &&
				this.fedUpWithWinter == ((DataCollectionQuestionaire) o).getFedUpWithWinter() &&
				this.favoriteColour.equals( ((DataCollectionQuestionaire) o).getFavoriteColour() );
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Age: ").append(this.age).append(", ");
		stringBuilder.append("programming skill: ").append(this.programmingSkill).append(", ");
		stringBuilder.append("years at university: ").append(this.yearsAtUniversiy).append(", ");
		stringBuilder.append("preferred OS: ").append(this.operatingSystemPreference).append(", ");
		stringBuilder.append("favorite programming language: ").append(Arrays.toString(this.favoriteProgrammingLanguages)).append(", ");
		stringBuilder.append("more mountains in Denmark?: ").append(this.moreMountainsShouldBeInDenmark).append(", ");
		stringBuilder.append("fed up with winter?: ").append(this.fedUpWithWinter).append(", ");
		stringBuilder.append("favorite color: ").append(this.favoriteColour).append(", ");
		return stringBuilder.toString();
	}
	
	
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
