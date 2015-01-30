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
    public static final int TICKS_PER_FRAME = 5;
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
    private int counter;
    private int index;
    private boolean isStarted = false;
    boolean repeat = true;

    public SpriteAnimation(GreenfootImage[] frames) {
        super();
        this.setAnimation(frames);

    }
    public SpriteAnimation(String spriteImage, int width, int height) {
        super();
        this.setAnimation(loadFramesLeftToRight(spriteImage, width, height));
        
    }

    public SpriteAnimation(GreenfootImage[] frames, ActorProcess after) {
        super(after);
        this.setAnimation(frames);
        this.repeat = false;
    }

    public void setAnimation(GreenfootImage[] frames) {
        this.frames = frames;
        this.restart();
    }

    @Override
    public void run() {
        if (++this.counter % TICKS_PER_FRAME == 0) {
            showNextFrame();
        }
    }

    @Override
    protected void onStart(){
        this.isStarted = true;
        restart();
    }

    private void showNextFrame() {
        this.owner.setImage(frames[index++ % frames.length]);
        if (frames.length < index && !this.repeat){
            this.success();
        }

    }

    public void restart() {
        if (this.isStarted) {
            this.counter = 0;
            this.index = 0;
            showNextFrame();
        }
    };
}