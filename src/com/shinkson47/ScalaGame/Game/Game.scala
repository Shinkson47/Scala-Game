package com.shinkson47.ScalaGame.Game

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper
import com.shinkson47.OPEX.backend.runtime.console.OPEXConsole
import com.shinkson47.OPEXTemp.{scalaToolBox, tempOPEXtools}
import com.shinkson47.ScalaGame.Client.GameClient
import com.shinkson47.ScalaGame.Test.GameTest
import org.junit.runner.Result
import org.junit.runner.notification.RunListener

import scala.util.control.Breaks._

/**
 * This class holds an instance of a simple game where 
 * a player moves on a field and collects bounties.
 * See the explanation sheet and comments in this file for details. The constructor builds an
 * instance of a game
 * 
 * @param wall A list of coordinates (as tuples) where walls exist. Example: The parameter List((0,0),(0,1)) puts two wall elements in the upper left corner and the position below.
 * @param bounty A list of bounties, each is a position and a function (i.e. a 3 value tuple). Example: List((0,0,(x: Int)=>x+1)) puts a bounty in the upper left corner which adds 1 to the score.
 * @param playerX The initial x position of the player.
 * @param playerY The initial y position of the player. If playerX and playerY are 0, the player starts in the upper left corner. As the player moves these positions update.
 */
class Game(wall: List[(Int, Int)], bounty: List[(Int,Int, Int=> Int)], var playerX: Int, var playerY: Int) {

  //the current grid, a 10x10 field, initially all cells are false (i.e. no walls)
  private var field: Array[Array[Boolean]] = Array.ofDim[Boolean](10, 10)
  
  //a separate grid holding bounties at relevant positions, initially all cells are set to null (i.e. no bounties)
  private var bounties: Array[Array[Int=>Int]] = Array.ofDim[Int=>Int](10, 10)
  
  /* Please note - to align with the overall case study (see explanation sheet), both of the above two-dimensional arrays 
   * should be accessed in the format field(col)(row) so field(2)(0) would retrieve the 3rd column and the 1st row (as indexing starts at zero), 
   * equivalent to an (x,y) coordinate of (2,0). You may therefore visualise each inner array as representing a column of data.
   */
  
  //the current score, initially 0
  var score: Int = 0
  
  //the current X and Y save position, initially -1
  private var saveX: Int = -1
  private var saveY: Int = -1 
  
  /**
   * Determines the rectangular area that must be covered by the player in a single move to
   * trigger a collection of all bounties in rect of movement.  
   */
  private final val BOUNTY_TOLLERANCE: Int = 9;
  
  /**
   * Default <<null>> mask value for player positions
   */
  private final val NULL_PLAYER_POSITION = -1;
  
  /* This code is executed as part of the constructor. 
   * It uses the list of walls provided to initialise the walls in the field array by setting given coordinates to true.
   * It then uses the list of bounties to initialise the corresponding array by setting given coordinates to the provided function.
   */
  wall.foreach(w => field(w._1)(w._2)=true)
  bounty.foreach(w => bounties(w._1)(w._2)=w._3)
  render()
  
  /**
   * Repeatedly run a sequence of commands. For example:
   *    for(i <- 1 to 5) println("Hello")
   * can be replaced by
   *    rpt(5)(println("Hello"))
   */
  def rpt (n: Int) ( commands: => Unit ) {
    for (i <- 1 to n) { commands }
  }
  
  
  
  
  
  
  /********************************************************************************
   * COURSEWORK STARTS HERE - COMPLETE THE DEFINITIONS OF EACH OF THE OPERATIONS
   * WE SUGGEST YOU RUN THE GameTest SUITE AFTER EVERY CHANGE YOU MAKE TO THESE
   * SO YOU CAN SEE PROGRESS AND CHECK THAT YOU'VE NOT BROKEN ANYTHING THAT USED
   * TO WORK.
   *******************************************************************************/
  
  /**
   * Returns the current position of the player as a tuple, in (x,y) order.
	 */
  def getPlayerPos(): (Int, Int) = {
    return (playerX,playerY);
  }
  
  
  /**
   * Updates saveX and saveY to the current player position.
   */
  def save(): Unit = {
    saveX = playerX;
    saveY = playerY;
  }
  
