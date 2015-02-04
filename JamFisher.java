import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;

public class JamFisher extends Enemy
{
    private int alert_wait_time = 100;
    private int aggresive_wait_time = 200;
    private int wait_time = 0;
    private double last_angle = 0;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("grapes.png", 1);
    
    public void act() 
    {
        
        if (playerInRange())
        {
            last_angle = getAngleToPlayer();
            facePlayer();
            aggresive();
        }
        else if (sawPlayer)
        {
            alert();
        }
        
        super.act();
    }    
    
    private void shootSeed()
    {
        try
        {
            BulletSeed bullet = new BulletSeed(last_angle);
            this.getWorld().addObject(bullet, this.getX(), this.getY() - 10);
        } catch(Exception ex) {}
    }
    
    private void alert()
    {
        if (wait_time == alert_wait_time)
        {
            shootSeed();
        }
        
        if (wait_time-- < 0) { wait_time = alert_wait_time; }
    }
    
    private void aggresive()
    {   
        if (wait_time == aggresive_wait_time)
        {
            shootSeed();
        }
        else if (wait_time == aggresive_wait_time * (6/8.0))
        {
            shootSeed();
        }
        
        if (wait_time-- < 0) { wait_time = aggresive_wait_time; }
    }
}
