package todolist.core.proxies;

import net.minecraftforge.client.ClientCommandHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import todolist.client.TodoKeybindings;
import todolist.commands.ListTasksCommand;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ClientCommandHandler.instance.registerCommand(new ListTasksCommand());
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();

        TodoKeybindings.RegisterKeys();
    }
}
