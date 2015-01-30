import greenfoot.*;


public class ProjectilePositionProcess extends ActorProcess
{
    private Projectile projectile;
    private double velocityX;
    private double velocityY;
    
    public ProjectilePositionProcess(double velocityX, double velocityY)
    {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    /*
    @Override
    protected void onStart()
    {
        if (Projectile.class.isAssignableFrom(this.owner.getClass()))
        {
            this.projectile = (Projectile) this.owner;
            if (this.child == null)
            {
                this.child = new RemoveActorsProcess(this.owner);
            }
        }
        else
        {
            this.abort();
        }
    }

    @Override
    public void run()
    {
        this.projectile.setVelocity(velocityX, velocityY);
        this.projectile.updatePosition();
        if (this.owner.getX() == 0 || this.owner.getX() == this.owner.getWorld().getWidth()) {
            success();
        }
        else if (this.owner.getY() == 0 || this.owner.getY() == this.owner.getWorld().getHeight()) {
            success();
        }
    }

    */
   @Override
   public void run() { }
}