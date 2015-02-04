import greenfoot.*;
import greenfoot.util.*;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.Scanner;

/**
 * Write a description of class MainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Menu extends CameraViewableWorld
{

    private static int MID_LINE_SEPARATION = 60;
    
    private int startMenuX = getWidth()/2;
    private int startMenuY = getHeight()/2;
    
    private int levelMenuX = 0;
    private int levelMenuY = -170;

    public Menu()
    {
        super(800, 600);
        int y = Greenfoot.getRandomNumber(2) == 0 ? -1 : 2;
        levelMenuX =  Greenfoot.getRandomNumber(2) == 0 ? getWidth()*3/2 : - getWidth()/2;
        
        levelMenuY = getHeight()*y;
        prepareStartMenu();
        prepareLevelSelectMenu(levelMenuX, levelMenuY);
        setCameraLocation(this.getWidth()/2, this.getHeight()/2);

    }
    
    private void prepareStartMenu()
    {
        int x = this.getWidth()/2;
        int y = this.getHeight()/3;
        addObject(new LevelText("Purebread Ninja", 100), x, y/2);
        addObject(new MenuNavButton("Start", new  Point2D.Double(levelMenuX, levelMenuY)), x, y*2);
        
    }
    
    private void prepareLevelSelectMenu(int x, int y)
    {
        int locY = y;
        y -= this.getHeight()/2;
        y += 75;
        addObject(new LevelText("Load Level", 100), x, y);
        try
        {
            URL url = GreenfootUtil.getURL("index.txt","levels");
            Scanner in = new Scanner(url.openStream());
            y += 75 + MID_LINE_SEPARATION/2;
            while(in.hasNextLine())
            {
                String dir = in.nextLine();
                addObject(new LoadLevelButton(dir), x, y);
                y += MID_LINE_SEPARATION;
            }
            y = locY + getHeight()/2 - MID_LINE_SEPARATION;
            addObject(new MenuNavButton("Back", new  Point2D.Double(startMenuX, startMenuY)), x, y);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
