import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Character
{
    private boolean usedUp = false;
    
    Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    
    private double ACC_GRAVITY = 0.25;
    private double ACC_GROUND_JUMP = 4.5;
    
    private double ACC_MOVEMENT_GROUND = 0.4;
    private double ACC_MOVEMENT_AIR = 0.2;
    private double ACC_FRICTION = 0.4;
    
    private double ACC_WALL_HOLD = 0.2;
    private double ACC_WALL_JUMP = 4.5;
    private double ACC_WALL_JUMP_HORZ = 2;
    private double SLOWEST_SLIDE = 1;

    
    public Player() {
        super();
        //this.addProcess(new SpriteAnimation("./images/BreadNinjaSprite.png", 108, 99));
        //System.out.println(new java.io.File(".").getAbsolutePath());
    }
    
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        super.act();
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
        enactMovement();
    }

    private boolean keyboardLeft()
    { return Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left"); }

    private boolean keyboardRight()
    { return Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right"); }

    private boolean keyboardUp()
    { return Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up"); }

    private HashSet groundTiles()
    {
        int leftX = -getImage().getWidth() / 2 + COLLISION_MARGIN;
        int rightX = getImage().getWidth() / 2 - COLLISION_MARGIN;
        int down = getImage().getHeight() / 2;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, down, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, down, Platform.class));
        return toReturn;
    }

    private boolean isOnGround()
    {
        return groundTiles().size() > 0;
    }
    
    private HashSet rightWallTiles()
    {
        int rightX = getImage().getWidth() / 2;
        int downY = getImage().getHeight() / 2 - COLLISION_MARGIN;
        int upY = -getImage().getHeight() / 2 + COLLISION_MARGIN;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(rightX, downY, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, upY, Platform.class));
        return toReturn;
    }
    
    private HashSet leftWallTiles()
    {
        int leftX = -getImage().getWidth() / 2 - 1;
        int downY = getImage().getHeight() / 2 - COLLISION_MARGIN;
        int upY = -getImage().getHeight() / 2 + COLLISION_MARGIN;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, downY, Platform.class));
        toReturn.addAll(getObjectsAtOffset(leftX, upY, Platform.class));
        return toReturn;
    }

    private HashSet wallTiles()
    {
        HashSet toReturn = new HashSet();
        toReturn.addAll(leftWallTiles());
        toReturn.addAll(rightWallTiles());
        return toReturn;
    }

    private boolean isOnWall()
    {
        return wallTiles().size() > 0;
    }
    
    private boolean isOnLeftWall()
    {
        return leftWallTiles().size() > 0;
    }
    
    private boolean isOnRightWall()
    {
        return rightWallTiles().size() > 0;
    }

    private HashSet ceilingTiles()
    {
        int leftX = -getImage().getWidth() / 2 + COLLISION_MARGIN;
        int rightX = getImage().getWidth() / 2 - COLLISION_MARGIN;
        int up = -getImage().getHeight() / 2;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, up, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, up, Platform.class));
        return toReturn;
    }

    private boolean isOnCeiling()
    {
        return ceilingTiles().size() > 0;
    }

    private void enactMovement()
    {
        // Calculate accelerations
        Point2D.Double acceleration = new Point2D.Double(0, 0);
        // - Gravity
        acceleration.y += ACC_GRAVITY;

        // - Horizontal movement
        if(keyboardLeft() || keyboardRight()) 
        {
            if(isOnGround())
            {
                if(keyboardLeft() && keyboardRight())
                { }
                else if(keyboardLeft())
                { acceleration.x -= ACC_MOVEMENT_GROUND; }
                else if(keyboardRight())
                { acceleration.x += ACC_MOVEMENT_GROUND; }
            }
            else if(isOnWall())
            {
                if(keyboardLeft() && keyboardRight())
                { }
                else if((keyboardLeft() && isOnLeftWall()) || (keyboardRight() && isOnRightWall()))
                { 
                    if(velocity.y < SLOWEST_SLIDE)
                        acceleration.y = Math.min(acceleration.y, SLOWEST_SLIDE - velocity.y);
                    else
                        acceleration.y = -Math.min(velocity.y - SLOWEST_SLIDE, ACC_WALL_HOLD);
                }
            }
            else
            {
                if(keyboardLeft() && keyboardRight())
                { }
                else if(keyboardLeft())
                { acceleration.x -= ACC_MOVEMENT_AIR; }
                else if(keyboardRight())
                { acceleration.x += ACC_MOVEMENT_AIR; }
            }
        }
        else if(isOnGround())
        {
            // Get friction in the correct direction
            acceleration.x += (velocity.x < 0 ? 1 : -1) * ACC_FRICTION;
            // Make sure the friction stops player rather than accelerating them backwards
            acceleration.x = (acceleration.x < 0 ? -1 : 1) * 
            Math.min(Math.abs(velocity.x), Math.abs(acceleration.x));
        }

        // - Jumping
        if(keyboardUp() && !usedUp)
        {
            if(isOnGround())
            { acceleration.y = -ACC_GROUND_JUMP; }
            else if(isOnWall())
            {
                int jump_dir = 0;
                if(isOnLeftWall() && keyboardLeft())
                {
                    jump_dir = 1;
                }
                else if(isOnRightWall() && keyboardRight())
                {
                    jump_dir = -1;
                }
                if(jump_dir != 0)
                {
                    acceleration = new Point2D.Double(jump_dir * ACC_WALL_JUMP_HORZ, -ACC_WALL_JUMP - velocity.y);
                }
            }
            usedUp = true;
        }
        else if(!keyboardUp())
        {
            usedUp = false;
        }

        // Calculate velocity
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;

        // Tweak to avoid superimposition
        velocity.x = stepX(velocity.x);
        velocity.y = stepY(velocity.y);

        // Apply Velocity
        velocity.x = (velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(velocity.x), MAX_VELOCITY.x);
        velocity.y = (velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(velocity.y), MAX_VELOCITY.y);
        position.x += velocity.x;
        position.y += velocity.y;

        setLocation((int)position.x, (int)position.y);
    }
}
