package dem.todolist.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.Nullable;

import com.dem.chestlib.util.nbt.NBTUuidUtil;

import betterquesting.api2.storage.UuidDatabase;
import dem.todolist.api.api.TodoAPI;
import dem.todolist.api.todo.task.ITask;
import dem.todolist.api.todo.task.ITaskDatabase;

public class TaskDatabase extends UuidDatabase<ITask> implements ITaskDatabase {

    public static final TaskDatabase INSTANCE = new TaskDatabase();

    @Override
    public ITask createNew(UUID uuid) {
        ITask quest = new TaskInstance();
        put(uuid, quest);
        return quest;
    }

    public ITask createNewWithUuidGen() {
        return createNew(generateKey());
    }

    @Override
    public synchronized NBTTagList writeToNBT(NBTTagList nbt, @Nullable List<UUID> subset) {
        orderedEntries().forEach(entry -> {
            if (subset != null && !subset.contains(entry.getKey())) {
                return;
            }

            if (entry.getValue() == null) {
                TodoAPI.getLogger()
                    .warn("Tried saving null task with ID {}", entry.getKey());
                return;
            }

            NBTTagCompound jq = new NBTTagCompound();
            entry.getValue()
                .writeToNBT(jq);
            NBTUuidUtil.writeIdToNbt(entry.getKey(), jq);
            nbt.appendTag(jq);
        });

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagList nbt, boolean merge) {
        if (!merge) {
            clear();
        }

        for (int i = 0; i < nbt.tagCount(); i++) {
            NBTTagCompound qTag = nbt.getCompoundTagAt(i);

            Optional<UUID> taskIDOptional = NBTUuidUtil.tryReadFromNbt("", qTag);
            UUID taskID;
            if (taskIDOptional.isPresent()) {
                taskID = taskIDOptional.get();
            } else {
                // Register in a lost task database for admins
                continue;
            }

            ITask task = get(taskID);
            task = task != null ? task : createNew(taskID);
            task.readFromNBT(qTag);
        }
    }
}
