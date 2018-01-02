package com.amdocs.owa.csv.entities;

public class MassRequestLine extends MassLine {
	public MassRequestLine(){setLineName("Mass Line");}
	private int lineNumber;
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
