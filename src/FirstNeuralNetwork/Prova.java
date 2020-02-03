package FirstNeuralNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.Dimension;

public class Prova {

	public static void main(String[] args) throws FileNotFoundException {

		int features = 30; // setting per scegliere features usare
		double lRate = 0.1; // learning rate
		double fattore = 0.01; // usato per trasformare il set di dati
		int iterazioni = 10000; // numero di iterazioni
		List<Double> costi = new ArrayList<Double>();
		
		List<ArrayList<Double>> myList = new ArrayList<ArrayList<Double>>();
		File csvFile = new File("train.csv");
		BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
		String row;
		try {
			while ((row = csvReader.readLine()) != null) { // leggo il file
				String[] data = row.split(",");
				List<Double> lineD = new ArrayList<>();
				for (int i = 0; i < data.length; i++) {
					lineD.add(Double.parseDouble(data[i]));
				}
				myList.add((ArrayList<Double>) lineD);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			csvReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// neurone
		Neurone p = new Neurone(features, lRate);

		System.out.println(p.toString());

		/* fase di pprendimento */
		Random r = new Random();
		double[] point = new double[30];
		for (int i = 0; i < iterazioni; i++) {
			int rd = r.nextInt(myList.size());
			for (int j = 1; j < myList.get(rd).size(); j++) {
				point[j - 1] = myList.get(rd).get(j) * fattore;
			}
			if (i % 100 == 0) {
				double a = p.train(point, myList.get(rd).get(0));
				costi.add(a);
			} else {
				p.train(point, myList.get(rd).get(0)); // addestro il neurone
			}
		}

		/* fase di test */
		myList.clear();
		csvFile = new File("test.csv");
		csvReader = new BufferedReader(new FileReader(csvFile));
		try {
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(",");
				List<Double> lineD = new ArrayList<>();
				for (int i = 0; i < data.length; i++) {
					lineD.add(Double.parseDouble(data[i]));
				}
				myList.add((ArrayList<Double>) lineD);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			csvReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int positiveCounter = 0; //utilizzato per fare percentuali
		int mal = 0, ben = 0;  //contatori
		
		for (int i = 0; i < myList.size(); i++) {
			if (myList.get(i).get(0) == 1) {
				positiveCounter++;
			}
			for (int j = 1; j < myList.get(i).size(); j++) {
				point[j - 1] = myList.get(i).get(j) * fattore;
			}
			if (p.test(point, myList.get(i).get(0))) {
				mal++;
			} else {
				ben++;
			}
		}
		/* risultati su grafico */
		System.out.println(p.toString());
		System.out.println("Train: " + iterazioni);
		System.out.println("Lr: " + lRate);
		System.out.println("Feature: " + features);
		System.out.println("Benigni: " + ben + ", maligni: " + mal + ". Casi analizzati: " + myList.size());
		System.out.println("Predizioni errate totali "
				+ Math.abs((positiveCounter - mal) - ((myList.size() - positiveCounter) - ben)) + " su " + myList.size()
				+ " ~"
				+ Math.abs(
						(((positiveCounter - mal) - ((myList.size() - positiveCounter) - ben)) * 100) / myList.size())
				+ "%");
		System.out.println("Maligni errati:" + Math.abs(positiveCounter - mal) + "/" + positiveCounter + " ovvero ~"
				+ Math.abs((((mal - positiveCounter) * 100) / positiveCounter)) + "%");
		GraphPanel a = new GraphPanel(costi);
		a.setPreferredSize(new Dimension(1200, 600));
		JFrame frame = new JFrame("Errore quadratico");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(a);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
