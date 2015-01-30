import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.geom.Point2D;

/**
 * Write a description of class BulletSeed here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulletSeed extends Projectile
{
    public BulletSeed() {
         this.setImage("images/rock2.png");
    }
    /**
     * Act - do whatever the BulletSeed wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
        
        velocity.x = -2.0;
        
        super.enactMovement();
    }    
}
