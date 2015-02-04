import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;

public class ButterRonin extends Enemy
{
    private int patrol_wait_time = 400;
    private int alert_wait_time = 800;
    private int aggresive_wait_time = 300;
    private int wait_time = 0;
    private int initial_direction = -1;
    private double speed = 4.5;
    
    private int cur_state = 0, prev_state = 0;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("chips-1.png");
    //public Sprite idle = Sprite.ImageSheet("player/standing.png");
    
    @Animates(MOVING_FLOOR)
    public Sprite running = Sprite.ImageSheet("chips-1.png");
    //public Sprite running = Sprite.ImageSheet("player/running.png");
    
    @Override
    public void act()
    {   
        if (playerInRange())
        {
            cur_state = 1;
            aggresive();
        }
        else
        {
            cur_state = 0;
            alert();
        }
        if (cur_state != prev_state) { wait_time = 0; }
        prev_state = cur_state;
        
        if ((velocity.x < 0 && atLeftEdge()) || (velocity.x > 0 && atRightEdge()))
        {
            haultVelocity();
        }
        
        
        super.act();
    }
    
    private void alert()
    {
        if (wait_time == alert_wait_time)
        {
            setVelocity(initial_direction * speed, 0.0);
            currentAction = MOVING_FLOOR;
        }
        else if (wait_time == alert_wait_time * (7/8.0))
        {
            haultVelocity();
            currentAction = IDLE;
        }
        else if (wait_time == alert_wait_time * (1/2.0))
        {
            setVelocity(-initial_direction * speed, 0.0);
            currentAction = MOVING_FLOOR;
        }
        else if (wait_time == alert_wait_time * (3/8.0))
        {
            haultVelocity();
            currentAction = IDLE;
        }
        
        if (wait_time-- < 0) { wait_time = alert_wait_time; }
    }
    
    private void aggresive()
    {
        currentAction = MOVING_FLOOR;
        
        if (wait_time == aggresive_wait_time)
        {
            setVelocity(initial_direction * speed, 0.0);
        }
        else if (wait_time == aggresive_wait_time * (1/2.0))
        {
            setVelocity(-initial_direction * speed, 0.0);
        }
        
        if (wait_time-- < 0) { wait_time = aggresive_wait_time; }
    }
}
