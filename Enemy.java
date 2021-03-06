import greenfoot.*;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Character
{
    
    public static final Point2D.Double MAX_VELOCITY = new Point2D.Double(6, 10);
    private double ACC_GRAVITY = 0.25;
    protected int VISION_RANGE;
    protected int PROXIMITY_RANGE;
    
    protected boolean sawPlayer = false;
    protected boolean engagedPlayer = false;
    public boolean isDying = false;
    protected int down_edge_distance = 30;
    protected int side_edge_distance = 30;
    
    protected GreenfootSound deathSound = new GreenfootSound("sounds/death.wav");
    
    public Enemy(int vision_range, int proximity_range)
    {
        VISION_RANGE = vision_range;
        PROXIMITY_RANGE = proximity_range;
    }
    
    @Override
    public void act()
    {
        enactMovement();
        super.act();
    }
    
    @Override
    public void addedToWorld(World world)
    {
        super.addedToWorld(world);
        
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
    }
    
    public void damage(Actor harmer)
    {
        deathSound.play();
        die();
    }
    
    protected void die()
    {
        getWorld().removeObject(this);
    }
    
    protected boolean playerInRange()
    {
        List<Actor> players = getObjectsInRange(VISION_RANGE, Player.class);
        if (!players.isEmpty())
        {
            Player player = (Player)players.get(0);
            
            if (isFacingPlayer(player))
            {
                int x_dist = Math.abs(getX() - player.getX());
                int y_dist = Math.abs(getY() - player.getY());
                
                // Vision is a cone from 45 to -45 in facing direction
                if (x_dist >= y_dist)
                {
                    return engagedPlayer = sawPlayer = true;
                }
            }
        }
        
        return false;
    }
    
    protected boolean playerInProximity()
    {
        List<Actor> players = getObjectsInRange(PROXIMITY_RANGE, Player.class);
        if (engagedPlayer && !players.isEmpty())
        {
            Player player = (Player)players.get(0);
            
            if (!isFacingPlayer(player))
            {
                int x_dist = Math.abs(getX() - player.getX());
                int y_dist = Math.abs(getY() - player.getY());
                
                // out of proximity is a cone from 45 to -45 in non-facing direction
                if (x_dist >= y_dist)
                {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
    
    // Only gets left or right (pi or 0), but we *might want real angle
    public double getAngleToPlayer()
    {
        Player player = (Player)getWorld().getObjects(Player.class).get(0);
        
        if (player.getX() < getX())
        {
            return Math.PI;
        }
        else
        {
            return 0;
        }
    }
    
    public boolean isFacingPlayer(Actor player)
    {
        return flipFrames != (player.getX() >= getX());
    }
    
    public boolean isVulnerableTo(Actor attacker)
    {
        int enemyHeight = getImage().getHeight();
        
        return !isFacingPlayer(attacker) && (Math.abs(getY() - attacker.getY()) <= enemyHeight/2.0);
    }
    
    public void facePlayer()
    {
        double angle = getAngleToPlayer();

        flipFrames = (angle >= Math.PI/2.0 && angle <= 3*Math.PI/2.0);
    }
    
    protected boolean atLeftEdge()
    {
        if (getOneObjectAtOffset(-side_edge_distance, 0, Platform.class) == null)
            return (getOneObjectAtOffset(-side_edge_distance, down_edge_distance, Platform.class) == null);
        return true;
    }
    
    protected boolean atRightEdge()
    {
        if (getOneObjectAtOffset(side_edge_distance, 0, Platform.class) == null)
               return (getOneObjectAtOffset(side_edge_distance, down_edge_distance, Platform.class) == null);
        return true;
    }
    
    protected void changeDirectionAtEdge()
    {
        if ((velocity.x < 0 && atLeftEdge()) || (velocity.x > 0 && atRightEdge()))
        {
            velocity.x *= -1;
        }
    }
    
    protected void setVelocity(double x, double y)
    {
        velocity.x = x;
        velocity.y = y;
    }
    
    protected void haultVelocity()
    {
        velocity.x = velocity.y = 0.0;
    }
    
    protected void enactMovement()
    {
        // Calculate velocity
        // velocity.x += 0.0;
        velocity.y += ACC_GRAVITY;
        
        // Tweak to avoid superimposition
        velocity.x = stepX(velocity.x);
        velocity.y = stepY(velocity.y);
        
        // Apply Velocity
        velocity.x = (velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(velocity.x), MAX_VELOCITY.x);
        velocity.y = (velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(velocity.y), MAX_VELOCITY.y);
        position.x += velocity.x;
        position.y += velocity.y;
        
        flipFrames = velocity.x < 0 ? true : (velocity.x > 0 ? false : flipFrames);
        
        setLocation((int)position.x, (int)position.y);
    }
}
