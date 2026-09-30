package sdp.assignment2;

import java.util.Locale;
import java.util.Objects;

/** Resolves external family names only during application initialization. */
public final class FactoryRegistry {
    private FactoryRegistry() {
    }

    public static FabricationFactory<?> select(String family) {
        String name = Objects.requireNonNull(family, "Family must not be null.")
                .trim().toLowerCase(Locale.ROOT);
        return switch (name) {
            case "printing" -> new PrintingFactory();
            case "laser" -> new LaserFactory();
            case "vinyl" -> new VinylFactory();
            default -> throw new IllegalArgumentException("Unknown family: " + name);
        };
    }
}
