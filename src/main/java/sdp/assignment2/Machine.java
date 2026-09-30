package sdp.assignment2;

import java.util.List;

public interface Machine<F extends Family> {
    /** Reject before mutation: a failed enqueue must leave the queue unchanged. */
    void enqueue(FabricationJob<F> job);
    boolean remove(FabricationJob<F> job);
    List<FabricationJob<F>> queuedJobs();
}
