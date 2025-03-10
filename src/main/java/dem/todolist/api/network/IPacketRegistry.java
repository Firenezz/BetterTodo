package dem.todolist.api.network;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dem.todolist.utils.GenericTuple;

/// Registry for client/server handling
public interface IPacketRegistry {

    /// Register a handler for the server side with an id of [ResourceLocation].
    void registerServerHandler(@Nonnull ResourceLocation idName,
        @Nonnull Consumer<GenericTuple<NBTTagCompound, EntityPlayerMP>> method);

    /// Register a handler for the client side with an id of [ResourceLocation].
    ///
    /// Remark:
    /// Client side only
    @SideOnly(Side.CLIENT)
    void registerClientHandler(@Nonnull ResourceLocation idName, @Nonnull Consumer<NBTTagCompound> method);

    /// Get the consumer for the handler with id [ResourceLocation] on the server side
    @Nullable
    Consumer<GenericTuple<NBTTagCompound, EntityPlayerMP>> getServerHandler(@Nonnull ResourceLocation idName);

    /// Get the consumer for the handler with id [ResourceLocation] on the client side
    ///
    /// Remark:
    /// Client side only
    @Nullable
    @SideOnly(Side.CLIENT)
    Consumer<NBTTagCompound> getClientHandler(@Nonnull ResourceLocation idName);
}
