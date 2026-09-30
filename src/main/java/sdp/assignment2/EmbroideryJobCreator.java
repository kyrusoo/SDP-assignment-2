package sdp.assignment2;

public final class EmbroideryJobCreator extends JobCreator<EmbroideryFamily> {
    @Override
    protected FabricationJob<EmbroideryFamily> createJob(Design design) {
        return new EmbroideryJob(design);
    }
}