  /**
	 * Returns the current score.
 	 */
  def getScore(): Int = score

  /**
	 * Returns the current save position as a tuple, in (x,y) order.
 	 */
  def getSavePos(): (Int, Int) =  {
    return (saveX,saveY);
  }

  /**
   * AL: Arrow Left.  Move the player one place to the left. If
   * there is a wall or the field ends, nothing happens. If there
   * is a bounty, it is collected. If 9 or more fields are covered 
   * from a saved position, the bounties in the rectangle are collected.
   */
  def al() {
    al(1);
  }
  
  /**
   * AR: Arrow Right.  Move the player one place to the right. If
   * there is a wall or the field ends, nothing happens. If there
   * is a bounty, it is collected. If 9 or more fields are covered 
   * from a saved position, the bounties in the rectangle are collected.
   */
  def ar() {
    ar(1)
  }
  
  /**
   * AU: Arrow Up.  Move the player one place up. If
   * there is a wall or the field ends, nothing happens. If there
   * is a bounty, it is collected. If 9 or more fields are covered 
   * from a saved position, the bounties in the rectangle are collected.
   */
  def au() {
    au(1)
  }
  
  /**
   * AD: Arrow Down.  Move the player one place down. If
   * there is a wall or the field ends, nothing happens. If there
   * is a bounty, it is collected. If 9 or more fields are covered 
   * from a saved position, the bounties in the rectangle are collected.
   */
  def ad() {
    ad(1)
  }

  /**
   * AL: Arrow Left n.  Move the player n places to the left. If
   * there is a wall or the field ends, the player stops before 
   * the wall or end of field. Any bounties are collected and if 
   * 9 or more fields are covered from a saved position after an 
   * individual move, the bounties in the rectangle are collected.
   * Negative numbers or 0 as a parameter cause no effect.
   */
  def al(n: Int) {
    move('l', n);
  }
  
  /**
   * AR: Arrow Right n.  Move the player n places to the right. If
   * there is a wall or the field ends, the player stops before 
   * the wall or end of field. Any bounties are collected and if 
   * 9 or more fields are covered from a saved position after an 
   * individual move, the bounties in the rectangle are collected.
   * Negative numbers or 0 as a parameter cause no effect.
   */
  def ar(n: Int) {
    move('r', n);
  }
  
  /**
   * AU: Arrow Up n.  Move the player n places to up. If
   * there is a wall or the field ends, the player stops before 
   * the wall or end of field. Any bounties are collected and if 
   * 9 or more fields are covered from a saved position after an 
   * individual move, the bounties in the rectangle are collected.
   * Negative numbers or 0 as a parameter cause no effect.
   */
  def au(n: Int) {
    move('u', n);
  }

  /**
   * AD: Arrow Down n.  Move the player n places down. If
   * there is a wall or the field ends, the player stops before 
   * the wall or end of field. Any bounties are collected and if 
   * 9 or more fields are covered from a saved position after an 
   * individual move, the bounties in the rectangle are collected.
   * Negative numbers or 0 as a parameter cause no effect.
   */
  def ad(n: Int) {
    move('d', n);
  }
 
  
  /**
   * Positively calculates difference between params
   * 
   *	-7, -10 = 3
   */
  def calcDistance(x$1: Int, x$2: Int): Int = {
    val diff = Math.abs(x$1-x$2);
    if ((String.valueOf(diff).charAt(0) == '-'))
      return 0 - diff;
    diff;
  }

  /**
   * Closes the gap between two values by 1.
   * positively calculated distances regardless of param's relative polarity.
   *
   * -7, -10 = 2
   *
   * @return positive distance between params, -1.
   */
  def closeCorrelation(x$1: Int, x$2: Int): Int = calcDistance(x$1, x$2) - 1;


  /**
   * This moves the player according to a string. The string can contain the 
   * letters l, r, u, d representing left, right, up, down moves.  If
   * there is a wall or the field ends, the individual move is not 
   * executed. Any further moves are done. Any bounties are collected and the
   * save position is evaluated.
   */
  def move(s: String) {
    s.map(move(_,1));
  }
  
