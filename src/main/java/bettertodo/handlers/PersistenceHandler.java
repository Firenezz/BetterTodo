package bettertodo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;

import betterquesting.core.BetterQuesting;
import bettertodo.core.BetterTodoSettings;
import bettertodo.handlers.persistence.DatabasePersistence;
import bettertodo.utils.BTScheduledJob;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PersistenceHandler {

    public static PersistenceHandler INSTANCE = new PersistenceHandler();

    public void saveData() {
        List<Future<Void>> allFutures = new ArrayList<>(5);

        allFutures.addAll(DatabasePersistence.INSTANCE.saveDatabases());

        for (Future<Void> future : allFutures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                BetterQuesting.logger.warn("Saving interrupted!", e);
            } catch (ExecutionException e) {
                BetterQuesting.logger.warn("Saving failed!", e.getCause());
            }
        }
    }

    public void unload() {
        DatabasePersistence.INSTANCE.unloadDatabases();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        if (!event.world.isRemote && BetterTodoSettings.curWorldDir != null && event.world.provider.dimensionId == 0) {
            PersistenceHandler.INSTANCE.saveData();
        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Load event) {
        if (!event.world.isRemote && BetterTodoSettings.curWorldDir != null && event.world.provider.dimensionId == 0) {
            BTScheduledJob.SCHEDULED_JOB.init();
            DatabasePersistence.INSTANCE.loadDatabases();
        }
    }

    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        BTScheduledJob.SCHEDULED_JOB.init();

        DatabasePersistence.INSTANCE.initFiles(server);
        DatabasePersistence.INSTANCE.loadDatabases();
    }

    public void serverStop(FMLServerStoppedEvent event) {
        BTScheduledJob.SCHEDULED_JOB.shutdown();

        unload();
    }
}
