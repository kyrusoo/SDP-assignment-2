package sdp.assignment2;

public final class EmbroideryFactory implements FabricationFactory<EmbroideryFamily> {
    private final JobCreator<EmbroideryFamily> creator = new EmbroideryJobCreator();

    @Override
    public FabricationJob<EmbroideryFamily> createJob(Design design) {
        return creator.prepareJob(design);
    }

    @Override
    public Machine<EmbroideryFamily> createMachine() {
        return new EmbroideryMachine();
    }

    @Override
    public MaterialCatalog<EmbroideryFamily> createMaterialCatalog() {
        return new EmbroideryMaterials();
    }
}
