import greenfoot.*;
import greenfoot.util.GreenfootUtil;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.Properties;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Write a description of class Menu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LevelMenu extends CameraViewableWorld
{
    private static int MID_LINE_SEPARATION = 60;

    public LevelMenu()
    {    
        super(800, 600);

        prepare();
    }
    
    private void prepareTitle()
    {
        int x = this.getWidth()/2;
        addObject(new LevelText("Load Level", 100), x, 75);
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
        prepareTitle();
        try
        {
            URL url = GreenfootUtil.getURL("index.txt","levels");
            Scanner in = new Scanner(url.openStream());
            int x = this.getWidth()/2;
            int y = 150 + MID_LINE_SEPARATION/2;
            while(in.hasNextLine())
            {
                String dir = in.nextLine();
                //if (!"".equals(dir))
                addObject(new LoadLevelButton(dir), x, y);
                y += MID_LINE_SEPARATION;
            }
            y = getHeight() - MID_LINE_SEPARATION;
            addObject(new MenuNavButton("Back", MainMenu.class), x, y);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
