package edu.cpp.cs555.imagingdemo;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		MatrixReader matrixReader = new MatrixReader();
		matrixReader.loadImageDataFromMatlabOutput();
		
		// Create a matrix D using the Euclidean distances between
		// all index values in the colormap.
		matrixReader.initializeDMatrix();
		
		// Create the P matrix using the values from matrix D
		matrixReader.createPMatrixFromDMatrix();
		
		// Adjust the P matrix using the algorithm psuedo-distance transform. 
		matrixReader.adjustPMatrix();
		
		// Generate a matrix E which contains error values
		// from matrix P according to the pixel values within the 
		// image data.
		matrixReader.createEMatrix();
	}

}
