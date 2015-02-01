import greenfoot.*;

/**
 * Write a description of class VisibleActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class VisibleActor extends Actor
{
    private CameraViewableWorld world;
    private int worldX;
    private int worldY;

    @Override
    public void addedToWorld(World world)
    {
        this.world = (CameraViewableWorld) world;
        this.worldX = super.getX();
        this.worldY = super.getY();
    }
    
    public void moveCamera(int x, int y)
    {
        this.world.setCameraLocation(x, y);
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
    public final void setLocation(int x, int y)
    {
        if (world != null)
        {
            super.setLocation(x - world.getCameraX() + world.getWidth()/2, y -  world.getCameraY() + world.getHeight()/2);
        }
        else
        {
            super.setLocation(x, y);
        }
        this.worldX = x;
        this.worldY = y;
    }
}