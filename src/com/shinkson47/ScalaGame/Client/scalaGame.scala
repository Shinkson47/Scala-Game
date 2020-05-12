package com.shinkson47.ScalaGame.Client

import java.awt.event.KeyEvent

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper
import com.shinkson47.OPEX.backend.io.input.OPEXKeyBindingSpec
import com.shinkson47.OPEX.backend.runtime.engine.{OPEX, Splash}
import com.shinkson47.OPEX.backend.runtime.hooking.{OPEXHook, OPEXHookUpdater}
import com.shinkson47.OPEX.backend.runtime.threading.OPEXGame
import com.shinkson47.OPEX.frontend.windows.rendering.ContentWindow
import com.shinkson47.OPEX.frontend.windows.{OPEXWindow, OPEXWindowHelper}
import com.shinkson47.ScalaGame.Game.{Game, GameProducer, staticKeyParser}
import com.shinkson47.ScalaGame._

/**
 * Static scala game client class.
 *
 * @See https://github.com/Shinkson47/Scala-Snake
 *
 * Houses, displays, and interacts with instances of the Game class.
 *
 * Built using my custom gaming library, OPEX (Open Phoenix Engine) to provide tools
 * for form management, key binding, update events and error management.
 * @See https://github.com/Shinkson47/OPEX
 * @Author Jordan Tyler Gray, P2540338
 * @Extends OPEXGame, runnable game interface for the engine framework.
 * @Extends OPEXHook, hook interface for engine's update loop.
 */
object scalaGame extends OPEXGame with OPEXHook {
  /**
   * @Extends OPEXVersionable, from OPEXGame.
   * Indicates the date based version of scala game.
   * @return  OPEXVersionable formatted verion.
   */
  override def VERSION(): String = "2020.5.10.A"

  /**
   *  Displayable game window
   */
  var gameWindow: OPEXWindow = null;

  /**
   * Content pane for the game window
   */
  var clientWindow: ClientWindow = null;

  /**
   * Current game instance
   */
   var gameSuper: Game = null;

  /**
   * Key binding specification,
   * Using OPEX tools, binds key presses directly to a method.
   */
  var keyspec: OPEXKeyBindingSpec = null;                                                                               //defined in startup routine.

  /**
   * jre entry point. Missed in JUnit.
   * Not used to directly execute anything, just to assert the engine is initalised.
   * OPEX will take over to start the client.
   */
  def main(args: Array[String]): Unit = {
    assertInitialised()                                                                                                 //Start engine with this class.
  }

   /**
   * OPEX game payload entry point.
   * Once OPEX has started it's internal systems, a thread for the game
   * payload is created with this class. This method implements java's runnable, and
   * is ran when in the payload thread. It's OPEX's intended entry point for
   * game code.
   */
  def run(): Unit = {
    StartupRoutine();                                                                                                   //Call for snakeGames's startup routine.

    /*
     * Allow to return, no need to keep payload thread open.
     * Key events are handled with java's dispachers, loop updates are handled with
     * OPEX's hook updater.
     */
  };

  /**
   * Snake's startup routine
   */
  def StartupRoutine(): Unit = {
     /*
      * Configure Engine.
      *
      * My engine has features to detect and handle exceptions.
      * Too many exceptions in too short of a time frame, and it assumes one error has caused
      * multiple others, and triggers a cascade based error induced shutdown of the engine and
      * run time. For the tests, these lines are needed.
      */
     EMSHelper.setAllowEIS(false);                                                                                      //Disable Error Induced Shutdowns
     EMSHelper.setAllowCascadeDetection(false);                                                                         //Disable cascade detection
     EMSHelper.setAllowErrNofif(false);                                                                                 //Disable notifications of errors.
    /*
     * prequisites
     */
    gameSuper = GameProducer.initialiseTest2()                                                                          //Create a new game. gameSuper is required for key binding, so it has to be done early.
    keyspec = new OPEXKeyBindingSpec();                                                                                 //Create key spec for the window
    keyspec.addBind(KeyEvent.VK_UP, staticKeyParser.getClass.getDeclaredMethod("au"), staticKeyParser);          //Bind arrows to appropriate methods
    keyspec.addBind(KeyEvent.VK_DOWN, staticKeyParser.getClass.getDeclaredMethod("ad"), staticKeyParser);        //Note: Keybind is required for the clientWindow instantiation, thus it must be defined first.
    keyspec.addBind(KeyEvent.VK_LEFT, staticKeyParser.getClass.getDeclaredMethod("al"), staticKeyParser);
    keyspec.addBind(KeyEvent.VK_RIGHT, staticKeyParser.getClass.getDeclaredMethod("ar"), staticKeyParser);
    keyspec.addBind(KeyEvent.VK_F1, GameProducer.getClass.getDeclaredMethod("initialiseTest1"), GameProducer);
    keyspec.addBind(KeyEvent.VK_F2, GameProducer.getClass.getDeclaredMethod("initialiseTest2"), GameProducer);
    keyspec.addBind(KeyEvent.VK_F3, GameProducer.getClass.getDeclaredMethod("initialiseTest3"), GameProducer);
    keyspec.addBind(KeyEvent.VK_F4, staticKeyParser.getClass.getDeclaredMethod("runJUnit"), staticKeyParser);

    /*
     *  Create and configure window
     */
    gameWindow = OPEXWindowHelper.newWindow(new ContentWindow(), "Snake", false);                                   //Create window for the client, keep it publicly available. Default not visible, content pane needs to change first.
    clientWindow = new ClientWindow();                                                                                  //Create a content pane using the predefined form.
    gameWindow.setContentPane(clientWindow.clientPanelMain);                                                            //OPEX does not yet support providing a content pane in place of a renderer, so it has to be done manually.
    gameWindow.addKeyListener(keyspec);                                                                                 //Add keyspec to window
    gameWindow.pack()                                                                                                   //pack with new content pane.
    gameWindow.setVisible(true);                                                                                        //Now it can be made visible.
    gameWindow.setLocationRelativeTo(null);                                                                             //Centre on screen TODO OPEX automatically centre to screen.

    OPEXHookUpdater.registerUpdateHook(this, "scalaGameClient");                                          //Add this the client to OPEX's hook updater for game updates.

    gameSuper.render();
  }

  /*
   * For compatability with JUnit, this method was merged into the test preperations to ensure the game had been initialised before performing tests,
   * since JUnit does not perform any initialisation by default, and i can't risk changing the test class, then moderation using thier own junit test classes.
   */
  /**
   * Ensure that the engine has been started, and an client is available to load games.
   */
  def assertInitialised(){
    if (!OPEX.isRunning()){
     /*
      * pre engine start prequisites
      */
      Splash.setDisplayMultiplyer(2L);//TODO mis-spelt method name in engine

      /*
       * ready to start engine
       */
     new OPEX(this);                                                                                             //Main OPEX engine startup call.
    }
  }

  /**
   * OPEX indicates that the game payload thread must, or is about to, close.
   */
  def stop(): Unit = {
    gameSuper.save();                                                                                                   //Kind of the intended use, save in this instance does not actually perform any sort of io data save though.
  }

  /**
   * OPEX indicates that this instance has been registered as an update hook
   */
  def enterUpdateEvent(): Unit = {}
  /**
   * OPEX indicates that this instance has been DE-registered as an update hook.
   */
  def exitUpdateEvent(): Unit = {}

  /**
   * OPEX calls to perform regular and constant updates (like a game loop)
   */
  def updateEvent(): Unit = {}
}
