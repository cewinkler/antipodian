/*
 * FlyingFox.java
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
public class FlyingFox extends Attacker
{    
    /**
     * Creates a new instance of FlyingFox
     */
    private FlyingFox(Level level, Point2D.Double start, Point2D.Double home, double rot_speed, double speed, double size, int hits, int missiles, int points)
    {
        super(level, start, home, rot_speed, speed, size, hits, missiles, points);
    }
    
    public static FlyingFox makeFlyingFox(Level level, Point2D.Double start, Point2D.Double home)
    {
        return new FlyingFox(level, start, home, FLYING_FOX_ROT_SPEED, FLYING_FOX_SPEED, FLYING_FOX_SIZE, FLYING_FOX_HITS, FLYING_FOX_MISSILES, FLYING_FOX_POINTS);
    }
    
    public void drawMe(Drawer drawer, GL gl, int frame)
    {
        drawer.drawFlyingFox(gl, location, size, Math.toDegrees(direction), frame);
    }

    public void drawMyDeathSequence(Drawer drawer, GL gl, int frame)
    {
        drawer.drawFlyingFoxDeathSequence(gl, location, size, Math.toDegrees(direction), frame);
    }
}
