import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;


/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Character
{
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    
    protected HashMap<PlayerView, GreenfootImage[][]> views;
    protected PlayerView currentView;
    protected CommandInterpreter controller;
    protected SpriteAnimation animation;
    
    public Player()
    {
        this.setImage("images/BreadNinjaStanding.png");
        initPlayerViews();
        this.animation = new SpriteAnimation(views.get(PlayerView.RUNNING)[RIGHT]);
        setCurrentView(PlayerView.RUNNING);
        this.addProcess(this.animation);
        this.controller = new KeyboardInterpreter();
        this.addProcess(new PlayerPositionProcess());
    }
    
    public CommandInterpreter getController()
    {
        return controller;
    }
    
    public void setCurrentView(PlayerView currentView)
    {
        if (this.currentView != currentView)
        {
            this.currentView = currentView;
            this.animation.setAnimation(views.get(currentView)[RIGHT], true);
        }
    }
    
    public void faceLeft()
    {
        this.animation.setAnimation(this.views.get(currentView)[LEFT], true);        
    }

    public void faceRight()
    {
        this.animation.setAnimation(this.views.get(currentView)[RIGHT], true);
        
    }
    
    private void initPlayerViews()
    {
        views = new HashMap<PlayerView, GreenfootImage[][]>();
        for (PlayerView v : PlayerView.values())
        {
            views.put(v, new GreenfootImage[2][]);
        }
        initView(PlayerView.RUNNING, Resource.loadSpriteFrames("images/BreadNinjaSpriteSmall.png", 32, 26, 1));
        initView(PlayerView.JUMPING, Resource.loadSpriteFrames("images/BreadNinjaSpriteJumpSmall.png", 32, 26, 1));
        initView(PlayerView.STANDING, Resource.loadSpriteFrames("images/BreadNinjaSpriteStandSmall.png", 32, 26, 1));
        
    }
    
    private void initView(PlayerView v, GreenfootImage[] frames)
    {
        views.get(v)[RIGHT] = frames;
        views.get(v)[LEFT] = SpriteAnimation.flipFrames(frames);
        
    }

    private Point2D.Double acceleration;
    private int direction = 1;
    private boolean usedUp = false;
    private GrapplingHook hook = null;
    
    Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    
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

    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(position == null)
        {
            position = new Point2D.Double(getX(), getY());
        }
        enactMovement();
        if(keyboardGrapple())
        {
            enactGrapple();
        }
        else
        {
            if(hook != null) { this.getWorld().removeObject(hook); hook = null; }
        }
        finalizeMovement();
    }

    private boolean keyboardLeft()
    { return Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left"); }

    private boolean keyboardRight()
    { return Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right"); }

    private boolean keyboardUp()
    { return Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("space"); }
    
    private boolean keyboardGrapple()
    { return Greenfoot.isKeyDown("shift"); }

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
        // Calculate accelerations
        acceleration = new Point2D.Double(0, 0);
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
        else if(isOnGround() || isOnCeiling())
        {
            // Get friction in the correct direction
            acceleration.x += (velocity.x < 0 ? 1 : -1) * ACC_FRICTION;
            // Make sure the friction stops player rather than accelerating them backwards
            acceleration.x = (acceleration.x < 0 ? -1 : 1) * 
            Math.min(Math.abs(velocity.x), Math.abs(acceleration.x));
        }

        // - Jumping
        if(keyboardUp())
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
        else if(!keyboardUp())
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
    { return this.getOneObjectAtOffset(dx, dy, cls); }
   
}
