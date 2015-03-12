package edu.cpp.cs555.imagingdemo;

import java.util.Collections;

public class MatrixValue implements Comparable<MatrixValue>{
	
	private double value;
	private int index;
	
	public MatrixValue(double value, int index) {
		this.value = value;
		this.index = index;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int compareTo(MatrixValue o) {
		int lastCmp = Double.compare(getValue(), o.getValue());
		return (lastCmp != 0 ? lastCmp : Double.compare(getValue(), o.getValue()));
	}


}
