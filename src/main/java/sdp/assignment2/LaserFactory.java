package sdp.assignment2;

public final class LaserFactory implements FabricationFactory<LaserFamily> {
    private final JobCreator<LaserFamily> creator = new LaserJobCreator();

    @Override
    public FabricationJob<LaserFamily> createJob(Design design) {
        return creator.prepareJob(design);
    }

    @Override
    public Machine<LaserFamily> createMachine() {
        return new LaserMachine();
    }

    @Override
    public MaterialCatalog<LaserFamily> createMaterialCatalog() {
        return new LaserMaterials();
    }
}
