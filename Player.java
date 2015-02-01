import greenfoot.*;
import purebreadninja.*;
import static purebreadninja.CharacterAction.*;
import static purebreadninja.Command.*;
import static purebreadninja.Sprite.*;

import java.awt.geom.Point2D;
import java.util.HashSet;



public class Player extends Character
{
    @Animates(IDLE)
    @DefaultAnimation
    public Sprite idle = ImageSheet("images/player/standing.png");
    
    @Animates(MOVING_FLOOR)
    public Sprite running = ImageSheet("images/player/running.png");
    
    @Animates(MOVING_AIR)
    public Sprite jumping = ImageSheet("images/player/jumping.png");
    
    public static final Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    public int health = 5;
    protected CommandInterpreter controller;
    private Point2D.Double acceleration;
    private int direction = 1;
    private boolean usedUp = false;
    private GrapplingHook hook = null;
    
    private double ACC_GRAVITY = 0.25;
    private double ACC_GROUND_JUMP = 4.5;
    private double ACC_HOLD_JUMP = 0.1;
    
    private double ACC_MOVEMENT_GROUND = 0.4;
    private double ACC_MOVEMENT_AIR = 0.25;
    private double ACC_FRICTION = 0.4;
    
    private double ACC_WALL_HOLD = 0.2;
    private double ACC_WALL_JUMP = 4.5;
    private double ACC_WALL_JUMP_HORZ = 2;
    private double SLOWEST_SLIDE = 1;
    
    private double GRAPPLE_SPEED = 2.5;
    
    public Player() {}
    
    @Override
    public void damage(Actor fromActor)
    {
        this.health -= 1;
    }

    @Override
    public void addedToWorld(World world)
    {
        super.addedToWorld(world);

        
        if (controller == null)
        {
            this.controller = new KeyboardInterpreter();
        }
    }
    
    @Override
    public void act() 
    {
        enactMovement();
        if(controller.check(GRAPPLE))
        {
            enactGrapple();
        }
        else
        {
            if(hook != null) { this.getWorld().removeObject(hook); hook = null; }
        }
        finalizeMovement();
        flipFrames = direction == -1;
        
        if (velocity.x != 0 && isOnGround())
        {
            currentAction = MOVING_FLOOR;
        }
        else if (isOnCeiling())
        {
            currentAction = velocity.x != 0 ? MOVING_CEILING : IDLE_CEILING;
        }
        else
        {
            
        }
        
        super.act();
    }

    private HashSet groundTiles()
    {
        int leftX = -getImage().getWidth() / 2 + collisionMargin;
        int rightX = getImage().getWidth() / 2 - collisionMargin;
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
        int downY = getImage().getHeight() / 2 - collisionMargin;
        int upY = -getImage().getHeight() / 2 + collisionMargin;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(rightX, downY, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, upY, Platform.class));
        return toReturn;
    }

    private HashSet leftWallTiles()
    {
        int leftX = -getImage().getWidth() / 2 - 1;
        int downY = getImage().getHeight() / 2 - collisionMargin;
        int upY = -getImage().getHeight() / 2 + collisionMargin;

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
        int leftX = -getImage().getWidth() / 2 + collisionMargin;
        int rightX = getImage().getWidth() / 2 - collisionMargin;
        int up = -getImage().getHeight() / 2 - 1;

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
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
        // Calculate accelerations
        acceleration = new Point2D.Double(0, 0);
        // - Gravity
        acceleration.y += ACC_GRAVITY;

        // - Horizontal movement
        if(controller.check(LEFT) || controller.check(RIGHT)) 
        {
            if(isOnGround())
            {
                currentAction = MOVING_FLOOR;
                if(controller.check(LEFT) && controller.check(RIGHT))
                { }
                else if(controller.check(LEFT))
                { acceleration.x -= ACC_MOVEMENT_GROUND; }
                else if(controller.check(RIGHT))
                { acceleration.x += ACC_MOVEMENT_GROUND; }
            }
            else if(isOnWall() && !(isOnCeiling() && controller.check(UP)))
            {
                currentAction = MOVING_WALL;
                if(controller.check(LEFT) && controller.check(RIGHT))
                { }
                else if((controller.check(LEFT) && isOnLeftWall()) || (controller.check(RIGHT) && isOnRightWall()))
                { 
                    if(velocity.y < SLOWEST_SLIDE)
                        acceleration.y = Math.min(acceleration.y, SLOWEST_SLIDE - velocity.y);
                    else
                        acceleration.y = -Math.min(velocity.y - SLOWEST_SLIDE, ACC_WALL_HOLD);
                }
            }
            else
            {
                currentAction = MOVING_AIR;
                if(controller.check(LEFT) && controller.check(RIGHT))
                { }
                else if(controller.check(LEFT))
                { acceleration.x -= ACC_MOVEMENT_AIR; }
                else if(controller.check(RIGHT))
                { acceleration.x += ACC_MOVEMENT_AIR; }
            }
        }
        else if(isOnGround() || isOnCeiling())
        {
            // Get friction in the correct direction
            acceleration.x += (velocity.x < 0 ? 1 : -1) * ACC_FRICTION;
            // Make sure the friction stops player rather than accelerating them backwards
            acceleration.x = (acceleration.x < 0 ? -1 : 1) * 
            Math.min(Math.abs(velocity.x), Math.abs(acceleration.x));
        }

        // - Jumping
        if(controller.check(UP))
        {
            if(isOnCeiling())
            {
                acceleration.y = -velocity.y;
            }
            else if(isOnGround() && !usedUp)
            { acceleration.y = -ACC_GROUND_JUMP; }
            else if(isOnWall() && !usedUp)
            {
                int jump_dir = 0;
                if(isOnLeftWall())
                {
                    jump_dir = 1;
                }
                else if(isOnRightWall())
                {
                    jump_dir = -1;
                }
                if(jump_dir != 0)
                {
                    acceleration = new Point2D.Double(jump_dir * ACC_WALL_JUMP_HORZ, -ACC_WALL_JUMP - velocity.y);
                }
            }
            else if(!isOnGround() && !isOnWall())
            {
                acceleration.y -= ACC_HOLD_JUMP;
            }
            usedUp = true;
        }
        else if(!controller.check(UP))
        {
            usedUp = false;
        }
    }
    
    private void enactGrapple()
    {
        if(hook == null)
        {
            hook = new GrapplingHook(this, this.direction);
            this.getWorld().addObject(hook, getX(), getY());
        }
        else
        {
            if(hook.getIsHooked())
            {
                java.awt.Point target = hook.getHookTarget();
                double angle = Math.atan2(target.y - this.getY(), target.x - this.getX());
                acceleration.x += GRAPPLE_SPEED * Math.cos(angle);
                acceleration.y += GRAPPLE_SPEED * Math.sin(angle);
            }
        }
    }
    
    private void finalizeMovement()
    {
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
        
        if(velocity.x < 0)
            direction = -1;
        else if(velocity.x > 0)
            direction = 1;

        setLocation((int)position.x, (int)position.y);
    }
    
    public Actor publicGetOneObjectAtOffset(int dx, int dy, java.lang.Class cls)
    {
        return this.getOneObjectAtOffset(dx, dy, cls);
    }
   
}
