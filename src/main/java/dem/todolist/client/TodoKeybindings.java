package dem.todolist.client;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import dem.todolist.core.Todo;

public class TodoKeybindings {

    public static KeyBinding openList;

    public static void RegisterKeys() {
        openList = new KeyBinding("key.todolist.list", Keyboard.KEY_PAUSE, Todo.NAME);

        ClientRegistry.registerKeyBinding(openList);
    }
}
