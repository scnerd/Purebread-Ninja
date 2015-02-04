/**
 * Write a description of class BulletSeedSpawnProcess here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulletSeedSpawnProcess extends ActorProcess 
{
    // instance variables - replace the example below with your own
    private int default_wait_time = 120;
    private int wait_time = 0;

    public BulletSeedSpawnProcess()
    {
    }

    /*
    @Override
    public void run()
    {
        if (wait_time > 0) {
            wait_time--;
        }
        else {
            // shoot
            try
            {
                BulletSeed bullet = BulletSeed.class.getConstructor().newInstance();
                this.owner.getWorld().addObject(bullet, this.owner.getX(), this.owner.getY());
            } catch(Exception ex)
            {
            }
            wait_time = default_wait_time;
        }
    }
    */
   @Override
   public void run() { }
}
