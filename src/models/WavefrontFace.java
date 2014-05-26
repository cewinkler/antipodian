package models;

import java.util.ArrayList;
import javax.media.opengl.GL;

public class WavefrontFace {
    public ArrayList<Integer> Points = new ArrayList<>();
    
    public WavefrontFace(String[] split) {
        int count = 0;
        for (String s : split) {
            count++;
            if (count == 1) continue;
            Points.add(Integer.parseInt(s)-1);
        }
    }
}
