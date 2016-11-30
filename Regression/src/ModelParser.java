import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ModelParser {
	public static Model parseModel() throws IOException {
		@SuppressWarnings("resource")
		BufferedReader bf = new BufferedReader(new FileReader(new File("model.txt")));
	
		int numParams = Integer.parseInt(bf.readLine());
		
		String[] params = new String[numParams];
		double[] coef = new double[numParams];
		
		String[] in;
		for(int i = 0; i < numParams; i++){
			in = bf.readLine().split(":");
			params[i] = in[0];
			coef[i] = Double.parseDouble(in[1]);
		}
		
		return new Model(params, coef);
		
	}
}
