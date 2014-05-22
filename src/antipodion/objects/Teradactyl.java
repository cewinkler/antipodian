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
public class Teradactyl extends Attacker
{    
    /**
     * Creates a new instance of Teradactyl
     */
    private Teradactyl(Level level, Point2D.Double start, Point2D.Double home, double rot_speed, double speed, double size, int hits, int missiles, int points)
    {
        super(level, start, home, rot_speed, speed, size, hits, missiles, points);
    }
    
    public static Teradactyl makeTeradactyl(Level level, Point2D.Double start, Point2D.Double home)
    {
        return new Teradactyl(level, start, home, TERADACTYL_ROT_SPEED, TERADACTYL_SPEED, TERADACTYL_SIZE, TERADACTYL_HITS, TERADACTYL_MISSILES, TERADACTYL_POINTS);
    }
    
    public void drawMe(Drawer drawer, GL gl, int frame)
    {
        drawer.drawTeradactyl(gl, location, size, Math.toDegrees(direction), frame);
    }

    public void drawMyDeathSequence(Drawer drawer, GL gl, int frame)
    {
        drawer.drawTeradactylDeathSequence(gl, location, size, Math.toDegrees(direction), frame);
    }
}
