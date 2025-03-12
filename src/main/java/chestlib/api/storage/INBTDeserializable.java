package chestlib.api.storage;

import net.minecraft.nbt.NBTBase;

public interface INBTDeserializable<T extends NBTBase> {

    void readFromNBT(T nbt);
}
