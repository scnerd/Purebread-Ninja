import greenfoot.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteAnimation extends ActorProcess
{
    public static GreenfootImage[] loadFramesFromSheet(GreenfootImage sheet,
        Dimension size, List<Point> locations)
    {
        int w = size.width;
        int h = size.height;
        int length = locations.size();
        GreenfootImage[] frames = new GreenfootImage[length];
        BufferedImage srcSheet = sheet.getAwtImage();
        Iterator<Point> itr = locations.iterator();
        for (int i=0; itr.hasNext(); i++)
        {
            Point p = itr.next();
            frames[i] =  new GreenfootImage(w, h);
            BufferedImage img = frames[i].getAwtImage();
            BufferedImage src = srcSheet.getSubimage(p.x, p.y, w, h);
            img.setData(src.getData());    
        }

        return frames;
    }

    public static GreenfootImage[] loadFramesLeftToRight(String imgFile,
        int frameWidth, int frameHeight)
    {
        Dimension size = new Dimension(frameWidth, frameHeight);
        GreenfootImage sheet = new GreenfootImage(imgFile);
        sheet.getWidth();
        List<Point> points = new LinkedList<Point>();
        for (int x=0; x < sheet.getWidth(); x = x + frameWidth)
        {
            points.add(new Point(x, 0));
        }
        return loadFramesFromSheet(sheet, size, points);
    }

    private GreenfootImage[] frames;
    private int duration;
    private int frameDuration;
    private int index;
    private long frameStartTime;
    private boolean isStarted = false;
    boolean repeat = true;

    public SpriteAnimation(GreenfootImage[] frames, int duration) {
        super();
        this.setAnimation(frames, duration);

    }

    public SpriteAnimation(GreenfootImage[] frames, int duration, ActorProcess after) {
        super(after);
        this.setAnimation(frames, duration);
        this.repeat = false;
    }

    public void setAnimation(GreenfootImage[] frames, int duration) {
        this.frames = frames;
        this.duration = duration;
        this.frameDuration = duration/frames.length;
        this.restart();
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - frameStartTime > frameDuration) {
            showNextFrame();
        }
    }

    @Override
    protected void onStart(){
        this.isStarted = true;
        restart();
    }

    private void showNextFrame() {
        this.frameStartTime = System.currentTimeMillis();
        this.owner.setImage(frames[index++ % frames.length]);
        if (frames.length < index && !this.repeat){
            this.success();
        }

    }

    public void restart() {
        if (this.isStarted) {
            this.index = 0;
            showNextFrame();
        }
    };
}