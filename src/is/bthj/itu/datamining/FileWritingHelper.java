package is.bthj.itu.datamining;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileWritingHelper {

	public static void writeMapToFile( Map<Integer, Float> map, String fileName ) {
		
		try {
			
			FileWriter writer = new FileWriter( fileName );
			
			for( Map.Entry<Integer, Float> entryPoint : map.entrySet() ) {
				
				writer.append( entryPoint.getKey().toString() ).append('\t');
				writer.append( entryPoint.getValue().toString() ).append('\n');
			}
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
