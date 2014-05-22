/*
 * Level.java
 *
 * Created on 16 August 2007, 20:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.game;

import java.awt.Component;
import javax.media.opengl.*;

import java.util.*;
import java.awt.geom.*;

import antipodion.*;
import antipodion.drawing.*;
import antipodion.objects.*;
import antipodion.sequences.*;
import antipodion.sounds.*;
import javax.swing.JOptionPane;

/**
 *
 * @author phingsto
 */
public class Level implements Constants, Runnable
{
    private int ID;
    
    private Swarm swarm = null;
    
    private Goanna goanna = null;
    private Goanna secondGoanna = null;
    
    private ArrayList<Missile> missiles = new ArrayList<Missile>();
    
    private ArrayList<AnimationSequence> sequences = new ArrayList<AnimationSequence>();
    
    private int frame = 0;
    private int goannasLeft = 0;
    private int score = 0;
    
    private boolean goannaCaptured = false;
    private boolean goannaDoubled = false;
    
    // exit codes
    private int levelExitCode = LEVEL_EXIT_ERROR;
    public static final int LEVEL_EXIT_ERROR = -1;
    public static final int LEVEL_EXIT_SUCCESS = 0;
    public static final int LEVEL_EXIT_DIED = 1;
    public static final int LEVEL_EXIT_RESTART = 2;
    public static final int LEVEL_EXIT_QUIT = 3;
    
    private static final int TICK = 50;

