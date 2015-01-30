import greenfoot.*;

public class ProjectileMoveProcess extends ActorProcess
{
    Actor actor;
    
    public ProjectileMoveProcess(ActorProcess p)
    {
        super(p);
    }


    /*
    @Override
    public void run() {
        // Apply Velocity
        Projectile p = (Projectile) this.owner;
        p.velocity.x = (p.velocity.x < 0 ? -1 : 1) * Math.min(Math.abs(p.velocity.x), p.MAX_VELOCITY.x);
        p.velocity.y = (p.velocity.y < 0 ? -1 : 1) * Math.min(Math.abs(p.velocity.y), p.MAX_VELOCITY.y);
        p.position.x += p.velocity.x;
        p.position.y += p.velocity.y;

        p.setLocation((int)p.position.x, (int)p.position.y);
    }

    */
   @Override
   public void run() { }
}