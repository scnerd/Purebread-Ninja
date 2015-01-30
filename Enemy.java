import greenfoot.*;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Character
{
    public void act()
    {
        
    }
    
    public void damage(Actor harmer)
    {
        getWorld().removeObject(this);
    }
}
