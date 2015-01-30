import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.reflect.InvocationTargetException;

/**
 * Write a description of class ButterRonin here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ButterRonin extends Enemy
{   
    private int default_wait_time = 120;
    private int wait_time = 0;
    
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if (wait_time > 0) {
            wait_time--;
        }
        else {
            // shoot
            try {
                this.getWorld().addObject(BulletSeed.class.getConstructor().newInstance(), getX(), getY());
            } catch(NoSuchMethodException ex) {
            } catch(InstantiationException ex) {
            } catch(IllegalAccessException ex) {
            } catch(IllegalArgumentException ex) {
            } catch(InvocationTargetException ex) {
            }
            wait_time = default_wait_time;
        }
    }    
}
