/*
 * Constants.java
 *
 * Created on 16 August 2007, 21:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion;

/**
 *
 * @author phingsto
 */
public interface Constants
{
    public final String GAME_NAME = "Antipodion";
    
    public final int WORLD_WIDTH = 1000;
    public final int WORLD_HEIGHT = 800;
    
    public final int GOANNAS = 5;
    
    public final int FRAME_MAX = 1000;
    public final int ANIM_FRAMES = 150;
       
    public final double MISSILE_WIDTH = 6;
    public final double MISSILE_LENGTH = 10;
    
    public final double GOANNA_SIZE = 20;
    public final double GOANNA_SPEED = 0.25;
    public final double GOANNA_MISSILE_SPEED = 0.45;
    public final int GOANNA_MISSILES = 3; 
    
    public final double ATTACKER_MISSILE_SPEED = 0.35;
    public final double MAX_ATTACKER_SIZE = 16;
    
    public final double CHARGE_PROB = 0.00005;
    
    public final double LOCUST_SIZE = 10;
    public final double LOCUST_ROT_SPEED = 0.001;
    public final double LOCUST_SPEED = 0.2;
    public final int LOCUST_HITS = 1;
    public final int LOCUST_MISSILES = 1;
    public final int LOCUST_POINTS = 50;

    public final double FLYING_FOX_SIZE = 12;
    public final double FLYING_FOX_ROT_SPEED = 0.0015;
    public final double FLYING_FOX_SPEED = 0.25;
    public final int FLYING_FOX_HITS = 2;
    public final int FLYING_FOX_MISSILES = 1;
    public final int FLYING_FOX_POINTS = 75;

    public final double WEDGE_TAILED_EAGLE_SIZE = 14;
    public final double WEDGE_TAILED_EAGLE_ROT_SPEED = 0.002;
    public final double WEDGE_TAILED_EAGLE_SPEED = 0.3;
    public final int WEDGE_TAILED_EAGLE_HITS = 2;
    public final int WEDGE_TAILED_EAGLE_MISSILES = 2;
    public final int WEDGE_TAILED_EAGLE_POINTS = 100;

    public final double TERADACTYL_SIZE = 16;
    public final double TERADACTYL_ROT_SPEED = 0.0025;
    public final double TERADACTYL_SPEED = 0.3;
    public final int TERADACTYL_HITS = 3;
    public final int TERADACTYL_MISSILES = 2;
    public final int TERADACTYL_POINTS = 150;
}
