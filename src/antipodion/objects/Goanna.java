/*
 * Goanna.java
 *
 * Created on 16 August 2007, 20:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.objects;

import javax.media.opengl.*;

import java.util.*;
import java.awt.geom.*;

import antipodion.*;
import antipodion.drawing.*;

/**
 *
 * @author phingsto
 */
public class Goanna implements Shooter, Constants
{    
    private Point2D.Double location;
    private Point2D.Double velocity;
    private double size;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    
    private int nMissilesAvailable = GOANNA_MISSILES;
        
    /**
     * Creates a new instance of Goanna
     */
    public Goanna()
    {
        size = GOANNA_SIZE;
        location = new Point2D.Double(WORLD_WIDTH*0.5, size/2);
        velocity = new Point2D.Double(0, 0);
        
        minX = size/2;
        maxX = WORLD_WIDTH - size/2;
        minY = size/2;
        maxY = WORLD_WIDTH*0.1 - size/2;
    }
    
    public void draw(Drawer drawer, GL gl, int frame)
    {
        drawer.drawGoanna(gl, location, size, nMissilesAvailable > 0, velocity.distanceSq(0, 0) > 0, frame);
    }
    
    public void update(long elapsed)
    {
        double x = location.x + velocity.x*elapsed;
        double y = location.y + velocity.y*elapsed;
        
        updateLocation(x, y);
    }
    
    public Point2D.Double getLocation()
    {
        return location;
    }
    
    public void left()
    {
       velocity.x = -GOANNA_SPEED; 
    }
    
    public void right()
    {
        velocity.x = GOANNA_SPEED;
    }
    
    public void stopX()
    {
        velocity.x = 0;
    }

    public void up()
    {
       velocity.y = GOANNA_SPEED; 
    }
    
    public void down()
    {
        velocity.y = -GOANNA_SPEED;
    }
    
    public void stopY()
    {
        velocity.y = 0;
    }
    
    public Missile fire()
    {
        if(nMissilesAvailable > 0)
        {
            Missile missile = new Missile(
                    this,
                    new Point2D.Double(location.x, location.y+size),
                    GOANNA_MISSILE_SPEED,
                    0);
            nMissilesAvailable--;
            
            return missile;
        }
        else
        {
            return null;
        }
    }
    
    public void reload()
    {
        nMissilesAvailable++;
    }
    
    public boolean hit(Point2D.Double hitPoint)
    {
        return location.distance(hitPoint) < size;
    }
    
    private void updateLocation(double x, double y)
    {
        location.x = Math.max(minX, Math.min(maxX, x));
        location.y = Math.max(minY, Math.min(maxY, y));
    }
}
