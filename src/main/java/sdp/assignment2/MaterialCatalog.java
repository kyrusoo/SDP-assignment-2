package sdp.assignment2;

public interface MaterialCatalog<F extends Family> {
    double availableStock();
    /** Returns a quotation without changing stock or reservations. */
    double quoteCost(FabricationJob<F> job);
    /** Reject before mutation if the reservation cannot be made. */
    void reserve(FabricationJob<F> job);
    /** Releases an existing reservation once; absent reservations return false. */
    boolean release(FabricationJob<F> job);
}
