import greenfoot.*;
import java.awt.geom.Point2D;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Character
{
    
    public static final Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    private double ACC_GRAVITY = 0.25;
    private int VISION_RANGE = 150;
    
    protected boolean sawPlayer = false;
    
    @Override
    public void act()
    {
        enactMovement();
        //checkCollisions();
        
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
        getWorld().removeObject(this);
    }
    
    protected boolean playerInRange()
    {
        if (!getObjectsInRange(VISION_RANGE, Player.class).isEmpty())
        {
            return sawPlayer = true;
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
        return isFacingPlayer(attacker);
    }
    
    protected void facePlayer()
    {
        double angle = getAngleToPlayer();

        flipFrames = (angle >= Math.PI/2.0 && angle <= 3*Math.PI/2.0);
    }
    
    protected boolean atLeftEdge()
    {
        int left_distance = 20;
        int down_distance = 20;
        
        return (getOneObjectAtOffset(-left_distance, down_distance, Platform.class) == null);
    }
    
    protected boolean atRightEdge()
    {
        int right_distance = 20;
        int down_distance = 20;
        
        return (getOneObjectAtOffset(right_distance, down_distance, Platform.class) == null);
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
