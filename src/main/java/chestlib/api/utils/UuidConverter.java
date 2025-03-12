package chestlib.api.utils;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;

public class UuidConverter {

    /** Returns a compact string representation of a UUID. */
    public static String encodeUuid(UUID uuid) {
        byte[] upper = Longs.toByteArray(uuid.getMostSignificantBits());
        byte[] lower = Longs.toByteArray(uuid.getLeastSignificantBits());

        return Base64.getUrlEncoder()
            .encodeToString(Bytes.concat(upper, lower));
    }

    /**
     * Returns a compact string representation of a UUID, with trailing '=' removed.
     * This is used for quest book translation keys, which must not contain '='.
     */
    public static String encodeUuidStripPadding(UUID uuid) {
        return encodeUuid(uuid).replace("=", "");
    }

    /** Decodes a compact string representation of a UUID. */
    public static UUID decodeUuid(String string) {
        byte[] bytes = Base64.getUrlDecoder()
            .decode(string);
        byte[] upper = Arrays.copyOfRange(bytes, 0, 8);
        byte[] lower = Arrays.copyOfRange(bytes, 8, 16);

        return new UUID(Longs.fromByteArray(upper), Longs.fromByteArray(lower));
    }
}
