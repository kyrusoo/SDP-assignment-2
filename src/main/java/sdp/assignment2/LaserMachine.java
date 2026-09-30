package sdp.assignment2;

import java.util.List;

public final class LaserMachine implements Machine<LaserFamily> {
    // Simulation capacity per job, in this family's material units.
    private static final double MATERIAL_CAPACITY = 4;
    private final JobQueue<LaserFamily> queue = new JobQueue<>();

    @Override
    public void enqueue(FabricationJob<LaserFamily> job) {
        queue.enqueue(job, MATERIAL_CAPACITY, true);
    }

    @Override
    public boolean remove(FabricationJob<LaserFamily> job) {
        return queue.remove(job);
    }

    @Override
    public List<FabricationJob<LaserFamily>> queuedJobs() {
        return queue.snapshot();
    }
}
