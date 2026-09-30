package sdp.assignment2;

import java.util.Objects;

public final class PrintingJob implements FabricationJob<PrintingFamily> {

    private static final double SETUP_MINUTES = 5.0;
    private static final double MINUTES_PER_WORK_UNIT = 3.0;
    private static final double GRAMS_PER_WORK_UNIT = 2.5;
    private static final double COST_PER_GRAM = 0.04;
    private static final double COST_PER_MINUTE = 0.10;

    private final Design design;

    public PrintingJob(Design design) {
        this.design = Objects.requireNonNull(design, "Design must not be null.");
    }

    @Override
    public Design getDesign() {
        return design;
    }

    @Override
    public String familyName() {
        return "3D printing";
    }

    @Override
    public String materialUnit() {
        return "grams of PLA";
    }

    @Override
    public double estimatedMinutes() {
        // Printing time grows with the total amount of work.
        return Math.ceil(SETUP_MINUTES
                + design.getTotalWorkUnits() * MINUTES_PER_WORK_UNIT);
    }

    @Override
    public double requiredMaterial() {
        // Material is measured in grams of PLA filament.
        return design.getTotalWorkUnits() * GRAMS_PER_WORK_UNIT;
    }

    @Override
    public double estimatedCost() {
        return requiredMaterial() * COST_PER_GRAM
                + estimatedMinutes() * COST_PER_MINUTE;
    }
}