import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;

public class JamFisher extends Enemy
{
    private int alert_wait_time = 200;
    private int aggresive_wait_time = 200;
    private int wait_time = 0;
    private double last_angle = 0;
    
    private int DEFAULT_SHOOTING_TICKS = 20;
    private int shooting_ticks = 0;
    private int DEFAULT_DYING_TICKS = 35;
    private int dying_ticks = 0;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("JamFisherIdle.png", 5);
    
    @Animates(ATTACKING)
    public Sprite attacking = Sprite.ImageSheet("JamFisherAttack.png", 3);
    
    @Animates(DYING)
    public Sprite dying = Sprite.ImageSheet("JamFisherDeath.png", 11);
    
    private GreenfootSound shootSound = new GreenfootSound("sounds/pew.wav");
    
    public JamFisher()
    {
        // VISION_RANGE = 400, PROXIMITY_RANGE = 200
        super(400, 200);
    }
    
    public void act() 
    {
        if (isDying)
        {
            if (dying_ticks-- <= 0)
            {
                getWorld().removeObject(this);
                return;
            }
        }
        else
        {
            if (shooting_ticks <= 0)
            {
                currentAction = IDLE;
            }
            else
                shooting_ticks--;
            
            if (playerInProximity() || playerInRange())
            {
                last_angle = getAngleToPlayer();
                facePlayer();
                aggresive();
            }
            else if (sawPlayer)
            {
                engagedPlayer = false;
                alert();
            }
        }
        super.act();
    }    
    
    private void shootSeed()
    {
        try
        {
            currentAction = ATTACKING;
            shooting_ticks = DEFAULT_SHOOTING_TICKS;
            
            BulletSeed bullet = new BulletSeed(last_angle);
            this.getWorld().addObject(bullet, this.getX(), this.getY() - 10);
            shootSound.play();
        } catch(Exception ex) {}
    }
    
    private void alert()
    {
        if (wait_time == alert_wait_time)
        {
            shootSeed();
        }
        else if (wait_time == alert_wait_time * (1/2.0))
        {
            last_angle = last_angle == 0 ? Math.PI : 0;
            flipFrames = !flipFrames;
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
    
    @Override
    protected void die()
    {
        isDying = true;
        dying_ticks = DEFAULT_DYING_TICKS;
        currentAction = DYING;
    }
}
