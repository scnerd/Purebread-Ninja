import greenfoot.*;
import java.awt.Point;

/**
 * Write a description of class Platform here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Platform extends Actor
{
    public Platform() {
        this.setImage("images/brick.jpg");
    }
    /**
     * Act - do whatever the Platform wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }
    
    public int left()
    { return getX() - getImage().getWidth() / 2; }
    public int right()
    { return getX() + getImage().getWidth() / 2; }
    public int top()
    { return getY() - getImage().getHeight() / 2; }
    public int bottom()
    { return getY() + getImage().getHeight() / 2; }
    
    public Point cornerUL()
    { return new Point(left(), top()); }
    public Point cornerUR()
    { return new Point(right(), top()); }
    public Point cornerBL()
    { return new Point(left(), bottom()); }
    public Point cornerBR()
    { return new Point(right(), bottom()); }
}
