package chestlib.api.storage;

import net.minecraft.nbt.NBTBase;

public interface INBTSaveLoad<T extends NBTBase> extends INBTSerializable<T>, INBTDeserializable<T> {

}
