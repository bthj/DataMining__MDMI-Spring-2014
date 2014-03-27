package is.bthj.itu.datamining.association;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KSubsets {
	
	private ItemStringSet superset;
	private List<ItemStringSet> subsets;

	public static void main(String[] args) {
		
		// quick test:
		ItemStringSet languages = new ItemStringSet( new String[] {"Java", "Python", "ObjectiveC", "TCL"} );
		KSubsets kSubsets = new KSubsets(languages);
		for( ItemStringSet oneSubset : kSubsets.getKSubsets( 3 ) ) {
			System.out.println( Arrays.toString(oneSubset.getSet()) );
		}
	}

	/**
	 * Creates a new instance of the KSubsets class
	 * @param superset The set to generate k subsets from
	 */
	public KSubsets( ItemStringSet superset ) {
		this.superset = superset;
	}
	
	/**
	 * @param k Size of subsets to generate.
	 * @return List of subsets of size k.
	 */
	public List<ItemStringSet> getKSubsets( int k ) {
		subsets = new ArrayList<ItemStringSet>();
		
		String[] branch = new String[k];
		
		combine(superset.getSet(), k, 0, branch, 0);
		
		return subsets;
	}
	
	/*
	 * Based on:  
	 * http://exceptional-code.blogspot.dk/2012/09/generating-all-permutations.html
	 */
	private void combine(
			String[] arr, int k, int startId, String[] branch, int numElem ) {
		
		if( numElem == k ) {
			
			subsets.add( new ItemStringSet( Arrays.copyOf(branch, branch.length) ) );
			return;
		}
		
		for( int i = startId; i < arr.length; ++i ) {
			
			branch[numElem++] = arr[i];
			combine( arr, k, ++startId, branch, numElem );
			--numElem;
		}
	}
	
}
