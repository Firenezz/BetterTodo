package dem.todolist.core.proxies;

import cpw.mods.fml.common.network.NetworkRegistry;
import dem.todolist.api.api.ApiReference;
import dem.todolist.commands.ClearDatabasesCommand;
import dem.todolist.commands.NewTaskCommand;
import dem.todolist.handlers.GuiHandler;
import dem.todolist.handlers.PersistenceHandler;
import dem.todolist.todo.TaskDatabase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dem.todolist.api.api.TodoAPI;
import dem.todolist.core.Config;
import dem.todolist.core.Tags;
import dem.todolist.core.Todo;
import dem.todolist.handlers.EventHandler;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        Todo.LOG.info("I am TODO at version " + Tags.VERSION);

        registerHandlers();

        TodoAPI.registerAPI(ApiReference.TASK_DB, TaskDatabase.INSTANCE);
        FMLCommonHandler.instance()
            .bus()
            .register(EventHandler.INSTANCE); // TODO: use this maybe in the persistence
    }

    public void registerHandlers(){
        TodoAPI.getLogger().debug("Registering handlers");
        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(PersistenceHandler.INSTANCE);

        NetworkRegistry.INSTANCE.registerGuiHandler(Todo.INSTANCE, new GuiHandler());
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        Todo.LOG.info("Loading infos");
        PersistenceHandler.INSTANCE.serverStart(event);

        MinecraftServer server = event.getServer();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager manager = (ServerCommandManager) command;

        manager.registerCommand(new NewTaskCommand());
        manager.registerCommand(new ClearDatabasesCommand());
    }

    public boolean isClient() {
        return false;
    }
}
