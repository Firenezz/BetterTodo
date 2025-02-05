package com.demel.todo.handlers;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.party.IParty;
import betterquesting.api.questing.party.IPartyDatabase;
import betterquesting.api2.storage.DBEntry;
import betterquesting.core.BetterQuesting;
import betterquesting.network.handlers.NetNameSync;
import betterquesting.questing.party.PartyManager;
import betterquesting.storage.NameCache;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.demel.todo.core.Tags;
import com.demel.todo.core.Todo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.integrated.IntegratedServer;

import java.util.ArrayDeque;
import java.util.UUID;

import static betterquesting.api.api.ApiReference.PARTY_DB;

/// Event handling for standard quests and core BetterQuesting functionality
public class EventHandler {

    public static final EventHandler INSTANCE = new EventHandler();

    private final ArrayDeque<EntityPlayerMP> opQueue = new ArrayDeque<>();
    private boolean openToLAN = false;

    private static Thread serverThread = null;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (serverThread == null) serverThread = Thread.currentThread();

            /*
             * synchronized (serverTasks) {
             * while (!serverTasks.isEmpty()) serverTasks.poll()
             * .run();
             * }
             */

            return;
        }

        MinecraftServer server = MinecraftServer.getServer();

        IPartyDatabase test = QuestingAPI.getAPI(PARTY_DB);

        if (!server.isDedicatedServer()) {
            boolean tmp = openToLAN;
            openToLAN = server instanceof IntegratedServer && ((IntegratedServer) server).getPublic();
            if (openToLAN && !tmp) opQueue.addAll(server.getConfigurationManager().playerEntityList);
        } else if (!openToLAN) {
            openToLAN = true;
        }

        while (!opQueue.isEmpty()) {
            EntityPlayerMP playerMP = opQueue.poll();
            DBEntry<IParty> party = test.getParty(QuestingAPI.getQuestingUUID(playerMP));
            if (party != null) {
                Todo.LOG.debug("Party id is {}", party.getID());
            }
        }
    }
}
