import greenfoot.*;

/**
 * Write a description of class VisibleActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class VisibleActor extends Actor
{
    protected Map map;
    private int worldX;
    private int worldY;

    @Override
    public void addedToWorld(World world)
    {
        if (Map.class.isAssignableFrom(world.getClass()))
        {
            this.map = (Map)world;
        }
        
    }
    
    @Override
    public int getX()
    {
        return this.worldX;
    }
    
    @Override
    public int getY()
    {
        return this.worldY;
    }
    
    @Override
    public void setLocation(int x, int y)
    {
        if (map != null)
        {
            super.setLocation( x - map.camX + map.getWidth()/2, y -  map.camY + map.getHeight()/2);
        }
        else
        {
            super.setLocation(x, y);
        }
        this.worldX = x;
        this.worldY = y;
  
    }
}
