import greenfoot.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.awt.Point;
import java.awt.Dimension;
import java.util.Arrays;

/**
 * Write a description of class AnimatedActor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class AnimatedActor extends Actor
{
    private final int ANIMATION_SPEED = 3;
    
    private int animationIndex = 0;
    protected SpriteAnimation currentAnimation;
    protected boolean flipFrames = false; // Right, true for Left
    
    protected void setCurrentAnimation(SpriteAnimation newAnimation)
    {
        currentAnimation = newAnimation;
        animationIndex = 0;
        displayCurrentFrame();
    }
    
    public void act()
    {
        //performProcesses();
        animationIndex = ++animationIndex % currentAnimation.length();
        displayCurrentFrame();
    }
    
    private void displayCurrentFrame()
    {
        GreenfootImage newFrame = currentAnimation.getFrame(animationIndex, flipFrames);
        setImage(newFrame);
    }
    
    private LinkedList<ActorProcess> processes = new LinkedList<ActorProcess>();
    private LinkedList<ActorProcess> toAdd = new LinkedList<ActorProcess>();

    public void addProcess(ActorProcess p)
    {
        toAdd.push(p);
        p.setOwner(this);
    }

    private void performProcesses() 
    {
        // Add new processes
        processes.addAll(toAdd);
        toAdd.clear();

        // Update processes
        ListIterator<ActorProcess> itr = processes.listIterator();
        while(itr.hasNext())
        {
            ActorProcess p = itr.next();
            if (!p.isDone())
            {
                p.update();
            }
            
            if (p.isDone())
            {
                if(p.hasChild())
                {
                    itr.set(p.getChild());
                }
                else
                {
                    itr.remove();
                }
            }
                
        }
    }
}
