import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class LevelText here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LevelText extends VisibleActor
{
    public LevelText(String text, int fontSize, Color fontColor, Color backgroundColor, Color  fontOutline)
    {
        this.setImage(new GreenfootImage(text, fontSize, fontColor, backgroundColor, fontOutline));
    }
    
    public LevelText(String text, int fontSize, Color fontColor)
    {
        this(text, fontSize, fontColor, new Color(0, 0, 0, 0), null);
    }
    
    public LevelText(String text, int fontSize)
    {
        this(text, fontSize, Color.BLACK, new Color(0, 0, 0, 0), null);
    }
    
    public LevelText(String text)
    {
        this(text, 48, Color.BLACK, new Color(0, 0, 0, 0), null);
    }
    /**
     * Act - do whatever the LevelText wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
