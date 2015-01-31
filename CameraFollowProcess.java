/**
 * Write a description of class CameraFollowProcess here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CameraFollowProcess extends ActorProcess 
{
    CameraViewableWorld world;
    int count;
    int waitFrames = 2;

    public CameraFollowProcess(CameraViewableWorld world)
    {
        this.world = world;
    }
    
    public void run()
    {
        this.world.setCameraLocation(this.owner.getX(), this.owner.getY());
    }
        
}
