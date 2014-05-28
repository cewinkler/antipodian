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
            new GraphicModel("locust-move").draw(gl);
            
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
            int flap = frame%10;
            if (flap >= 5) new GraphicModel("pteradactyl-flap-high").draw(gl);
            else new GraphicModel("pteradactyl-flap-low").draw(gl);
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
            
            gl.glColor3d(1.0, 0.0, 0.0);    
            glu.gluDisk(circle, 0, 1.0, 20, 1);
            
            gl.glColor3d(0.0, 0.0, 0.0);
            gl.glBegin(GL.GL_TRIANGLE_FAN);
            {
                gl.glVertex2d(0, 0);
                gl.glVertex2d(Math.cos(5*Math.PI/4), Math.sin(5*Math.PI/4));
                gl.glVertex2d(Math.cos(Math.PI/2), Math.sin(Math.PI/2));
                gl.glVertex2d(Math.cos(7*Math.PI/4), Math.sin(7*Math.PI/4));
                gl.glVertex2d(Math.cos(Math.PI/2), Math.sin(Math.PI/2));
            }
            gl.glEnd();
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
            new GraphicModel("pteradactyl").draw(gl);
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
                new GraphicModel("goanna-1").draw(gl);
            }
            else new GraphicModel("goanna").draw(gl);
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
    }

    public void drawMissile(GL gl, Point2D.Double location, double rotation, double width, double height, int frame)
    {
        gl.glPushMatrix();
        {
            gl.glTranslated(location.x, location.y, 0);
            gl.glRotated(rotation, 0, 0, 1);
            gl.glColor3d(0, 1, 0);
            //gl.glColor3d(0.0, 1.0, 1.0);
            gl.glBegin(GL.GL_QUADS);
            {
                gl.glVertex2d(-width/2, -height/2);
                gl.glVertex2d(-width/2, height/2);
                gl.glVertex2d(width/2, height/2);
                gl.glVertex2d(width/2, -height/2);
            }
            gl.glEnd();
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
            
            gl.glColor4d(0.0, 0.5, 1.0, 0.5);    
            glu.gluDisk(circle, 0, 1.0, 20, 1);
        }
        gl.glPopMatrix();
    }

    public void drawBackground(GL gl, double width, double height, int level, int frame)
    {
        gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
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
