import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ModelParser {
	public static Model parseModel() {
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File("model.txt")));
			bf.readLine(); // first line indicates param length
			String line;
			ArrayList<String> params = new ArrayList<String>();
			ArrayList<Double> coef = new ArrayList<Double>();
			while ((line = bf.readLine()) != null) {
				String[] comp = line.split(" ");
				params.add(comp[0]);
				coef.add(Double.parseDouble(comp[1]));
			}
			double[] c = new double[params.size()];
			for (int i = 0; i < coef.size(); i++) {
				c[i] = coef.get(i);
			}
			return new Model(params, c);
		} catch (FileNotFoundException e) {
			System.err.println("No model file found");
			System.exit(1);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
