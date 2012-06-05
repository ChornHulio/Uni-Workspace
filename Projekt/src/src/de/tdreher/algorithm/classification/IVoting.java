package de.tdreher.algorithm.classification;

import de.tdreher.core.Speaker;

public interface IVoting {
	
	public double[] process(Speaker speakerToCheck) throws Exception;

}
