import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.geom.Point2D;
//import static purebreadninja.Sprite.*;

/**
 * Write a description of class BulletSeed here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulletSeed extends Projectile
{
    int waitTime = 5;
    public BulletSeed(double angle)
    {
        super(angle);
    }
    
        
    @Override
    public void addedToWorld(World world)
    {
        super.addedToWorld(world);
        // THIS LINE SETS THE LOCATION RELATIVE TO THE CURRENT CAMERA DONT CHANGE
        this.setLocation(getX(), getY());
        setCurrentAnimation(null);
        setCurrentAnimation(Sprite.ImageSheet("seed-bullet.png"));
        
    }
    
}
