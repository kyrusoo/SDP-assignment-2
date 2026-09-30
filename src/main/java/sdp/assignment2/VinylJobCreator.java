package sdp.assignment2;

public final class VinylJobCreator extends JobCreator<VinylFamily> {
    @Override
    protected FabricationJob<VinylFamily> createJob(Design design) {
        return new VinylJob(design);
    }
}
