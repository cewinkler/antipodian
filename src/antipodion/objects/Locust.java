/*
 * FruitBat.java
 *
 * Created on 19 August 2007, 09:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.objects;

import javax.media.opengl.*;

import java.awt.geom.*;

import antipodion.drawing.*;
import antipodion.game.*;
import antipodion.sequences.*;

/**
 *
 * @author phingsto
 */
public class Locust extends Attacker
{    
    /** Creates a new instance of Locust */
    private Locust(Level level, Point2D.Double start, Point2D.Double home, double rot_speed, double speed, double size, int hits, int missiles, int points)
    {
        super(level, start, home, rot_speed, speed, size, hits, missiles, points);
    }
    
    public static Locust makeLocust(Level level, Point2D.Double start, Point2D.Double home)
    {
        return new Locust(level, start, home, LOCUST_ROT_SPEED, LOCUST_SPEED, LOCUST_SIZE, LOCUST_HITS, LOCUST_MISSILES, LOCUST_POINTS);
    }
    
    public void drawMe(Drawer drawer, GL gl, int frame)
    {
        drawer.drawLocust(gl, location, size, Math.toDegrees(direction), frame);
    }

    public void drawMyDeathSequence(Drawer drawer, GL gl, int frame)
    {
        drawer.drawLocustDeathSequence(gl, location, size, Math.toDegrees(direction), frame);
    }
}
