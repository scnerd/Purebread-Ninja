import greenfoot.*;

/**
 * Write a description of class MainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainMenu extends CameraViewableWorld
{
    private static int MID_LINE_SEPARATION = 60;

    public MainMenu()
    {
        super(800, 600);
        int x = this.getWidth()/2;
        int y = this.getHeight()/3;
        addObject(new LevelText("Purebread Ninja", 100), x, y/2);
        addObject(new MenuNavButton("Start", LevelMenu.class), x, y*2);

    }
}
