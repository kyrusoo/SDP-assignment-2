package sdp.assignment2;

import java.util.List;

public interface Machine<F extends Family> {
    void enqueue(FabricationJob<F> job);
    boolean remove(FabricationJob<F> job);
    List<FabricationJob<F>> queuedJobs();
}
