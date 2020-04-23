package com.shinkson47.ScalaSnake;

import java.lang.reflect.Array;

import backend.errormanagement.EMSHelper;

/**
 * temporary container for developing java tools
 * that not yet implemented in the jgel toolbox.
 */
public class tempjgeltools {
	
	/**
	 * returns the larger of two integers.
	 * If values are equal, second value is returned.
	 */
	public static int largerOf(int i, int j) {
		return (i > j) ? i : j;  
	}
	
	/**
	 * returns the smaller of two integers.
	 * If values are equal, second value is returned.
	 */
	public static int smallerOf(int i, int j) {
		return (i < j) ? i : j;  
	}
	
	/**
	 * Checks index of x and y are within the bounds of the specified 2d array.
	 * 
	 * @param x
	 * @param y
	 * @param testArray
	 * @return true if within bounds, else false.
	 */
	public static <T> boolean checkBoundaries(int x, int y, T[][] testArray) {
		return (checkBoundaries(x, testArray) && checkBoundaries(y, testArray[x]));
	}
	
	/**
	 * Checks index is within bounds of specified array.
	 * 
	 * @param i
	 * @param testArray
	 * @return true if within bounds, else false.
	 */
	public static <T> boolean checkBoundaries(int i, T[] testArray) {
		try {
			Object testObject = testArray[i];
		} catch (ArrayIndexOutOfBoundsException e) {
			//Part of test, do not report to ems.
			return false;
		} catch (Exception e) {
			//Unexpected exception, report.
			EMSHelper.handleException(e);
		}
		return true;
	}
}
