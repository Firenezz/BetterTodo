package bettertodo.network.handlers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import bettertodo.api.api.ApiReference;
import bettertodo.api.api.BetterTodoAPI;
import bettertodo.api.network.NetworkPacket;
import bettertodo.api.todo.task.ITask;
import bettertodo.api.todo.task.ITaskDatabase;
import bettertodo.core.Todo;
import bettertodo.network.PacketSender;
import bettertodo.network.PacketTypeRegistry;
import bettertodo.utils.BTThreaded;
import bettertodo.utils.GenericTuple;
import chestlib.util.nbt.NBTUuidUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NetTaskSync {

    private static final ResourceLocation ID_NAME = new ResourceLocation("bettertodo:task_sync");

    public static void registerHandler() {
        PacketTypeRegistry.INSTANCE.registerServerHandler(ID_NAME, NetTaskSync::onServer);

        if (Todo.proxy.isClient()) {
            PacketTypeRegistry.INSTANCE.registerClientHandler(ID_NAME, NetTaskSync::onClient);
        }
    }

    public static final String TASK_TAG_NAME = "task";
    public static final String TASK_LIST_NAME = "data";

    static ITaskDatabase TASK_DB = BetterTodoAPI.getAPI(ApiReference.TASK_DB);

    public static void sendSync(@Nullable EntityPlayerMP player) {
        // TODO: Send to specific players

        BTThreaded.SEQUENTIAL_EXECUTOR.enqueue(() -> {
            NBTTagList dataList = new NBTTagList();

            final Map<UUID, ITask> taskSubset = TASK_DB;

            for (Map.Entry<UUID, ITask> entry : taskSubset.entrySet()) {
                NBTTagCompound tagEntry = new NBTTagCompound();

                tagEntry.setTag(
                    TASK_TAG_NAME,
                    entry.getValue()
                        .writeToNBT(new NBTTagCompound()));

                NBTUuidUtil.writeIdToNbt("", entry.getKey(), tagEntry);
                dataList.appendTag(tagEntry);
            }

            NBTTagCompound payload = new NBTTagCompound();

            payload.setTag(TASK_LIST_NAME, dataList);

            if (player == null) {
                PacketSender.INSTANCE.sendToAll(new NetworkPacket(ID_NAME, payload));
            } else {
                PacketSender.INSTANCE.sendToPlayers(new NetworkPacket(ID_NAME, payload), player);
            }
        });
    }

    /// Request sync of tasks
    @SideOnly(Side.CLIENT)
    public static void requestSync() {
        NBTTagCompound payload = new NBTTagCompound();

        PacketSender.INSTANCE.sendToServer(new NetworkPacket(ID_NAME, payload));
    }

    private static void onServer(GenericTuple<NBTTagCompound, EntityPlayerMP> message) {
        NBTTagCompound payload = message.getFirst();
        EntityPlayerMP playerMP = message.getSecond();

        sendSync(playerMP);
    }

    @SideOnly(Side.CLIENT)
    private static void onClient(NBTTagCompound message) {
        NBTTagList data = message.getTagList(TASK_LIST_NAME, 10);

        TASK_DB.clear();

        for (int i = 0; i < data.tagCount(); i++) {
            NBTTagCompound tag = data.getCompoundTagAt(i);
            Optional<UUID> taskIDOptional = NBTUuidUtil.tryReadFromNbt("", tag);

            if (!taskIDOptional.isPresent()) continue;

            UUID taskID = taskIDOptional.get();

            ITask task = TASK_DB.get(taskID);

            if (task == null) {
                task = TASK_DB.createNew(taskID);
            }

            task.readFromNBT(tag.getCompoundTag(TASK_TAG_NAME));
        }

        // TODO: Send database update event
    }
}
