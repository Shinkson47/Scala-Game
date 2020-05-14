package com.shinkson47.ScalaGame.Entry;

import com.shinkson47.ScalaGame.Client.GameClient;

/**
 * JAVA RUNTIME ENTRY POINT
 *
 * Entry point class for the scala game project.
 *
 * @author gordie
 * @version 1
 */
public class ScalaGameJava {

    /**
     * Missed in JUnit.
     * Not used to directly execute anything, just to assert the engine is initalised.
     * OPEX will take over to create and start the client, if it isn't already started.
     */
    public static void main(String[] args) {
        GameClient.assertInitialised();                                                                                 //Call to assert the startup of the main game client.
                                                                                                                        //Main thread is allowed to return as it's no longer required, renaming the thread to destroyjvm, attempting to indicate for jre to close.
    }
}
