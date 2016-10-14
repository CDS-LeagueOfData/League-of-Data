
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.Styler.LegendPosition;

public class ScatterChart01 implements ExampleChart<XYChart> {

	public static void main(String[] args) {

		List<Integer> xData = new LinkedList<Integer>(Arrays.asList(23, 17, 61, 15, 59, 25, 47, 47, 2, 59, 53, 14, 100,
				21, 18, 5, 77, 11, 46, 85, 62, 88, 37, 43, 11, 17, 54, 36, 32, 10, 3, 34, 52, 87, 95, 35, 81, 66, 83,
				25, 92, 53, 42, 97, 64, 73, 27, 30, 76, 23, 35, 48, 20, 67, 93, 21, 29, 100, 85, 71, 46, 85, 25, 60, 9,
				27, 54, 99, 89, 75, 25, 24, 77, 10, 11, 2, 94, 45, 61, 65, 88, 50, 3, 51, 45, 66, 90, 92, 89, 75, 23, 4,
				68, 34, 5, 94, 74, 23, 63, 85));
		List<Integer> yData = new LinkedList<Integer>(Arrays.asList(69, 48, 84, 100, 51, 59, 53, 86, 41, 17, 73, 74, 6,
				64, 69, 90, 34, 10, 52, 8, 71, 57, 74, 5, 8, 84, 94, 64, 33, 51, 13, 29, 59, 45, 84, 15, 11, 86, 75, 65,
				71, 11, 63, 88, 4, 21, 78, 49, 8, 30, 45, 89, 64, 45, 53, 66, 40, 46, 95, 26, 53, 33, 3, 64, 54, 61, 57,
				50, 23, 80, 34, 76, 33, 15, 56, 20, 40, 12, 15, 65, 76, 40, 49, 46, 100, 3, 26, 47, 94, 65, 79, 33, 52,
				18, 15, 99, 34, 79, 69, 48));

		display(xData, yData);
	}

	public static void display(List a, List b) {
		ExampleChart<XYChart> exampleChart = new ScatterChart01();
		XYChart chart = ((ScatterChart01) exampleChart).plotScatter(a, b);
		new SwingWrapper<XYChart>(chart).displayChart();
		System.out.println(calcCorrelation(a, b));
	}

	public static double getMean(List<Integer> a) {
		double temp = 0;
		for (Integer b : a) {
			temp += b;
		}
		return temp / a.size();
	}

	public static double stdDev(List<Integer> a) {
		double sumSq = 0;
		double mean = getMean(a);
		for (Integer b : a) {
			sumSq += Math.pow(b - mean, 2);
		}
		return Math.sqrt(sumSq / (a.size() - 1));
	}

	public static double calcCorrelation(List<Integer> a, List<Integer> b) {
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

	public XYChart plotScatter(List a, List b) {

		// Create Chart
		XYChart chart = new XYChartBuilder().width(1000).height(800).title("League of Data").build();

		// Customize Chart
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
		chart.getStyler().setChartTitleVisible(true);

		chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
		chart.getStyler().setMarkerSize(8);

		// Series
		chart.addSeries("Gaussian Blob", a, b);

		return chart;
	}

	@Override
	public XYChart getChart() {
		// TODO Auto-generated method stub
		return null;
	}

}