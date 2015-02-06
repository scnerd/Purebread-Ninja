import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HealthDisplay extends VisibleActor
{
    private CameraViewableWorld world;
    private int x_offset;
    private int y_offset;
    private int old_health = 5;
    
    public HealthDisplay()
    {
        this.setImage(new GreenfootImage("BreadNinjaHealth1.png"));
    }
    
    public void act() 
    {
        if (world == null)
        {
            world = (CameraViewableWorld)getWorld();
            x_offset = world.getWidth() / 2 - 60;
            y_offset = world.getHeight() / 2 - 60;
        }
        int health = ((Player)getWorld().getObjects(Player.class).get(0)).health;
        if (old_health != health)
        {
            old_health = health;
            
            switch (health) {
                case 5: this.setImage(new GreenfootImage("BreadNinjaHealth1.png"));
                        break;
                case 4: this.setImage(new GreenfootImage("BreadNinjaHealth2.png"));
                        break;
                case 3: this.setImage(new GreenfootImage("BreadNinjaHealth3.png"));
                        break;
                case 2: this.setImage(new GreenfootImage("BreadNinjaHealth4.png"));
                        break;
                case 1: this.setImage(new GreenfootImage("BreadNinjaHealth5.png"));
                        break;
            }
        }
        
        
        setLocation(world.getCameraX() - x_offset, world.getCameraY() - y_offset);
    }    
}
