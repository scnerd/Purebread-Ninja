import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.reflect.InvocationTargetException;

public class ButterRonin extends Enemy
{   
    public ButterRonin()
    {
        this.setImage("images/chips-1.png");
        this.addProcess(new BulletSeedSpawnProcess());
    }  
}
