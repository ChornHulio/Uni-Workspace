package de.tdreher.common;

public class SortArray implements Comparable<SortArray> {
	
	  public double value = 0;
	  public int index = 0;
	  
	  public SortArray(double v, int i) {
		  this.value = v;
		  this.index = i;
	  }
	 
	  @Override
	  public int compareTo(SortArray w) {
	    if(this.value < w.value) {
	      return -1;
	    }
	    if(this.value > w.value) {
	      return 1;
	    }
	    return 0;
	  }
}
