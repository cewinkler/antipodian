/*
 * Main.java
 *
 * Created on 16 August 2007, 20:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion;

import antipodion.gui.*;
import antipodion.game.*;

/**
 *
 * @author phingsto
 */
public class Main
{
    private Game game;
    private GUI gui;
    
    /** Creates a new instance of Main */
    public Main()
    {
        gui = new GUI();
        game = new Game(gui);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Main main = new Main();
        
        main.start();
    }
    
    public void start()
    {
        game.play();
        
        System.exit(0);
    }
}
