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
    private LinkedList<ActorProcess> processes = new LinkedList<ActorProcess>();
    private LinkedList<ActorProcess> toAdd = new LinkedList<ActorProcess>();

    public void addProcess(ActorProcess p)
    {
        toAdd.push(p);
        p.setOwner(this);
    }

    public void act() 
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
