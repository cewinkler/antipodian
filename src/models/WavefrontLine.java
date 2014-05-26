/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author Seikho
 */
public class WavefrontLine {
    public int start;
    public int end;
    
    public WavefrontLine(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    public WavefrontLine(String[] split) {
        start = Integer.parseInt(split[1])-1;
        end = Integer.parseInt(split[2])-1;
    }
}
