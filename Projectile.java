import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.geom.Point2D;

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectile extends AnimatedActor
{
    public Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    
    public Point2D.Double position = null;
    public Point2D.Double velocity = new Point2D.Double(0, 0);

    public int COLLISION_MARGIN = 2;
    
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
    }
    
    protected void enactMovement()
    {
        // Apply Velocity
        velocity.x = (velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(velocity.x), MAX_VELOCITY.x);
        velocity.y = (velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(velocity.y), MAX_VELOCITY.y);
        position.x += velocity.x;
        position.y += velocity.y;

        setLocation((int)position.x, (int)position.y);
    }
}
