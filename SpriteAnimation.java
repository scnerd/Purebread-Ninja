import greenfoot.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteAnimation extends ActorProcess
{
    public static final int TICKS_PER_FRAME = 3;

    private GreenfootImage[] frames;
    private int counter;
    private int index;
    private boolean isStarted = false;
    private boolean repeat;
    
    public static GreenfootImage[] flipFrames(GreenfootImage[] frames)
    {
        GreenfootImage[] result = new GreenfootImage[frames.length];
        for (int i = 0; i < frames.length; i++)
        {
            result[i] = new GreenfootImage(frames[i]);
            result[i].mirrorHorizontally();
        }
        return result;
    }

    public SpriteAnimation(GreenfootImage[] frames)
    {
        this.setAnimation(frames, true);
    }
    
    public SpriteAnimation(String imageFile, int width, int height)
    {
        
        this.setAnimation(frames, true);
    }

    public SpriteAnimation(GreenfootImage[] frames, ActorProcess after)
    {
        super(after);
        this.setAnimation(frames, false);
    }

    public void setAnimation(GreenfootImage[] frames, boolean repeat)
    {
        if (frames != null && this.frames != frames)
        {
            this.frames = frames;
            this.repeat = repeat;
            this.restart();
        }
    }

    @Override
    protected void onStart()
    {
        this.isStarted = true;
        restart();
    }
    
    @Override
    public void run()
    {
        if (++this.counter % TICKS_PER_FRAME == 0)
        {
            showNextFrame();
        }
    }

    private void showNextFrame()
    {
        this.owner.setImage(frames[index++ % frames.length]);
        if (frames.length < index && !this.repeat)
        {
            this.success();
        }

    }

    public void restart() {
        if (this.isStarted)
        {
            this.counter = 0;
            this.index = 0;
            showNextFrame();
        }
    }
}