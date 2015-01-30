import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.reflect.InvocationTargetException;

/**
 * Write a description of class ButterRonin here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ButterRonin extends Enemy
{   
    public ButterRonin()
    {
        this.setImage("images/chips-1.png");
        this.addProcess(new BulletSeedSpawnProcess());
    }  
}
