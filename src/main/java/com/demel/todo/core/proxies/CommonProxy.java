package com.demel.todo.core.proxies;

import com.demel.todo.handlers.EventHandler;
import net.minecraftforge.common.MinecraftForge;

import com.demel.todo.core.Config;
import com.demel.todo.core.Tags;
import com.demel.todo.core.Todo;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        Todo.LOG.info(Config.greeting);
        Todo.LOG.info("I am TODO at version " + Tags.VERSION);

        MinecraftForge.EVENT_BUS.register(EventHandler.INSTANCE);
        FMLCommonHandler.instance()
            .bus()
            .register(EventHandler.INSTANCE);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        Todo.LOG.info("Server starting");
    }
}
