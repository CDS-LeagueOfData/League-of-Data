import java.util.LinkedList;

public class Model {
	LinkedList<String> params;
	double[] coefficients;
	
	public Model(LinkedList<String> p, double[] c){
		params = p;
		coefficients = c;
	}
}
