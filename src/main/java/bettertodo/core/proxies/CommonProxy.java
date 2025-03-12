package bettertodo.core.proxies;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import bettertodo.api.api.ApiReference;
import bettertodo.api.api.BetterTodoAPI;
import bettertodo.commands.ClearDatabasesCommand;
import bettertodo.commands.NewTaskCommand;
import bettertodo.core.Config;
import bettertodo.core.Tags;
import bettertodo.core.Todo;
import bettertodo.handlers.EventHandler;
import bettertodo.handlers.GuiHandler;
import bettertodo.handlers.PersistenceHandler;
import bettertodo.network.PacketTypeRegistry;
import bettertodo.todo.TaskDatabase;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        Todo.LOG.info("I am TODO at version " + Tags.VERSION);

        registerHandlers();

        PacketTypeRegistry.INSTANCE.registerHandlers();

        BetterTodoAPI.registerAPI(ApiReference.TASK_DB, TaskDatabase.INSTANCE);
        FMLCommonHandler.instance()
            .bus()
            .register(EventHandler.INSTANCE); // TODO: use this maybe in the persistence
    }

    public void registerHandlers() {
        BetterTodoAPI.getLogger()
            .debug("Registering handlers");
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
