package antipodion.sequences;

import java.awt.geom.*;

import antipodion.*;
import antipodion.objects.*;
import antipodion.sounds.*;

/**
 *
 * @author phingsto
 */
public class AnimationSequence implements Constants
{
    private int frame;
    private Object object;
    private SequenceType type;
    private int sound;
    
    /** Creates a new instance of AnimationSequence */
    private AnimationSequence()
    {
        frame = 0;
        sound = -1;
    }
    
    public static AnimationSequence getAttackerDeathSequence(Attacker attacker)
    {
        AnimationSequence sequence = new AnimationSequence();
        
        sequence.object = attacker;
        sequence.type = SequenceType.ATTACKER_DEATH;
        
        return sequence;
    }

    public static AnimationSequence getExplosionSequence(Point2D.Double location)
    {
        AnimationSequence sequence = new AnimationSequence();
        
        sequence.type = SequenceType.EXPLOSION;
        sequence.sound = Sounds.EXPLOSION;
        sequence.object = location;
        
        return sequence;
    }

    public Object getObject()
    {
        return object;
    }
    
    public SequenceType getType()
    {
        return type;
    }
    
    public int getSound()
    {
        return sound;
    }
    
    public int getFrame()
    {
        return frame;
    }
    
    public void next()
    {
        frame++;
    }
    
    public boolean done()
    {
        return frame > ANIM_FRAMES;
    }
}