  def move(dir: Char, dis: Int){
    var n = dis;                                                                                                        //make writeable
    if (n <= 0) return;                                                                                                 //reject negative or nil movement

    breakable {while (n > 0){                                                                                           //Move player, temporarily
      var prevX = playerX                                                                                               //store position before each movement, to revert if movement is impossible.
      var prevY = playerY

      /*
       * Perform movement
       */
      dir match {                                                                                                       //Move player based on the char provided.
        case 'l' => {
          if (playerX <= 0) {break;}
          playerX -= 1;  
        }
        case 'r' => {
          if (playerX >= field.size - 1) {break;}
          playerX += 1;
        }
        case 'u' => {
          if (playerY <= 0) {break;}
          playerY -= 1;
        }  
        case 'd' => {
          if (playerY >= field(playerX).size -1) {break;}
          playerY += 1;
        }
        case _ => {
           EMSHelper.handleException(new Exception("Invalid direction supplied"));                                      //No need to restore values; no move will be made with an invalid char.
           break;
        }
      }
      
      //bound check.
      if (field(playerX)(playerY)) {  //TODO Should use bound check method, but it broke everything so i rolled it back in git. this works for now.
        playerX = prevX;                                                                                                //revert movement before returning.
        playerY = prevY;
        break;                                                                                                          //reject continued movement; wall collision
      }

      /*
       * Player has made a valid movement.
       */
      checkBounty();                                                                                                    //Check for, and handle bounty.
      n -= 1;                                                                                                           //Reduce count of moves remaining by one.
    }}
    checkBounties();                                                                                                    //Check move for bounties to collect.
    render();                                                                                                           //Render the new move.
  }

  /**
   * Determins if the x,y provided are within bounds of the field, and in a place where a player can be.
   *
   * @return true if a player may exsist at position provided.
   */
  def boundCheck(x: Int, y: Int):Boolean = (scalaToolBox.checkBoundaries[Boolean](x, y, field) && !field(x)(y))

  /**
   * Identifies the maximum overall bounty in the game. This is the sum 
   * of the current score and the possible score from applying all of the remaining bounties.
   * No bounties are collected here, only the max score is returned.
   */
  def maxBounty(): Int = {
    var max = score;
    for(x <- bounties){
      for(y <- x){
         if (y != null){
           max = y(max);
         }
      }
    }
    max
  }


  /**
   * Sets the savePos to the values of the parameters. This method is
   * for testing only. Normally, save() is used for this purpose.
   */
  def setSavePos(saveX: Int, saveY: Int): Unit =  {
    this.saveX=saveX
    this.saveY=saveY
  }


  /**
   * Sets the saved player position back to default null position.
   */
  def resetSave(): Unit = {
    saveX = NULL_PLAYER_POSITION;
    saveY = NULL_PLAYER_POSITION;
  }


  /**
   * Checks if the rectangle defined by the current position and saved position 
   * covers nine or more positions. If yes, it collects bounties in it, increases the 
   * score, and erases the bounties.
   */
  def checkBounties() {
    val disX = calcDistance(playerX, saveX) + 1;                                                                        //Calculate area of rect between save and player
    val disY = calcDistance(playerY, saveY) + 1;
    if (disX * disY < BOUNTY_TOLLERANCE) return;                                                                        //If rect does not meet tollerence for collection, return.
    
    var goalx = tempOPEXtools.largerOf(playerX, saveX);                                                                 //Determine relative correlation values for itteration
    var goaly = tempOPEXtools.largerOf(playerY, saveY);
    var startx = tempOPEXtools.smallerOf(playerX, saveX);
    var starty = tempOPEXtools.smallerOf(playerY, saveY);
    
    
    if (!scalaToolBox.checkBoundaries[Boolean](startx, starty, field) || !scalaToolBox.checkBoundaries(goalx, goaly, field)) {
      return;                                                                                                           //Don't continue if either save or player is out of bounds of the array.
    }
    
    while(startx <= goalx){
      var itty = starty;
      while(itty <= goaly){                                                                                             //Collect all bounties within rect
        checkBounty(startx, itty);
        itty += 1
      }
      startx += 1;
    }
    resetSave();                                                                                                        //Clear save once rectangle is spent.
  }

