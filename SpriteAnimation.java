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
    private GreenfootImage[] frames;
    private GreenfootImage[] flipped;
    
    public SpriteAnimation(String imageFile, int numFrames, int ticksPerFrame)
    {
        // Load the raw frames into a frame buffer
        GreenfootImage[] baseFrames = new GreenfootImage[numFrames];
        GreenfootImage baseImg = new GreenfootImage(imageFile);
        int frameWidth = baseImg.getWidth() / numFrames;
        int frameHeight = baseImg.getHeight();
        BufferedImage rawImg = baseImg.getAwtImage();
        for(int x = 0; x < numFrames; x++)
        {
            baseFrames[x] = new GreenfootImage(frameWidth, frameHeight);
            baseFrames[x].getAwtImage().setData(rawImg.getSubimage(x * frameWidth, 0, frameWidth, frameHeight).getData());
        }
        
        // Re-use object references to save from copying the images multiple times
        frames = new GreenfootImage[numFrames * ticksPerFrame];
        flipped = new GreenfootImage[numFrames * ticksPerFrame];
        for(int f = 0; f < numFrames; f++)
            for(int t = 0; t < ticksPerFrame; t++)
            {
                frames[f * ticksPerFrame + t] = baseFrames[f];
                flipped[f * ticksPerFrame + t] = new GreenfootImage(baseFrames[f]);
                flipped[f * ticksPerFrame + t].mirrorHorizontally();
            }
    }
    
    public GreenfootImage getFrame(int index, boolean flipFrame)
    { return flipFrame ? flipped[index] : frames[index]; }
    
    public int length()
    { return frames.length; }
    
    public static final int TICKS_PER_FRAME = 5;

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
    
    /*
    public SpriteAnimation(String imageFile, int width, int height)
    {
        
        this.setAnimation(frames, true);
    }
    */

    public SpriteAnimation(GreenfootImage[] frames, ActorProcess after)
    {
        super(after);
        this.setAnimation(frames, false);
    }

    public void setAnimation(GreenfootImage[] frames, boolean repeat)
    {
        this.frames = frames;
        this.repeat = repeat;
        this.restart();
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