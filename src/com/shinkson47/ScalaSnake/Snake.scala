

package com.shinkson47.ScalaSnake

import backend.runtime.threading.JGELGame
import backend.runtime.engine.JGEL
import frontend.windows.JGELWindow
import backend.runtime.hooking.JGELHook
import frontend.windows.JGELWindowHelper
import backend.io.input.JGELKeyBindingSpec
import backend.runtime.hooking.JGELHookUpdater
import scala.swing.event.KeyEvent
import java.awt.event.KeyEvent
import backend.errormanagement.EMSHelper
import backend.runtime.console.JGELConsole


/**
 * Entry point for a snake game written in scala.
 * @See https://github.com/Shinkson47/Scala-Snake
 * 
 * Built on my custom gaming engine framework, JGEL.
 * (Java Gaming Engine (Library))
 * @See https://github.com/Shinkson47/JGE
 * 
 * @Author Jordan Tyler Gray, P2540338
 * @Extends JGELGame, runnable game interface for the engine framework.
 * @Extends JGELHool, hook interface for engine's update loop.
 */
object Snake extends JGELGame with JGELHook {
  private var gameWindow: JGELWindow = null;
  var gameSuper: Game = GameProducer.initialiseTest2()
  
  /*
   * Java runtime entry point.
   */
  def main(args: Array[String]): Unit = {
    new JGEL(this);             //Start engine.
  } 
  
   /**
   * JGEL game payload entry point.
   * Once JGEL has started it's internal systems, a thread for the game 
   * payload is created with this class. This method implements java's runnable, and
   * is ran when in the payload thread. It's JGEL's intended entry point for
   * game code.
   */
  def run(): Unit = {
    StartupRoutine();
    
    /* Allow to return, no need to keep payload thread open.
     * Key events are handled with java's dispachers, loop updates are handled with
     * JGEL's hook updater.
     */
  };
  
  /**
   * Snake's startup routine
   */
  def StartupRoutine(): Unit = {
     /* My engine has features to detect and handle exceptions.
      * Too many exceptions in too short of a time frame, and it assumes one error has caused
      * multiple others, and triggers a cascade based error induced shutdown of the engine and 
      * run time. For the test, these lines are needed to: 
      * 1) disable error induced shutdowns
      * 2) disable error cascade detection
      * 3) disable popup windows indicating an exception has been caught
      */
     EMSHelper.setAllowEIS(false);
     EMSHelper.setAllowCascadeDetection(false);
     EMSHelper.setAllowErrNofif(false);
    
     gameWindow = JGELWindowHelper.newWindow(Renderer, "Snake"); //Create window, keep reference to it in a variable
     
     val keyspec: JGELKeyBindingSpec = new JGELKeyBindingSpec(); //Create key spec for that window
     
     //Bind arrows to keyspec
     keyspec.addBind(KeyEvent.VK_UP, classOf[Game].getDeclaredMethod("au"), gameSuper); 
     keyspec.addBind(KeyEvent.VK_DOWN, classOf[Game].getDeclaredMethod("ad"), gameSuper); 
     keyspec.addBind(KeyEvent.VK_LEFT, classOf[Game].getDeclaredMethod("al"), gameSuper); 
     keyspec.addBind(KeyEvent.VK_RIGHT, classOf[Game].getDeclaredMethod("ar"), gameSuper);
     
     //Add keyspec to window
     gameWindow.addKeyListener(keyspec); 
     
     //Add this to the hook updater for game updates.
     JGELHookUpdater.registerUpdateHook(this, "PayloadUpdater");
  }
  
  
  /**
   * For compatability with JUnit, this method was merged into the test preperations to ensure the game had been initialised before performing tests,
   * since JUnit does not perform any initialisation by default, and i can't risk changing the test class, then moderation using thier own junit test classes.
   */
  def assertInitialised(){
    if (!JGEL.isRunning()){
     new JGEL(this); 
    }
  }
  
  /**
   * JGEL indicates that the game payload thread must, or is about to, close.
   */
  def stop(): Unit = {
    gameSuper.save(); //Kind of the intended use, save in this instance does not actually perform any sort of io data save though.
  } 
  
  /**
   * JGEL indicates that this instance has been registered as an update hook
   */
  def enterUpdateEvent(): Unit = {}
  
  /**
   * JGEL calls to perform regular and constant updates (like a game loop)
   */
  def exitUpdateEvent(): Unit = {
    
  }
  
  /**
   * JGEL indicates that this instance has been DE-registered as an update hook.
   */
  def updateEvent(): Unit = {
    
  }
}