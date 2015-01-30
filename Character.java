import greenfoot.*;
import java.awt.geom.Point2D;

/**
 * Write a description of class Character here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Character extends AnimatedActor
{
    protected Point2D.Double position = null;
    protected Point2D.Double velocity = new Point2D.Double(0, 0);

    protected int COLLISION_MARGIN = 2;
    
    private int sign(double n)
    { return n < 0 ? -1 : 1; }
    
    /**
     * Act - do whatever the Character wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    

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
