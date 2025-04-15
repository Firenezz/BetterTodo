package bettertodo.utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.Future;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import betterquesting.api.api.QuestingAPI;
import bettertodo.api.api.BetterTodoAPI;

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
            DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(fos));
            CompressedStreamTools.write(nbtTagCompound, dataoutputstream);
            // CompressedStreamTools.writeCompressed(nbtTagCompound, fos);
            dataoutputstream.close();
            BetterTodoAPI.getLogger()
                .debug("NBT written");
        } catch (Exception e) {
            BetterTodoAPI.getLogger()
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
                BetterTodoAPI.getLogger()
                    .error("An error occurred while saving JSON to file (Temp copy):", e);
            }
        } catch (Exception e) {
            BetterTodoAPI.getLogger()
                .error("An error occurred while saving JSON to file (Temp copy):", e);
        }
    }

    public static Future<Void> writeNBTToFileSafe(File file, NBTTagCompound nbtTagCompound) {

        return BTThreadedIO.DISK_IO.enqueue(() -> {
            writeNBTToFile(file, nbtTagCompound);

            return null;
        });
    }

    public static Optional<NBTTagCompound> readNBTFile(File file) {
        Future<NBTTagCompound> task = BTThreadedIO.DISK_IO.enqueue(() -> {
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
            return Optional.ofNullable(task.get()); // Wait for other scheduled file ops to finish
        } catch (Exception e) {
            QuestingAPI.getLogger()
                .error("Unable to read from file " + file, e);
            return Optional.of(new NBTTagCompound());
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
