import greenfoot.*;
import java.lang.NoSuchMethodException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.awt.Point;
import java.util.HashMap;

/**
 * Write a description of class Map1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map extends CameraViewableWorld
{
    /*
     * TODO: Implement camera following
     * TODO: Implement background
     */
    
    public static int GRID_SIZE = 32;
    public static int levelNumber = 0;
    public static int levelWidth = 0;
    public static int levelHeight = 0;
    protected static HashMap TYPE_MAPPING = new HashMap<Character, Class<? extends Actor>>();
    public Player player;
    /**
     * Constructor for objects of class Map.
     * 
     */
    public Map()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        this(Levels.DEFAULT);
        
        if(TYPE_MAPPING.isEmpty())
        {
            TYPE_MAPPING.put(' ', null);
            TYPE_MAPPING.put('_', Platform.class);
            TYPE_MAPPING.put('0', Player.class);
            TYPE_MAPPING.put('1', ButterRonin.class);
            TYPE_MAPPING.put('2', JamFisher.class);
            TYPE_MAPPING.put('3', HazelShogun.class);
            TYPE_MAPPING.put('4', PaniniSumoPresser.class);
        }


    }
    
    public Map(String data)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600);
        
        if(TYPE_MAPPING.isEmpty())
        {
            TYPE_MAPPING.put(' ', null);
            TYPE_MAPPING.put('_', Platform.class);
            TYPE_MAPPING.put('0', Player.class);
            TYPE_MAPPING.put('1', ButterRonin.class);
            TYPE_MAPPING.put('2', JamFisher.class);
            TYPE_MAPPING.put('3', HazelShogun.class);
            TYPE_MAPPING.put('4', PaniniSumoPresser.class);
        }

        loadLevel(data);
    }

    private boolean loadLevel(String mapData)
    {
        if(mapData != null)
        {
            removeObjects(getObjects(null));
            levelWidth = 0;
            levelHeight = 0;
            int row = 0;
            for(String line : mapData.split("\\r?\\n"))
            {
                levelWidth = Math.max(levelWidth, line.length());
                levelHeight++;
                for(int col = 0; col < line.length(); col++)
                {
                    Point loc = gridToCenter(col, row);
                    Class<? extends Actor> type = (Class<? extends Actor>)
                        TYPE_MAPPING.get(line.charAt(col));
                    
                    if(type != null)
                    {
                        try {
                            this.addObject(type.getConstructor().newInstance(), loc.x, loc.y);
                        } catch(NoSuchMethodException ex) {
                            return false;
                        } catch(InstantiationException ex) {
                            return false;
                        } catch(IllegalAccessException ex) {
                            return false;
                        } catch(IllegalArgumentException ex) {
                            return false;
                        } catch(InvocationTargetException ex) {
                            return false;
                        }
                    }
                }
                row++;
            }
            return true;
        }
        return false;
    }
    
    public static Point pointToGrid(double x, double y)
    {
        return new Point((int)x / GRID_SIZE, (int)y / GRID_SIZE);
    }

    public static Point gridToCenter(int x, int y)
    {
        return cornerToCenter(x * GRID_SIZE, y * GRID_SIZE);
    }

    public static Point cornerToCenter(int x, int y)
    {
        return new Point(x + GRID_SIZE / 2, y + GRID_SIZE / 2);
    }

    public static Point cornerToCenter(Point p)
    {
        return cornerToCenter(p.x, p.y);
    }

    private void startLevel()
    {

    }

    private void endLevel()
    {

    }
}
