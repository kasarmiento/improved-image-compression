package edu.cpp.cs555.imagingdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class MatrixReader {
	
	double D_matrix[][];
	int P_matrix[][];
	int image[][];
	int error[][];
	static int matrixDim = 32;
	int imageHeight = 173;
	int imageWidth = 265;
	
	public MatrixReader() {
		D_matrix = new double[matrixDim][matrixDim];
		P_matrix = new int[matrixDim][matrixDim];
		image = new int[imageHeight][imageWidth];
		error = new int[imageHeight][imageWidth];
	}
	
	/**
	 * This method loads data from D-matrix matlab output.
	 * D-matrix contains double-type values. Each value is a
	 * Euclidean distance derived from the values between each
	 * pair of indices in the color matrix.
	 * @throws FileNotFoundException
	 */
	public void initializeDMatrix() throws FileNotFoundException {
		String filename = "lotus-d-results.txt";
		Scanner in = new Scanner(new File(filename));
		
		for(int i = 0; i < matrixDim; i++) {
			for(int j = 0; j < matrixDim; j++) {
				
				if(in.hasNext()) {
					String temp = in.next();
					double input = Double.parseDouble(temp);
					D_matrix[i][j] = input;
				}
			}
		}
		if(in.hasNext()) {
			System.out.println("There were some unparsed numbers");
		}
		System.out.println("D matrix");
		prettyprint(D_matrix);
		
	}
	
	public void loadImageDataFromMatlabOutput() throws FileNotFoundException {
		String filename = "lotus.txt";
		Scanner in = new Scanner(new File(filename));
		
		for(int i = 0; i < imageHeight; i++) {
			for(int j = 0; j < imageWidth; j++) {
				
				if(in.hasNext()) {
					String temp = in.next();
					int input = Integer.parseInt(temp);
					image[i][j] = input;
				}
			}
		}
		if(in.hasNext()) {
			System.out.println("There were some unparsed numbers");
		}
		System.out.println("Image Data");
		prettyprint(image);
		
	}
	
	private int predict (int a, int b, int c) {
		if(a == b) {
			return c;
		}
		if(b == c) {
			return a;
		}
		if(a ==c) {
			return b;
		}
		return c;
	}
	
	public void adjustPMatrix() {
		// Not sure yet
		int p;
		
		// Neighbors a, b and c. Current pixel value x.
		int a, b, c, x; 
		
		// Error value e found in P matrix. Not sure yet
		int e, e2; 
		
		// Start at coordinate 1,1 because we are referring 
		// to the neighbors a, b, and c that would otherwise
		// be out of bounds.
		for(int u = 0; u < imageHeight; u++) {
			for(int v = 0; v < imageWidth; v++) {
				
				x = image[u][v];
				
				if (u == 0 && v == 0) {
					p = image[u][v];
					
				} else if(v == 0 && u != 0) { // if we are at a left most pixel
					p = image[u-1][v]; // choose the pixel immediately above x
					
				} else if(u == 0 && v != 0) { // if we are at a top most pixel
					p = image[u][v-1]; // choose the pixel immediately before it
					
				} else {
					a = image[u][v-1];
					b = image[u-1][v-1];
					c = image[u-1][v];
					p = predict(a, b, c);
				}
				e = P_matrix[p][x]; // line 3 in the algorithm			// using value p instead
																		// of using value a to locate
				// Search the row p and increase all the entries		// around P_matrix, we improve				
				// which are smaller than the error value e by 1.		// impression gain.
				for(int h = 0; h < matrixDim; h++) { 
					if (e > P_matrix[p][h]) {
						P_matrix[p][h] = P_matrix[p][h] + 1;
					}
				}
				P_matrix[p][x] = 0;
				
				if(u != 0) { // if this is not the 1st row, then I can define c
					c = image[u-1][v];
					if (p != c) {
					
						e2 = P_matrix[c][x];
						for(int h = 1; h < matrixDim; h++) {
							if (e2 > P_matrix[c][h]) {
								P_matrix[c][h] = P_matrix[c][h] + 1;
							}
						}
						P_matrix[c][x] = 0;
					}
				}
			}
		}
		
		System.out.println("\n\nP-transformed matrix");
		prettyprint(P_matrix);
	}
	
	private static void prettyprint(int[][] d) {
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[0].length; j++) {
				System.out.print(d[i][j] + "\t");
			}
			System.out.println();
		}
	}

	private static void prettyprint(double[][] d) {
		for(int i = 0; i < d.length; i++) {
			for(int j = 0; j < d[0].length; j++) {
				System.out.print(d[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	private static void prettyprint(ArrayList<MatrixValue> r) {
		for(int i = 0; i < matrixDim; i++) {
			System.out.print(r.get(i).getValue() + "\t");
		}
	}
	
	public void createPMatrixFromDMatrix() {
		ArrayList<MatrixValue> listOfMatrixValuesFromRowInD = new ArrayList<MatrixValue>();
		int counter = 0;
		for(double[] row : D_matrix) { // for each row in D
			for(int i = 0; i < matrixDim; i++) { // make a valuedRow to sort in a moment
				MatrixValue v = new MatrixValue(row[i], i);
				listOfMatrixValuesFromRowInD.add(v);
			} //valuedRow is now filled
			
			Collections.sort(listOfMatrixValuesFromRowInD); // sort the valued row
			
			for(int i = 0; i < matrixDim; i++) { // change each value in the valued row to be a unique number from 1 to matrixDim
				listOfMatrixValuesFromRowInD.get(i).setValue(i * 1.0);
			}
			
			for(MatrixValue v : listOfMatrixValuesFromRowInD) {
				P_matrix[counter][v.getIndex()] = (int) v.getValue();
			}
			counter++;
			listOfMatrixValuesFromRowInD.clear();
		}
		
		System.out.println("\n\nP matrix");
		prettyprint(P_matrix);
		
	}
	
	public void createEMatrix() {
		// Integers x, a, b, and c will contain pixel color values
		// ranging from 1 to 32.
		int x, a, b, c; 
		
		// Integer p will contain the pixel color value with least error
		// ranging from 1 to 32.
		int p;
		
		// e will contain the error value which is derived from P[p][x]
		int e;
		
		for(int u = 0; u < imageHeight; u++) {
			for(int v = 0; v < imageWidth; v++) {
				
				// For each pixel in image I,
				// find its corresponding e value from matrix P = p.
				x = image[u][v];
				
				if (u == 0 && v == 0) {
					p = image[u][v];
					
				} else if(v == 0 && u != 0) { // if we are at a left most pixel
					p = image[u-1][v]; // choose the pixel immediately above x
					
				} else if(u == 0 && v != 0) { // if we are at a top most pixel
					p = image[u][v-1]; // choose the pixel immediately before it
					
				} else {
					a = image[u][v-1];
					b = image[u-1][v-1];
					c = image[u-1][v];
					p = predict(a, b, c);
				}
				// Variable p obtained smallest error value from the above if-else statement
				e = P_matrix[p][x];
				
				// For each pixel in error matrix,
				// set its value to p.
				error[u][v] = e;
				
			}
		}
		System.out.println("Error matrix");
		prettyprint(error);
	}
	
	
	

}
