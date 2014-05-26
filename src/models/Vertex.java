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
    
    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vertex(String[] split) {
        x = Double.parseDouble(split[1]);
        y = Double.parseDouble(split[3]);
        z = Double.parseDouble(split[2]);
    }
}
