/*
 * Swarm.java
 *
 * Created on 16 August 2007, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.game;

import antipodion.sounds.Sounds;
import javax.media.opengl.*;

import antipodion.*;
import antipodion.objects.*;
import antipodion.drawing.*;
import antipodion.gui.*;
import antipodion.sequences.*;

import java.awt.geom.*;
import java.util.*;

/**
 *
 * @author phingsto
 */
public class Swarm implements Constants
{
    private ArrayList<Attacker> attackers;    
    private int nCanCharge;
    
    /**
     * Creates a new instance of Swarm
     */
    private Swarm()
    {
        attackers = new ArrayList<Attacker>();
        nCanCharge = 0;
    }
    
    public void update(long elapsed, Goanna fighter)
    {
        if(nCanCharge > 0 && attackers.size() > 0 && Math.random() < CHARGE_PROB)
        {
            int n = (int)(Math.random()*attackers.size());
            Attacker attacker = attackers.get(n);
            if(attacker.getState() == AttackerState.WAITING)
            {
                Sounds.play(Sounds.CHARGE);
                attacker.charge(this, fighter);
                nCanCharge--;
            }
        }
        
        for(Attacker attacker: attackers)
        {
            attacker.update(elapsed);
        }
    }
    
    public void doneCharge()
    {
        nCanCharge++;
    }

    synchronized public int hitPoints(Level level, Point2D.Double hitPoint)
    {
        for(Attacker attacker: attackers)
        {
            if(attacker.isHit(hitPoint))
            {
                boolean charging = attacker.getState() == AttackerState.CHARGING;
                attacker.hit();
                if(!attacker.isAlive())
                {
                    level.startSequence(AnimationSequence.getAttackerDeathSequence(attacker));
                    if(charging) doneCharge();
                    attackers.remove(attacker);
                }
                return attacker.getPoints()*(charging? 2: 1);
            }
        }
        
        return 0;
    }
    
    public boolean isAlive()
    {
        return attackers.size() > 0;
    }

    synchronized public void draw(Drawer drawer, GL gl, int frame)
    {
        for(Attacker attacker: attackers)
        {
            attacker.draw(drawer, gl, frame);
        }
    }
    
    public static Swarm makeSwarm_1(Level level)
    {
        Swarm swarm = new Swarm();
        
        int rows = 3;
        int cols = 6;        
        double width = 3*cols*MAX_ATTACKER_SIZE;
        double height = 3*rows*MAX_ATTACKER_SIZE;
        Point2D.Double swarmHome = new Point2D.Double(WORLD_WIDTH*0.5, WORLD_HEIGHT - height - MAX_ATTACKER_SIZE);
                
        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < cols; col++)
            {
                double left = swarmHome.x - width/2;
                double bottom = swarmHome.y;
                Point2D.Double home =  new Point2D.Double(
                        left + (col+0.5)*width/cols,
                        bottom + (row+0.5)*height/rows);
                Point2D.Double start = new Point2D.Double(home.x - WORLD_WIDTH, home.y - WORLD_HEIGHT/2);
                if(row == 2 && (col == 1 || col == 4))
                {
                    swarm.attackers.add(FlyingFox.makeFlyingFox(level, start, home));
                }
                else
                {
                    swarm.attackers.add(Locust.makeLocust(level, start, home));
                }
            }
        }
        
        swarm.nCanCharge = 3;
    
        return swarm;
    }

    public static Swarm makeSwarm_2(Level level)
    {
        Swarm swarm = new Swarm();
        
        int rows = 4;
        int cols = 8;        
        double width = 3*cols*MAX_ATTACKER_SIZE;
        double height = 3*rows*MAX_ATTACKER_SIZE;
        Point2D.Double swarmHome = new Point2D.Double(WORLD_WIDTH*0.5, WORLD_HEIGHT - height - MAX_ATTACKER_SIZE);
        
        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < cols; col++)
            {
                double left = swarmHome.x - width/2;
                double bottom = swarmHome.y;
                Point2D.Double home =  new Point2D.Double(
                        left + (col+0.5)*width/cols,
                        bottom + (row+0.5)*height/rows);
                Point2D.Double start = new Point2D.Double(home.x + WORLD_WIDTH, home.y - WORLD_HEIGHT/2);
                if(row == 3 && (col == 1 || col == 3 || col == 4 || col == 6))
                {
                    swarm.attackers.add(FlyingFox.makeFlyingFox(level, start, home));
                }
                else
                {
                    swarm.attackers.add(WedgeTailedEagle.makeWedgeTailedEagle(level, start, home));
                }
            }
        }
        
        swarm.nCanCharge = 4;
    
        return swarm;
    }

    public static Swarm makeSwarm_3(Level level)
    {
        Swarm swarm = new Swarm();
        
        int rows = 5;
        int cols = 12;        
        double width = 3*cols*MAX_ATTACKER_SIZE;
        double height = 3*rows*MAX_ATTACKER_SIZE;
        Point2D.Double swarmHome = new Point2D.Double(WORLD_WIDTH*0.5, WORLD_HEIGHT - height - MAX_ATTACKER_SIZE);
        
        for(int row = 0; row < rows; row++)
        {
            for(int col = 0; col < cols; col++)
            {
                double left = swarmHome.x - width/2;
                double bottom = swarmHome.y;
                Point2D.Double home =  new Point2D.Double(
                        left + (col+0.5)*width/cols,
                        bottom + (row+0.5)*height/rows);
                Point2D.Double start;
                if(Math.random() > 0.5)
                {
                    start = new Point2D.Double(home.x - WORLD_WIDTH, home.y - WORLD_HEIGHT/2);
                }
                else
                {
                    start = new Point2D.Double(home.x + WORLD_WIDTH, home.y - WORLD_HEIGHT/2);
                }
                if(Math.random() > 0.2)
                {
                    swarm.attackers.add(WedgeTailedEagle.makeWedgeTailedEagle(level, start, home));
                }
                else
                {
                    swarm.attackers.add(Teradactyl.makeTeradactyl(level, start, home));
                }
            }
        }
        
        swarm.nCanCharge = 5;
    
        return swarm;
    }
}
