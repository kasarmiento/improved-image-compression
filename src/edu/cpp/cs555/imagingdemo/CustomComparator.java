package edu.cpp.cs555.imagingdemo;

import java.util.Comparator;

public class CustomComparator implements Comparator<MatrixValue> {

	@Override
	public int compare(MatrixValue o1, MatrixValue o2) {
		return Double.compare(o1.getValue(), o2.getValue());
	}
}
