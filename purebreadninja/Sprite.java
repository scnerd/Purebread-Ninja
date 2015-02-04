package purebreadninja;

import greenfoot.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.LinkedList;


public class Sprite extends SpriteAnimation
{
    private static final int MIN_FRAME_WIDTH = 24;
    private static final int MAX_FRAME_WIDTH = 48;
    private static final IllegalArgumentException FRAME_WIDTH_ERROR = new IllegalArgumentException();
    
    public static Sprite ImageSheet(String file)
    {
        return new Sprite(file);
    }
    
    public Sprite(String imageFile, int ticksPerFrame)
    {
      try
        {
            // Load the raw frames into a frame buffer
            GreenfootImage baseImg = new GreenfootImage(imageFile);
            if (baseImg == null)
            {
                System.err.println("img file null");
                throw new RuntimeException();
            }
            LinkedList<GreenfootImage> baseFrames = new LinkedList<GreenfootImage>();

            int imgWidth = baseImg.getWidth();
            int frameWidth = MAX_FRAME_WIDTH;
            int frameHeight = baseImg.getHeight();
            while(imgWidth  % frameWidth != 0)
            {
                --frameWidth;
            }
            
            if (frameWidth < MIN_FRAME_WIDTH)
            {
                throw FRAME_WIDTH_ERROR;
            }
            
            BufferedImage rawImg = baseImg.getAwtImage();
            for(int x = 0; x*frameWidth < imgWidth; x++)
            {
                GreenfootImage tmp = new GreenfootImage(frameWidth, frameHeight);
                tmp.getAwtImage().setData(rawImg.getSubimage(x * frameWidth, 0, frameWidth, frameHeight).getData());
                baseFrames.add(tmp);                
            }
            
            // Re-use object references to save from copying the images multiple times
            frames = new GreenfootImage[baseFrames.size() * ticksPerFrame];
            flipped = new GreenfootImage[baseFrames.size() * ticksPerFrame];
            for(int f = 0; !baseFrames.isEmpty(); f++)
            {
                GreenfootImage tmp = baseFrames.pop();
                for(int t = 0; t < ticksPerFrame; t++)
                {

                    frames[f * ticksPerFrame + t] = tmp;
                    flipped[f * ticksPerFrame + t] = new GreenfootImage(tmp);
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
    
    public Sprite(String imageFile)
    {
        this(imageFile, SpriteAnimation.DEFAULT_TICKS_PER_FRAME);
    }
}
