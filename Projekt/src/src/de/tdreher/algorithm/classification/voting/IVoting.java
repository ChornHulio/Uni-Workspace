package de.tdreher.algorithm.classification.voting;

import de.tdreher.core.Speaker;

public interface IVoting {
	
	public double[] process(Speaker speakerToCheck) throws Exception;

}
