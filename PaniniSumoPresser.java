import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;

public class PaniniSumoPresser extends Enemy
{
    private int patrol_wait_time = 400;
    private int aggresive_wait_time = 300;
    private int wait_time = 0;
    private int initial_direction = -1;
    private double speed = 3.0;
    
    private int cur_state = 0, prev_state = 0;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("pumpkin.png", 1);
    
    public PaniniSumoPresser()
    {
        // VISION_RANGE = 350, PROXIMITY_RANGE = 200
        super(350, 200);
    }
    
    @Override
    public void act()
    {   
        patrol();
        /*
        if (playerInRange())
        {
            cur_state = 1;
            aggresive();
        }
        else
        {
            cur_state = 0;
            patrol();
        }
        if (cur_state != prev_state) { wait_time = 0; }
        prev_state = cur_state;
        */
       
        changeDirectionAtEdge();
        super.act();
    }
    
    private void patrol()
    {
        if (wait_time == patrol_wait_time)
        {
            setVelocity(initial_direction * speed, 0.0);
        }
        else if (wait_time == patrol_wait_time * (6/8.0))
        {
            haultVelocity();
        }
        else if (wait_time == patrol_wait_time * (1/2.0))
        {
            setVelocity(-initial_direction * speed, 0.0);
        }
        else if (wait_time == patrol_wait_time * (2/8.0))
        {
            haultVelocity();
        }
        
        if (wait_time-- < 0) { wait_time = patrol_wait_time; }
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
    
    // Only can be damaged from above
    @Override
    public boolean isVulnerableTo(Actor attacker)
    {
        int playerWidth = attacker.getImage().getWidth();
        int enemyHeight = getImage().getHeight();
        int enemyWidth = getImage().getWidth();
        int trueEnemyHeight = getY() - enemyHeight/2;

        return (attacker.getY() <= trueEnemyHeight) && (Math.abs(attacker.getX() - getX()) <= (enemyWidth/2 + playerWidth/2));
    }
}
