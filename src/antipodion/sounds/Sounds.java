package antipodion.sounds;

import java.awt.*;
import java.applet.*;
import java.net.*;

public class Sounds
{
    // Sound implementation
    
    private static String[] audioFilenames = {
        "ambience.wav",
        "explosion.wav",
        "start.wav",
        "lose.wav",
        "win.wav",
        "charge.wav",
        "fire.wav"};
    private static AudioClip[] audioClips = new AudioClip[audioFilenames.length];
    
    /**
     * Load audio clips for sound effects
     */
    public static void loadSounds()
    {
        for(int i = 0; i < audioFilenames.length; i++)
        {
            try
            {
                audioClips[i] = Applet.newAudioClip(new URL("file:"+audioFilenames[i]));
            }
            catch (Exception e)
            {
                e.printStackTrace(System.out);
                audioClips[i] = null;
            }
        }
    }
    
    public static void play(int aCode)
    {
        AudioClip clip = null;
        if(aCode >= 0 && aCode < audioFilenames.length)
        {
            clip = audioClips[aCode];
        }
        
        if(clip != null)
        {
            clip.play();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    public static void loop(int aCode)
    {
        AudioClip clip = null;
        if(aCode >= 0 && aCode < audioFilenames.length)
        {
            clip = audioClips[aCode];
        }
        
        if(clip != null)
        {
            clip.loop();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    public static void stop(int aCode)
    {
        AudioClip clip = null;
        if(aCode >= 0 && aCode < audioFilenames.length)
        {
            clip = audioClips[aCode];
        }
        
        if(clip != null)
        {
            clip.stop();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    public static final int AMBIENCE = 0;
    public static final int EXPLOSION = 1;
    public static final int START_LEVEL = 2;
    public static final int LOSE = 3;
    public static final int WIN = 4;
    public static final int CHARGE = 5;
    public static final int FIRE = 6;
}
