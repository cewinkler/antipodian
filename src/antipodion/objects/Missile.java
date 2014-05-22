/*
 * Missile.java
 *
 * Created on 16 August 2007, 20:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.objects;

import javax.media.opengl.*;

import java.awt.geom.*;

import antipodion.*;
import antipodion.drawing.*;

/**
 *
 * @author phingsto
 */
public class Missile implements SimObject, Constants
{
    private Shooter shooter;
    private Point2D.Double location; // centre
    private Point2D.Double velocity;
    private double direction;
    
    private boolean alive = true;
    
    /** Creates a new instance of Missile */
    public Missile(Shooter shooter, Point2D.Double start, double speed, double direction)
    {
        this.shooter = shooter;
        this.direction = direction;
        
        location = start;
        
        velocity = new Point2D.Double(-speed*Math.sin(direction), speed*Math.cos(direction));
    }

    public void update(long elapsed)
    {
        location.x += velocity.x*elapsed;
        location.y += velocity.y*elapsed;
        
        if( !   (location.x >= 0 &&
                location.x <= WORLD_WIDTH &&
                location.y >= 0 &&
                location.y <= WORLD_HEIGHT))
        {
            die();
        }
    }
    
    public void draw(Drawer drawer, GL gl, int frame)
    {
        drawer.drawMissile(gl, location, Math.toDegrees(direction), MISSILE_WIDTH, MISSILE_LENGTH, frame);
    }
    
    public void die()
    {
        alive = false;
        shooter.reload();
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public Point2D.Double getLocation()
    {
        return location;
    }
}
