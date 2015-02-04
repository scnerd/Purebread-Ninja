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
    
    public static Sprite ImageSheet(String file, int numFrames)
    {
        return new Sprite(file, numFrames);
    }
    
    public Sprite(String imageFile, int numFrames)
    {
       super(imageFile, numFrames);
    }
    
    public Sprite(String imageFile)
    {
        this(imageFile, SpriteAnimation.DEFAULT_TICKS_PER_FRAME);
    }
}
