package de.tdreher.morris;

public class Move {
	
	private int fromCell = -1; // -1 is in the beginning
	private int toCell = 0;
	private int removal = -1; // -1 == no stone to remove
	
	public void setFromCell(int fromCell) {
		this.fromCell = fromCell;
	}

	public void setToCell(int toCell) {
		this.toCell = toCell;
	}

	public void setRemoval(int removal) {
		this.removal = removal;
	}

	@Override 
	public String toString() {
		return fromCell + " " + toCell + " " + removal;		
	}
}
