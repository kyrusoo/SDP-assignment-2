package sdp.assignment2;

import java.util.Objects;

public final class VinylJob {

    private static final double SETUP_MINUTES = 2.0;
    private static final double CUTTING_MINUTES_PER_WORK_UNIT = 0.25;
    private static final double FINISHING_MINUTES_PER_ITEM = 2.0;
    private static final double CENTIMETERS_PER_WORK_UNIT = 4.0;
    private static final double COST_PER_CENTIMETER = 0.02;
    private static final double COST_PER_MINUTE = 0.10;

    private final Design design;

    public VinylJob(Design design) {
        this.design = Objects.requireNonNull(design, "Design must not be null.");
    }

    public double requiredMaterial() {
        // Material is measured as length taken from a fixed-width roll.
        return design.getTotalWorkUnits() * CENTIMETERS_PER_WORK_UNIT;
    }

    public double estimatedMinutes() {
        // Each item needs finishing, in addition to machine cutting time.
        return Math.ceil(SETUP_MINUTES
                + design.getTotalWorkUnits() * CUTTING_MINUTES_PER_WORK_UNIT
                + design.getQuantity() * FINISHING_MINUTES_PER_ITEM);
    }

    public double estimatedCost() {
        return requiredMaterial() * COST_PER_CENTIMETER
                + estimatedMinutes() * COST_PER_MINUTE;
    }
}