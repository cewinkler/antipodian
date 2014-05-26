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
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import javax.media.opengl.GL;
/**
 *
 * @author Carl Winkler <carl at carlossus.com>
 */
public final class GraphicModel {
    protected int DrawingMode = GL.GL_QUADS;//GL.GL_LINES;
    public String Shape;
    protected String Source;
    protected ArrayList<Vertex> Vertices = new ArrayList<>();
    protected ArrayList<WavefrontLine> Lines = new ArrayList<>();
    protected ArrayList<WavefrontFace> Faces = new ArrayList<>();
    protected ArrayList<WavefrontMaterial> Materials = new ArrayList<>();
    protected GL gl;
    protected String CurrentMaterial;
    protected String MaterialFile;
    protected String[] split;
    /**
     * It is assumed that 'obj' files are stored in the ./lib folder.
     * @param filename
     * @throws java.io.IOException
     */
    public GraphicModel(String filename) throws IOException, Exception
    {
        Source = filename;
        readFile(getObjectFilename(), objectParser);
        readFile(getMaterialFilename(), materialParser);
    }
  
    private void readFile(String filename, Callable<Void> parser) throws IOException, Exception {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Object not found");
        }
        BufferedReader reader =  new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            split = line.split(" ");
            parser.call();
        }
    }
    
    Callable<Void> materialParser = new Callable<Void>() {
        @Override
        public Void call() {
            parseMaterialLine();
            return null;
        }
    };
    
    Callable<Void> objectParser = new Callable<Void>() {
        @Override
        public Void call() {
            parseLine();
            return null;
        }
    };
    
    public void parseMaterialLine() {
        WavefrontMaterial material = new WavefrontMaterial("");
        if (!Materials.isEmpty()) material = Materials.get(Materials.size()-1);
        switch (split[0]) {
            case "newmtl":
                Materials.add(new WavefrontMaterial(split[1]));
                break;
            case "Ns":
                material.Ns = Double.parseDouble(split[1]);
                break;
            case "Ka":
                material.Ka = new Double[] {
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3])};
                break;
            case "Kd":
                material.Kd = new Double[] {
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3])};
                break;
        }
    }
    
    private void parseLine() {
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
                Faces.add(new WavefrontFace(split,CurrentMaterial));
                break;
            case "usemtl":
                CurrentMaterial = split[1];
                break;
            case "mtllib":
                MaterialFile = split[1];
                break;
        }
    }
   
    public String getObjectFilename() {
        String lib = Paths.get("").toAbsolutePath().toString() + "\\lib";
        return String.format("%s\\%s.obj",lib,Source);
    }
    
    public String getMaterialFilename() {
        String lib = Paths.get("").toAbsolutePath().toString() + "\\lib";
        return String.format("%s\\%s.mtl",lib,MaterialFile);
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