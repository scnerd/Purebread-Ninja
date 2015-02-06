import greenfoot.*;
import java.awt.Point;
import purebreadninja.Sprite;
import static purebreadninja.Sprite.*;

/**
 * Write a description of class Platform here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Platform extends AnimatedActor
{
    public static final Sprite sprite = ImageSheet("images/FloorTileSmall.png", 1);
    public Platform()
    {
        setCurrentAnimation(sprite);
    }
    
    public void act()
    { super.act(); }
    
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
