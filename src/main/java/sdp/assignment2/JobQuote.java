package sdp.assignment2;

public record JobQuote<F extends Family>(FabricationJob<F> job, double cost) {
}
