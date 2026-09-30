package sdp.assignment2;

public final class PrintingFactory implements FabricationFactory<PrintingFamily> {
    private final JobCreator<PrintingFamily> creator = new PrintingJobCreator();

    @Override
    public FabricationJob<PrintingFamily> createJob(Design design) {
        return creator.prepareJob(design);
    }

    @Override
    public Machine<PrintingFamily> createMachine() {
        return new PrintingMachine();
    }

    @Override
    public MaterialCatalog<PrintingFamily> createMaterialCatalog() {
        return new PrintingMaterials();
    }
}
