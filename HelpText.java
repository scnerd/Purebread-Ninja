import greenfoot.*;

/**
 * Write a description of class HelpText here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HelpText extends LevelText
{
    public HelpText(String message)
    { super(message, 22); }
    
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
    { public HelpTextGrapple() { super("Hold SHIFT to throw your grappling hook"); } }
    static class HelpTextWallJump extends HelpText
    { public HelpTextWallJump() { super("Jump\nagainst\nwall\nto\nget\nhigher"); } }
    static class HelpTextNext extends HelpText
    { public HelpTextNext() { super("Carry on!"); } }
}
