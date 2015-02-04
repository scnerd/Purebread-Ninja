import greenfoot.*;

/**
 * Write a description of class NextLevelTrigger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NextLevelTrigger extends VisibleActor
{
    public NextLevelTrigger()
    {
        this.setImage("images/NextLevelTrigger2.png");
    }
    /**
     * Act - do whatever the NextLevelTrigger wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        Player p = null;
        if(getOneIntersectingObject(Player.class) != null)
        {
            Map m = (Map) getWorld();
            Greenfoot.setWorld(m.nextWorld());
        }
    }  
}
