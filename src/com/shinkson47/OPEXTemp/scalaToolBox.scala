package com.shinkson47.OPEXTemp

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper

object scalaToolBox {

  def checkBoundaries[T](x: Int, y: Int, testArray: Array[Array[T]]): Boolean = {
		try {
			val testObject: T = testArray(x)(y);
		} catch {
			case e: ArrayIndexOutOfBoundsException => return false; //part of test, do not report to ems
			case e: Exception => EMSHelper.handleException(e); return false;//Unexpected exception, report
		}
		return true;
  }
}
