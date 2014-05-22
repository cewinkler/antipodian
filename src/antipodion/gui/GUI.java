/*
 * GUI.java
 *
 * Created on 16 August 2007, 21:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.*;

import au.edu.ecu.jogl.SimpleGLEventListener;
import au.edu.ecu.jogl.SimpleGLFrame;

import antipodion.*;
import antipodion.game.*;
import antipodion.drawing.*;
import antipodion.sounds.*;

/**
 *
 * @author phingsto
 */
public class GUI extends SimpleGLFrame implements SimpleGLEventListener, KeyListener, Constants
{
    private Level level;
    private Drawer drawer;
    
    private LevelSetter setter = new LevelSetter();
    private static final int TRANSITION_FRAMES = 45;
    private static final int TICK = 5;
    private int frame = -1;
    
    public GUI()
    {
        super(Constants.GAME_NAME+" (c)", 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        setExtendedState(MAXIMIZED_BOTH); // full screen
        
        addSimpleGLKeyListener(this);
        
        /// ****** Here is where you put the name of your Drawer!!! ****** //
        drawer = new DefaultDrawer();
        
        setListener(this);
        startJOGL();
        
        Sounds.loadSounds();
    }
    
    public void setLevel(Level level)
    {
        setter.setLevels(this.level, level);
        (new Thread(setter)).start();
    }
    
    public void _init(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        
        gl.glClearColor(0f, 0f, 0f, 0f);
        drawer.init(gl);
    }
    
    public void display(GLAutoDrawable drawable)
    {
        // Grab context for drawing
        GL gl = drawable.getGL();
        
        // clear the page (default colour is white)
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        
        if(level != null)
        {
            int currentFrame = frame;
            
            if(currentFrame >= 0)
            {
                double fraction = currentFrame/(double)TRANSITION_FRAMES;
                gl.glPushMatrix();
                gl.glTranslated(WORLD_WIDTH*0.5, WORLD_HEIGHT*0.5, 0);
                gl.glScaled(1-fraction, 1-fraction, 1);
                gl.glTranslated(-WORLD_WIDTH*0.5, -WORLD_HEIGHT*0.5, 0);
            }
            
            level.draw(drawer, gl);
            
            if(currentFrame >= 0)
            {
                gl.glPopMatrix();
            }
        }
        
        gl.glFlush();
    }
    
    public void keyTyped(KeyEvent arg0)
    {
        // This space intentionally left blank
    }
    
    public void keyPressed(KeyEvent arg0)
    {
        if(level == null) return;
        
        switch(arg0.getKeyCode())
        {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                level.goannaLeft();
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                level.goannaRight();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                level.goannaUp();
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                level.goannaDown();
                break;
            case KeyEvent.VK_ESCAPE:
                level.handleEscape(getContentPane());
                break;
            case KeyEvent.VK_ENTER:
                level.goannaFire();
                break;
            default:
        }
    }
    
    public void keyReleased(KeyEvent arg0)
    {
        if(level == null) return;
        
        switch(arg0.getKeyCode())
        {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                level.goannaStopX();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                level.goannaStopY();
                break;
        }
    }
    
    // little class to run in its own thread and handle
    // transitions between levels
    public class LevelSetter implements Runnable
    {
        private Level oldLevel;
        private Level newLevel;
        
        public void setLevels(Level oldLevel, Level newLevel)
        {
            this.oldLevel = oldLevel;
            this.newLevel = newLevel;
        }
        
        public void run()
        {
            Sounds.stop(Sounds.AMBIENCE);
            Sounds.play(Sounds.START_LEVEL);
            
            if(oldLevel != null)
            {
                for(frame = 0; frame < TRANSITION_FRAMES; frame++)
                {
                    try
                    {
                        Thread.sleep(TICK);
                    }
                    catch(InterruptedException e)
                    {}
                }
            }
            level = newLevel;
            for(frame = TRANSITION_FRAMES; frame >= 0; frame--)
            {
                try
                {
                    Thread.sleep(TICK);
                }
                catch(InterruptedException e)
                {}
            }
            
            Sounds.loop(Sounds.AMBIENCE);
        }
    }
}
