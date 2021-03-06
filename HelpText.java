import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class HelpText here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HelpText extends LevelText
{
    public HelpText(String message)
    { super(message, 22, Color.WHITE); }
    
    static class HelpTextMove extends HelpText
    { public HelpTextMove() { super("A/D or LEFT/RIGHT to move"); } }
    static class HelpTextJump extends HelpText
    { public HelpTextJump() { super("UP/W/SPACE to jump"); } }
    static class HelpTextBurner extends HelpText
    { public HelpTextBurner() { super("Avoid the burner!"); } }
    static class HelpTextWall extends HelpText
    { public HelpTextWall() { super("Hold right to slide on wall"); } }
    static class HelpTextCeiling extends HelpText
    { public HelpTextCeiling() { super("Hold up to walk on the ceiling"); } }
    static class HelpTextGrapple extends HelpText
    { public HelpTextGrapple() { super("Hold SHIFT/J to throw your grappling hook"); } }
    static class HelpTextWallJump extends HelpText
    { public HelpTextWallJump() { super("Jump\nagainst\nwall\nto\nget\nhigher"); } }
    static class HelpTextNext extends HelpText
    { public HelpTextNext() { super("Carry on!"); } }
    
    static class HelpTextButter1 extends HelpText
    { public HelpTextButter1() { super("Surprise Butter Ronin by running into him from behind"); } }
    static class HelpTextButter2 extends HelpText
    { public HelpTextButter2() { super("Get behind them before they notice!"); } }
    static class HelpTextJam1 extends HelpText
    { public HelpTextJam1() { super("Maybe you could get\naround Jam Fisher"); } }
    static class HelpTextJam2 extends HelpText
    { public HelpTextJam2() { super("           ...then strike as he turns away!\nWait here...                               "); } }
    static class HelpTextPanini1 extends HelpText
    { public HelpTextPanini1() { super("Slam Panini Sumo Presser\ndown from above!"); } }
    
}
