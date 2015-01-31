import greenfoot.*;
import greenfoot.util.GreenfootUtil;
import java.net.URL;
import java.util.Properties;
import java.io.InputStream;
/**
 * Write a description of class LoadLevelButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LoadLevelButton extends TextButton
{
    String levelDir;
    Properties info = new Properties();
    
    public LoadLevelButton(String levelDir)
    {
        this.levelDir = levelDir;
        InputStream in = null;
        try {
            try {
                URL url = GreenfootUtil.getURL("level.properties", "levels/" + levelDir);
                in = url.openStream();
                info.load(in);
            } finally {
                if (in != null)
                    in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.text = info.getProperty("name");
        createImage();
        
    }
    
    @Override
    public void onClick()
    {
        Greenfoot.setWorld(new Map(Resource.loadLevel(levelDir)));
    }
}
