package sdp.assignment2;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/** Reservations belong to this stock instance and use job object identity. */
final class MaterialStock<F extends Family> {
    private final double initialStock;
    private final boolean wholeUnits;
    private final Map<FabricationJob<F>, Double> reservations = new IdentityHashMap<>();

    MaterialStock(double initialStock, boolean wholeUnits) {
        if (!Double.isFinite(initialStock) || initialStock < 0
                || (wholeUnits && initialStock != Math.rint(initialStock))) {
            throw new IllegalArgumentException("Invalid initial stock.");
        }
        this.initialStock = initialStock;
        this.wholeUnits = wholeUnits;
    }

    double available() {
        return initialStock - reservations.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    void reserve(FabricationJob<F> job) {
        Objects.requireNonNull(job, "Job must not be null.");
        if (reservations.containsKey(job)) {
            throw new IllegalStateException("Job already has a reservation.");
        }
        double amount = job.requiredMaterial();
        if (!Double.isFinite(amount) || amount <= 0
                || (wholeUnits && amount != Math.rint(amount))) {
            throw new IllegalArgumentException("Invalid material requirement.");
        }
        if (amount > available()) {
            throw new IllegalStateException("Insufficient material stock.");
        }
        reservations.put(job, amount);
    }

    boolean release(FabricationJob<F> job) {
        Objects.requireNonNull(job, "Job must not be null.");
        return reservations.remove(job) != null;
    }
}
