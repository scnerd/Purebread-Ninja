import greenfoot.*;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.StringWriter;
import java.io.FileReader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Resource {
   public static String loadLevel(String fileName) {
      FileReader in = null;
      try {
         try {
            in = new FileReader(fileName);
            Scanner scan = new Scanner(in);
            StringWriter buf = new StringWriter();
            while (scan.hasNextLine()) {
               buf.append(scan.nextLine());
               buf.append('\n');
            }
            return buf.toString();
         } finally {
            if (in != null)
               in.close();
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
    public static GreenfootImage[] loadSpriteFrames(String imgFile, int width, int height, double scale)
    {
        Dimension size = new Dimension(width, height);
        GreenfootImage sheet = new GreenfootImage(imgFile);
        int imageWidth = sheet.getWidth();
        List<Point> points = new LinkedList<Point>();
        for (int x=0; x < imageWidth; x = x + width)
        {
            points.add(new Point(x, 0));
        }
        return loadFramesFromSheet(sheet, size, points, scale);
    }
    
    private static GreenfootImage[] loadFramesFromSheet(GreenfootImage sheet, Dimension size, List<Point> locations, double scale)
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
}
