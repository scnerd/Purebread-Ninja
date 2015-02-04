import greenfoot.*;

/**
 * Write a description of class ScreenWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScreenWorld extends World
{
    private World next;
    public ScreenWorld(String image, World next)
    {    
        super(800, 600, 1);
        this.next = next;
        Greenfoot.getKey();
        
    }
    
    public ScreenWorld(String image)
    {
        super(800, 600, 1);
        this.next = new Menu();
        Greenfoot.getKey();
    }
    
    @Override 
    public void act()
    {
        if (Greenfoot.getKey() != null)
        {
            Greenfoot.setWorld(next);
        }
        
    }
}
