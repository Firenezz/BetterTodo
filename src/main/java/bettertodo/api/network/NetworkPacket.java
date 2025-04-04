package bettertodo.api.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class NetworkPacket {

    private final ResourceLocation handler;
    private final NBTTagCompound payload;

    public NetworkPacket(ResourceLocation handler, NBTTagCompound payload) {
        this.handler = handler;
        this.payload = payload;
    }

    public ResourceLocation getHandler() {
        return handler;
    }

    public NBTTagCompound getPayload() {
        return payload;
    }
}
