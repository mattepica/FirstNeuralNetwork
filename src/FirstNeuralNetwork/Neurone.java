package FirstNeuralNetwork;

import java.util.Arrays;
import java.util.Random;

public class Neurone {
	private double lRate; // Learning rate ( velocità di apprendimento
	private double w[]; // pesi
	private double bias;

	private double sigmoide(double t) {
		return (double) 1 / (1 + Math.exp(-t));
		// return (double) 1 / (1 + Math.pow(Math.E, -t));
	}

	private double dSigmoide(double t) { 	// derivata della sigmoide
		return sigmoide(t) * (1 - sigmoide(t));
	}

	public Neurone(int nInput, double lRate) {
		this.lRate = lRate;
		Random r = new Random();
		w = new double[nInput];
		for (int i = 0; i < w.length; i++) {
			w[i] = r.nextDouble(); 		// inizializzo con pesi casuali
		}
		bias = 0 + (1 - 0) * r.nextDouble(); // bias casuale tra 0 e 1
	}

	public double train(double data[], double target) {
		double z = 0;
		for (int i = 0; i < w.length; i++) { // calcolo previsione della rete
			z = z + data[i] * w[i];
		}
		z = z + bias;
		/*
		 * il target è il mio obiettivo nell'addestramento del neurone
		 */
		double pred = sigmoide(z); 					// normalizzo la previsione

		double cost = Math.pow(pred - target, 2); 	// costo del punto

		double dcost_dpred = 2 * (pred - target); 	// derivata parziale del costo rispetto alla previsione

		double dpred_dz = dSigmoide(z); 			// derivata parziale della previsione rispetto a z

		double dz_db = 1; 							// derivata parziale di z rispetto a b

		double dcost_dz = dcost_dpred * dpred_dz; 	// derivata parziale di z rispetto alla previsione (uso la regola della catena)

		double[] dCosto_ws = new double[w.length]; 	// derivate parziale del costo rispetto ai pesi
		for (int i = 0; i < w.length; i++) {
			dCosto_ws[i] = dcost_dz * data[i];
		}
		double dCosto_db = dcost_dz * dz_db;

		/*
		 * 
		 * aggiornamento dei pesi e del bias
		 * 
		 */
		for (int i = 0; i < w.length; i++) {
			w[i] = w[i] - lRate * dCosto_ws[i];
		}
		bias = bias - lRate * dCosto_db;
		
		return cost;
	}

	public boolean test(double data[], double target) {
		double z = 0;
		for (int i = 0; i < w.length; i++) { // calcolo previsione della rete
			z = z + data[i] * w[i];
		}
		z = z + bias;
		/*
		 * il target è il mio obbiettivo nell'addestramento del neurone
		 */
		double predizione = sigmoide(z);
		if (predizione <= 0.5) {

			return false;

		} else {

			return true;
		}
	}

	public double getlRate() {
		return lRate;
	}

	public void setlRate(double lRate) {
		this.lRate = lRate;
	}

	public double[] getW() {
		return w;
	}

	public void setW(double[] w) {
		this.w = w;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	@Override
	public String toString() {
		return "Percettrone [lRate=" + lRate + ", bias=" + bias + ", w=" + Arrays.toString(w) + "]";
	}

}
