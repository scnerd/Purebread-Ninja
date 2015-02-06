import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;

/**
 * Write a description of class HazelShogun here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HazelShogun extends Enemy
{
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("BreadNinjaHealth1.png", 1);
    //public Sprite idle = Sprite.ImageSheet("butter_idle.png", 8);
    
    public HazelShogun()
    {
        super(0,0);
    }
    
    public void act() 
    {
        // Add your action code here.
    }    
}
