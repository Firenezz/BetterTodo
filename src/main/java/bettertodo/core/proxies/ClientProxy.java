package bettertodo.core.proxies;

import net.minecraftforge.client.ClientCommandHandler;

import bettertodo.client.TodoKeybindings;
import bettertodo.commands.ListTasksCommand;
import cpw.mods.fml.common.event.FMLInitializationEvent;

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
