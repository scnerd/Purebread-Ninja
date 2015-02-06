import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class DeathScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DeathScreen extends ScreenWorld
{

    /**
     * Constructor for objects of class DeathScreen.
     * 
     */
    public DeathScreen(World next)
    {
        super("images/pause.png", next);
        addObject(new LevelText("press any key to continue", 14, Color.BLACK, Color.WHITE, null), getWidth()/2, getHeight() - 20);
    }
    

}
