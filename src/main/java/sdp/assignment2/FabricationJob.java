package sdp.assignment2;

public interface FabricationJob<F extends Family> {
    Design getDesign();
    String familyName();
    String materialUnit();
    double estimatedMinutes();
    double requiredMaterial();
    double estimatedCost();
}
