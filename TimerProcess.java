public class TimerProcess extends ActorProcess {

    private int duration;
    private int count;
    public TimerProcess(int duration, ActorProcess after) {
        super(after);
        this.duration = duration;
    }

    /*
    @Override
    public void run() {
        if (count++ == duration) {
            success();
        }
    }

    */
   @Override
   public void run() { }
}