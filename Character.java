import greenfoot.*;
import purebreadninja.*;

import java.awt.geom.Point2D;

import java.lang.reflect.Field;

/**
 * Write a description of class Character here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Character extends AnimatedActor
{
    public static final int DEFAULT_COLLISION_MARGIN = 2;
    
    protected int collisionMargin = DEFAULT_COLLISION_MARGIN;
    protected Point2D.Double position = null;
    protected Point2D.Double velocity = new Point2D.Double(0, 0);
    protected CharacterAction currentAction = CharacterAction.IDLE;   
    private CharacterAction previousAction = CharacterAction.IDLE;
    private SpriteAnimation[] animations = new SpriteAnimation[CharacterAction.values().length];
    
    public Character()
    {

    }
    
    @Override
    public void addedToWorld(World world)
    {
        loadSpriteSheets();
        setCurrentAnimation(animations[currentAction.ordinal()]);

    }
    
    protected void loadSpriteSheets()
    {
        Field[] fields = this.getClass().getFields();
        for (Field field : fields)
        {
            DefaultAnimation catchAll = field.getAnnotation(DefaultAnimation.class);
            if (catchAll != null)
            {
                try
                {
                    for (CharacterAction a : CharacterAction.values())
                    {
                        animations[a.ordinal()] = (SpriteAnimation) field.get(this);
                    }
                    break;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    throw new RuntimeException(e.getCause());
                    
                }

            }
        }
        
        for (Field field : fields)
        {
            Animates action = field.getAnnotation(Animates.class);
            if (action != null)
            {
                
                try
                {
                    animations[action.value().ordinal()] = (SpriteAnimation) field.get(this);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e.getCause());
                }
            }
        }
        
    }
    
    public void act()
    {
        if(currentAction != previousAction)
        {
            SpriteAnimation newAnimation = animations[currentAction.ordinal()];
            if(newAnimation != currentAnimation)
            {
                setCurrentAnimation(newAnimation);
            }
        }
        previousAction = currentAction;
        super.act();
    }
    
    private int sign(double n)
    {
        return n < 0 ? -1 : 1;
    }  

    protected double stepX(double velocity)
    {
        int direction = velocity < 0 ? -1 : 1;
        double front = position.x + direction * getImage().getWidth() / 2;
      
        int front_tile = Map.pointToGrid(front, position.y).x;
        int forward_limit = direction < 0 ?
        Math.max(-1, Map.pointToGrid(front + velocity, position.y).x - 1) : 
        Math.min(Map.levelWidth, Map.pointToGrid(front + velocity, position.y).x + 1);
        
        int top_tile = Map.pointToGrid(front, position.y + collisionMargin - getImage().getHeight() / 2).y;
        int bottom_tile = Map.pointToGrid(front, position.y - collisionMargin + getImage().getHeight() / 2).y;
        
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

    protected double stepY(double velocity)
    {
        int direction = velocity < 0 ? -1 : 1;
        double front = position.y + direction * getImage().getHeight() / 2;
        
        int front_tile = Map.pointToGrid(position.x, front).y;
        int forward_limit = direction < 0 ?
        Math.max(-1, Map.pointToGrid(position.x, front + velocity).y - 1) : 
        Math.min(Map.levelHeight, Map.pointToGrid(position.x, front + velocity).y + 1);
        
        int left_tile = Map.pointToGrid(position.x + collisionMargin - getImage().getWidth() / 2, front).x;
        int right_tile = Map.pointToGrid(position.x - collisionMargin + getImage().getWidth() / 2, front).x;
        
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
                    if(correction != 0 && correction_direction != direction)
                        continue; // Different from X, which moves to correct
                    else
                        velocity = direction * Math.min(Math.abs(velocity), Math.abs(correction));
                    break;
                }
            }
        }
        return velocity;
    }
    
    public abstract void damage(Actor fromActor);
}
