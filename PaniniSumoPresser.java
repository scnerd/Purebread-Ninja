import greenfoot.*;
import static purebreadninja.CharacterAction.*;

public class PaniniSumoPresser extends Enemy
{
    private int patrol_wait_time = 400;
    private int alert_wait_time = 800;
    private int aggresive_wait_time = 300;
    private int wait_time = 0;
    private int initial_direction = -1;
    private double speed = 1.0;
    
    private int cur_state = 0, prev_state = 0;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("butter_idle.png");
    
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
        }
        else if (wait_time == alert_wait_time * (7/8.0))
        {
            haultVelocity();
        }
        else if (wait_time == alert_wait_time * (1/2.0))
        {
            setVelocity(-initial_direction * speed, 0.0);
        }
        else if (wait_time == alert_wait_time * (3/8.0))
        {
            haultVelocity();
        }
        
        if (wait_time-- < 0) { wait_time = alert_wait_time; }
    }
    
    private void aggresive()
    {   
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
    
    @Override
    public boolean isVulnerableTo(Actor attacker)
    {
        int playerHeight = attacker.getImage().getHeight();
        int enemyHeight = getImage().getHeight();
        int lenience = -10;

        // TODO: write better algorithm
        return ((attacker.getY() + playerHeight/2.0 - lenience) >= (getY() + enemyHeight/2.0));
    }
}
