package sdp.assignment2;

import java.util.Objects;

public abstract class JobCreator<F extends Family> {

    // Example rule for this classroom simulation, not a real machine limit.
    private static final int MAX_BOOKING_MINUTES = 120;

    public final FabricationJob<F> prepareJob(Design design) {
        Objects.requireNonNull(design, "Design must not be null.");
        FabricationJob<F> job = createJob(design);
        if (job.estimatedMinutes() > MAX_BOOKING_MINUTES) {
            throw new IllegalArgumentException(
                    "This job exceeds the 120-minute booking limit.");
        }
        return job;
    }

    protected abstract FabricationJob<F> createJob(Design design);
}
