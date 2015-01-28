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
    Point2D.Double position = null;
    Point2D.Double velocity = new Point2D.Double(0, 0);
    Point2D.Double MAX_VELOCITY = new Point2D.Double(3, 6);
    private double ACC_GRAVITY = 0.25;
    private double ACC_MOVEMENT = 0.4;
    private double ACC_FRICTION = 0.4;
    private double ACC_JUMP = 4.5;
    private double ACC_HOLD_JUMP = 0.1;

    private int COLLISION_MARGIN = 2;

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
    }
    
    private int sign(double n)
    { return n < 0 ? -1 : 1; }

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

    private HashSet wallTiles()
    {
        int leftX = -getImage().getWidth() / 2 + COLLISION_MARGIN;
        int rightX = getImage().getWidth() / 2 - COLLISION_MARGIN;
        int downY = getImage().getHeight() / 2 - COLLISION_MARGIN;
        int upY = -getImage().getHeight() / 2 + COLLISION_MARGIN;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, downY, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, downY, Platform.class));
        toReturn.addAll(getObjectsAtOffset(leftX, upY, Platform.class));
        toReturn.addAll(getObjectsAtOffset(rightX, upY, Platform.class));
        return toReturn;
    }

    private boolean isOnWall()
    {
        return wallTiles().size() > 0;
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

        // - Jumping
        if(keyboardUp())
        {
            if(isOnGround())
            { acceleration.y = -ACC_JUMP; }
            else
            { acceleration.y -= ACC_HOLD_JUMP; }
        }

        // - Horizontal movement
        if(keyboardLeft() || keyboardRight()) 
        {
            if(keyboardLeft() && keyboardRight())
            { }
            else if(keyboardLeft())
            { acceleration.x -= ACC_MOVEMENT; }
            else if(keyboardRight())
            { acceleration.x += ACC_MOVEMENT; }
        }
        else if(isOnGround())
        {
            // Get friction in the correct direction
            acceleration.x += (velocity.x < 0 ? 1 : -1) * ACC_FRICTION;
            // Make sure the friction stops player rather than accelerating them backwards
            acceleration.x = (acceleration.x < 0 ? -1 : 1) * 
            Math.min(Math.abs(velocity.x), Math.abs(acceleration.x));
        }

        // Calculate velocity
        velocity.x += acceleration.x;
        velocity.y += acceleration.y;

        // Tweak to avoid superimposition
        velocity.x = stepX(velocity.x);
        velocity.y = stepY(velocity.y);
        /*
        ArrayList collided = new ArrayList();
        boolean needToCheck = true;
        while(needToCheck)
        {
        Platform toAvoid;
        int left = (int)(velocity.x - getImage().getWidth() / 2);
        int right = (int)(velocity.x + getImage().getWidth() / 2);
        int top = (int)(velocity.y - getImage().getHeight() / 2);
        int bottom = (int)(velocity.y + getImage().getHeight() / 2);
        int shiftX = 0;
        int shiftY = 0;

        if((toAvoid = (Platform)getOneObjectAtOffset(right, bottom, Platform.class)) != null
        && !collided.contains(toAvoid))
        {
        collided.add(toAvoid);
        shiftX = toAvoid.left() - (right + getX());
        shiftY = toAvoid.top() - (bottom + getY());
        }
        else if((toAvoid = (Platform)getOneObjectAtOffset(left, bottom, Platform.class)) != null
        && !collided.contains(toAvoid))
        {
        collided.add(toAvoid);
        shiftX = (left + getX()) - toAvoid.right();
        shiftY = toAvoid.top() - (bottom + getY());
        }
        else if((toAvoid = (Platform)getOneObjectAtOffset(right, top, Platform.class)) != null
        && !collided.contains(toAvoid))
        {
        collided.add(toAvoid);
        shiftX = toAvoid.left() - (right + getX());
        shiftY = (top + getY()) - toAvoid.bottom();
        }
        else if((toAvoid = (Platform)getOneObjectAtOffset(left, top, Platform.class)) != null
        && !collided.contains(toAvoid))
        {
        collided.add(toAvoid);
        shiftX = (left + getX()) - toAvoid.right();
        shiftY = (top + getY()) - toAvoid.bottom();
        }
        else
        { needToCheck = false; break; }
        //Greenfoot.ask(String.format("%d, %d", shiftX, shiftY));
        if(Math.abs(shiftX) < Math.abs(shiftY))
        { velocity.x = shiftX; }
        else
        { velocity.y = shiftY; }
        }
         */

        // Apply Velocity
        velocity.x = (velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(velocity.x), MAX_VELOCITY.x);
        velocity.y = (velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(velocity.y), MAX_VELOCITY.y);
        position.x += velocity.x;
        position.y += velocity.y;

        setLocation((int)position.x, (int)position.y);
    }

    private double stepX(double velocity)
    {
        int direction = velocity < 0 ? -1 : 1;
        double front = position.x + direction * getImage().getWidth() / 2;
      
        int front_tile = Map.pointToGrid(front, position.y).x;
        int forward_limit = direction < 0 ?
        Math.max(-1, Map.pointToGrid(front + velocity, position.y).x - 1) : 
        Math.min(Map.levelWidth, Map.pointToGrid(front + velocity, position.y).x + 1);
        
        int top_tile = Map.pointToGrid(front, position.y + COLLISION_MARGIN - getImage().getHeight() / 2).y;
        int bottom_tile = Map.pointToGrid(front, position.y - COLLISION_MARGIN + getImage().getHeight() / 2).y;
        
        Platform collide = null;
        
        for(int tile_y = top_tile; tile_y <= bottom_tile; tile_y++)
        {
            for(int tile_x = front_tile; tile_x != forward_limit
            && sign(forward_limit - tile_x) == direction; tile_x += direction)
            {
                java.awt.Point loc = Map.gridToCenter(tile_x, tile_y);
                if((collide = 
                (Platform)getOneObjectAtOffset((int)(loc.x - position.x), (int)(loc.y - position.y), Platform.class))
                != null)
                {
                    int nearest_edge = direction < 0 ? collide.right() : collide.left();
                    double correction = nearest_edge - front;
                    int correction_direction = correction < 0 ? -1 : 1;
                    if(correction_direction != direction)
                        velocity = correction;
                    else
                        velocity = direction * Math.min(Math.abs(velocity), Math.abs(correction));
                    break;
                }
            }
        }
        return velocity;
    }

    private double stepY(double velocity)
    {
        int direction = velocity < 0 ? -1 : 1;
        double front = position.y + direction * getImage().getHeight() / 2;
        
        int front_tile = Map.pointToGrid(position.x, front).y;
        int forward_limit = direction < 0 ?
        Math.max(-1, Map.pointToGrid(position.x, front + velocity).y - 1) : 
        Math.min(Map.levelHeight, Map.pointToGrid(position.x, front + velocity).y + 1);
        
        int left_tile = Map.pointToGrid(position.x + COLLISION_MARGIN - getImage().getWidth() / 2, front).x;
        int right_tile = Map.pointToGrid(position.x - COLLISION_MARGIN + getImage().getWidth() / 2, front).x;
        
        Platform collide = null;
        
        for(int tile_x = left_tile; tile_x <= right_tile; tile_x++)
        {
            for(int tile_y = front_tile; tile_y != forward_limit && 
            sign(forward_limit - tile_y) == direction; tile_y += direction)
            {
                java.awt.Point loc = Map.gridToCenter(tile_x, tile_y);
                if((collide = 
                (Platform)getOneObjectAtOffset((int)(loc.x - position.x), (int)(loc.y - position.y), Platform.class))
                != null)
                {
                    int nearest_edge = direction < 0 ? collide.bottom() : collide.top();
                    double correction = nearest_edge - front;
                    int correction_direction = correction < 0 ? -1 : 1;
                    if(correction_direction != direction)
                        continue; // Different from X, which moves to correct
                    else
                        velocity = direction * Math.min(Math.abs(velocity), Math.abs(correction));
                    break;
                }
            }
        }
        return velocity;
    }
}
