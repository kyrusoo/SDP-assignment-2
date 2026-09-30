package sdp.assignment2;

import java.util.Objects;

public final class PrintingMaterials implements MaterialCatalog<PrintingFamily> {
    // Classroom stock assumption, in this family's material units.
    private static final double DEFAULT_STOCK = 1000;
    private final MaterialStock<PrintingFamily> stock;

    public PrintingMaterials() {
        this(DEFAULT_STOCK);
    }

    public PrintingMaterials(double initialStock) {
        stock = new MaterialStock<>(initialStock, false);
    }

    @Override
    public double availableStock() {
        return stock.available();
    }

    @Override
    public double quoteCost(FabricationJob<PrintingFamily> job) {
        // Jobs remain the single owner of the original quotation rates.
        return Objects.requireNonNull(job, "Job must not be null.").estimatedCost();
    }

    @Override
    public void reserve(FabricationJob<PrintingFamily> job) {
        stock.reserve(job);
    }

    @Override
    public boolean release(FabricationJob<PrintingFamily> job) {
        return stock.release(job);
    }
}
