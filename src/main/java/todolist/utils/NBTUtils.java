package todolist.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Future;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api2.utils.BQThreadedIO;
import todolist.api.api.TodoAPI;

public class NBTUtils {

    public static void writeNBTToFile(File file, NBTTagCompound nbtTagCompound) {
        final File tmp = new File(file.getAbsolutePath() + ".tmp");

        try {
            checkAndCreateFile(tmp);
        } catch (Exception e) {
            QuestingAPI.getLogger()
                .error("An error occurred while saving JSON to file (Directory setup):", e);
            return;
        }

        try (var fos = new FileOutputStream(tmp)) {
            // DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(fos))) {
            // CompressedStreamTools.write(nbtTagCompound, dataoutputstream);
            CompressedStreamTools.writeCompressed(nbtTagCompound, fos);
            TodoAPI.getLogger()
                .debug("NBT written");
        } catch (Exception e) {
            TodoAPI.getLogger()
                .error("An error occurred while saving NBT to file (File write):", e);
            return;
        }

        try {
            Files
                .move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ignored) {
            try {
                Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                TodoAPI.getLogger()
                    .error("An error occurred while saving JSON to file (Temp copy):", e);
            }
        } catch (Exception e) {
            TodoAPI.getLogger()
                .error("An error occurred while saving JSON to file (Temp copy):", e);
        }
    }

    public static Future<Void> writeNBTToFileSafe(File file, NBTTagCompound nbtTagCompound) {

        return TodoThreadedIO.DISK_IO.enqueue(() -> {
            writeNBTToFile(file, nbtTagCompound);

            return null;
        });
    }

    @Nullable
    public static NBTTagCompound readNBTFile(File file) {
        Future<NBTTagCompound> task = BQThreadedIO.INSTANCE.enqueue(() -> {
            if (!file.exists() || !file.isFile()) {
                return new NBTTagCompound();
            }

            try (InputStream stream = new FileInputStream(file)) {
                return CompressedStreamTools.readCompressed(stream);
            } catch (Exception ex) {
                try {
                    return CompressedStreamTools.read(file);
                } catch (Exception ex1) {
                    return new NBTTagCompound();
                }
            }
        });

        try {
            return task.get(); // Wait for other scheduled file ops to finish
        } catch (Exception e) {
            QuestingAPI.getLogger()
                .error("Unable to read from file " + file, e);
            return new NBTTagCompound();
        }
    }

    private static void checkAndCreateFile(@NotNull File file) throws IOException {
        if (file.getParentFile() != null) {
            final boolean _var = file.getParentFile()
                .mkdirs();
        }
        if (!file.createNewFile()) {
            final boolean _var1 = file.delete();
            final boolean _var2 = file.createNewFile();
        }
    }
}
