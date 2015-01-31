import greenfoot.*;
import java.awt.geom.Point2D;
/**
 * Write a description of class CameraViewable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class CameraViewableWorld  extends World
{
    private int camX;
    private int camY;
    
    public int getCameraX()
    {
        return this.camX;
    }
    
    public int getCameraY()
    {
        return this.camY;
    }
    
    public final void setCameraLocation(int x, int y)
    {
        if (this.getCameraX() != x || this.getCameraY() != y)
        {
            this.camX = x;
            this.camY = y;
            for (Object o : this.getObjects(null))
            {
                Actor a = (Actor) o;
                a.setLocation(a.getX(), a.getY());
            }
        }
        
    }

    public CameraViewableWorld(int width, int height)
    {
        super(width, height, 1, false);
        
    }
}
