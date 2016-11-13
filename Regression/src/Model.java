import java.util.ArrayList;

public class Model {
	ArrayList<String> params;
	double[] coefficients;
	
	public Model(ArrayList<String> p, double[] c){
		params = p;
		coefficients = c;
	}
}
