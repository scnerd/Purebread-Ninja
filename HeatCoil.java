import greenfoot.*;
import java.util.HashSet;

/**
 * Write a description of class HeatCoil here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HeatCoil extends Platform
{
    public static final int DEFAULT_COLLISION_MARGIN = 6;
    protected int collisionMargin = DEFAULT_COLLISION_MARGIN;
    
    public HeatCoil()
    {
        this.setImage("images/HeatCoil.png");
    }
    
    /**
     * Act - do whatever the HeatCoil wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        HashSet tiles = new HashSet();
        tiles.addAll(lowerTiles());
        tiles.addAll(rightTile());
        tiles.addAll(leftTile());
        tiles.addAll(aboveTile());
        for (Object o : tiles)
        {
            Player p = (Player) o;
            p.damage(this);
        }
    }
    
    private HashSet lowerTiles()
    {
        int leftX = -getImage().getWidth() / 2 + collisionMargin;
        int rightX = getImage().getWidth() / 2 - collisionMargin;
        int down = getImage().getHeight() / 2 + 1;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, down, Player.class));
        toReturn.addAll(getObjectsAtOffset(rightX, down, Player.class));
        return toReturn;
    }

    private HashSet rightTile()
    {
        int rightX = getImage().getWidth() / 2;
        int downY = getImage().getHeight() / 2 - collisionMargin;
        int upY = -getImage().getHeight() / 2 + collisionMargin;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(rightX, downY, Player.class));
        toReturn.addAll(getObjectsAtOffset(rightX, upY, Player.class));
        return toReturn;
    }

    private HashSet leftTile()
    {
        int leftX = -getImage().getWidth() / 2 - 1;
        int downY = getImage().getHeight() / 2 - collisionMargin;
        int upY = -getImage().getHeight() / 2 + collisionMargin;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, downY, Player.class));
        toReturn.addAll(getObjectsAtOffset(leftX, upY, Player.class));
        return toReturn;
    }


    private HashSet aboveTile()
    {
        int leftX = -getImage().getWidth() / 2 + collisionMargin;
        int rightX = getImage().getWidth() / 2 - collisionMargin;
        int up = -getImage().getHeight() / 2 - 1;

        HashSet toReturn = new HashSet();
        toReturn.addAll(getObjectsAtOffset(leftX, up, Player.class));
        toReturn.addAll(getObjectsAtOffset(rightX, up, Player.class));
        return toReturn;
    }
}
