import greenfoot.*;

/**
 * Write a description of class HelpText here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HelpText extends VisibleActor
{
    public HelpText(String message)
    {
        getWorld().showText(message, getX(), getY());
    }
    
    public void act() 
    { }
    
    class HelpTextJump extends HelpText
    { public HelpTextJump() { super("Press UP (or W or SPACE) to jump"); } }
}
