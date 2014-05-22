/*
 * Drawer.java
 *
 * Created on 17 August 2007, 09:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.drawing;

import javax.media.opengl.*;

import java.awt.geom.*;

/**
 *
 * @author phingsto
 */
public interface Drawer
{
    public void init(GL gl);
    
    public void drawBackground(GL gl, double width, double height, int level, int frame);
    
    public void drawGoanna(GL gl, Point2D.Double location, double size, boolean haveMissiles, boolean moving, int frame);
    
    public void drawLocust(GL gl, Point2D.Double location, double size, double rotation, int frame);
    public void drawFlyingFox(GL gl, Point2D.Double location, double size, double rotation, int frame);
    public void drawWedgeTailedEagle(GL gl, Point2D.Double location, double size, double rotation, int frame);
    public void drawTeradactyl(GL gl, Point2D.Double location, double size, double rotation, int frame);
    
    public void drawMissile(GL gl, Point2D.Double location, double rotation, double width, double height, int frame);
    
    public void drawExplosionSequence(GL gl, Point2D.Double location, int frame);
    public void drawLocustDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame);
    public void drawFlyingFoxDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame);
    public void drawWedgeTailedEagleDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame);
    public void drawTeradactylDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame);
    
    public void drawForeground(GL gl, double width, double height, int level, int score, int goannasLeft, int frame);    
}
