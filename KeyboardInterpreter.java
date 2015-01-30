import greenfoot.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class KeyboardInterpreter implements CommandInterpreter 
{
    private HashMap<CommandInterpreter.Command, ArrayList<String>> keyBindings;
    
    public KeyboardInterpreter()
    {
        this.keyBindings = new HashMap<CommandInterpreter.Command, ArrayList<String>>();
        for (CommandInterpreter.Command cmd : Command.values())
        {
            this.keyBindings.put(cmd, new ArrayList<String>());
        }
        loadDefaults();
    }
    
    public void bind(CommandInterpreter.Command cmd, String key) {
        unbind(key);
        keyBindings.get(cmd).add(key);
    }
    
    public boolean commandLeft()
    {
        return checkCommandKeys(Command.LEFT);
    }
    
    public boolean commandRight()
    {
        return checkCommandKeys(Command.RIGHT);
    }
    
    public boolean commandUp()
    {
        return checkCommandKeys(Command.UP);
    }
    
    public void unbind(String key) {
        for (CommandInterpreter.Command cmd : Command.values())
        {
            ArrayList<String> list = keyBindings.get(cmd);
            list.remove(key);
        }
    }
    
    public void loadDefaults() {
        clearKeyBindings();
        keyBindings.get(Command.LEFT).addAll(Arrays.asList("a", "left"));
        keyBindings.get(Command.RIGHT).addAll(Arrays.asList("d", "right"));
        keyBindings.get(Command.UP).addAll(Arrays.asList("w", "up", "space"));
    }
    
    public void clearKeyBindings() {
        for (CommandInterpreter.Command cmd : Command.values())
        {
            keyBindings.get(cmd).clear();
        }
    }
    
    private boolean checkCommandKeys(CommandInterpreter.Command cmd)
    {
        for (String key : keyBindings.get(cmd))
        {
            if (Greenfoot.isKeyDown(key))
            {
                return true;
            }
                
        }
        return false;
    }
}
