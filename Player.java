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
    public Sprite idle = ImageSheet("images/player/standing.png", 8);
    
    @Animates(IDLE_CEILING)
    public Sprite hanging = ImageSheet("images/player/hanging.png", 1);
    
    @Animates(MOVING_FLOOR)
    public Sprite running = ImageSheet("images/player/running.png", 6);
    
    @Animates(MOVING_AIR)
    public Sprite jumping = ImageSheet("images/player/flying.png", 6);
    
    @Animates(MOVING_WALL)
    public Sprite flying = ImageSheet("images/player/sliding.png", 2);
    
    @Animates(MOVING_CEILING)
    public Sprite climbing = ImageSheet("images/player/climbing.png", 6);
    
    @Animates(HURT)
    public Sprite hurt = Sprite.ImageSheet("images/player/hurting.png", 4);

    @Animates(INVULNERABLE)
    public Sprite invulnerable = Sprite.ImageSheet("images/player/invulnerable.png", 1);
    
    public static final Point2D.Double MAX_VELOCITY = new Point2D.Double(6, 12);
    public int health = 5;
    protected CommandInterpreter controller;
    private Point2D.Double acceleration;
    private int direction = 1;
    private boolean usedUp = false;
    private GrapplingHook hook = null;
    private boolean isHookReady = true;
    
    private double ACC_GRAVITY = 0.375;
    private double ACC_GROUND_JUMP = 6.75;
    private double ACC_HOLD_JUMP = 0.15;
    
    private double ACC_MOVEMENT_GROUND = 0.6;
    private double ACC_MOVEMENT_AIR = 0.375;
    private double ACC_FRICTION = 0.6;
    
    private double ACC_WALL_HOLD = 0.3;
    private double ACC_WALL_JUMP = 6.75;
    private double ACC_WALL_JUMP_HORZ = 3;
    private double SLOWEST_SLIDE = 1.5;
    
    private double GRAPPLE_SPEED = 3.75;
    
    private int HURT_DISPLAY_DEFAULT = 10;
    private int INVULNERABLE_DISPLAY_DEFAULT = 40;
    
    private boolean onGround;
    private boolean onLeftWall;
    private boolean onRightWall;
    private boolean onWall;
    private boolean onCeiling;
    private boolean isInvulnerable = false;
    
    private int hurtDisplayDuration;
    private int invulnerableDisplayDuration;
    
    public Player() {}
    
    @Override
    public void damage(Actor fromActor)
    {
        if (!isInvulnerable)
        {
            double knockback_x = 20.0;
            double knockback_y = 8.0;
            int direction = fromActor.getX() <= getX() ? 1 : -1;
            velocity.x = direction * knockback_x;
            velocity.y = - knockback_y;
            
            if (--health == 0)
            {
                Map m = (Map)getWorld();
                Greenfoot.setWorld(new ScreenWorld("images/game_over.png", m.reload()));
            }
            isInvulnerable = true;
            hurtDisplayDuration = HURT_DISPLAY_DEFAULT;
            invulnerableDisplayDuration = INVULNERABLE_DISPLAY_DEFAULT;
        }
    }

    @Override
    public void addedToWorld(World world)
    {
        super.addedToWorld(world);
        
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
        
        if (controller == null)
        {
            this.controller = new KeyboardInterpreter();
        }
    }
    
    @Override
    public void act() 
    {
        updateOnTouchBooleans();
        updatePosition();
        if(controller.check(GRAPPLE))
        {
            triggerGrapple();
        }
        else
        {
            if(hook != null)
            {
                this.getWorld().removeObject(hook);
                hook = null;
            }
            isHookReady = true;
        }
        finalizeMovement();
        flipFrames = direction == -1;
        updateOnTouchBooleans();
        checkCollisions();
        
        if (hurtDisplayDuration-- >= 0)
        {
            currentAction = HURT;
        }
        else if (invulnerableDisplayDuration-- >= 0)
        {
            currentAction = INVULNERABLE;
            isInvulnerable = (invulnerableDisplayDuration > 0);
        }
        else if(onGround)
        {
            if(velocity.x != 0)
            { currentAction = MOVING_FLOOR; }
            else
            { currentAction = IDLE; }
        }
        else if (!onGround)
        {
            if(onCeiling && controller.check(UP))
            {
                if(velocity.x != 0)
                { currentAction = MOVING_CEILING; }
                else
                { currentAction = IDLE_CEILING; }
            }
            else if(velocity.y > 0 && ((controller.check(RIGHT) && onRightWall) ^ (controller.check(LEFT) && onLeftWall)))
            { currentAction = MOVING_WALL; }
            else
            { currentAction = MOVING_AIR; }
        }
        
        super.act();
    }
    
    private void updateOnTouchBooleans()
    {
        onGround = groundTiles().size() > 0;
        onLeftWall = leftWallTiles().size() > 0;
        onRightWall = rightWallTiles().size() > 0;
        onWall = onLeftWall || onRightWall;
        onCeiling = ceilingTiles().size() > 0;
    }

    private HashSet groundTiles()
    {
        int leftX = -getImage().getWidth() / 2 + collisionMargin;
        int rightX = getImage().getWidth() / 2 - collisionMargin;
        int down = getImage().getHeight() / 2 + 1;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, down, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, down, Platform.class));
        return toReturn;
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

    private void updatePosition()
    {
        // Calculate accelerations
        acceleration = new Point2D.Double(0, 0);
        // - Gravity
        acceleration.y += ACC_GRAVITY;

       // - Horizontal movement
        if(controller.check(LEFT) || controller.check(RIGHT)) 
        {
            if(onGround)
            {
                if(controller.check(LEFT) && controller.check(RIGHT))
                { }
                else if(controller.check(LEFT))
                { acceleration.x -= ACC_MOVEMENT_GROUND; }
                else if(controller.check(RIGHT))
                { acceleration.x += ACC_MOVEMENT_GROUND; }
            }
            else if(!onCeiling && (controller.check(LEFT) && onLeftWall) ^ (controller.check(RIGHT) && onRightWall))
            {
                if(velocity.y < SLOWEST_SLIDE)
                    acceleration.y = Math.min(acceleration.y, SLOWEST_SLIDE - velocity.y);
                else
                    acceleration.y = -Math.min(velocity.y - SLOWEST_SLIDE, ACC_WALL_HOLD);
            }
            else
            {
                if(controller.check(LEFT) && controller.check(RIGHT))
                { }
                else if(controller.check(LEFT))
                { acceleration.x -= ACC_MOVEMENT_AIR; }
                else if(controller.check(RIGHT))
                { acceleration.x += ACC_MOVEMENT_AIR; }
            }
        }
        else if(onGround || onCeiling)
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
            if(onCeiling)
            {
                    acceleration.y = -velocity.y;
            }
            else if(onGround && !usedUp)
            { acceleration.y = -ACC_GROUND_JUMP; }
            else if(onWall && !usedUp)
            {
                int jump_dir = 0;
                if(onLeftWall)
                {
                    jump_dir = 1;
                }
                else if(onRightWall)
                {
                    jump_dir = -1;
                }
                if(jump_dir != 0)
                {
                    acceleration = new Point2D.Double(jump_dir * ACC_WALL_JUMP_HORZ, -ACC_WALL_JUMP - velocity.y);
                }
            }
            else if(!onGround && !onWall)
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
    
    private void triggerGrapple()
    {
        
        if(hook == null && isHookReady)
        {
            hook = new GrapplingHook(this, this.direction);
            getWorld().addObject(hook, getX(), getY());
            isHookReady = false;
        }
        else if(hook != null && hook.getIsHooked())
        {
            java.awt.Point target = hook.getHookTarget();
            Platform platform = hook.platform;
            double dy = platform.getY() - getY();
            double dx = platform.getX() - getX();
            double distance = Math.sqrt(dy*dy + dx*dx);
            if (distance > 64)
            {
                double angle = Math.atan2(dy, dx);
                acceleration.x += GRAPPLE_SPEED * Math.cos(angle);
                acceleration.y += GRAPPLE_SPEED * Math.sin(angle);   
            }
            else 
            {
                this.getWorld().removeObject(hook);
                hook = null;
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
        this.moveCamera((int)position.x, (int)position.y);
    }
    
    public Actor publicGetOneObjectAtOffset(int dx, int dy, java.lang.Class cls)
    {
        return this.getOneObjectAtOffset(dx, dy, cls);
    }
   
    protected void checkCollisions()
    {
        Enemy enemy = null;
        if((enemy = (Enemy)getOneIntersectingObject(Enemy.class)) != null)
        {
            if (enemy.isVulnerableTo(this))
            {
                damage(enemy);
            }
            else if (!isInvulnerable)
            {
                enemy.damage(this);
            }
        }
    }
}
