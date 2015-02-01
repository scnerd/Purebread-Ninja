import greenfoot.*;
import greenfoot.util.*;
import java.awt.geom.Point2D;

public class MenuNavButton extends TextButton
{
    private static final int SPEED = 30;
    
    private double theta;
    private Point2D.Double velocity;
    private int numSteps;
    private int count;
    private boolean moving;
    Point2D.Double menuLocation;
    
    public MenuNavButton(String text, Point2D.Double menuLocation)
    {
        super(text);
        this.menuLocation = menuLocation;
        this.velocity = new Point2D.Double();
    }
    
    private void setupManeuver(Point2D.Double start, Point2D.Double end)
    {
        double dy = end.y - start.y;
        double dx = end.x - start.x;
        this.numSteps = (int) Math.round(Math.sqrt(dx*dx + dy*dy)/SPEED);
        this.theta = Math.atan2(dy, dx);
        velocity.x = SPEED*Math.cos(theta);
        velocity.y = SPEED*Math.sin(theta);
        count = 0;
        moving = true;
    }
    
    @Override
    public void onClick()
    {
        Point2D.Double currentLocation = new Point2D.Double(world.getCameraX(), world.getCameraY());
        setupManeuver(currentLocation, this.menuLocation);
    }
    
    private void updateManeuver()
    {
        if (++count < numSteps)
        {
            int x = (int) Math.round(world.getCameraX() + velocity.x);
            int y = (int) Math.round(world.getCameraY() + velocity.y);
            moveCamera(x, y);
        }
        else
        {
            moveCamera((int) menuLocation.x, (int)menuLocation.y);
            moving = false; 
        }
    }
    
    @Override
    public void act() 
    {
        if (moving)
        {
            updateManeuver();
        }
        else
        {
            super.act();
        }
        
    }
}