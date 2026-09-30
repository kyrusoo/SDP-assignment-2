package sdp.assignment2;

import java.util.List;

public final class VinylMachine implements Machine<VinylFamily> {
    // Simulation capacity per job, in this family's material units.
    private static final double MATERIAL_CAPACITY = 400;
    private final JobQueue<VinylFamily> queue = new JobQueue<>();

    @Override
    public void enqueue(FabricationJob<VinylFamily> job) {
        queue.enqueue(job, MATERIAL_CAPACITY, false);
    }

    @Override
    public boolean remove(FabricationJob<VinylFamily> job) {
        return queue.remove(job);
    }

    @Override
    public List<FabricationJob<VinylFamily>> queuedJobs() {
        return queue.snapshot();
    }
}
