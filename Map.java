import greenfoot.*;
import greenfoot.util.GreenfootUtil;
import java.lang.NoSuchMethodException;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;
import java.awt.Point;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 * Write a description of class Map here.
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
    public static int GRID_SIZE = 55;
    public static int levelNumber = 0;
    public static int levelWidth = 0;
    public static int levelHeight = 0;
    public static final String DEFAULT = "\n\n__ 0    _\n_  _ _   _\n_        _\n_ _   _  _\n_  ___   _\n \n \n                1\n__________________\n";
    
    private static boolean MUSIC_PLAYING = false;
    private static GreenfootSound BACKGROUND_MUSIC = new GreenfootSound("sounds/background_loop.mp3");
    protected static HashMap<java.lang.Character, Class<? extends Actor>> TYPE_MAPPING;
    static {
        TYPE_MAPPING = new HashMap<java.lang.Character, Class<? extends Actor>>();
        TYPE_MAPPING.put(' ', null);
        TYPE_MAPPING.put('_', Platform.class);
        TYPE_MAPPING.put('H', HeatCoil.class);
        TYPE_MAPPING.put('0', Player.class);
        TYPE_MAPPING.put('1', ButterRonin.class);
        TYPE_MAPPING.put('2', JamFisher.class);
        TYPE_MAPPING.put('3', HazelShogun.class);
        TYPE_MAPPING.put('4', PaniniSumoPresser.class);
        TYPE_MAPPING.put('5', ButterRoninTutorial.class);
        TYPE_MAPPING.put('6', Toaster.class);
        TYPE_MAPPING.put('N', NextLevelTrigger.class);
        TYPE_MAPPING.put('a', HelpText.HelpTextMove.class);
        TYPE_MAPPING.put('b', HelpText.HelpTextJump.class);
        TYPE_MAPPING.put('c', HelpText.HelpTextBurner.class);
        TYPE_MAPPING.put('d', HelpText.HelpTextWall.class);
        TYPE_MAPPING.put('e', HelpText.HelpTextCeiling.class);
        TYPE_MAPPING.put('f', HelpText.HelpTextGrapple.class);
        TYPE_MAPPING.put('g', HelpText.HelpTextWallJump.class);
        TYPE_MAPPING.put('h', HelpText.HelpTextNext.class);
        TYPE_MAPPING.put('i', HelpText.HelpTextButter1.class);
        TYPE_MAPPING.put('j', HelpText.HelpTextButter2.class);
        TYPE_MAPPING.put('k', HelpText.HelpTextJam1.class);
        TYPE_MAPPING.put('l', HelpText.HelpTextJam2.class);
        TYPE_MAPPING.put('m', HelpText.HelpTextPanini1.class);
    }

    Properties props;
    
    private String mapData;
    
    public Map(String data, Properties props)
    {
        super(800, 600);
        this.props = props;
        this.setPaintOrder(HealthDisplay.class, Player.class, Character.class, Actor.class);
        loadLevel(data);
        this.mapData = data;
        
        initHealthDisplay();
    }

    public Map(String data)
    {
        this(data, new Properties());
    }

    public Map()
    {    
        this(DEFAULT);
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
                            try
                            {
                                this.addObject(type.getConstructor().newInstance(), loc.x, loc.y);
                            }
                            catch (InvocationTargetException e)
                            {
                                e.printStackTrace();
                                throw e.getTargetException();
                            }
                        } catch(Throwable ex) {
                            String msg = String.format("Construction failed at (%d, %d) with exception %s", col, row, ex.toString());
                            showText(msg, getWidth() / 2, getHeight() / 2);
                            ex.printStackTrace();
                            throw new RuntimeException(msg);
                            //return false;
                        }
                    }
                }
                row++;
            }
            return true;
        }
        return false;
    }
    
    private void initHealthDisplay()
    {
        this.addObject(new HealthDisplay(), 40, 40);
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
    
    public World reload()
    {
        return new Map(this.mapData, this.props);
    }

    private void startLevel()
    {
        
    }

    private void endLevel()
    {
        
    }
    
    @Override
    public void act()
    {
        if(!MUSIC_PLAYING)
        { BACKGROUND_MUSIC.playLoop(); MUSIC_PLAYING = true; }
        
        if(Greenfoot.isKeyDown("escape"))
            Greenfoot.setWorld(new PauseScreen("pause.png", this));
            
    }
    
    public World nextWorld()
    {
        String nextMap = props.getProperty("next.map");
        if (nextMap != null)
        {
            Properties nextInfo = loadNextWorldProps(nextMap);
            return new Map(Resource.loadLevel(nextMap), nextInfo);
        }
        else
        {
            return new Menu();
        }
    }
    
    private Properties loadNextWorldProps(String worldDir)
    {
        Properties info = new Properties();
        InputStream in = null;
        try {
            try {
                URL url = GreenfootUtil.getURL("level.properties", "levels/" + worldDir);
                in = url.openStream();
                info.load(in);
                return info;
            } finally {
                if (in != null)
                    in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
