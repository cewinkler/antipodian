/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.media.opengl.GL;
/**
 *
 * @author Carl Winkler <carl at carlossus.com>
 */
public final class GraphicModel implements IGraphicModel {
    protected int DrawingMode = GL.GL_LINES;
    public String Shape;
    protected String Source;
    protected ArrayList<Vertex> Vertices = new ArrayList<>();
    /**
     * It is assumed that 'obj' files are stored in the ./lib folder.
     * @param filename
     */
    public GraphicModel(String filename)
    {
        Source = filename;
        read();
    }
    @Override
    public void read() {
        BufferedReader reader;
        try {
            File file = new File(getFilename());
            if (!file.exists()) {
                System.out.println("Object not found");
                return;
            }
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                if (split[0].equals("o")) Shape = split[1];
                if (split[0].equals("v")) {
                    // We ignore split[2] as this is height off the plane
                    Vertices.add(new Vertex(Double.parseDouble(split[1]),Double.parseDouble(split[3])));
                }
            }
        } catch (Exception ex) { // gotta catch em all!
        }
    }
    
    @Override
    public String getFilename() {
        String lib = Paths.get("").toAbsolutePath().toString() + "\\lib";
        return String.format("%s\\%s.obj",lib,Source);
    }
    
    @Override
    public String toString() {
        String output = "Shape: " + this.Shape;
        for (Vertex v : Vertices) {
            output += "\nx: " + v.x + " y: " + v.y;
        }
        return output;
    }
    
    @Override
    public void draw(GL gl) {
        gl.glBegin(DrawingMode);
        int count = 0;
        for (Vertex v : Vertices) {
            count++;
            if (count == 1) {
                gl.glVertex2d(v.x, v.y);
                continue;
            }
            if (count <= Vertices.size()) gl.glVertex2d(v.x,v.y);
            gl.glVertex2d(v.x,v.y);
        }
        // join the first and last vertices
        Vertex v = Vertices.get(0);
        gl.glVertex2d(v.x,v.y);
        gl.glEnd();
    }
    
    public void draw(GL gl, int drawingMode) {
        DrawingMode = drawingMode;
        draw(gl);
    }
}