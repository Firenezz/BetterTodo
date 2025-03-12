package todolist.network.handlers;

import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import betterquesting.api.api.QuestingAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import todolist.api.network.NetworkPacket;
import todolist.api.todo.task.ITask;
import todolist.core.Todo;
import todolist.handlers.persistence.DatabasePersistence;
import todolist.network.PacketSender;
import todolist.network.PacketTypeRegistry;
import todolist.todo.TaskDatabase;
import todolist.utils.GenericTuple;

public class NetTaskCreate {

    private static final ResourceLocation ID_NAME = new ResourceLocation("todolist:task_create");

    public static void registerHandler() {
        PacketTypeRegistry.INSTANCE.registerServerHandler(ID_NAME, NetTaskCreate::onServer);

        if (Todo.proxy.isClient()) {
            PacketTypeRegistry.INSTANCE.registerClientHandler(ID_NAME, NetTaskCreate::onClient);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void sendCreate(@Nonnull Collection<ITask> tasks) {
        NBTTagCompound payload = new NBTTagCompound(); // Message
        NBTTagList dataList = new NBTTagList(); // List of new tasks

        tasks.forEach((task) -> {
            NBTTagCompound entry = new NBTTagCompound(); // The new task

            task.writeToNBT(entry);

            dataList.appendTag(entry);
        });

        payload.setTag("data", dataList);

        PacketSender.INSTANCE.sendToServer(new NetworkPacket(ID_NAME, payload));
    }

    private static void onServer(GenericTuple<NBTTagCompound, EntityPlayerMP> message) {
        EntityPlayerMP sender = message.getSecond();
        MinecraftServer server = sender.mcServer;
        if (server == null) return;

        // TODO: Check list permission

        NBTTagCompound tag = message.getFirst();
        var _senderID = QuestingAPI.getQuestingUUID(sender);
        createTasks(tag.getTagList("data", 10));

        NetTaskSync.sendSync(sender);
    }

    private static void createTasks(NBTTagList data) {
        for (int i = 0; i < data.tagCount(); i++) {
            var entry = data.getCompoundTagAt(i);

            ITask task = TaskDatabase.INSTANCE.createNewWithUuidGen();

            task.readFromNBT(entry);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void onClient(NBTTagCompound message) {
        if (message.getBoolean("reset") && !Minecraft.getMinecraft()
            .isIntegratedServerRunning()) // DON'T do this on LAN hosts
        {
            DatabasePersistence.INSTANCE.unloadDatabases();
        }

        if (message.getBoolean("respond")) // Client doesn't really have to honour
        {
            PacketSender.INSTANCE.sendToServer(new NetworkPacket(ID_NAME, new NBTTagCompound()));
        }
    }
}
