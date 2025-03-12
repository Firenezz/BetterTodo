package bettertodo.client;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import bettertodo.core.Todo;

public class TodoKeybindings {

    public static KeyBinding openList;

    public static void RegisterKeys() {
        openList = new KeyBinding("key.bettertodo.list", Keyboard.KEY_PAUSE, Todo.NAME);

        ClientRegistry.registerKeyBinding(openList);
    }
}
