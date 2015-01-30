/**
 * Write a description of class CameraFollowProcess here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CameraFollowProcess extends ActorProcess 
{
    Map map;

    public CameraFollowProcess(Map map)
    {
        this.map = map;
    }
    public void run()
    {
        map.setCameraLocation(this.owner.getX()-4, -1*(this.owner.getY()) + 600);
    }
        
}
