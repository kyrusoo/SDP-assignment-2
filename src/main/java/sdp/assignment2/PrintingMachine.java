package sdp.assignment2;

import java.util.List;

public final class PrintingMachine implements Machine<PrintingFamily> {
    // Simulation capacity per job, in this family's material units.
    private static final double MATERIAL_CAPACITY = 250;
    private final JobQueue<PrintingFamily> queue = new JobQueue<>();

    @Override
    public void enqueue(FabricationJob<PrintingFamily> job) {
        queue.enqueue(job, MATERIAL_CAPACITY, false);
    }

    @Override
    public boolean remove(FabricationJob<PrintingFamily> job) {
        return queue.remove(job);
    }

    @Override
    public List<FabricationJob<PrintingFamily>> queuedJobs() {
        return queue.snapshot();
    }
}
