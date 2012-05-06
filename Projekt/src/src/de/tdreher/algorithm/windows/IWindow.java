package de.tdreher.algorithm.windows;

public interface IWindow {
	
	/**
	 * Applies window to an array of shorts.
	 * @param data array of shorts to apply the window
	 */
	public void calc(short[] data);
}
