package todolist.todo.integration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import todolist.api.todo.task.ITaskExtension;

/// Extension for BetterQuestion quests
public class BQTaskExtension implements ITaskExtension {

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation("todolist:bqid");
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        return null;
    }
}
