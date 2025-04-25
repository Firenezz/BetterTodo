package bettertodo.api.todo;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import chestlib.api.storage.INBTSaveLoad;
import chestlib.util.nbt.NBTUuidUtil;

public abstract class Ownership implements INBTSaveLoad<NBTTagCompound> {

    public static final int PLAYER = 1;
    public static final int PARTY = 2;

    public UUID id;

    protected Ownership(UUID id) {

        this.id = id;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        id = NBTUuidUtil.readIdFromNbt(nbt.getCompoundTag("id"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (id != null) {
            nbt.setTag("id", NBTUuidUtil.writeIdToNbt(id));
        }

        return nbt;
    }
}
