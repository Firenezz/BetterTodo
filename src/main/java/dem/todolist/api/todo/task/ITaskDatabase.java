package dem.todolist.api.todo.task;

import java.util.UUID;

import net.minecraft.nbt.NBTTagList;

import com.dem.chestlib.api.database.IUuidDatabase;
import com.dem.chestlib.api.storage.INBTPartial;

public interface ITaskDatabase extends IUuidDatabase<ITask>, INBTPartial<NBTTagList, UUID> {

    ITask createNew(UUID uuid);

}
