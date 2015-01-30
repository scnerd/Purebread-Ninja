import greenfoot.*;

public class RemoveActorsProcess extends ActorProcess
{
    Actor[] actors;
    public RemoveActorsProcess(Actor...actors)
    {
        this.actors = actors;
    }

    /*
    @Override
    public void run() {
        success();
        for (Actor a : actors)
        {
            if (a != null && a.getWorld() != null)
                a.getWorld().removeObject(a);
        }
    }

    */
   @Override
   public void run() { }
}