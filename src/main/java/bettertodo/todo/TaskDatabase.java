package bettertodo.todo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.Nullable;

import bettertodo.api.api.BetterTodoAPI;
import bettertodo.api.todo.task.ITask;
import bettertodo.api.todo.task.ITaskDatabase;
import bettertodo.core.Todo;
import chestlib.api.database.UuidDatabase;
import chestlib.util.nbt.NBTUuidUtil;

public class TaskDatabase extends UuidDatabase<ITask> implements ITaskDatabase {

    public static final TaskDatabase INSTANCE = new TaskDatabase();
    public static final TaskDatabase DELETED_ENTRIES = new TaskDatabase();
    public static final TaskDatabase LOST_ENTRIES = new TaskDatabase();

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
                BetterTodoAPI.getLogger()
                    .warn("Tried saving null task with ID {}", entry.getKey());
                return;
            }

            NBTTagCompound tag = new NBTTagCompound();
            entry.getValue()
                .writeToNBT(tag);
            NBTUuidUtil.writeIdToNbt(entry.getKey(), tag);
            nbt.appendTag(tag);
        });

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagList nbt, boolean merge) {
        if (!merge) {
            clear();
        }

        final boolean isEmpty = this.isEmpty();

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

            ITask task = isEmpty ? null : get(taskID);
            task = task != null ? task : createNew(taskID);
            task.setId(taskID);
            task.readFromNBT(qTag);
        }
    }

    public void checkIntegrity() {
        Map<UUID, ITask> lostTasks = new HashMap<>();
        this.forEach(((uuid, iTask) -> {
            Optional<UUID> parent = iTask.getParentID();
            if (parent.isPresent() && !containsKey(parent.get())) {
                lostTasks.put(uuid, iTask);
                TaskDatabase.INSTANCE.remove(uuid);
            }
        }));

        if (!lostTasks.isEmpty()) {
            Todo.LOG.info("Lost tasks have been found and added to the Lost Entries database");
            // TODO: add info to show the db as a command for admins
            TaskDatabase.LOST_ENTRIES.putAll(lostTasks);
        }

    }

}
