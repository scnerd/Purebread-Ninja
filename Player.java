import greenfoot.*;
import java.util.List;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Player extends Character
{
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    
    protected HashMap<PlayerView, GreenfootImage[][]> views;
    protected PlayerView currentView;
    protected CommandInterpreter controller;
    protected SpriteAnimation animation;
    
    public Player()
    {
        this.setImage("images/BreadNinjaStanding.png");
        initPlayerViews();
        this.animation = new SpriteAnimation(views.get(PlayerView.RUNNING)[RIGHT]);
        setCurrentView(PlayerView.RUNNING);
        this.addProcess(this.animation);
        this.controller = new KeyboardInterpreter();
        this.addProcess(new PlayerPositionProcess());
        
    }
    
    public CommandInterpreter getController()
    {
        return controller;
    }
    
    public void setCurrentView(PlayerView currentView)
    {
        if (this.currentView != currentView)
        {
            this.currentView = currentView;
            this.animation.setAnimation(views.get(currentView)[RIGHT], true);
        }
    }
    
    public void faceLeft()
    {
        this.animation.setAnimation(this.views.get(currentView)[LEFT], true);        
    }

    public void faceRight()
    {
        this.animation.setAnimation(this.views.get(currentView)[RIGHT], true);
        
    }
    
    private void initPlayerViews()
    {
        views = new HashMap<PlayerView, GreenfootImage[][]>();
        for (PlayerView v : PlayerView.values())
        {
            views.put(v, new GreenfootImage[2][]);
        }
        initView(PlayerView.RUNNING, Resource.loadSpriteFrames("images/BreadNinjaSpriteSmall.png", 32, 26, 1));
        initView(PlayerView.JUMPING, Resource.loadSpriteFrames("images/BreadNinjaSpriteJumpSmall.png", 32, 26, 1));
        initView(PlayerView.STANDING, Resource.loadSpriteFrames("images/BreadNinjaSpriteStandSmall.png", 32, 26, 1));
        
    }
    
    private void initView(PlayerView v, GreenfootImage[] frames)
    {
        views.get(v)[RIGHT] = frames;
        views.get(v)[LEFT] = SpriteAnimation.flipFrames(frames);
        
    }
    
    public Actor publicGetOneObjectAtOffset(int dx, int dy, java.lang.Class cls)
    {
        return super.getOneObjectAtOffset(dx, dy, cls);
    }
    
    public List getObjectsAtOffset(int dx, int dy, java.lang.Class cls)
    {
        return super.getObjectsAtOffset(dx, dy, cls);
    }
    
    @Override
    public void addedToWorld(World world)
    {
        super.addedToWorld(world);
        if (CameraViewableWorld.class.isAssignableFrom(world.getClass()))
        {
            //((CameraViewableWorld)world).player = this;
            this.addProcess(new CameraFollowProcess((CameraViewableWorld) world));
        }
    }
}
