package bettertodo.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bettertodo.core.proxies.CommonProxy;
import bettertodo.network.NetworkMessage;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Todo.MODID, version = Todo.VERSION, name = Todo.NAME, acceptedMinecraftVersions = "[1.7.10]")
@SuppressWarnings("unused")
public class Todo {

    public static final String MODID = "bettertodo";
    public static final String NAME = "BetterTodo";
    public static final String PACKAGE_ROOT = "bettertodo";
    public static final String VERSION = betterquesting.core.Tags.VERSION;
    public static final String PROXY = PACKAGE_ROOT + ".core.proxies";
    public static final String CHANNEL = "BT_NET_CHAN";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(MODID)
    public static Todo INSTANCE;
    public SimpleNetworkWrapper network;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);

        proxy.preInit(event);

        network.registerMessage(NetworkMessage.HandleClient.class, NetworkMessage.class, 0, Side.CLIENT);
        network.registerMessage(NetworkMessage.HandleServer.class, NetworkMessage.class, 0, Side.SERVER);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppedEvent event) {

    }
}
