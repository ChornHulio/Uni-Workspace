package de.tdreher.algorithms.windows;

public interface IWindow {
	
	/**
	 * Applies window to an array of shorts.
	 * @param data array of doubles to apply the window
	 * @return 
	 */
	public double[] calc(double[] data);
}
