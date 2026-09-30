package sdp.assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Shared queue mechanics; concrete machines supply their physical limits. */
final class JobQueue<F extends Family> {
    private final List<FabricationJob<F>> jobs = new ArrayList<>();

    void enqueue(FabricationJob<F> job, double materialLimit, boolean wholeUnits) {
        Objects.requireNonNull(job, "Job must not be null.");
        double material = job.requiredMaterial();
        if (!Double.isFinite(material) || material <= 0 || material > materialLimit
                || (wholeUnits && material != Math.rint(material))) {
            throw new IllegalArgumentException("Job exceeds machine material capacity or uses invalid units.");
        }
        if (jobs.stream().anyMatch(existing -> existing == job)) {
            throw new IllegalStateException("Job is already queued.");
        }
        jobs.add(job);
    }

    boolean remove(FabricationJob<F> job) {
        Objects.requireNonNull(job, "Job must not be null.");
        return jobs.removeIf(existing -> existing == job);
    }

    List<FabricationJob<F>> snapshot() {
        return List.copyOf(jobs);
    }
}