  /**
   * Checks for and handles any bounty at the player's current position.
   */
  def checkBounty(): Unit = checkBounty(playerX, playerY)


  /**
   * Checks if x,y is a bounty. A bounty exists if the cell is not
   * set to null. If a bounty does exist, increase the score, 
   * and then erase the bounty, i.e. set it back to null.
   */
  def checkBounty(x: Int, y: Int) {
  try {   
      if (bounties(x)(y) != null){
        score = bounties(x)(y)(score);
        bounties(x)(y) = null;
      }
     } catch {
       case e: ArrayIndexOutOfBoundsException => EMSHelper.handleException(e)
     }
  }

  /**
   * This gives a string in the format for move, which collects the maximum bounty. No specific
   * requirements for the efficiency of the solution exist, but the solution must consist of a finite number 
   * of steps. The move is combined of a number of moves
   * given by suggestMove. If these are not possible, an empty string is returned. No bounties are collected 
   * and the player must be at the original position after the execution of the method.
   */
  def suggestSolution(): String = {
    ""
  }

  object Axis extends  {
    val VERTICAL: Int = 1
    val HORIZONTAL: Int = 2
    val POSITIVE: Int = 3
    val NEGATIVE: Int = 4
  }

  /**
   * This gives a string in the format for move, which moves from the current position to 
   * position x,y. No specific requirements for the efficiency of the solution exist. The move
   * cannot jump walls. The method is restricted to finding a path which is combined of a number of 
   * left and then a number of up movement, or left/down, or right/up, or right/down movements only.
   * If this is not possible due to walls, it returns an empty string. No actual move is done. If 
   * x or y are outside the field, an empty string is returned as well.
   */
  def suggestMove(x: Int, y: Int): String = {
    if (boundCheck(x, y))""                                                                                             //If out of bounds, return nothing.
    var targetX = x;
    var targetY = y;

    val moveVer = calcSuggestMoveVerticalPriority(x,y);

    if (moveVer._2 == (targetX, targetY)) {
      return moveVer._1;                                                                                                //vertical priority was addequate
    }

    val moveHor = calcSuggestMoveHorizontalPriority(x,y);
    moveHor._1;
    }

   /**
   * Calculates the suggested movement with priority for horizontal movement.
   * @param targetX
   * @param targetY
   * @return movement string to target, using movements following the order
    *         [horizontal, vertical, horizontal, veritcal]
   */
  private def calcSuggestMoveHorizontalPriority(targetX: Int, targetY: Int): (String, (Int, Int)) = {
    var move = "";
    var currentX = playerX;
    var currentY = playerY;
    var previous: (Int, Int) = (currentX, currentY);

    breakable {
      while ((currentX != targetX) && (currentY != targetY)) {
        if (currentX != targetX) {
          var calc: (String, Int) = partialHorizontalMove((currentX, currentY), targetX);
          move += calc._1;
          currentX += calc._2;
        }

        if (currentY != targetY) {
          var calc: (String, Int) = partialVerticalMove((currentX, currentY), targetY);
          move += calc._1;
          currentY += calc._2;
        }

        if (previous == (currentX, currentY)) break; //If no move was made, don't continue. Move cannot be made.
        previous = (currentX, currentY)
      }}
    (move, (currentX, currentY))
    }

  /**
   * Calculates the suggested movement with priority for vertical movement.
   * @param targetX
   * @param targetY
   * @return movement string to target, using movements following the order
   *         [vertical, horizontal, veritcal, horizontal]
   */
  private def calcSuggestMoveVerticalPriority(targetX: Int, targetY: Int): (String, (Int, Int)) = {
      var move = "";
      var currentX = playerX;
      var currentY = playerY;
      var previous: (Int, Int) = (currentX, currentY);

      breakable {
        while ((currentX != targetX) && (currentY != targetY)) {
          if (currentY != targetY) {
            var calc: (String, Int) = partialVerticalMove((currentX, currentY), targetY);
            move += calc._1;
            currentY += calc._2;
          }

          if (currentX != targetX) {
            var calc: (String, Int) = partialHorizontalMove((currentX, currentY), targetX);
            move += calc._1;
            currentX += calc._2;
          }

          if (previous == (currentX, currentY)) break; //If no move was made, don't continue. Move cannot be made.
          previous = (currentX, currentY)
        }}
      (move, (currentX, currentY))
  }

