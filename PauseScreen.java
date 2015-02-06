import greenfoot.*;
import java.awt.Color;
/**
 * Write a description of class PauseScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PauseScreen extends ScreenWorld
{
    public PauseScreen(String image, World w)
    {
        super(image, w);
        int dist = 50;
        int x = this.getWidth()/2;
        int y = this.getHeight()/2;
        addObject(new SwitchWorldButton("Resume", w), x, y-dist/2);
        addObject(new SwitchWorldButton("Quit", new Menu()), x, y+dist/2);
        
        //y = 100;
        //addObject(new LevelText("Paused", 48, Color.WHITE), x, y);
    }
    
    @Override
    public void act()
    {
        String key = Greenfoot.getKey();
        if("escape".equals(key))
        {
            Greenfoot.setWorld(next);
        }
    }
}
