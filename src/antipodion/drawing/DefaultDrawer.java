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
    private float[] Colours = new float[]{ 0.0f, 0.0f, 0.0f};
    private float CurrentColour = 0.0f;
    private boolean ColourIncrease = true;
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
        
        drawTeradactyl(gl, location, newSize, rotation, 0);
    }

    public void drawGoanna(GL gl, Point2D.Double location, double size, boolean haveMissiles, boolean moving, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glScaled(size, size, 0);
            if (moving) {
                MovingCount++;
                if (MovingCount > 20) MovingCount = 0;
                if (MovingCount >= 10) gl.glRotated(180,0,1,0);
                Goanna[1].draw(gl);
            }
            else Goanna[0].draw(gl);
            if(haveMissiles)
            {
                gl.glColor3d(0.0, 1.0, 1.0);
                gl.glBegin(GL.GL_QUADS);
                {
                    gl.glVertex2d(-0.1, 0.8);
                    gl.glVertex2d(-0.1, 1.0);
                    gl.glVertex2d(0.1, 1.0);
                    gl.glVertex2d(0.1, 0.8);
                }
                gl.glEnd();
            }
        }
        gl.glPopMatrix();
        PreviousX = location.x;
    }

    public void drawMissile(GL gl, Point2D.Double location, double rotation, double width, double height, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glRotated(rotation, 0, 0, 1);
            new GraphicModel("missile").draw(gl);
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
        System.out.println(level);
        Colours[0] = 0.0f;
        Colours[1] = 0.0f;
        Colours[2] = 0.0f;
        if (CurrentColour > 0.5) ColourIncrease = false;
        if (CurrentColour < 0) ColourIncrease = true;
        CurrentColour += ColourIncrease?0.01:-0.01;
        Colours[level-1] = CurrentColour;
        gl.glClearColor(Colours[0],Colours[1],Colours[2],0.0f);
    }

    public void drawForeground(GL gl, double width, double height, int level, int score, int goannasLeft, int frame)
    {
        int ystep = -10;
        double x = 10;
        double y = 50;
        
        gl.glColor3d(1.0, 0.0, 0.0);

        gl.glRasterPos2d(x, y); y -= ystep;
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Level: " + level);

        gl.glColor3d(1.0, 0.5, 0.0); 

        gl.glRasterPos2d(x, y); y -= ystep;
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Lives left: " + goannasLeft);

        gl.glRasterPos2d(x, y); y -= ystep;
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, "Score: " + score);
    }
    
    private boolean rotateObject(int frame) {
        return (frame % 10)*2 > 10;
    }
}
