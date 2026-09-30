package sdp.assignment2;

import java.util.Objects;

public final class EmbroideryJob implements FabricationJob<EmbroideryFamily> {
    // Fictional rates: work units represent stitch-pattern complexity.
    private static final double SETUP_MINUTES = 4;
    private static final double HOOPING_MINUTES_PER_ITEM = 3;
    private static final double STITCHING_MINUTES_PER_WORK_UNIT = 0.75;
    private static final double THREAD_METERS_PER_WORK_UNIT = 0.5;
    private static final double COST_PER_METER = 0.30;
    private static final double COST_PER_MINUTE = 0.15;

    private final Design design;

    public EmbroideryJob(Design design) {
        this.design = Objects.requireNonNull(design, "Design must not be null.");
    }

    @Override
    public Design getDesign() { return design; }

    @Override
    public String familyName() { return "Embroidery"; }

    @Override
    public String materialUnit() { return "meters of thread"; }

    @Override
    public double requiredMaterial() {
        return design.getTotalWorkUnits() * THREAD_METERS_PER_WORK_UNIT;
    }

    @Override
    public double estimatedMinutes() {
        return Math.ceil(SETUP_MINUTES
                + design.getQuantity() * HOOPING_MINUTES_PER_ITEM
                + design.getTotalWorkUnits() * STITCHING_MINUTES_PER_WORK_UNIT);
    }

    @Override
    public double estimatedCost() {
        return requiredMaterial() * COST_PER_METER
                + estimatedMinutes() * COST_PER_MINUTE;
    }
}
