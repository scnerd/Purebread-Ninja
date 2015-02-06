import greenfoot.*;

/**
 * Write a description of class ScreenWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScreenWorld extends World
{
    protected World next;
    public ScreenWorld(String image, World next)
    {    
        super(800, 600, 1);
        this.next = next;
        Greenfoot.getKey();
        setBackground(image);
        
    }
    
    public ScreenWorld(String image)
    {
        super(800, 600, 1);
        this.next = new Menu();
        Greenfoot.getKey();
    }
    
    public void clearGetKey()
    {
        while(Greenfoot.getKey() != null)
            ;
    }
    
    @Override
    public void started()
    {
        clearGetKey();
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
