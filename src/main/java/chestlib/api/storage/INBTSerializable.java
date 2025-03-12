package chestlib.api.storage;

import net.minecraft.nbt.NBTBase;

public interface INBTSerializable<T extends NBTBase> {

    T writeToNBT(T nbt);
}
