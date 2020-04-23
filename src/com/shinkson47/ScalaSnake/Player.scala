package com.shinkson47.ScalaSnake

class Player {
  var posX: Int = 0;
  var posY: Int = 0;
  
  def Player(x: Int, y: Int) = {
    posX = x;
    posY = y;
  }
}