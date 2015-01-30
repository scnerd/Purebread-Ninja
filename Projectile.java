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
    protected static final double BULLET_VELOCITY = 5;
    
    protected double angle;
    protected Point2D.Double position;
    
    public Projectile(Point2D.Double position, double angle)
    {
        this.position = position;
        this.angle = angle;
    }
    
    public void act()
    {
        updatePosition();
        checkCollisions();
    }
    
    protected void updatePosition()
    {
        position.x += BULLET_VELOCITY * Math.cos(angle);
        position.y += BULLET_VELOCITY * Math.sin(angle);
        setLocation((int)position.x, (int)position.y);
    }
    
    protected void checkCollisions()
    {
        Actor intersects = null;
        if((intersects = getOneIntersectingObject(Player.class)) != null)
        {
            ((Player)intersects).damage(this);
            getWorld().removeObject(this);
        }
        else if((intersects = getOneIntersectingObject(Platform.class)) != null)
        {
            getWorld().removeObject(this);
        }
    }
}
