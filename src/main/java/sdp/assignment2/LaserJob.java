package sdp.assignment2;

import java.util.Objects;

public final class LaserJob implements FabricationJob<LaserFamily> {

    private static final double SETUP_MINUTES = 2.0;
    private static final double WORK_UNITS_PER_SHEET = 25.0;
    private static final double LOADING_MINUTES_PER_SHEET = 3.0;
    private static final double CUTTING_MINUTES_PER_WORK_UNIT = 0.5;
    private static final double COST_PER_SHEET = 4.0;
    private static final double COST_PER_MINUTE = 0.20;

    private final Design design;

    public LaserJob(Design design) {
        this.design = Objects.requireNonNull(design, "Design must not be null.");
    }

    @Override
    public Design getDesign() {
        return design;
    }

    @Override
    public String familyName() {
        return "Laser cutting";
    }

    @Override
    public String materialUnit() {
        return "material sheets";
    }

    @Override
    public double requiredMaterial() {
        // Purchase whole sheets, even when the final sheet is partly unused.
        return Math.ceil(design.getTotalWorkUnits() / WORK_UNITS_PER_SHEET);
    }

    @Override
    public double estimatedMinutes() {
        // Every sheet needs loading, followed by cutting work.
        return Math.ceil(SETUP_MINUTES
                + requiredMaterial() * LOADING_MINUTES_PER_SHEET
                + design.getTotalWorkUnits() * CUTTING_MINUTES_PER_WORK_UNIT);
    }

    @Override
    public double estimatedCost() {
        return requiredMaterial() * COST_PER_SHEET
                + estimatedMinutes() * COST_PER_MINUTE;
    }
}