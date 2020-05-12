package com.shinkson47.ScalaGame.Game

class Player {
  var posX: Int = 0;
  var posY: Int = 0;
  
  def Player(x: Int, y: Int) = {
    posX = x;
    posY = y;
  }
}