package todolist.api.network;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.NetworkRegistry;

public interface IPacketSender {

    // Server to Client
    void sendToPlayers(NetworkPacket payload, EntityPlayerMP... players);

    void sendToAll(NetworkPacket payload);

    // Client to Server
    void sendToServer(NetworkPacket payload);

    // Misc.
    void sendToAround(NetworkPacket payload, NetworkRegistry.TargetPoint point);

    void sendToDimension(NetworkPacket payload, int dimension);
}
