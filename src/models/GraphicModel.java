/**
 * Author: Carl Winkler
 * Purpose: Import Wavefront .OBJ files and transform in to OpenGL instructions using dependency injection.
 * Requirements: 
 * 1. .OBJ files should be in project /lib folder.
 * 2. .OBJ files should contain edge (line) data. This is done by default in Blender.
 * Limitations: This library currently only draws in 2D.
 * 
 * If [object].mtl exists, this will be scanned to locate the material diffuse colour (r,g,b) and apply it to the object.
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
    protected int DrawingMode = GL.GL_QUADS;//GL.GL_LINES;
    public String Shape;
    protected String Source;
    protected ArrayList<Vertex> Vertices = new ArrayList<>();
    protected ArrayList<WavefrontLine> Lines = new ArrayList<>();
    protected ArrayList<WavefrontFace> Faces = new ArrayList<>();
    protected GL gl;
    /**
     * It is assumed that 'obj' files are stored in the ./lib folder.
     * @param filename
     */
    public GraphicModel(String filename)
    {
        Source = filename;
        read();
    }
    
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
                parseLine(line.split(" "));
            }   
        } catch (Exception ex) { // gotta catch em all!
        }
    }
    
    private void parseLine(String[] split) {
        switch (split[0]) {
            case "o":
                Shape = split[1];
                break;
            case "v":
                Vertices.add(new Vertex(split));
                break;
            case "l":
                Lines.add(new WavefrontLine(split));
                break;
            case "f":
                Faces.add(new WavefrontFace(split));
                break;
        }
    }
    
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
    
   
    public void draw(GL gl) {
        this.gl = gl;
        drawLines();
        drawFaces();
    }
    
    private void drawLines() {
        if (Lines.isEmpty()) return;
        gl.glBegin(GL.GL_LINES);
        int count = 0;
        for (WavefrontLine l : Lines) {
            drawVertex(Vertices.get(l.start));
            drawVertex(Vertices.get(l.end));
        }
        gl.glEnd();
    }
    
    private void drawFaces() {
        if (Faces.isEmpty()) return;
        
        for (WavefrontFace f : Faces) {
            gl.glBegin(GL.GL_TRIANGLE_FAN);
            for (int x : f.Points) drawVertex(Vertices.get(x));
            gl.glEnd();
        }            
    }
    
    private void drawVertex(Vertex v) {
        gl.glVertex2d(v.x, v.y);
    }
}