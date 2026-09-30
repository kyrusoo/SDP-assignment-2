package sdp.assignment2;

public final class VinylFactory implements FabricationFactory<VinylFamily> {
    private final JobCreator<VinylFamily> creator = new VinylJobCreator();

    @Override
    public FabricationJob<VinylFamily> createJob(Design design) {
        return creator.prepareJob(design);
    }

    @Override
    public Machine<VinylFamily> createMachine() {
        return new VinylMachine();
    }

    @Override
    public MaterialCatalog<VinylFamily> createMaterialCatalog() {
        return new VinylMaterials();
    }
}
