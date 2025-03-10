package dem.todolist.network;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import betterquesting.network.PacketAssembly;
import cpw.mods.fml.common.network.NetworkRegistry;
import dem.todolist.api.network.IPacketSender;
import dem.todolist.api.network.NetworkPacket;
import dem.todolist.core.Todo;
import dem.todolist.utils.TodoThreadedIO;

public class PacketSender implements IPacketSender {

    public static final PacketSender INSTANCE = new PacketSender();

    @Override
    public void sendToPlayers(NetworkPacket payload, EntityPlayerMP... players) {
        payload.getPayload()
            .setString(
                "ID",
                payload.getHandler()
                    .toString());

        TodoThreadedIO.INSTANCE.enqueue(() -> {
            List<NBTTagCompound> fragments = PacketAssembly.INSTANCE.splitPacket(payload.getPayload());
            for (EntityPlayerMP p : players) {
                for (NBTTagCompound tag : fragments) {
                    Todo.INSTANCE.network.sendTo(new NetworkMessage(tag), p);
                }
            }
        });
    }

    @Override
    public void sendToAll(NetworkPacket payload) {
        payload.getPayload()
            .setString(
                "ID",
                payload.getHandler()
                    .toString());

        TodoThreadedIO.INSTANCE.enqueue(() -> {
            for (NBTTagCompound p : PacketAssembly.INSTANCE.splitPacket(payload.getPayload())) {
                Todo.INSTANCE.network.sendToAll(new NetworkMessage(p));
            }
        });
    }

    @Override
    public void sendToServer(NetworkPacket payload) {
        payload.getPayload()
            .setString(
                "ID",
                payload.getHandler()
                    .toString());

        TodoThreadedIO.INSTANCE.enqueue(() -> {
            for (NBTTagCompound p : PacketAssembly.INSTANCE.splitPacket(payload.getPayload())) {
                Todo.INSTANCE.network.sendToServer(new NetworkMessage(p));
            }
        });
    }

    @Override
    public void sendToAround(NetworkPacket payload, NetworkRegistry.TargetPoint point) {
        payload.getPayload()
            .setString(
                "ID",
                payload.getHandler()
                    .toString());

        TodoThreadedIO.INSTANCE.enqueue(() -> {
            for (NBTTagCompound p : PacketAssembly.INSTANCE.splitPacket(payload.getPayload())) {
                Todo.INSTANCE.network.sendToAllAround(new NetworkMessage(p), point);
            }
        });
    }

    @Override
    public void sendToDimension(NetworkPacket payload, int dimension) {
        payload.getPayload()
            .setString(
                "ID",
                payload.getHandler()
                    .toString());

        TodoThreadedIO.INSTANCE.enqueue(() -> {
            for (NBTTagCompound p : PacketAssembly.INSTANCE.splitPacket(payload.getPayload())) {
                Todo.INSTANCE.network.sendToDimension(new NetworkMessage(p), dimension);
            }
        });
    }
}
