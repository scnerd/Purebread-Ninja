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
        if(Greenfoot.isKeyDown("escape"))
            Greenfoot.setWorld(this.next);
    }
}
