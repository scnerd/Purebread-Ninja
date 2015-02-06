import greenfoot.*;

/**
 * Write a description of class ScreenWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScreenWorld extends CameraViewableWorld
{
    protected World next;
    boolean keysCleared = false;
    public ScreenWorld(String image, World next)
    {    
        super(800, 600);
        this.next = next;
        Greenfoot.getKey();
        setBackground(image);
        
    }
    
    public ScreenWorld(String image)
    {
        super(800, 600);
        this.next = new Menu();
        Greenfoot.getKey();
    }
    
    public void clearGetKey()
    {
        while(Greenfoot.getKey() != null)
            ;
    }
    
    @Override 
    public void act()
    {
        if (!keysCleared)
        {
            clearGetKey();
            keysCleared = true;
        }
        if (Greenfoot.getKey() != null)
        {
            Greenfoot.setWorld(next);
        }
        
    }
}
