import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Character
{

    
    protected CommandInterpreter controller;
    protected SpriteAnimation animation;
    protected GreenfootImage[] runFrames;
    
    public Player()
    {
        this.runFrames = Resource.loadSpriteFrames("images/BreadNinjaSprite.png", 109, 88, 0.5);
        this.animation = new SpriteAnimation(runFrames);
        //this.addProcess(this.animation);
        this.controller = new KeyboardInterpreter();
        this.addProcess(new PlayerPositionProcess());
    }
    
    @Override
    public java.util.List getObjectsAtOffset(int dx, int dy, java.lang.Class cls)
    {
        return super.getObjectsAtOffset(dx, dy, cls);
    }
    
    public CommandInterpreter getController()
    {
        return controller;
    }
   
}