  /**
   * Calculates a partial move along the vertical axis.
   * @param position starting Y position
   * @param to target Y position
   * @return move string to move to target on the vertical axis.
   */
  private def partialVerticalMove(position: (Int, Int), to: Int): (String, Int) ={
    partialAxisMove(position, to, 'd', 'u', Axis.VERTICAL);
  }

  /**
   * Calculates a partial move along the horizontal axis.
   * @param position starting X position
   * @param to target X position
   * @return move string to move to target on the horizontal axis.
   */
  private def partialHorizontalMove(position: (Int, Int), to: Int): (String, Int) ={
    partialAxisMove(position, to, 'r', 'l', Axis.HORIZONTAL);
  }

  /**
   * Calculates a single axis, single direction move from one point to another, including boundary detection.
   *
   * @param position - 2d starting location
   * @param axisTarget - on axis target
   * @param positive - character to indicate a positive move
   * @param negative - character to indicate a negative move
   * @param axis - axis of move, horizontal or vertical.
   * @apinote: for positive, negative, and axis, use literals defined in this.Axis.
   * @return move string for a single axis using positive and negative chars provided to reach target.
   */
  private def partialAxisMove(position: (Int, Int), axisTarget: Int, positive: Char, negative: Char, axis: Int): (String, Int) ={
    var move = "";                                                                                                      //result store
    val from = if (axis == Axis.VERTICAL) position._2 else position._1;
    var direction: Int = if (tempOPEXtools.largerOf(from, axisTarget) == from) Axis.NEGATIVE else Axis.POSITIVE;        //Determin direction of travel within axis.
    var oPosition: (Int, Int) = position;                                                                               //Open scope of indexables for boundary testing.
    var oitt: Int = 0;                                                                                                  //open scope of itterator
    val rel = if (from < axisTarget) 1 else - 1
    breakable{
      for (itt <- from to axisTarget by rel) {
        oPosition = getNextPosition(position._1, position._2, axis, direction);
        oitt = itt;
      if (!boundCheck(position._1, position._2)) break;                                                                 //break from horizontal PF if no more movement is possible.
      move += (if (direction == Axis.POSITIVE) positive else negative);                                                 //Else more movement was possible, record it.

      if (axis == Axis.VERTICAL){                                                                                       //If we're at the target, break
        if (axisTarget == position._1) break;
      } else if (axis == Axis.HORIZONTAL) {
        if (axisTarget == position._2) break;
      }

                                                //Get position for next itteration.
    }}
    if (move != "") {move = move.substring(0, move.length - 1); }                                                       //I don't like this, but it's easier than reducing a gap between ints, requireing calculations of directions and whatnot. It will always do one itteration extra, but at least  that's predictable enough to manage retrospectivly here.
    (move, (if (direction == Axis.POSITIVE) oitt else oitt * -1))                                                       //No more movement is possible, return.,
  }

  /**
   * Determine the next x y using the axis of travel and positive/negative correlation.
   * @param x position
   * @param y position
   * @param axis of travel, Axis.VERTICAL, Axis.HORIZONTAL
   * @param direction of travel, Axis.POSITIVE,
   * @return
   */
  def getNextPosition(x: Int, y: Int, axis: Int, direction: Int): (Int, Int) = {
    var ox = x;
    var oy = y;

    if (axis == Axis.HORIZONTAL) {
      if (direction == Axis.POSITIVE) {
        ox += 1;
      } else if (direction == Axis.NEGATIVE) {
        ox += -1;
      } else {
        //TODO invalid direction
      }

    } else if (axis == Axis.VERTICAL) {
      if (direction == Axis.POSITIVE) {
        oy += 1;
      } else if (direction == Axis.NEGATIVE) {
        oy += -1;
      } else {
        //TODO invalid direction
      }

    } else {
      //TODO invalid axis
    }

    (ox, oy);
  }

