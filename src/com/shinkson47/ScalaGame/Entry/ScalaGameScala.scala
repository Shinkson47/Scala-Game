package com.shinkson47.ScalaGame.Entry

import com.shinkson47.ScalaGame.Client.GameClient

/**
 * SCALA RUNTIME ENTRY POINT
 *
 *
 * KOTLIN ENTRY POINT
 *
 * Entry point class for the scala game project.
 *
 * jre entry point. Missed in JUnit.
 * Not used to directly execute anything, just to assert the engine is initalised.
 * OPEX will take over to create and start the client, if it isn't already started.
 * @author gordie
 * @version 1
 **/
class ScalaGameScala {
  def main(args: Array[String]): Unit = {
    GameClient.assertInitialised();                                                                                     //Call to assert the startup of the main game client.
  }
}
