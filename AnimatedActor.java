import greenfoot.*;
import purebreadninja.*;

import java.awt.Point;
import java.awt.Dimension;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Arrays;

/**
 * Write a description of class AnimatedActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class AnimatedActor extends VisibleActor
{   
    protected SpriteAnimation currentAnimation;
    protected boolean flipFrames = false; // Right, true for Left
    private int animationIndex;
    
    protected void setCurrentAnimation(SpriteAnimation newAnimation)
    {
        if (newAnimation == null)
        {
            if (getImage() != null)
            {
                return;
            }
            newAnimation = SpriteAnimation.ERROR_ANIMATION;
        }
        currentAnimation = newAnimation;
        animationIndex = -1;
        displayImage();
    }
    
    @Override
    public void act()
    {
        displayImage();
    }
    
    public void displayImage()
    {
        GreenfootImage frame = currentAnimation.getFrame(++animationIndex % currentAnimation.length(), flipFrames);
        if (frame != this.getImage())
        {
            // This method is slow
            this.setImage(frame);
        }
    }
}
