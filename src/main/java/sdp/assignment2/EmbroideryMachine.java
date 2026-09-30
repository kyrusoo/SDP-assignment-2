package sdp.assignment2;

import java.util.List;

public final class EmbroideryMachine implements Machine<EmbroideryFamily> {
    // Simulation capacity per job, in this family's material units.
    private static final double MATERIAL_CAPACITY = 100;
    private final JobQueue<EmbroideryFamily> queue = new JobQueue<>();

    @Override
    public void enqueue(FabricationJob<EmbroideryFamily> job) {
        queue.enqueue(job, MATERIAL_CAPACITY, false);
    }

    @Override
    public boolean remove(FabricationJob<EmbroideryFamily> job) {
        return queue.remove(job);
    }

    @Override
    public List<FabricationJob<EmbroideryFamily>> queuedJobs() {
        return queue.snapshot();
    }
}
