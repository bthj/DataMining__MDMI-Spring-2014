package is.bthj.itu.datamining.association;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * The ItemStringSet class is used to store single transaction informatino,
 * where the elements of the transaction are strings.  
 * Based on an ItemSet class for integers 
 * provided in a DataMining class Lab by andershh, jang
 * @author bthj
 *
 */

public class ItemStringSet implements Comparable<ItemStringSet> {

    final String[] set;

    /***
     * Creates a new instance of the ItemStringSet class.
     * @param set Transaction content
     */
    public ItemStringSet( String[] set ) {
        this.set = set;
    }
    
    /**
     * Create a new ItemStringSet instance 
     * from the concatenation of two arrays 
     */
    public ItemStringSet( String[] set1, String[] set2 ) {
    	this.set = concatenate(set1, set2);
    	Arrays.sort( this.set );
    }
    
    /**
     * Create a new ItemStringSet instance
     * from the concatenation of two other 
     * ItemStringSet instances.
     */
    public ItemStringSet( ItemStringSet set1, ItemStringSet set2 ) {
    	this.set = concatenate( set1.getSet(), set2.getSet() );
    	Arrays.sort( this.set );
    }

    @Override
    /**
     * hashCode functioned used internally in Hashtable
     */
    public int hashCode() {
    	
    	return toString().hashCode();
       
    }
    
    /**
     * Comparator for sorted collections
     * - for example as keys in a TreeMap
     */
    public int compareTo( ItemStringSet other ) {
    	return toString().compareTo( other.toString() );
    }
    
    public String toString() {
    	StringBuilder setConcatenation = new StringBuilder();
    	for( int i = 0; i < set.length; i++ ) {
    		setConcatenation.append( set[i] );
    		if( i < set.length - 1 ) setConcatenation.append(",");
    	}
    	return setConcatenation.toString();
    }
    
    public int size() {
    	return this.set.length;
    }
    
    public ItemStringSet getItemSubset( int from, int to ) {
    	return new ItemStringSet( getSubset(from, to) );
    }
    public String[] getSubset( int from, int to ) {
    	return Arrays.copyOfRange(this.set, from, to);
    }
    
    public String getItem( int index ) {
    	return this.set[ index ];
    }
    public String[] getSet() {
    	return this.set;
    }

    
    @Override
    /**
     * Used to determine whether two ItemSet objects are equal
     */
    public boolean equals( Object o ) {
        if (!(o instanceof ItemStringSet)) {
            return false;
        }
        ItemStringSet other = (ItemStringSet) o;
        if (other.set.length != this.set.length) {
            return false;
        }
        for (int i = 0; i < set.length; i++) {
            if( ! set[i].equals(other.set[i]) ) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Concatenation method.  
     * From: http://stackoverflow.com/a/80503/169858  
     * @param First array
     * @param Second array
     * @return The concatenation of the input arrays.
     */
    private <T> T[] concatenate (T[] A, T[] B) {
        int aLen = A.length;
        int bLen = B.length;

        @SuppressWarnings("unchecked")
        T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen+bLen);
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);

        return C;
    }

}
