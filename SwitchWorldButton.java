import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class SwitchWorldButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SwitchWorldButton extends TextButton
{
    World world;
    public SwitchWorldButton(String text, World world)
    {
        this.text = text; 
        this.world = world;
        this.fontColor = Color.WHITE;
        createImage();
    }
    
    @Override
    public void onClick()
    {
        Greenfoot.setWorld(this.world);
    }   
}
