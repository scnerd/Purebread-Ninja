import greenfoot.*;
import java.awt.Color;

public abstract class TextButton extends VisibleActor
{
    protected int fontSize = 48;
    protected Color fontColor =  Color.BLACK;
    protected Color mouseOverFontColor =  Color.ORANGE;
    protected Color backgroundColor = new Color(0, 0, 0, 0);
    protected Color fontOutline;
    protected String text;
    protected int x;
    protected int y;
    
    private GreenfootImage img;
    private GreenfootImage mouseOverImg;
    
    protected MouseInfo mouse;
    
    public TextButton(String text)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        
        createImage();
    }
    
    public TextButton()
    {
        this.x = x;
        this.y = y;
    }
    
    protected void createImage()
    {
        this.img = new GreenfootImage(text, fontSize, fontColor, backgroundColor, fontOutline);
        this.setImage(img);
        this.mouseOverImg = new GreenfootImage(text, fontSize, mouseOverFontColor, backgroundColor, fontOutline);
    }
    
 
    
    @Override
    public void act() 
    {
        if(Greenfoot.mousePressed(this))
        {
            this.onClick();
        }
        mouse = Greenfoot.getMouseInfo();
        if (mouse != null && this.getWorld().getObjectsAt(mouse.getX(), mouse.getY(), null).contains(this))
        {
            this.setImage(mouseOverImg);
        }
        else
        {
            this.setImage(img);
        }

    }
    
    public abstract void onClick();
}
