/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Carl Winkler <carl at carlossus.com>
 */
public class Vertex {
    public double x;
    public double y;
    public double z;
    
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vertex(String[] split) {
        this.x = Double.parseDouble(split[1]);
        this.y = Double.parseDouble(split[3]);
        this.z = Double.parseDouble(split[2]);
        
    }
    
//    public Vertex(String[] split) {
//        this.x = Double.parseDouble(split[1]);
//        this.y = Double.parseDouble(split[3]);
//        this.z = Double.parseDouble(split[2]);
//    }
}
