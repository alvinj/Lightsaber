package com.alvinalexander.lightsaber

import akka.actor._
import com.alvinalexander.sound._
import javazoom.jlgui.basicplayer._
import scala.concurrent.{Future => ConcurrentTask}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import javax.sound.sampled.UnsupportedAudioFileException
import java.io.IOException
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import java.io.File
import javax.sound.sampled.Clip
import javax.sound.sampled.LineListener
import javax.sound.sampled.LineEvent
import sun.audio.AudioStream
import sun.audio.AudioPlayer
import java.io.StringWriter
import java.io.PrintWriter
import java.io.BufferedInputStream

case class PlayHumSound(distanceMoved: Double)
case object PlayClashSound

class SoundPlayerActor extends Actor {

    val helper = context.actorOf(Props[SoundPlayerHelper], name = "SoundPlayerHelper")

    def receive = {
        case PlayHumSound(distanceMoved) => helper ! PlayHumSound(distanceMoved) 
        case PlayClashSound => helper ! PlayClashSound 
        case _ =>  //
    }

}

/**
 * TODO i can cache these sounds like i did in TypewriterFX
 */
class SoundPlayerHelper extends Actor {
    
    // note: i manually adjusted the volumes to equalize them, and 'q' means 'quieter'
    val humSound       = "hum2qq.wav"     // length = ~61
    val humSoundSlow   = "hum2fqq.wav"    // length = 63
    val humSoundMedium = "hum2gaqq.wav"   // length = 52.75
    val humSoundHigh   = "hum2aqq.wav"    // length = 56
    val clashSound     = "clash2qq.wav"   // length = 59

    var isPlaying = false
    
    def receive = {
        case PlayHumSound(distanceMoved) => playHumSound(distanceMoved)
        case PlayClashSound => playSoundFile(clashSound, 57)
        case _ => // 
    }

    /**
     * play different sounds based on the distance the mouse moved.
     * i started to use "mouse speed" here, but that wasn't necessary.
     */
    def playHumSound(distanceMoved: Double) {
        if (distanceMoved < 2.0) {
            playSoundFile(humSoundSlow, 61)
        } else if (distanceMoved > 2.0 && distanceMoved < 4.0) {
            playSoundFile(humSound, 59)
        } else if (distanceMoved > 4.0 && distanceMoved < 8.0) {
            playSoundFile(humSoundMedium, 50)
        } else {
            playSoundFile(humSoundHigh, 54)
        }
    }
    
    def playSoundFile(soundFileName: String, delay: Int) {
        // don't play a new sound if i think one is currently playing
        // (otherwise the sounds stack up and are not in real time)
        if (isPlaying) return 
        try {
            isPlaying = true
            val thread = new Thread {
                override def run {
                    playSoundFileWithJavaAudio(soundFileName)
                    Thread.sleep(delay)
                    isPlaying = false
                }
            }.start
        } catch {
            case t: Throwable => 
                isPlaying = false
                // logger.debug(Arrays.toString(e.getStackTrace))
        }
    }

    
    // mostly from java2s.com/Code/Java/Development-Class/AnexampleofloadingandplayingasoundusingaClip.htm
    @throws(classOf[UnsupportedAudioFileException])
    @throws(classOf[IOException])
    @throws(classOf[LineUnavailableException])
    def playSoundFileWithJavaAudio(soundFileName: String) {
        val bis = new BufferedInputStream(getClass.getResourceAsStream(soundFileName))
        val audioInputStream = AudioSystem.getAudioInputStream(bis)
        val info = new DataLine.Info(classOf[Clip], audioInputStream.getFormat) // DataLine.Info
        val clip = AudioSystem.getLine(info).asInstanceOf[Clip]
        clip.open(audioInputStream)
        clip.addLineListener(new LineListener {
            def update(event: LineEvent) {
                if (event.getType == LineEvent.Type.STOP) {
                    event.getLine.close
                    bis.close
                }
            }
        })
        clip.start
    }
    
    

}
    
    
    
    





