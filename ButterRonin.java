import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;

public class ButterRonin extends Enemy
{
    private int vision = 350;
    private int proximity = 200;

    private int alert_wait_time = 400;
    private int aggresive_wait_time = 100;
    private int wait_time = 0;
    private int next_direction = 1;
    private double speed = 4.0;
    
    private int cur_state = 0, prev_state = 0;
    private int DEFAULT_ENGAGED_DURATION = 500;
    private int engagedDuration = 0;
    private boolean inRange;
    private int DEFAULT_DYING_TICKS = 40;
    private int dying_ticks = 0;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("chips-1.png", 1);
    //public Sprite idle = Sprite.ImageSheet("butter_idle.png", 8);

    @Animates(MOVING_FLOOR)
    public Sprite running = Sprite.ImageSheet("chips-1.png", 1);
    //public Sprite running = Sprite.ImageSheet("butter_attack.png", 6);
    
    @Animates(DYING)
    public Sprite dying = Sprite.ImageSheet("chips-1.png", 1);
    
    public ButterRonin()
    {
        // VISION_RANGE = 250, PROXIMITY_RANGE = 200
        super(250, 200);
    }
    public ButterRonin(boolean tutorial)
    {
        // VISION_RANGE = 0, PROXIMITY_RANGE = 0
        super(0, 0);
    }
    
    @Override
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
            inRange = playerInRange();
            if (engagedPlayer)
            {
                if (inRange)
                    engagedDuration = DEFAULT_ENGAGED_DURATION;
                if (engagedDuration <= 0)
                    next_direction = (isFacingPlayer((Player)getWorld().getObjects(Player.class).get(0)) ? 1 : -1);
                cur_state = 1;
                aggresive();
            }
            else
            {
                cur_state = 0;
                alert();
            }
            if (engagedDuration > 0)
                engagedDuration--;
            else
                engagedPlayer = false;
    
            if (cur_state != prev_state) { wait_time = 0; }
            prev_state = cur_state;
            
            changeDirectionAtEdge();
        }
        super.act();
    }
    
    protected void alert()
    {
        if (wait_time == alert_wait_time)
        {
            setVelocity(next_direction * speed, 0.0);
            currentAction = MOVING_FLOOR;
        }
        else if (wait_time == alert_wait_time * (7/8.0))
        {
            haultVelocity();
            currentAction = IDLE;
        }
        else if (wait_time == alert_wait_time * (1/2.0))
        {
            setVelocity(-next_direction * speed, 0.0);
            currentAction = MOVING_FLOOR;
        }
        else if (wait_time == alert_wait_time * (3/8.0))
        {
            haultVelocity();
            currentAction = IDLE;
        }
        
        if (wait_time-- < 0) { wait_time = alert_wait_time; }
    }
    
    protected void aggresive()
    {
        currentAction = MOVING_FLOOR;
        
        if (wait_time == aggresive_wait_time)
        {
            setVelocity(next_direction * speed, 0.0);
        }
        else if (wait_time == aggresive_wait_time * (1/2.0))
        {
            setVelocity(-next_direction * speed, 0.0);
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