    /** Creates a new instance of Level */
    private Level(int ID)
    {
        this.ID = ID;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public void draw(Drawer drawer, GL gl)
    {
        drawer.drawBackground(gl, WORLD_WIDTH, WORLD_HEIGHT, ID, frame);
        
        if(goanna != null)
        {
            goanna.draw(drawer, gl, frame);
        }
        swarm.draw(drawer, gl, frame);
        
        synchronized(missiles)
        {
            for(Missile missile: missiles)
            {
                missile.draw(drawer, gl, frame);
            }
        }

        // draw sequences
        synchronized(sequences)
        {
            for(AnimationSequence sequence: sequences)
            {
                Point2D.Double location;
                switch(sequence.getType())
                {
                    case EXPLOSION:
                        location = (Point2D.Double)sequence.getObject();
                        drawer.drawExplosionSequence(gl, location, sequence.getFrame());
                        break;
                    case ATTACKER_DEATH:
                        Attacker attacker = (Attacker)sequence.getObject();
                        attacker.drawDeathSequence(drawer, gl, sequence.getFrame());
                        break;
                    default:
                }
            }
        }

        drawer.drawForeground(gl, WORLD_WIDTH, WORLD_HEIGHT, ID, score, goannasLeft, frame);
    }
    
    public Thread startLevel(int goannasLeft, int score)
    {
        this.goannasLeft = goannasLeft;
        this.score = score;
        
        goannaCaptured = false;
        goannaDoubled = false;
        
        levelExitCode = LEVEL_EXIT_ERROR;
        
        sequences = new ArrayList<AnimationSequence>();
        
        Thread thread = new Thread(this);
        thread.start();
        
        return thread;
    }
    
    public void run()
    {
        goanna = new Goanna();

        long gameTime = 0;
        long previousTime = System.currentTimeMillis()-TICK;
        
        while(  levelExitCode != LEVEL_EXIT_DIED
                && levelExitCode != LEVEL_EXIT_RESTART
                && levelExitCode != LEVEL_EXIT_SUCCESS
                && levelExitCode != LEVEL_EXIT_QUIT)
        {
            // time stuff
            long currentTime = System.currentTimeMillis();
            long elapsed = Math.min(TICK, currentTime - previousTime);
            previousTime = currentTime;
            gameTime += elapsed;
            frame = (int)((gameTime/TICK)%FRAME_MAX);
            
            // update objects
            
            swarm.update(elapsed, goanna);
            
            goanna.update(elapsed);
            
            ArrayList<Missile> deadMissiles = new ArrayList<Missile>();
            synchronized(missiles)
            {
                for(Missile missile: missiles)
                {
                    missile.update(elapsed);
                    if(!missile.isAlive())
                    {
                        deadMissiles.add(missile);
                    }
                }
                for(Missile missile: deadMissiles)
                {
                    missiles.remove(missile);
                }
            }

            // handle collisions

            boolean goannaHit = false;

            // missiles hit attackers or goanna?
            deadMissiles = new ArrayList<Missile>();
            synchronized(missiles)
            {
                for(Missile missile: missiles)
                {
                    Point2D.Double location = missile.getLocation();
                    int points = 0;
                    
                    if(goanna.hit(location))
                    {
                        goannaHit = true;
                        deadMissiles.add(missile);
                        missile.die();
                        startSequence(AnimationSequence.getExplosionSequence(location));
                    }
                    else if((points = swarm.hitPoints(this, location)) > 0)
                    {
                        score += points;
                        deadMissiles.add(missile);
                        missile.die();
                        startSequence(AnimationSequence.getExplosionSequence(location));
                    }
                }
                for(Missile missile: deadMissiles)
                {
                    missiles.remove(missile);
                }
            }
            
            if(goannaHit)
            {
                goannasLeft--;
                if(goannasLeft == 0)
                {
                    goanna = null;
                }
                else
                {
                    goanna = new Goanna();
                }
            }
            
            // handle sequences
            boolean sequencesRunning = updateSequences();
            
            if(escapePressed)
            {
                currentTime += doHandleEscape();
                previousTime = currentTime;
            }
            
            if(sequencesRunning) // sequences need some time to be seen
            {
                try
                {
                    Thread.sleep(0, 10);
                }
                catch(InterruptedException e)
                {}
            }
                                
            if(goannasLeft == 0)
            {
                levelExitCode = LEVEL_EXIT_DIED;
            }

            if(!swarm.isAlive())
            {
                levelExitCode = LEVEL_EXIT_SUCCESS;
            }
        }

        // play out pending sequences
        while(updateSequences())
        {
            try
            {
                Thread.sleep(0, 10);
            }
            catch(InterruptedException e)
            {}
        }    
    }
    
    public void startSequence(AnimationSequence sequence)
    {
        synchronized(sequences)
        {
            sequences.add(sequence);
            if(sequence.getSound() > 0)
            {
                Sounds.play(sequence.getSound());
            }
        }
    }
    
    private boolean updateSequences()
    {
        synchronized(sequences)
        {
            ArrayList<AnimationSequence> finished = new ArrayList<AnimationSequence>();
            for(AnimationSequence sequence: sequences)
            {
                sequence.next();
                if(sequence.done())
                {
                    finished.add(sequence);
                }
            }
            for(AnimationSequence sequence: finished)
            {
                sequences.remove(sequence);
            }
            
            return !sequences.isEmpty();
        }
    }
    
    private boolean escapePressed = false;
    private Component escapeComponent = null;
    
    synchronized public void handleEscape(Component parent)
    {
        escapePressed = true;
        escapeComponent = parent;
    }
    
    synchronized public double doHandleEscape()
    {
        long startTime = System.currentTimeMillis();
        
        int option = JOptionPane.showOptionDialog(escapeComponent,
                "What do you want to do now?",
                Constants.GAME_NAME,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Restart the game", "Quit", "Continue"},
                "Cancel"
                );
        
        switch(option)
        {
            case 0: levelExitCode = LEVEL_EXIT_RESTART; break;
            case 1: levelExitCode = LEVEL_EXIT_QUIT; break;
            default:
        }
        escapePressed = false;
        
        return System.currentTimeMillis()-startTime;
    }

    public int getExitCode()
    {
        return levelExitCode;
    }
    
    public int getGoannasLeft()
    {
        return goannasLeft;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public void goannaLeft()
    {
        if(goanna != null)
        {
            goanna.left();
        }
    }
    
    public void goannaRight()
    {
        if(goanna != null)
        {
            goanna.right();
        }
    }
    
    public void goannaUp()
    {
        if(goanna != null)
        {
            goanna.up();
        }
    }
    
    public void goannaDown()
    {
        if(goanna != null)
        {
            goanna.down();
        }
    }
    
    public void goannaStopX()
    {
        if(goanna != null)
        {
            goanna.stopX();
        }
    }
    
    public void goannaStopY()
    {
        if(goanna != null)
        {
            goanna.stopY();
        }
    }
    
    public void goannaFire()
    {
        if(goanna != null)
        {
            Missile missile = goanna.fire();
            if(missile != null)
            {
                fireMissile(missile);
            }
        }
    }
    
    public void fireMissile(Missile missile)
    {
        synchronized(missiles)
        {
            Sounds.play(Sounds.FIRE);
            missiles.add(missile);
        }
    }
    
    public static ArrayList<Level> makeLevels()
    {
        ArrayList<Level> levels = new ArrayList<Level>();
        
        levels.add(makeLevel_1());
        levels.add(makeLevel_2());
        levels.add(makeLevel_3());
        
        return levels;
    }
    
    private static Level makeLevel_1()
    {
        Level level = new Level(1);
        
        level.swarm = Swarm.makeSwarm_1(level);
        
        return level;
    }

    private static Level makeLevel_2()
    {
        Level level = new Level(2);
        
        level.swarm = Swarm.makeSwarm_2(level);
        
        return level;
    }

    private static Level makeLevel_3()
    {
        Level level = new Level(3);
        
        level.swarm = Swarm.makeSwarm_3(level);
        
        return level;
    }
}
