/*
 * Game.java
 *
 * Created on 16 August 2007, 20:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package antipodion.game;

import java.util.*;

import javax.swing.JOptionPane;

import antipodion.*;
import antipodion.gui.*;
import antipodion.sounds.*;

/**
 *
 * @author phingsto
 */
public class Game implements Constants
{
    private GUI gui;
    
    private ArrayList<Level> levels;
    
    private int goannasLeft = GOANNAS;
    private Level currentLevel;
    private int currentLevelIndex;
    private int score = 0;
    
    /** Creates a new instance of Game */
    public Game(GUI gui)
    {
        this.gui = gui;
        levels = Level.makeLevels();
    }
    
    public void play()
    {
        restart();

        boolean over = false;
        int levelExitCode = Level.LEVEL_EXIT_ERROR;
        
        while(!over)
        {
            gui.setLevel(currentLevel);

            Thread thread = currentLevel.startLevel(goannasLeft, score);
            
            while(thread.isAlive())
            {
                try
                {
                    Thread.sleep(250);
                }
                catch(InterruptedException e){}
            }
        
            levelExitCode = currentLevel.getExitCode();
            
            switch(levelExitCode)
            {
                case Level.LEVEL_EXIT_SUCCESS:
                    Sounds.play(Sounds.WIN);
                    if(currentLevelIndex == levels.size()-1)
                    {
                        int option = JOptionPane.showConfirmDialog(gui.getContentPane(),
                                    "Well done - you win - try again?",
                                    GAME_NAME,
                                    JOptionPane.YES_NO_OPTION);                
                        if(option == JOptionPane.YES_OPTION)
                        {
                            restart();
                        }
                        else
                        {
                            over = true;
                        }
                    }
                    else
                    {
                        goannasLeft = currentLevel.getGoannasLeft();
                        score = currentLevel.getScore();
                        currentLevelIndex++;
                        currentLevel = levels.get(currentLevelIndex);
                    }
                    break;
                case Level.LEVEL_EXIT_DIED:
                    Sounds.play(Sounds.LOSE);
                    int option = JOptionPane.showConfirmDialog(gui.getContentPane(),
                                "Bad luck - you lost this time - try again?",
                                GAME_NAME,
                                JOptionPane.YES_NO_OPTION);                
                    if(option == JOptionPane.YES_OPTION)
                    {
                        restart();
                    }
                    else
                    {
                        over = true;
                    }
                    break;
                case Level.LEVEL_EXIT_ERROR:
                    JOptionPane.showMessageDialog(gui.getContentPane(),
                        "Sorry - an unexpected error has come up - have to stop!",
                        GAME_NAME,
                        JOptionPane.INFORMATION_MESSAGE);
                    over = true;
                    break;
                case Level.LEVEL_EXIT_RESTART: 
                    restart();
                    break;
                case Level.LEVEL_EXIT_QUIT:
                    over = true;
                    break;
                default: break;
            }
        }
    }
    
    private void restart()
    {
        goannasLeft = GOANNAS;
        levels = Level.makeLevels();
        currentLevel = levels.get(0);
        currentLevelIndex = 0;
        score = 0;
    }
}
