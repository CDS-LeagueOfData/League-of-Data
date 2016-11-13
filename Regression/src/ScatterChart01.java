
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

public class ScatterChart01 implements ExampleChart<XYChart> {

	public static void main(String[] args) {
		String[] inputClean = { "kills", "deaths", "assists", "goldEarned", "minionsKilled" };

		String path = new File("./data/clean").getAbsolutePath();
		File folder = new File("./data/clean");
		File[] listOfFiles = folder.listFiles();
		
//		String[] files = new String[listOfFiles.length];
//
//		for (int i = 0; i < listOfFiles.length; i++) {
//				files[i]=listOfFiles[i].getAbsolutePath();
//			
//		}
		
		String[] files = new String[] { path + "/amber-clean-1.json", path + "/amber-clean-2.json",
				path + "/david-clean-1.json", path + "/david-clean-2.json", path + "/david-clean-3.json",
				path + "/david-clean-4.json", path + "/david-clean-5.json", path + "/david-clean-6.json",
				path + "/david-clean-7.json", path + "/david-clean-8.json", path + "/david-clean-9.json",
				path + "/sam-clean-1.json", path + "/sam-clean-2.json", path + "/sam-clean-3.json",
				path + "/sam-clean-4.json", path + "/wilson-clean-1.json", path + "/Anklespankin - 1988321629.json"};

		ParseJson parsey = new ParseJson(files, inputClean);
		double[][] a1 = parsey.getValues();
		System.out.println("VALUES");

		List<Double> gameTimes = parsey.getGameTime();
		List<Double> xData = new LinkedList<Double>();
		List<Double> yData = new LinkedList<Double>();

		int varOne = 1;
		int varTwo = 4;

		for (int i = 0; i < a1.length; i++) {
			xData.add(a1[i][varOne] / gameTimes.get(i));
			yData.add(a1[i][varTwo] / gameTimes.get(i));
		}
		System.out.println(gameTimes);
		// System.out.println(xData);
		// System.out.println(yData);
		//
		display(inputClean[varOne] + " vs " + inputClean[varTwo], xData, yData);
	}

	public static void display(String name, List a, List b) {
		ExampleChart<XYChart> exampleChart = new ScatterChart01();
		XYChart chart = ((ScatterChart01) exampleChart).plotScatter(name, a, b);
		new SwingWrapper<XYChart>(chart).displayChart();
		System.out.println(calcCorrelation(a, b));
	}

	public static double getMean(List<Double> a) {
		double temp = 0;
		for (Double b : a) {
			temp += b;
		}
		return temp / a.size();
	}

	public static double stdDev(List<Double> a) {
		double sumSq = 0;
		double mean = getMean(a);
		for (Double b : a) {
			sumSq += Math.pow(b - mean, 2);
		}
		return Math.sqrt(sumSq / (a.size() - 1));
	}

	public static double calcCorrelation(List<Double> a, List<Double> b) {
		double sigma = 0;
		double aMean = getMean(a);
		double bMean = getMean(b);
		double aSTD = stdDev(a);
		double bSTD = stdDev(b);
		for (int i = 0; i < a.size(); i++) {
			sigma += (a.get(i) - aMean) * (b.get(i) - bMean);
		}
		sigma = sigma / (aSTD * bSTD * (a.size() - 1));

		return sigma;

	}

	public XYChart plotScatter(String name, List a, List b) {

		// Create Chart
		XYChart chart = new XYChartBuilder().width(1000).height(800).title(name).build();

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setChartTitleVisible(true);

		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		chart.getStyler().setMarkerSize(8);

		// Series
		chart.addSeries("Gaussian Blob", a, b);

		return chart;
	}

	public XYChart getChart() {
		// TODO Auto-generated method stub
		return null;
	}

}