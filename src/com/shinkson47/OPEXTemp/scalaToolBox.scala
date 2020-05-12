package com.shinkson47.OPEXTemp

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper

object scalaToolBox {

  def checkBoundaries[T](x: Int, y: Int, testArray: Array[Array[T]]): Boolean = {
    (checkBoundaries(x, testArray) && checkBoundaries(y, testArray(x)))
  }

  /**
	 * Checks index is within bounds of specified array.
	 *
	 * @param i
	 * @param testArray
	 * @return true if within bounds, else false.
	 */
  def checkBoundaries[T](i: Int, testArray: Array[T]): Boolean = {
		try {
			val testObject: T = testArray(i);
		} catch {
		  case e: ArrayIndexOutOfBoundsException => return false; //part of test, do not report to ems
		  case e: Exception => EMSHelper.handleException(e);      //Unexpected exception, report
		}
		return true;
  }
}
