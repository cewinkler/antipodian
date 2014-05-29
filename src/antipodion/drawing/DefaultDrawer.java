/*
 * DefaultDrawer.java
 *
 * Created on 17 August 2007, 09:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.drawing;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.GLUT;

import java.awt.geom.*;

import antipodion.*;
import models.GraphicModel;

/**
 *
 * @author phingsto
 */
public class DefaultDrawer implements Drawer, Constants
{
    // useful things for drawing filled circles
    private GLU glu = new GLU();
    private GLUquadric circle = glu.gluNewQuadric();
    private GLUT glut = new GLUT();
    private int MovingCount = 0;
    private double PreviousX = 0;
    private GraphicModel[] Goanna = new GraphicModel[]{
        new GraphicModel("goanna"),
        new GraphicModel("goanna-1")
    };
    private GraphicModel Missile = new GraphicModel("missile");
    private GraphicModel[] Eagle = new GraphicModel[] {
        new GraphicModel("eagle"),
        new GraphicModel("eagle-flap")
    };
    private GraphicModel FlyingFox = new GraphicModel("flyingfox");
    private GraphicModel Locust = new GraphicModel("locust-move");
    private GraphicModel[] Teradactyl = new GraphicModel[] {
        new GraphicModel("pteradactyl-flap-high"),
        new GraphicModel("pteradactyl-flap"),
        new GraphicModel("pteradactyl-flap-low"),
    };
    private GraphicModel Explosion = new GraphicModel("explosion");
    private GraphicModel Hud = new GraphicModel("hud-bar");
    private GraphicModel HudLevel = new GraphicModel("hud-level");
    private GraphicModel HudLives = new GraphicModel("hud-lives");
    private GraphicModel HudScore = new GraphicModel("hud-score");
    private float[] Colours = new float[]{ 0.0f, 0.0f, 0.0f};
    private float CurrentColour = 0.0f;
    private boolean ColourIncrease = true;
    private double YOffset;
    /** Creates a new instance of DefaultDrawer */
    public DefaultDrawer()
    {
        
    }
    
    public void init(GL gl)
    {
        // enable transparency
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public void drawLocust(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glScaled(size, size, 0);
            gl.glRotated(rotation, 0, 0, 1);
            gl.glColor3d(1.0, 1.0, 0.0);
            if (rotateObject(frame)) gl.glRotated(180,0.0,1.0,0.0);
            Locust.draw(gl);
            
        }
        gl.glPopMatrix();
    }

