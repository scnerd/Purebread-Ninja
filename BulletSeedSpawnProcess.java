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

    /**
     * Constructor for objects of class BulletSeedSpawnProcess
     */
    public BulletSeedSpawnProcess()
    {
    }

    @Override
    public void run()
    {
        if (wait_time > 0) {
            wait_time--;
        }
        else {
            // shoot
            try {
                this.owner.getWorld().addObject(BulletSeed.class.getConstructor().newInstance(), this.owner.getX(), this.owner.getY());
            } catch(NoSuchMethodException ex) {
            } catch(InstantiationException ex) {
            } catch(IllegalAccessException ex) {
            } catch(IllegalArgumentException ex) {
            } catch(Exception ex) {
            }
            wait_time = default_wait_time;
        }
    }
}
