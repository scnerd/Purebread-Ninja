import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

public class PlayerPositionProcess extends ActorProcess 
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
    
    private Player player;
    private CommandInterpreter controller;
    
    private int lastVelocityX = 1;
    
    @Override
    public void onStart()
    {
        if (Player.class.isAssignableFrom(this.owner.getClass()))
        {
            this.player = (Player) this.owner;
            this.controller = this.player.controller;
            if(player.position == null)
            {
                player.position = new Point2D.Double(player.getX(), player.getY());
            }
        }
        else
        {
            this.abort();
        }
        
    }
    
    @Override
    public void run() 
    {
        updatePosition();
        
        if (player.velocity.x < 0 && lastVelocityX > 0)
        {
            lastVelocityX = -1;
        }
        else if (player.velocity.x > 0 && lastVelocityX < 0)
        {
            lastVelocityX = 1;
        }
        
        if (player.velocity.x != 0 && isOnGround())
        {
            player.setCurrentView(PlayerView.RUNNING);
        }

        else if (player.velocity.x == 0 && player.velocity.y == 0)
        {
            player.setCurrentView(PlayerView.STANDING);
        }
        else if (player.velocity.y != 0)
        {
            player.setCurrentView(PlayerView.JUMPING);
        }
        
        if (lastVelocityX == -1)
        {
            player.faceLeft();
        }
        else
        {
            player.faceRight();
        }
    }

    private HashSet groundTiles()
    {
        int leftX = -player.getImage().getWidth() / 2 + player.COLLISION_MARGIN;
        int rightX = player.getImage().getWidth() / 2 - player.COLLISION_MARGIN;
        int down = player.getImage().getHeight() / 2;

        HashSet toReturn = new HashSet();
        toReturn.addAll(player.getObjectsAtOffset(leftX, down, Platform.class));
        toReturn.addAll(player.getObjectsAtOffset(rightX, down, Platform.class));
        return toReturn;
    }

    private boolean isOnGround()
    {
        return groundTiles().size() > 0;
    }
    
    private HashSet rightWallTiles()
    {
        int rightX = player.getImage().getWidth() / 2;
        int downY = player.getImage().getHeight() / 2 - player.COLLISION_MARGIN;
        int upY = -player.getImage().getHeight() / 2 + player.COLLISION_MARGIN;

        HashSet toReturn = new HashSet();
        toReturn.addAll(player.getObjectsAtOffset(rightX, downY, Platform.class));
        toReturn.addAll(player.getObjectsAtOffset(rightX, upY, Platform.class));
        return toReturn;
    }
    
    private HashSet leftWallTiles()
    {
        int leftX = -player.getImage().getWidth() / 2 - 1;
        int downY = player.getImage().getHeight() / 2 - player.COLLISION_MARGIN;
        int upY = -player.getImage().getHeight() / 2 + player.COLLISION_MARGIN;

        HashSet toReturn = new HashSet();
        toReturn.addAll(player.getObjectsAtOffset(leftX, downY, Platform.class));
        toReturn.addAll(player.getObjectsAtOffset(leftX, upY, Platform.class));
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
        int leftX = -player.getImage().getWidth() / 2 + player.COLLISION_MARGIN;
        int rightX = player.getImage().getWidth() / 2 - player.COLLISION_MARGIN;
        int up = -player.getImage().getHeight() / 2;

        HashSet toReturn = new HashSet();
        toReturn.addAll(player.getObjectsAtOffset(leftX, up, Platform.class));
        toReturn.addAll(player.getObjectsAtOffset(rightX, up, Platform.class));
        return toReturn;
    }

    private boolean isOnCeiling()
    {
        return ceilingTiles().size() > 0;
    }

    private void updatePosition()
    {
        // Calculate accelerations
        Point2D.Double acceleration = new Point2D.Double(0, 0);
        // - Gravity
        acceleration.y += ACC_GRAVITY;

        // - Horizontal movement
        if(controller.commandLeft() || controller.commandRight()) 
        {
            if(isOnGround())
            {
                if(controller.commandLeft() && controller.commandRight())
                { }
                else if(controller.commandLeft())
                { acceleration.x -= ACC_MOVEMENT_GROUND; }
                else if(controller.commandRight())
                { acceleration.x += ACC_MOVEMENT_GROUND; }
            }
            else if(isOnWall())
            {
                if(controller.commandLeft() && controller.commandRight())
                { }
                else if((controller.commandLeft() && isOnLeftWall()) || (controller.commandRight() && isOnRightWall()))
                { 
                    if(player.velocity.y < SLOWEST_SLIDE)
                        acceleration.y = Math.min(acceleration.y, SLOWEST_SLIDE - player.velocity.y);
                    else
                        acceleration.y = -Math.min(player.velocity.y - SLOWEST_SLIDE, ACC_WALL_HOLD);
                }
            }
            else
            {
                if(controller.commandLeft() && controller.commandRight())
                { }
                else if(controller.commandLeft())
                { acceleration.x -= ACC_MOVEMENT_AIR; }
                else if(controller.commandRight())
                { acceleration.x += ACC_MOVEMENT_AIR; }
            }
        }
        else if(isOnGround())
        {
            // Get friction in the correct direction
            acceleration.x += (player.velocity.x < 0 ? 1 : -1) * ACC_FRICTION;
            // Make sure the friction stops player rather than accelerating them backwards
            acceleration.x = (acceleration.x < 0 ? -1 : 1) * 
            Math.min(Math.abs(player.velocity.x), Math.abs(acceleration.x));
        }

        // - Jumping
        if(controller.commandUp() && !usedUp)
        {
            if(isOnGround())
            { acceleration.y = -ACC_GROUND_JUMP; }
            else if(isOnWall())
            {
                int jump_dir = 0;
                if(isOnLeftWall() && controller.commandLeft())
                {
                    jump_dir = 1;
                }
                else if(isOnRightWall() && controller.commandRight())
                {
                    jump_dir = -1;
                }
                if(jump_dir != 0)
                {
                    acceleration = new Point2D.Double(jump_dir * ACC_WALL_JUMP_HORZ, -ACC_WALL_JUMP - player.velocity.y);
                }
            }
            usedUp = true;
        }
        else if(!controller.commandUp())
        {
            usedUp = false;
        }

        // Calculate velocity
        player.velocity.x += acceleration.x;
        player.velocity.y += acceleration.y;

        // Tweak to avoid superimposition
        player.velocity.x = player.stepX(player.velocity.x);
        player.velocity.y = player.stepY(player.velocity.y);

        // Apply Velocity
        player.velocity.x = (player.velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(player.velocity.x), MAX_VELOCITY.x);
        player.velocity.y = (player.velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(player.velocity.y), MAX_VELOCITY.y);
        player.position.x += player.velocity.x;
        player.position.y += player.velocity.y;

        player.setLocation((int)player.position.x, (int)player.position.y);
    }
}
