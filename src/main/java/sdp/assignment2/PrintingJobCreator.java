package sdp.assignment2;

public final class PrintingJobCreator extends JobCreator<PrintingFamily> {
    @Override
    protected FabricationJob<PrintingFamily> createJob(Design design) {
        return new PrintingJob(design);
    }
}
