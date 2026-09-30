package sdp.assignment2;

import java.util.Objects;

public final class VinylMaterials implements MaterialCatalog<VinylFamily> {
    // Classroom stock assumption, in this family's material units.
    private static final double DEFAULT_STOCK = 2000;
    private final MaterialStock<VinylFamily> stock;

    public VinylMaterials() {
        this(DEFAULT_STOCK);
    }

    public VinylMaterials(double initialStock) {
        stock = new MaterialStock<>(initialStock, false);
    }

    @Override
    public double availableStock() {
        return stock.available();
    }

    @Override
    public double quoteCost(FabricationJob<VinylFamily> job) {
        // Jobs remain the single owner of the original quotation rates.
        return Objects.requireNonNull(job, "Job must not be null.").estimatedCost();
    }

    @Override
    public void reserve(FabricationJob<VinylFamily> job) {
        stock.reserve(job);
    }

    @Override
    public boolean release(FabricationJob<VinylFamily> job) {
        return stock.release(job);
    }
}
