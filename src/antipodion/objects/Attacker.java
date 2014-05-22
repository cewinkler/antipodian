/*
 * Attacker.java
 *
 * Created on 16 August 2007, 20:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.objects;

import javax.media.opengl.*;

import java.awt.geom.*;

import antipodion.*;
import antipodion.drawing.*;
import antipodion.game.*;
import antipodion.sequences.*;

/**
 *
 * @author phingsto
 */

public abstract class Attacker implements Shooter, Constants
{
    protected double rot_speed;
    protected double speed;
    protected int hitsRemaining;
    protected int nMissilesAvailable;
    protected int points;    
    protected double size;
    protected Point2D.Double home;
    
    protected Point2D.Double location;
    protected double direction = 0.0;
    
    protected Level level;
    protected Swarm swarm = null;
    protected Goanna goanna = null;

    protected AttackerState state = AttackerState.WAITING;
    
    /**
     * Creates a new instance of Attacker
     */
    public Attacker(Level level, Point2D.Double location, Point2D.Double home,
            double rot_speed, double speed, double size,
            int maxHits, int nMissiles, int points)
    {
        this.location = location;
        this.home = home;
        this.rot_speed = rot_speed;
        this.speed = speed;
        this.size = size;
        this.hitsRemaining = maxHits;
        this.nMissilesAvailable = nMissiles;
        this.points = points;
        this.level = level;
        this.state = AttackerState.RETURNING;
    }

    public void update(long elapsed)
    {   
        if(state == AttackerState.CHARGING)
        {
            double turn = headFor(goanna.getLocation(), elapsed);
            direction = correctAngle(direction + turn);
    
            step(elapsed);
            
            if(Math.abs(turn) < 0.0001*elapsed)
            {
                fire();
            }
            
            if(location.y < 0.2*WORLD_HEIGHT)
            {
                state = AttackerState.RETURNING;
                if(swarm != null) swarm.doneCharge();
            }
        }
        else if(state == AttackerState.RETURNING)
        {
            double turn = headFor(home, elapsed);
            direction = correctAngle(direction + turn);
            step(elapsed);
            
            if(location.distance(home) < size/2)
            {
                location.x = home.x;
                location.y = home.y;
                direction = 0;
                state = AttackerState.WAITING;
            }
        }
    }
    
    public void draw(Drawer drawer, GL gl, int frame)
    {
        if(hitsRemaining > 0)
        {
            drawMe(drawer, gl, frame);
        }
    }
    
    public void drawDeathSequence(Drawer drawer, GL gl, int frame)
    {
        drawMyDeathSequence(drawer, gl, frame);
    }
    
    protected abstract void drawMe(Drawer drawer, GL gl, int frame);
    protected abstract void drawMyDeathSequence(Drawer drawer, GL gl, int frame);
    
    public AttackerState getState()
    {
        return state;
    }
    
    public int getPoints()
    {
        return points;
    }
    
    public void charge(Swarm swarm, Goanna goanna)
    {
        this.swarm = swarm;
        this.goanna = goanna;
        this.direction = 2*Math.PI*Math.random();
        state = AttackerState.CHARGING;
    }
    
    public void fire()
    {
        if(nMissilesAvailable > 0)
        {
            Missile missile = new Missile(
                    this,
                    new Point2D.Double(location.x-2*size*Math.sin(direction), location.y+2*size*Math.cos(direction)),
                    ATTACKER_MISSILE_SPEED,
                    direction);
            nMissilesAvailable--;
            
            level.fireMissile(missile);
        }
    }
    
    public void reload()
    {
        nMissilesAvailable++;
    }
    
    public boolean isHit(Point2D.Double hitPoint)
    {
        return location.distance(hitPoint) < size;
    }
    
    public void hit()
    {
        hitsRemaining--;
    }
    
    public boolean isAlive()
    {
        return hitsRemaining > 0;
    }
    
    private double headFor(Point2D.Double target, double elapsed)
    {
        double tx = target.x - location.x;
        double ty = target.y - location.y;
        
        double angle = Math.atan2(ty, tx) - direction - Math.PI/2;
        angle = correctAngle(angle);
        
        angle = Math.max(-rot_speed*elapsed, Math.min(rot_speed*elapsed, angle));
        
        return angle;
    }
    
    private void step(double elapsed)
    {
        location.x -= speed*Math.sin(direction)*elapsed;
        location.y += speed*Math.cos(direction)*elapsed;
    }
    
    private double correctAngle(double angle)
    {
        while(angle > Math.PI) angle -= 2*Math.PI;
        while(angle < -Math.PI) angle += 2*Math.PI;

        return angle;
    }
}

