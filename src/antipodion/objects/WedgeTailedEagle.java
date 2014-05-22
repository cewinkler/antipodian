/*
 * WedgeTailedEagle.java
 *
 * Created on 19 August 2007, 15:18
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
public class WedgeTailedEagle extends Attacker
{    
    /**
     * Creates a new instance of WedgeTailedEagle
     */
    private WedgeTailedEagle(Level level, Point2D.Double start, Point2D.Double home, double rot_speed, double speed, double size, int hits, int missiles, int points)
    {
        super(level, start, home, rot_speed, speed, size, hits, missiles, points);
    }
    
    public static WedgeTailedEagle makeWedgeTailedEagle(Level level, Point2D.Double start, Point2D.Double home)
    {
        return new WedgeTailedEagle(level, start, home, WEDGE_TAILED_EAGLE_ROT_SPEED, WEDGE_TAILED_EAGLE_SPEED, WEDGE_TAILED_EAGLE_SIZE, WEDGE_TAILED_EAGLE_HITS, WEDGE_TAILED_EAGLE_MISSILES, WEDGE_TAILED_EAGLE_POINTS);
    }

    public void drawMe(Drawer drawer, GL gl, int frame)
    {
        drawer.drawWedgeTailedEagle(gl, location, size, Math.toDegrees(direction), frame);
    }

    public void drawMyDeathSequence(Drawer drawer, GL gl, int frame)
    {
        drawer.drawWedgeTailedEagleDeathSequence(gl, location, size, Math.toDegrees(direction), frame);
    }
}
