import greenfoot.*;

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
    }
    
    @Override
    public void act()
    {
        String key = Greenfoot.getKey();
        if("escape".equals(key))
            Greenfoot.setWorld(new Menu());
        else if (key != null)
        {
            Greenfoot.setWorld(next);
        }
    }
}
