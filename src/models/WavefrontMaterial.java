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
public class WavefrontMaterial {
    public String Name;
    public Double Ns;
    public Double[] Ka = new Double[3];
    public Double[] Kd = new Double[3];
    public Double[] Ks = new Double[3];
    
    public WavefrontMaterial(String name) {
        Name = name;
    }
    
}
