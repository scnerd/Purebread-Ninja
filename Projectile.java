import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.geom.Point2D;
import static purebreadninja.Sprite.*;

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectile extends AnimatedActor
{
    protected static final double BULLET_VELOCITY = 5;
    protected Point2D.Double velocity = new Point2D.Double();
    protected Point2D.Double position;
    
    private boolean isFlying = true;
    private int deathDisplayDuration = 10;
    private static GreenfootImage deathImage = new GreenfootImage("images/boom.png");
    public Projectile(double theta)
    {
        flipFrames = (theta >= Math.PI/2.0 && theta <= 3*Math.PI/2.0);
        velocity.x = BULLET_VELOCITY*Math.cos(theta);
        velocity.y = BULLET_VELOCITY*Math.sin(theta);
    }
    
    public void act()
    {
        if (isFlying)
        {
            super.act();
            updatePosition();
            checkCollisions();

        }
        else
        {
            if (--deathDisplayDuration == 0)
            {
                getWorld().removeObject(this);
            }
        }

    }
    
    @Override
    public void addedToWorld(World world)
    {
        super.addedToWorld(world);
        
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
    }
    
    protected void updatePosition()
    {
        position.x += velocity.x;
        position.y += velocity.y;
        setLocation((int)Math.round(position.x), Math.round((int)position.y));
    }
    
    protected void checkCollisions()
    {
        Actor intersects = null;
        if((intersects = getOneIntersectingObject(Player.class)) != null)
        {
            isFlying = false;
            ((Player)intersects).damage(this);
            setImage(deathImage);
            
        }
        else if((intersects = getOneIntersectingObject(Platform.class)) != null)
        {
            isFlying = false;
            setImage(deathImage);

        }
    }
}
