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
    protected GreenfootImage[][] standFrames;
    protected GreenfootImage[] leftStandFrames;
    
    public Player()
    {
        this.setImage("images/BreadNinjaPlaceholder.png");
        this.standFrames = new GreenfootImage[2][];
        this.standFrames[0] = Resource.loadSpriteFrames("images/BreadNinjaSpriteSmall.png", 32, 26, 1);
        this.standFrames[1] = SpriteAnimation.flipFrames(this.standFrames[0]);
        this.animation = new SpriteAnimation(standFrames[0]);
        this.addProcess(this.animation);
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
    
    public void faceLeft()
    {
        this.animation.setAnimation(this.standFrames[1], true);        
    }
    
    public void faceRight()
    {
        this.animation.setAnimation(this.standFrames[0], true);
        
    }
   
}
