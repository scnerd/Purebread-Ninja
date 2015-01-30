/**
 * Write a description of class InputProcess here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface CommandInterpreter
{
    public enum Command
    {
        LEFT,
        RIGHT,
        UP
    }
        
    public boolean commandLeft();
    public boolean commandRight();
    public boolean commandUp();
}
