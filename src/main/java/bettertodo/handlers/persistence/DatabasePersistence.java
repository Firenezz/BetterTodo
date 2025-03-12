package bettertodo.handlers.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;

import betterquesting.core.BetterQuesting;
import bettertodo.api.api.BetterTodoAPI;
import bettertodo.core.TodoSettings;
import bettertodo.todo.TaskDatabase;
import bettertodo.utils.NBTUtils;

public class DatabasePersistence {

    public static DatabasePersistence INSTANCE = new DatabasePersistence();

    private File fileDatabase = null;

    private File rootDir;

    private void setFiles(MinecraftServer server) {
        if (BetterQuesting.proxy.isClient()) {
            TodoSettings.curWorldDir = server.getFile("saves/" + server.getFolderName() + "/bettertodo");
            rootDir = server.getFile("saves/" + server.getFolderName());
        } else {
            TodoSettings.curWorldDir = server.getFile(server.getFolderName() + "/bettertodo");
            rootDir = server.getFile(server.getFolderName());
        }

        fileDatabase = new File(TodoSettings.curWorldDir, "db.nbt");
    }

    public void loadDatabases(MinecraftServer server) {
        setFiles(server);

        loadTasks();

        BetterTodoAPI.getLogger()
            .info("Loaded " + TaskDatabase.INSTANCE.size() + " tasks");
    }

    public void loadTasks() {
        NBTTagCompound nbt = NBTUtils.readNBTFile(fileDatabase);

        TaskDatabase.INSTANCE.readFromNBT(nbt.getTagList("tasks", 10), false);
    }

    public List<Future<Void>> saveDatabases() {
        List<Future<Void>> allFutures = new ArrayList<>(5);
        allFutures.add(saveTasks());

        return allFutures;
    }

    public Future<Void> saveTasks() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag("tasks", TaskDatabase.INSTANCE.writeToNBT(new NBTTagList(), null));

        return NBTUtils.writeNBTToFileSafe(fileDatabase, nbt);
    }

    public void unloadDatabases() {
        TaskDatabase.INSTANCE.clear();
    }
}
