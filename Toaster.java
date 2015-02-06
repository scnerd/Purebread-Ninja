import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;
import java.util.List;

/**
 * Write a description of class Toaster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Toaster extends Enemy
{
    private double speed = 2.0;
    private double charge_speed = 4.0;
    private int health = 3;
    
    private int wait_time = 0;
    private int turn_time = 400;
    private int jump_time = 200;
    private int walk_time = 250;
    private int charge_time = 400;
    private int damage_time = 150;
    private int DEFAULT_DYING_TICKS = 40;
    private int dying_ticks = 0;
    
    private int move = 0;
    private int next_direction = 1;
    
    private boolean isBeingDamaged = false;
    private boolean willJumpDefend = true;
    private boolean isJumpDefending = false;
    
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = Sprite.ImageSheet("ToasterDaimyo.png", 8);
    
    @Animates(MOVING_FLOOR)
    public Sprite moving_floor = Sprite.ImageSheet("ToasterDaimyoWalkAttack.png", 8);
    
    @Animates(MOVING_AIR)
    public Sprite moving_air = Sprite.ImageSheet("ToasterDaimyoJump.png", 8);
    
    @Animates(HURT)
    public Sprite hurt = Sprite.ImageSheet("ToasterDaimyo.png", 8);
    
    @Animates(DYING)
    public Sprite dying = Sprite.ImageSheet("ToasterDaimyo.png", 8);
    
    public Toaster()
    {
        // VISION_RANGE = 250, PROXIMITY_RANGE = 100
        super(400, 100);
        down_edge_distance = 50;
        side_edge_distance = 50;
    }
    
    @Override
    public void act() 
    {
        if (!sawPlayer)
        {
            playerInRange();
        }
        else if (isDying && dying_ticks > 0)
        {
            if (--dying_ticks <= 0)
            {
                getWorld().removeObject(this);
                return;
            }
        }
        else
        {
            jumpDefend();
            
            if (--wait_time < 0)
            {
                facePlayer();
                willJumpDefend = true;
                move = Greenfoot.getRandomNumber(3);
            }
            
            if (isBeingDamaged)
            {
                if (wait_time == 0)
                    isBeingDamaged = isDying = false;
            }
            else if (move == 0)
            {
                if (wait_time < 0)
                    wait_time = jump_time;
                jump();
            }
            else if (move == 1)
            {
                if (wait_time < 0)
                    wait_time = walk_time;
                walk();
            }
            else if (move == 2)
            {
                if (wait_time < 0)
                    wait_time = charge_time;
                charge();
            }
            setAction();
        }
        
        super.act();
    }    
    
    private void jump()
    {
        if (wait_time == jump_time)
            setVelocity(0.0, -10.0);
    }
    
    private void walk()
    {
        if (wait_time == walk_time)
        {
            facePlayer();
            next_direction = (flipFrames ? -1 : 1);
            setVelocity(next_direction * speed, 0.0);
        }
        else if (wait_time == 0)
        {
            haultVelocity();
            facePlayer();
            next_direction = (flipFrames ? -1 : 1);
        }
    }
    
    private void charge()
    {
        changeDirectionAtEdge();
        
        if (wait_time == charge_time)
        {
            willJumpDefend = false;
            
            facePlayer();
            next_direction = (flipFrames ? -1 : 1);
            //setVelocity(0.0, -3.0);
        }
        else if (wait_time == charge_time * (7/8.0))
        {
            setVelocity(0.0, -3.0);
        }
        else if (wait_time == charge_time * (6/8.0))
        {
            setVelocity(0.0, -3.0);
        }
        else if (wait_time == charge_time * (5/8.0))
        {
            setVelocity(0.0, -3.0);
        }
        else if (wait_time == charge_time * (4/8.0))
        {
            setVelocity(next_direction * charge_speed, 0.0);
        }
        else if (wait_time == 0)
        {
            haultVelocity();
            facePlayer();
            next_direction = (flipFrames ? -1 : 1);
            
            willJumpDefend = true;
        }
    }
    
    private void jumpDefend()
    {
        if (willJumpDefend && velocity.y == 0)
        {
            List<Actor> players = getObjectsInRange(PROXIMITY_RANGE, Player.class);
            if (!players.isEmpty())
            {
                Player player = (Player)players.get(0);
                
                if (isFacingPlayer(player))
                {
                    int trueEnemyHeight = getY() - getImage().getHeight() / 2;
                    int truePlayerHeight = player.getY() - player.getImage().getHeight() / 2;
                    if (truePlayerHeight < trueEnemyHeight)
                    {
                        setVelocity(0.0, -6.0);
                    }
                }
            }
        }
    }
    
    @Override
    public void damage(Actor harmer)
    {
        if (--health <= 0)
            super.damage(harmer);
        else
        {
            wait_time = damage_time;
            isBeingDamaged = isDying = true;
            willJumpDefend = false;
            changeSpriteHealth();
            next_direction = flipFrames ? -1 : 1;
            setVelocity(next_direction * 10.0, -15.0);
            releaseMinion();
        }
    }
    
    @Override
    protected void die()
    {
        isDying = true;
        dying_ticks = DEFAULT_DYING_TICKS;
        currentAction = DYING;
    }
    
    private void setAction()
    {
        if (isBeingDamaged)
            currentAction = MOVING_AIR;
        else if (velocity.y != 0)
            currentAction = MOVING_AIR;
        else if (velocity.x == 0)
            currentAction = IDLE;
        else
            currentAction = MOVING_FLOOR;
    }
    
    private void changeSpriteHealth()
    {
        if (health == 2)
        {
            
        }
        else if (health == 1)
        {
            
        }
    }
    
    @Override
    protected boolean playerInRange()
    {
        List<Actor> players = getObjectsInRange(VISION_RANGE, Player.class);
        return engagedPlayer = sawPlayer = true;
    }
    
    private void releaseMinion()
    {
        try
        {
            PaniniSumoPresser new_panini = new PaniniSumoPresser();
            new_panini.setVelocity(next_direction * 5.0, -10.0);
            this.getWorld().addObject(new_panini, this.getX(), this.getY() - 400);
        } catch(Exception ex) {}
    }
}
