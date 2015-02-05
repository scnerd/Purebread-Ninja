import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Toaster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Toaster extends Enemy
{
    
    public Toaster()
    {
        // VISION_RANGE = 250, PROXIMITY_RANGE = 200
        super(250, 200);
    }
    
    public void act() 
    {
        int move = Greenfoot.getRandomNumber(4);
        
        // Add your action code here.
    }    
}
