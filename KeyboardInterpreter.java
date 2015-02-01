import greenfoot.*;

import purebreadninja.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class KeyboardInterpreter implements CommandInterpreter 
{
    private HashMap<Command, ArrayList<String>> keyBindings;
    
    public KeyboardInterpreter()
    {
        this.keyBindings = new HashMap<Command, ArrayList<String>>();
        for (Command cmd : Command.values())
        {
            this.keyBindings.put(cmd, new ArrayList<String>());
        }
        loadDefaults();
    }
    
    public void bind(Command cmd, String key) {
        unbind(key);
        keyBindings.get(cmd).add(key);
    }
    
    public void unbind(String key) {
        for (Command cmd : Command.values())
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
        for (Command cmd : Command.values())
        {
            keyBindings.get(cmd).clear();
        }
    }
    
    public boolean check(Command cmd)
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
