public class TimerProcess extends ActorProcess {

    private long duration;
    private long start;
    public TimerProcess(long duration, ActorProcess after) {
        super(after);
        this.duration = duration;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - start > duration) {
            success();
        }
    }

    @Override
    protected void onStart(){
        this.start = System.currentTimeMillis();
    }

}