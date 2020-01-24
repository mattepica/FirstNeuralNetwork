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

		int features = 30; //setting per scegliere se usare tutte e 30 le features o solamente le 2 piu importanti
		double lRate = 0.01;
		double fattore = 0.01; // usato per trasformare il set di dati
		
		List<Double> costi = new ArrayList<Double>();
		List<ArrayList<Double>> myList = new ArrayList<ArrayList<Double>>();
		File csvFile = new File("train.csv");
		BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
		String row;
		try {
			while ((row = csvReader.readLine()) != null) { //leggo il file
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
		
		Neurone p = new Neurone(features, lRate);

		System.out.println(p.toString());
		Random r = new Random();
		double[] point = new double[30];

		long iterazioni = 10000; // numero di iterazioni

		if (features == 30) {
			for (int i = 0; i < iterazioni; i++) {

				int rd = r.nextInt(myList.size());

				for (int j = 1; j < myList.get(rd).size(); j++) {
					point[j - 1] = myList.get(rd).get(j) * fattore;
				}
				if (i % 100 == 0) {
					double a = p.train(point, myList.get(rd).get(0));
					costi.add(a);
				} else {
					p.train(point, myList.get(rd).get(0)); //addestro il neurone
				}
			}
		} else {
			for (int i = 0; i < iterazioni; i++) {
				int rd = r.nextInt(myList.size());
				point[0] = myList.get(rd).get(11) * fattore;
				point[1] = myList.get(rd).get(28) * fattore;

				if (i % 100 == 0) {
					double a = p.train(point, myList.get(rd).get(0));
					costi.add(a);
				} else {
					p.train(point, myList.get(rd).get(0));
				}
			}
		}
		myList.clear();
		csvFile = new File("test.csv"); //si dia per noto la presenza di 66 tumori maligni nel file.
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

		int mal = 0, ben = 0;
		if (features == 30) {
			for (int i = 0; i < myList.size(); i++) {

				for (int j = 1; j < myList.get(i).size(); j++) {
					point[j - 1] = myList.get(i).get(j) * fattore;
				}
				if (p.test(point, myList.get(i).get(0))) {
					mal++;
				} else {
					ben++;
				}
			}
		} else {
			for (int i = 0; i < myList.size(); i++) {
				point[0] = myList.get(i).get(11) * fattore;
				point[1] = myList.get(i).get(28) * fattore;

				if (p.test(point, myList.get(i).get(0))) {
					mal++;
				} else {
					ben++;
				}
			}
		}
		System.out.println(p.toString());
		System.out.println("Train: "+iterazioni);
		System.out.println("Lr: "+lRate);
		System.out.println("Feature: "+features);
		System.out.println("Benigni " + ben + " maligni " + mal + "; errati il " + (int)Math.sqrt(Math.pow((((mal - 66) * 100) / 270),2)) + "%"+"->" +(int)Math.sqrt(Math.pow((mal-66),2)));
		GraphPanel a = new GraphPanel(costi);
		a.setPreferredSize(new Dimension(1200, 600));
		JFrame frame = new JFrame("Accuratezza (costo)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(a);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