    public void drawLocustDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        double newSize = size*(1 + frame/(double)ANIM_FRAMES);
        rotation += (frame%10)*37.5;
        drawLocust(gl, location, newSize, rotation, 0);
    }
    
    public void drawFlyingFox(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glScaled(size, size, 0);
            gl.glRotated(rotation, 0, 0, 1);
            FlyingFox.draw(gl);
        }
        gl.glPopMatrix();
    }

    public void drawFlyingFoxDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        double newSize = size*(1 + frame/(double)ANIM_FRAMES);
        rotation += (frame%10)*37.5;
        drawFlyingFox(gl, location, newSize, rotation, 0);
    }

    public void drawWedgeTailedEagle(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glScaled(size, size, 0);
            gl.glRotated(rotation, 0, 0, 1);
            int flap = frame % 20;
            (flap > 5?Eagle[0]:Eagle[1]).draw(gl);
        }
        gl.glPopMatrix();
    }

    public void drawWedgeTailedEagleDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        double newSize = size*(1 + frame/(double)ANIM_FRAMES);
        rotation += (frame%10)*37.5;
        drawWedgeTailedEagle(gl, location, newSize, rotation, 0);
    }

    public void drawTeradactyl(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glScaled(size, size, 0);
            gl.glRotated(rotation, 0, 0, 1);
            int flap = frame%20;
            if (flap <= 5) Teradactyl[0].draw(gl);
            else if (flap <= 10) Teradactyl[1].draw(gl);
            else if (flap <= 15) Teradactyl[2].draw(gl);
            else Teradactyl[0].draw(gl);
        }
        gl.glPopMatrix();
    }

    public void drawTeradactylDeathSequence(GL gl, Point2D.Double location, double size, double rotation, int frame)
    {
        double newSize = size*(1 + frame/(double)ANIM_FRAMES);
        rotation += (frame%10)*37.5;
        drawTeradactyl(gl, location, newSize, rotation, 0);
    }

    public void drawGoanna(GL gl, Point2D.Double location, double size, boolean haveMissiles, boolean moving, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y+YOffset, 0);
            gl.glScaled(size, size, 0); 
           if (moving) {
                MovingCount++;
                if (MovingCount > 20) MovingCount = 0;
                if (MovingCount >= 10) gl.glRotated(180,0,1,0);
                Goanna[1].draw(gl);
            }
            else Goanna[0].draw(gl);
        }
        gl.glPopMatrix();
        drawGoannaMissileIndicator(gl, location, haveMissiles, frame);
        PreviousX = location.x;
    }
    
    private void drawGoannaMissileIndicator(GL gl, Point2D.Double location, boolean haveMissiles, int frame) {
        gl.glPushMatrix();
        if (haveMissiles) {
            gl.glTranslated(location.x - 50, location.y+YOffset - 20, 0);
            gl.glRotated((frame%10)*36, 0, 1, 0);
            Missile.draw(gl);
        }
        gl.glPopMatrix();
    }

    public void drawMissile(GL gl, Point2D.Double location, double rotation, double width, double height, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glRotated(rotation, 0, 0, 1);
            Missile.draw(gl);
        }
        gl.glPopMatrix();
    }

    public void drawExplosionSequence(GL gl, Point2D.Double location, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            double scale = (30.0*frame)/ANIM_FRAMES;
            gl.glScaled(scale, scale, 1);
            Explosion.draw(gl);
        }
        gl.glPopMatrix();
    }

    public void drawBackground(GL gl, double width, double height, int level, int frame)
    {
        Colours[0] = 0.0f;
        Colours[1] = 0.0f;
        Colours[2] = 0.0f;
        if (CurrentColour > 0.4) ColourIncrease = false;
        if (CurrentColour < 0) ColourIncrease = true;
        CurrentColour += ColourIncrease?0.01:-0.01;
        Colours[level-1] = CurrentColour;
        gl.glClearColor(Colours[0],Colours[1],Colours[2],0.0f);
    }

    public void drawForeground(GL gl, double width, double height, int level, int score, int goannasLeft, int frame)
    {
        YOffset = height*0.1;
        gl.glPushMatrix();
        gl.glTranslated(0,0,0); 
        gl.glScaled(width,height*0.1,0);
        Hud.draw(gl);
        gl.glPopMatrix();
        
        addHudText(gl, HudLevel, 100);
        addHudText(gl, HudLives, width-75);
        addHudText(gl, HudScore, 200);
        
        gl.glColor3d(1.0, 1.0, 1.0);
        gl.glRasterPos2d(75, 25);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ""+level);
        
        gl.glRasterPos2d(175, 25);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ""+score);

        double xStep = 25;
        for (int i = 1; i <= goannasLeft;i++) {
            gl.glPushMatrix();
            gl.glTranslated(width-(i*xStep),25,0);
            gl.glScaled(5,5,0);
            Goanna[0].draw(gl);
            gl.glPopMatrix();
        }
    }
    
    private boolean rotateObject(int frame) {
        return (frame % 10)*2 > 10;
    }
    
    private void addHudText(GL gl, GraphicModel model, double x) {
        gl.glPushMatrix();
        gl.glTranslated(x, 40, 0);
        gl.glScaled(20,35,0);
        model.draw(gl);
        gl.glPopMatrix();
    }
}
