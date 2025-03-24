package bettertodo.network;

import java.util.concurrent.Executors;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Level;

import betterquesting.network.PacketAssembly;
import bettertodo.api.api.BetterTodoAPI;
import bettertodo.core.Todo;
import bettertodo.handlers.EventHandler;
import bettertodo.utils.GenericTuple;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/// This is the handler that deals with the byte packets from the network
public class NetworkMessage implements IMessage {

    protected NBTTagCompound tags = new NBTTagCompound();

    @SuppressWarnings("unused")
    public NetworkMessage() // For use only by forge
    {}

    public NetworkMessage(NBTTagCompound tags) // Use PacketDataTypes to instantiate new packets
    {
        this.tags = tags;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tags = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tags);
    }

    public static class HandleServer implements IMessageHandler<NetworkMessage, IMessage> {

        @Override
        public IMessage onMessage(NetworkMessage packet, MessageContext ctx) {
            if (packet == null || packet.tags == null || ctx.getServerHandler().playerEntity.mcServer == null) {
                Todo.LOG.log(
                    Level.ERROR,
                    "A critical NPE error occured during while handling a TodoList packet server side",
                    new NullPointerException());
                return null;
            }

            final EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
            final NBTTagCompound message = PacketAssembly.INSTANCE
                .assemblePacket(sender == null ? null : BetterTodoAPI.getQuestingUUID(sender), packet.tags);

            if (message == null) {
                return null;
            } else if (!message.hasKey("ID")) {
                Todo.LOG.log(Level.WARN, "Received a packet server side without an ID");
                return null;
            }

            final Consumer<GenericTuple<NBTTagCompound, EntityPlayerMP>> method = PacketTypeRegistry.INSTANCE
                .getServerHandler(new ResourceLocation(message.getString("ID")));

            if (method == null) {
                Todo.LOG
                    .log(Level.WARN, "Received a packet server side with an invalid ID: " + message.getString("ID"));
                return null;
            } else if (sender != null) {
                EventHandler
                    .scheduleServerTask(Executors.callable(() -> method.accept(new GenericTuple<>(message, sender))));
            }

            return null;
        }
    }

    public static class HandleClient implements IMessageHandler<NetworkMessage, IMessage> {

        @Override
        public IMessage onMessage(NetworkMessage packet, MessageContext ctx) {
            if (packet == null || packet.tags == null) {
                Todo.LOG.log(
                    Level.ERROR,
                    "A critical NPE error occured during while handling a TodoList packet client side",
                    new NullPointerException());
                return null;
            }

            final NBTTagCompound message = PacketAssembly.INSTANCE.assemblePacket(null, packet.tags);

            if (message == null) {
                return null;
            } else if (!message.hasKey("ID")) {
                Todo.LOG.log(Level.WARN, "Received a packet client side without an ID");
                return null;
            }

            final Consumer<NBTTagCompound> method = PacketTypeRegistry.INSTANCE
                .getClientHandler(new ResourceLocation(message.getString("ID")));

            if (method == null) {
                Todo.LOG
                    .log(Level.WARN, "Received a packet client side with an invalid ID: " + message.getString("ID"));
                return null;
            } else {
                Minecraft.getMinecraft()
                    .func_152343_a(Executors.callable(() -> method.accept(message)));
            }

            return null;
        }
    }
}
