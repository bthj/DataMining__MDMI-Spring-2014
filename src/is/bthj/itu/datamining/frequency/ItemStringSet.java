package is.bthj.itu.datamining.frequency;


/**
 * The ItemStringSet class is used to store single transaction informatino,
 * where the elements of the transaction are strings.  
 * Based on an ItemSet class for integers 
 * provided in a DataMining class Lab by andershh, jang
 * @author bthj
 *
 */

public class ItemStringSet {

    final String[] set;

    /***
     * Creates a new instance of the ItemSet class.
     * @param set Transaction content
     */
    public ItemStringSet( String[] set ) {
        this.set = set;
    }

    @Override
    /**
     * hashCode functioned used internally in Hashtable
     */
    public int hashCode() {
    	
    	StringBuilder setConcatenation = new StringBuilder();
    	for( int i = 0; i < set.length; i++ ) {
    		setConcatenation.append( set[i] );
    	}
    	return setConcatenation.hashCode();
       
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
}
