package todolist.api.todo.task;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import chestlib.api.storage.INBTSaveLoad;

public interface ITaskExtension extends INBTSaveLoad<NBTTagCompound> {

    ResourceLocation getType();

}
