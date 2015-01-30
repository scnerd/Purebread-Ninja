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
    public final Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    
    private Point2D.Double position = null;
    private Point2D.Double velocity = new Point2D.Double(0,0);
    
    protected ProjectilePositionProcess moveProcess = new ProjectilePositionProcess(-2.0, 0.0);
    public int COLLISION_MARGIN = 2;
    
    public Projectile() {
        this.addProcess(this.moveProcess);
    }
    
    protected void updatePosition()
    {
        if(position == null)
        {
            this.position = new Point2D.Double(this.getX(), this.getY());
        }
        velocity.x = (velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(velocity.x), MAX_VELOCITY.x);
        velocity.y = (velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(velocity.y), MAX_VELOCITY.y);
        position.x += velocity.x;
        position.y += velocity.y;

        setLocation((int)position.x, (int)position.y);
    }
    
    public void setVelocity(double x, double y) {
        this.velocity.x = x;
        this.velocity.y = y;
    }
    
    public void setVelocity(Point2D.Double velocity) {
        this.velocity = velocity;
    }
}
