import greenfoot.*;

/**
 * Write a description of class MenuNavButton here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MenuNavButton extends TextButton
{
    private Class<? extends CameraViewableWorld> menu;
    public MenuNavButton(String text, Class<? extends CameraViewableWorld> menu)
    {
        super(text);
        this.menu = menu;
    }
    
    @Override
    public void onClick()
    {
        try
        {
            Greenfoot.setWorld(menu.getConstructor().newInstance());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }   
}
