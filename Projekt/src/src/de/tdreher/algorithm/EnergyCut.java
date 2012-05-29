package de.tdreher.algorithm;

public class EnergyCut {
	
	/**
	 * @param energyLevel level as double value
	 * @param frame
	 * @return true if the frame is to cut
	 */
	public static boolean calc(double energyLevel, double[] frame) {
		double energy = 0;
		for(int i = 0; i < frame.length; i++) {
			energy += Math.abs(frame[i]);
		}
		energy /= frame.length;
		if(energy > energyLevel) {
			return false;
		}
		return true;
	}

	public static double calcEnergyLevel(int energyLevel, double[] buffer) {
		int length = buffer.length;
		double energy = 0;
		for(int i = 0; i < buffer.length; i++) {
			energy += Math.abs(buffer[i]);
			if(energy >= Double.MAX_VALUE - 100) {
				length = i;
				break;
			}
		}
		energy /= length;		
		return energy*((double) energyLevel/100);
	}

}
