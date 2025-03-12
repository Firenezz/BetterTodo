package todolist.network.handlers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import todolist.api.network.NetworkPacket;
import todolist.core.Todo;
import todolist.handlers.persistence.DatabasePersistence;
import todolist.network.PacketSender;
import todolist.network.PacketTypeRegistry;
import todolist.utils.GenericTuple;

public class NetBulkSync {

    private static final ResourceLocation ID_NAME = new ResourceLocation("todolist:main_sync");

    public static void registerHandler() {
        PacketTypeRegistry.INSTANCE.registerServerHandler(ID_NAME, NetBulkSync::onServer);

        if (Todo.proxy.isClient()) {
            PacketTypeRegistry.INSTANCE.registerClientHandler(ID_NAME, NetBulkSync::onClient);
        }
    }

    public static void sendReset(@Nullable EntityPlayerMP player, boolean reset, boolean respond) {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setBoolean("reset", reset);
        payload.setBoolean("respond", respond);

        if (player == null) {
            PacketSender.INSTANCE.sendToAll(new NetworkPacket(ID_NAME, payload));
        } else {
            PacketSender.INSTANCE.sendToPlayers(new NetworkPacket(ID_NAME, payload), player);
        }
    }

    public static void sendSync(@Nonnull EntityPlayerMP player) {
        NetTaskSync.sendSync(player);

    }

    private static void onServer(GenericTuple<NBTTagCompound, EntityPlayerMP> message) {
        sendSync(message.getSecond()); // Can include more sync options at a later date
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
