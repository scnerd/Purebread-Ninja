import greenfoot.*;
import java.util.HashSet;
import purebreadninja.Sprite;
import static purebreadninja.Sprite.*;

/**
 * Write a description of class HeatCoil here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HeatCoil extends Platform
{   
    public static final Sprite animation = ImageSheet("images/HeatCoil.png", 40);
    public HeatCoil()
    {
        setCurrentAnimation(animation);
    }
}
