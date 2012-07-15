package de.tdreher.algorithms.windows;

public class HannWindow implements IWindow {

	@Override
	public double[] calc(double[] data) {
		int length = data.length;
		for(int i = 0; i < length; i++)
		{
			data[i] *=	0.5 * (1 - Math.cos((2 * Math.PI * i) / (length - 1)));
		}
		return data;
	}

}