  /**
   * Rasters the field, player position, bounties etc to both the client window and console.
   */
  def render(){
    System.out.println("=====================")
    if (GameClient.clientWindow != null){
      GameClient.clientWindow.txtTerminal.setText(null);
      GameClient.clientWindow.txtTerminal.append("Score: " + getScore());}

    for (y <- 0 to 9){
      var line: String = "";
      for (x <- 0 to 9){
        breakable { if (playerX == x && playerY == y){
          line += "[p]"
          break;
        }
        
        if (bounties(x)(y) != null){
          line += "[b]"
        } else if (field(x)(y)) {
          line += "[w]"
        } else {
          line += "[  ]"
        }
        }}
      if (GameClient.clientWindow != null){
      GameClient.clientWindow.txtTerminal.append("\n" + line);}
      System.out.println(line);
    }
  } 
}

/**
 * This object returns a standard instance of Game
 */
object GameProducer{
  
  
  /**
 	 * @return A game with 
 	 * - walls in positions 3,0 3,1 and 3,2
 	 * - a bounty at 4,1 which increases score by 5
 	 * - a bounty at 3,3 which increases score by 10
 	 * - the player in position 0,0
	 */
  def initialiseTest1(): Game = {
    GameClient.assertInitialised();
    GameClient.gameSuper = new Game(List((3,0),(3,1),(3,2)), List((4,1, (x: Int)=>x+5),(3,3, (x: Int)=>x+10)), 0, 0)
    return GameClient.gameSuper;
  }

  /**
 	 * @return A game with 
 	 * - walls in positions 3,3 3,4 3,5 5,3 5,4 and 5,5
 	 * - a bounty at 4,4 which increases score by 1
 	 * - a bounty at 6,3 which increases score by 1 if the current score is 0 or else increases the score by 3
 	 * - the player in position 3,2
	 */
  def initialiseTest2(): Game = {
    GameClient.assertInitialised();
    GameClient.gameSuper = new Game(List((3,3),(3,4),(3,5),(5,3),(5,4),(5,5)), List((4,4, (x: Int)=>x+1),(6,3, (x: Int)=>if(x == 0) x+1 else x+3)), 3, 2)
    return GameClient.gameSuper;
  }

  /**
 	 * @return A game with 
 	 * - walls in positions 3,0 3,1 and 3,2
 	 * - a bounty at 4,1 which increases score by 5
 	 * - a bounty at 3,3 which increases score by 10
 	 * - the player in position 4,1
	 */
  def initialiseTest3(): Game = {
    GameClient.assertInitialised();
    GameClient.gameSuper = new Game(List((3,0),(3,1),(3,2)), List((4,1, (x: Int)=>x+5),(3,3, (x: Int)=>x+10)), 4, 1)
    return GameClient.gameSuper;
  }
}

/**
 * Keybinds are made to this static parser.
 *
 * Binding keys to a game instance would require rebinding with each new game instance.
 * Once bound here, the static reference invokes the current game instance.
 */
object staticKeyParser {
  import org.junit.runner.JUnitCore
  val junit = new JUnitCore

  def au() {
    GameClient.gameSuper.au()
  }

  def ad() {
    GameClient.gameSuper.ad()
  }

  def al() {
    GameClient.gameSuper.al()
  }

  def ar() {
    GameClient.gameSuper.ar()
  }

  /*
  Engine does not feature junit testing, so this method exists instead, using the currently unprotected internalLog method
  to act as if it were inside of OPEX.
   */
  /**
   * Use JUnit library at runtime to execute the test class.
   */
  def runJUnit() {
    junit.addListener(new RunListener{
      override def testRunFinished(res: Result) = {
        OPEXConsole.internalLog("JUnit test completed. passed: " + res.wasSuccessful() + ", " + res.getFailureCount + " failures");
        GameClient.clientWindow.txtJUnit.setText("passed junit test: " + res.wasSuccessful() + ", " + res.getFailureCount + " failures");
    }})
    OPEXConsole.internalLog("User invoked JUnit test.");
    junit.run(classOf[GameTest])
  }


}