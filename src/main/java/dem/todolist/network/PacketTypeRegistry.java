package dem.todolist.network;

import java.util.HashMap;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dem.todolist.api.network.IPacketRegistry;
import dem.todolist.network.handlers.NetBulkSync;
import dem.todolist.network.handlers.NetTaskCreate;
import dem.todolist.network.handlers.NetTaskSync;
import dem.todolist.utils.GenericTuple;

public class PacketTypeRegistry implements IPacketRegistry {

    public static final PacketTypeRegistry INSTANCE = new PacketTypeRegistry();

    private final HashMap<ResourceLocation, Consumer<GenericTuple<NBTTagCompound, EntityPlayerMP>>> serverHandlers = new HashMap<>();
    private final HashMap<ResourceLocation, Consumer<NBTTagCompound>> clientHandlers = new HashMap<>();

    public void registerHandlers() {
        NetTaskCreate.registerHandler();
        NetTaskSync.registerHandler();

        NetBulkSync.registerHandler();
    }

    @Override
    public void registerServerHandler(@NotNull ResourceLocation idName,
        @NotNull Consumer<GenericTuple<NBTTagCompound, EntityPlayerMP>> method) {
        if (serverHandlers.containsKey(idName)) {
            throw new IllegalArgumentException("Cannot register duplicate packet handler: " + idName);
        }

        serverHandlers.put(idName, method);
    }

    @Override
    public void registerClientHandler(@NotNull ResourceLocation idName, @NotNull Consumer<NBTTagCompound> method) {
        if (clientHandlers.containsKey(idName)) {
            throw new IllegalArgumentException("Cannot register dupliate packet handler: " + idName);
        }

        clientHandlers.put(idName, method);
    }

    @Override
    public @Nullable Consumer<GenericTuple<NBTTagCompound, EntityPlayerMP>> getServerHandler(
        @NotNull ResourceLocation idName) {
        return serverHandlers.get(idName);
    }

    @Override
    public @Nullable Consumer<NBTTagCompound> getClientHandler(@NotNull ResourceLocation idName) {
        return clientHandlers.get(idName);
    }
}
