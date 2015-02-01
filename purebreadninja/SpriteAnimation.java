package purebreadninja;

import greenfoot.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteAnimation
{
    private static final class ErrorAnimation extends SpriteAnimation
    {
        public GreenfootImage getFrame(int index, boolean flipFrame)
        {
            return SpriteAnimation.ERROR_IMAGE; 
        }
    
        public int length()
        {
            return 1;
        }
        
    }
    
    protected static final GreenfootImage ERROR_IMAGE = new GreenfootImage("images/error.png");
    
    public static final int DEFAULT_TICKS_PER_FRAME = 4;
    
    public static final SpriteAnimation ERROR_ANIMATION = new ErrorAnimation();
    
    protected GreenfootImage[] frames;
    protected GreenfootImage[] flipped;
    
    public SpriteAnimation(String imageFile, int numFrames, int ticksPerFrame)
    {
        try
        {
            // Load the raw frames into a frame buffer
            GreenfootImage baseImg = new GreenfootImage(imageFile);
            GreenfootImage[] baseFrames = new GreenfootImage[numFrames];
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
            {
                for(int t = 0; t < ticksPerFrame; t++)
                {
                    frames[f * ticksPerFrame + t] = baseFrames[f];
                    flipped[f * ticksPerFrame + t] = new GreenfootImage(baseFrames[f]);
                    flipped[f * ticksPerFrame + t].mirrorHorizontally();
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            frames = flipped = new GreenfootImage[1];
            frames[0] = ERROR_IMAGE;
        }
    }
    
    public SpriteAnimation(String imageFile, int numFrames)
    {
        this(imageFile, numFrames, DEFAULT_TICKS_PER_FRAME);
    }
    
    protected SpriteAnimation(){ }
    
    public GreenfootImage getFrame(int index, boolean flipFrame)
    {
        return flipFrame ? flipped[index] : frames[index];
    }
    
    public int length()
    {
        return frames.length;
    }
}