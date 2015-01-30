import greenfoot.*;

public abstract class ActorProcess
{
    public enum Status
    {
        NOT_STARTED,
        STARTING,
        RUNNING,
        SUCCESS,
        FAILED,
        ABORTED,
        WAITING
    }

    protected ActorProcess child;
    protected Actor owner;
    
    private Status state = Status.NOT_STARTED;

    abstract void run();

    public ActorProcess()
    {
    }

    public ActorProcess(ActorProcess child)
    {
        this.child = child;
    }

    public void setOwner(Actor owner){
        this.owner = owner;
    }

    public final void update()
    {
        if (this.state == Status.NOT_STARTED)
        {
            this.state = Status.STARTING;
            this.onStart();
        }

        if (this.state == Status.STARTING || this.state == Status.WAITING)
        {
            this.state = Status.RUNNING;
            this.run();
        }

        if (this.state == Status.RUNNING)
        {
            this.state = Status.WAITING;
        }
        else if (this.state == Status.SUCCESS)
        {
            this.onSuccess();
        }
        else if (this.state == Status.FAILED)
        {
            this.onFail();
            this.abortChildren();
        }
    }

    public boolean isDone()
    {
        switch(this.state)
        {
            case ABORTED:
            case FAILED:
            case SUCCESS:
                return true;
            default:
                return false;
        }
    }

    public boolean hasChild()
    {
        return this.state == Status.SUCCESS && this.child != null;
    }

    public ActorProcess getChild()
    {
        if (this.child != null)
        {
            this.child.setOwner(this.owner);
        }
        return this.child;
    }

    public final void abort()
    {
        if (!this.isDone()) {
            this.state = Status.ABORTED;
            this.onAbort();
            this.abortChildren();
        }
    }
    protected final void fail()
    {
        if (this.state != Status.RUNNING) {
            throwStateException();    
        }
        this.state = Status.FAILED;
    }
    protected final void success()
    {
        if (this.state != Status.RUNNING) {
            throwStateException();    
        }
        this.state = Status.SUCCESS;
    }
    
    protected void onStart(){}
    protected void onAbort(){}
    protected void onSuccess(){}
    protected void onFail(){}

    private void abortChildren()
    {
        if (this.child != null)
        {
            this.child.abort();
        }
    }

    private void throwStateException()
    {
        String error = "ActorProcess not running. Use abort() instead.";
        throw new IllegalStateException(error);
    }
}