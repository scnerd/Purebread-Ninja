import greenfoot.*;
import java.awt.geom.Point2D;

/**
 * Write a description of class Character here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Character extends AnimatedActor
{
    private enum CharacterState
    {
        IDLE,
        MOVING_FLOOR,
        MOVING_AIR,
        MOVING_WALL,
        MOVING_CEILING,
        ATTACKING,
        GRAPPLING
    }
    
    private CharacterState previousState = CharacterState.IDLE;
    protected CharacterState state = CharacterState.IDLE;
    public CharacterState getState() { return state; }
    
    protected static String animationFileNameRoot = "";
    
    // Will contain the animations for each state
    protected static SpriteAnimation
    idleAnimation,
    movingFloorAnimation,
    movingAirAnimation,
    movingWallAnimation,
    movingCeilingAnimation,
    attackingAnimation,
    grapplingAnimation;
    
    // Determines the filename suffix associated with each sprite sheet
    protected static String 
    idleFile = "idle",
    movingFloorFile = "moving_floor",
    movingAirFile = "moving_air",
    movingWallFile = "moving_wall",
    movingCeilingFile = "moving_ceiling",
    attackingFile = "attacking",
    grapplingFile = "grappling";
    
    // Determines the number of frames in each animation
    protected static int
    idleCount = 8,
    movingFloorCount = 8,
    movingAirCount = 8,
    movingWallCount = 8,
    movingCeilingCount = 8,
    attackingCount = 8,
    grapplingCount = 8,
    frameCount = 4;
    
    protected Point2D.Double position = null;
    protected Point2D.Double velocity = new Point2D.Double(0, 0);

    protected int COLLISION_MARGIN = 2;
    
    private String getSpriteSheetFileName(String file)
    { return String.format("%s_%s.png", animationFileNameRoot, file); }
    
    protected void loadSpriteSheets()
    {
        if(idleFile != null)
            idleAnimation = new SpriteAnimation(getSpriteSheetFileName(idleFile), idleCount, frameCount);
        if(movingFloorFile != null)
            movingFloorAnimation = new SpriteAnimation(getSpriteSheetFileName(movingFloorFile), movingFloorCount, frameCount);
        if(movingAirFile != null)
            movingAirAnimation = new SpriteAnimation(getSpriteSheetFileName(movingAirFile), movingAirCount, frameCount);
        if(movingWallFile != null)
            movingWallAnimation = new SpriteAnimation(getSpriteSheetFileName(movingWallFile), movingWallCount, frameCount);
        if(movingCeilingFile != null)
            movingCeilingAnimation = new SpriteAnimation(getSpriteSheetFileName(movingCeilingFile), movingCeilingCount, frameCount);
        if(attackingFile != null)
            attackingAnimation = new SpriteAnimation(getSpriteSheetFileName(attackingFile), attackingCount, frameCount);
        if(grapplingFile != null)
            grapplingAnimation = new SpriteAnimation(getSpriteSheetFileName(grapplingFile), movingFloorCount, frameCount);
        setCurrentAnimation(idleAnimation);
    }
    
    public void act()
    {
        if(state != previousState)
        {
            SpriteAnimation newAnimation = selectAnimation(state);
            if(newAnimation != currentAnimation)
            {
                setCurrentAnimation(newAnimation);
            }
        }
        previousState = state;
        super.act();
    }
    
    private SpriteAnimation selectAnimation(CharacterState curState)
    {
        switch(curState)
        {
            case IDLE: return idleAnimation;
            case MOVING_FLOOR: return movingFloorAnimation != null ?
            movingFloorAnimation :
            selectAnimation(CharacterState.IDLE);
            case MOVING_AIR: return movingFloorAnimation != null ?
            movingAirAnimation :
            selectAnimation(CharacterState.MOVING_FLOOR);
            case MOVING_WALL: return movingFloorAnimation != null ?
            movingWallAnimation :
            selectAnimation(CharacterState.MOVING_FLOOR);
            case MOVING_CEILING: return movingFloorAnimation != null ?
            movingCeilingAnimation :
            selectAnimation(CharacterState.MOVING_FLOOR);
            case ATTACKING: return movingFloorAnimation != null ?
            attackingAnimation :
            selectAnimation(CharacterState.IDLE);
            case GRAPPLING: return movingFloorAnimation != null ?
            grapplingAnimation :
            selectAnimation(CharacterState.MOVING_AIR);
        }
        return null;
    }
    
    private int sign(double n)
    { return n < 0 ? -1 : 1; }  

    protected double stepX(double velocity)
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

    protected double stepY(double velocity)
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
    
    public abstract void damage(Actor harmer);
}
