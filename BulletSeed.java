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
    private static GreenfootImage[] frames = Resource.loadSpriteFrames("images/seed-bullet.png", 32, 32, 1);
    public BulletSeed()
    {
        super();
        this.addProcess(new SpriteAnimation(frames));
        this.addProcess(new TimerProcess(1000, new RemoveActorsProcess(this)));
    } 
}
