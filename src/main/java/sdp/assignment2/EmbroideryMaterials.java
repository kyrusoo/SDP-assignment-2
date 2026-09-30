package sdp.assignment2;

import java.util.Objects;

public final class EmbroideryMaterials implements MaterialCatalog<EmbroideryFamily> {
    // Classroom stock assumption, in this family's material units.
    private static final double DEFAULT_STOCK = 500;
    private final MaterialStock<EmbroideryFamily> stock;

    public EmbroideryMaterials() {
        this(DEFAULT_STOCK);
    }

    public EmbroideryMaterials(double initialStock) {
        stock = new MaterialStock<>(initialStock, false);
    }

    @Override
    public double availableStock() {
        return stock.available();
    }

    @Override
    public double quoteCost(FabricationJob<EmbroideryFamily> job) {
        // Jobs remain the single owner of the original quotation rates.
        return Objects.requireNonNull(job, "Job must not be null.").estimatedCost();
    }

    @Override
    public void reserve(FabricationJob<EmbroideryFamily> job) {
        stock.reserve(job);
    }

    @Override
    public boolean release(FabricationJob<EmbroideryFamily> job) {
        return stock.release(job);
    }
}
