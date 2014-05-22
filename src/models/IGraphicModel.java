/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import javax.media.opengl.GL;

/**
 *
 * @author Carl Winkler <carl at carlossus.com>
 */
public interface IGraphicModel {
    public void read();
    public void draw(GL gl);
    public String getFilename();
}
