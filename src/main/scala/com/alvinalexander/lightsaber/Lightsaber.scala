package com.alvinalexander.lightsaber

import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.mouse.NativeMouseEvent
import org.jnativehook.mouse.NativeMouseInputListener
import java.awt.Toolkit
import akka.actor._

/**
 * Project Name:   Lightsaber
 * Project Author: Alvin Alexander, http://alvinalexander.com
 * Version:        1.0
 * License:        GNU GPL v.3.0
 * 
 * This code plays "lightsaber" sound effects as you move your mouse pointer/cursor around
 * the screen. It currently also plays lightsaber "clashing" sounds when you move the 
 * cursor to the edges of the screen.
 * 
 */
class Lightsaber extends NativeMouseInputListener {
    
    var lastX = 0
    var lastY = 0
    
    // screen info
    val screenSize = Toolkit.getDefaultToolkit.getScreenSize
    val screenHeight = screenSize.height
    val screenWidth = screenSize.width

    // actors
    val system = ActorSystem("LightsaberSystem")
    val soundPlayerActor = system.actorOf(Props[SoundPlayerActor], name = "soundPlayerActor")
    
    def nativeMouseMoved(e: NativeMouseEvent) {
        val x = e.getX
        val y = e.getY

        // play the clash sound effect when the mouse gets to the edges
        if (x <= 1 || x > screenWidth-3 || y <= 1 || y > screenHeight-3) {
            soundPlayerActor ! PlayClashSound
        } else {
            val deltaX = x - lastX
            val deltaY = y - lastY
            val distanceMoved = math.hypot(deltaX.abs, deltaY.abs)
            soundPlayerActor ! PlayHumSound(distanceMoved)
        }
        
        lastX = x
        lastY = y
    }

    def nativeMouseDragged(e: NativeMouseEvent) {
        soundPlayerActor ! PlayClashSound
    }
    
    def nativeMouseClicked(e: NativeMouseEvent) {
        // could play a clash here as well
    }
    def nativeMousePressed(e: NativeMouseEvent) {}
    def nativeMouseReleased(e: NativeMouseEvent) {}

}

object Lightsaber extends App {

    try {
        GlobalScreen.registerNativeHook
    } catch {
        case e: NativeHookException =>
            System.err.println("There was a problem registering the native hook.")
            System.err.println(e.getMessage)
            System.exit(1)
    }
    
    val lightsaber = new Lightsaber

    // add the native listener stuff
    GlobalScreen.getInstance.addNativeMouseListener(lightsaber)
    GlobalScreen.getInstance.addNativeMouseMotionListener(lightsaber)
}














