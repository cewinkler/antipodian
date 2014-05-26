package models;

import java.util.ArrayList;

public class WavefrontFace {
    public ArrayList<Integer> Points = new ArrayList<>();
    public String Material;
    
    public WavefrontFace(String[] split) {
        readPoints(split);
    }
    
    public WavefrontFace(String[] split, String material) {
        Material = material;
        readPoints(split);
    }
    
    private void readPoints(String[] split) {
        int count = 0;
        for (String s : split) {
            count++;
            if (count == 1) continue;
            Points.add(Integer.parseInt(s)-1);
        }
    }
}
