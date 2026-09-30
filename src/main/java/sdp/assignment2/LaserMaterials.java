package sdp.assignment2;

import java.util.Objects;

public final class LaserMaterials implements MaterialCatalog<LaserFamily> {
    // Classroom stock assumption, in this family's material units.
    private static final double DEFAULT_STOCK = 20;
    private final MaterialStock<LaserFamily> stock;

    public LaserMaterials() {
        this(DEFAULT_STOCK);
    }

    public LaserMaterials(double initialStock) {
        stock = new MaterialStock<>(initialStock, true);
    }

    @Override
    public double availableStock() {
        return stock.available();
    }

    @Override
    public double quoteCost(FabricationJob<LaserFamily> job) {
        // Jobs remain the single owner of the original quotation rates.
        return Objects.requireNonNull(job, "Job must not be null.").estimatedCost();
    }

    @Override
    public void reserve(FabricationJob<LaserFamily> job) {
        stock.reserve(job);
    }

    @Override
    public boolean release(FabricationJob<LaserFamily> job) {
        return stock.release(job);
    }
}
