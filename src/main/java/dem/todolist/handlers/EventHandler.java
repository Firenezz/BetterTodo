package dem.todolist.handlers;

import static betterquesting.api.api.ApiReference.PARTY_DB;

import java.util.ArrayDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import cpw.mods.fml.common.gameevent.PlayerEvent;
import dem.todolist.network.handlers.NetBulkSync;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;

import org.apache.commons.lang3.Validate;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.party.IParty;
import betterquesting.api.questing.party.IPartyDatabase;
import betterquesting.api2.storage.DBEntry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dem.todolist.client.TodoKeybindings;
import dem.todolist.client.gui.todolist.GuiTodoList;
import dem.todolist.core.Todo;

/// Event handling for standard quests and core BetterQuesting functionality
public class EventHandler {

    public static final EventHandler INSTANCE = new EventHandler();

    private final ArrayDeque<EntityPlayerMP> opQueue = new ArrayDeque<>();
    private boolean openToLAN = false;

    private static Thread serverThread = null;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKey(InputEvent.KeyInputEvent event) {
        if (TodoKeybindings.openList.isPressed()) {
            var mc = Minecraft.getMinecraft();

            if (mc.thePlayer.isSneaking() && mc.thePlayer.getCommandSenderName()
                .equalsIgnoreCase("Developer")) {
                mc.displayGuiScreen(new GuiTodoList(null)); // Testing purposes
            } else {
                mc.thePlayer.openGui(
                    Todo.INSTANCE,
                    GuiHandler.GUI_LIST,
                    mc.thePlayer.worldObj,
                    mc.thePlayer.serverPosX,
                    mc.thePlayer.serverPosY,
                    mc.thePlayer.serverPosZ);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.worldObj.isRemote || MinecraftServer.getServer() == null || !(event.player instanceof EntityPlayerMP entityPlayerMP)) return;

        if (Todo.proxy.isClient()
            && !MinecraftServer
                .getServer()
                .isDedicatedServer()
            && MinecraftServer.getServer()
                .getServerOwner()
                .equals(
                    event.player.getGameProfile()
                        .getName())) {
            return;
        }

        NetBulkSync.sendReset(entityPlayerMP, true, true); // Make sure the player is synced
    }
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

    private static final ArrayDeque<FutureTask> serverTasks = new ArrayDeque<>();

    @SuppressWarnings("UnstableApiUsage")
    public static <T> ListenableFuture<T> scheduleServerTask(Callable<T> task) {
        Validate.notNull(task);

        if (Thread.currentThread() != serverThread) {
            ListenableFutureTask<T> listenablefuturetask = ListenableFutureTask.create(task);

            synchronized (serverTasks) {
                serverTasks.add(listenablefuturetask);
                return listenablefuturetask;
            }
        } else {
            try {
                return Futures.immediateFuture(task.call());
            } catch (Exception exception) {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
    }
}
