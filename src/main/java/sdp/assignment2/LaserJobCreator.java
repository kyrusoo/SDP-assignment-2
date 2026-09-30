package sdp.assignment2;

public final class LaserJobCreator extends JobCreator<LaserFamily> {
    @Override
    protected FabricationJob<LaserFamily> createJob(Design design) {
        return new LaserJob(design);
    }
}
