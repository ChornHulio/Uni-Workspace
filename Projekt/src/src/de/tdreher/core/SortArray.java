package de.tdreher.core;

public class SortArray implements Comparable<SortArray> {
	
	  public double distance = 0;
	  public int index = 0;
	  
	  public SortArray(double d, int i) {
		  this.distance = d;
		  this.index = i;
	  }
	 
	  @Override
	  public int compareTo(SortArray w) {
	    if(this.distance < w.distance) {
	      return -1;
	    }
	    if(this.distance > w.distance) {
	      return 1;
	    }
	    return 0;
	  }
}
