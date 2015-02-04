import greenfoot.*;
import java.awt.Color;
import java.awt.Point;

public class GrapplingHook extends VisibleActor
{
    Platform platform;
    private double SPEED = 6;
    private double ANGLE = 60;
    private Color COLOR = Color.BLACK;
    
    private int direction;
    private double length = 0;
    private GreenfootImage img;
    private boolean isHooked = false;
    private Player parent;
    private Point target = null;
    private double initialVelocityX;
    
    public GrapplingHook(Player thrower, int direction)
    {
        this.parent = thrower;
        this.direction = direction;
        this.target = new Point(parent.getX(), parent.getY());
        this.initialVelocityX = parent.velocity.x;
    }
    
    public void act() 
    {
        int width, height;
        if(!isHooked)
        {
            target.x += direction * SPEED * Math.cos(ANGLE * Math.PI / 180) + this.initialVelocityX;
            target.y -= SPEED * Math.sin(ANGLE * Math.PI / 180);
            this.platform = (Platform) parent.publicGetOneObjectAtOffset(target.x - parent.getX(), target.y - parent.getY(), Platform.class);
            if(platform != null)
            {
                isHooked = true;
            }
        }
        width = Math.abs(target.x - parent.getX()) + 1;
        height = Math.abs(target.y - parent.getY()) + 1;
        
        int disp_direction = (target.x < parent.getX() ? -1 : 1) * (target.y < parent.getY() ? 1 : -1);
        img = new GreenfootImage(width, height);
        img.setColor(COLOR);
        img.drawLine((disp_direction < 0 ? width - 1 : 0), height - 1, (disp_direction < 0 ? 0 : width - 1), 0);
        setLocation((parent.getX() + target.x) / 2, (parent.getY() + target.y) / 2);
        setImage(img);
    }
    
    public boolean getIsHooked()
    {
        return isHooked;
    }
    
    public Point getHookTarget()
    {
        return isHooked ? target : null;
    }
}
