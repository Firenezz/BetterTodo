package dem.todolist.api.todo.task;

import java.util.UUID;

import com.dem.chestlib.api.database.IUuidDatabase;
import com.dem.chestlib.api.storage.INBTPartial;
import net.minecraft.nbt.NBTTagList;


public interface ITaskDatabase extends IUuidDatabase<ITask>, INBTPartial<NBTTagList, UUID> {

    ITask createNew(UUID uuid);


}
