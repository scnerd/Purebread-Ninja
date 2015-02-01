import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.reflect.InvocationTargetException;

public class ButterRonin extends Enemy
{
    private int default_wait_time = 120;
    private int wait_time = 0;
    public ButterRonin()
    {
        this.setImage("images/chips-1.png");
    }
    
    @Override
    public void act()
    {
        if (wait_time > 0) {
            wait_time--;
        }
        else {
            // shoot
            try
            {
                BulletSeed bullet = new BulletSeed(Math.PI);
                this.getWorld().addObject(bullet, this.getX(), this.getY());
            } catch(Exception ex)
            {
            }
            wait_time = default_wait_time;
        }
    }
}
