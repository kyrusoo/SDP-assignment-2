package sdp.assignment2;

import java.util.Locale;

public class App {

    // Example rule for this classroom simulation, not a real machine limit.
    private static final int MAX_BOOKING_MINUTES = 120;

    public static void main(String[] args) {
        try {
            if (args.length != 0 && args.length != 1 && args.length != 4) {
                throw new IllegalArgumentException(
                        "Provide no arguments, one family, or all four arguments.");
            }

            String family = args.length == 0
                    ? "printing"
                    : args[0].trim().toLowerCase(Locale.ROOT);

            Design design = args.length == 4
                    ? new Design(args[1], Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]))
                    : new Design("Desk sign", 2, 10);

            // Part A: direct creation and repeated booking checks are intentional.
            if (family.equals("printing")) {
                PrintingJob job = new PrintingJob(design);

                if (job.estimatedMinutes() > MAX_BOOKING_MINUTES) {
                    throw new IllegalArgumentException(
                            "This job exceeds the 120-minute booking limit.");
                }

                printReport(design, "3D printing", job.estimatedMinutes(),
                        job.requiredMaterial(), "grams of PLA", job.estimatedCost());

            } else if (family.equals("laser")) {
                LaserJob job = new LaserJob(design);

                if (job.estimatedMinutes() > MAX_BOOKING_MINUTES) {
                    throw new IllegalArgumentException(
                            "This job exceeds the 120-minute booking limit.");
                }

                printReport(design, "Laser cutting", job.estimatedMinutes(),
                        job.requiredMaterial(), "material sheets", job.estimatedCost());

            } else if (family.equals("vinyl")) {
                VinylJob job = new VinylJob(design);

                if (job.estimatedMinutes() > MAX_BOOKING_MINUTES) {
                    throw new IllegalArgumentException(
                            "This job exceeds the 120-minute booking limit.");
                }

                printReport(design, "Vinyl cutting", job.estimatedMinutes(),
                        job.requiredMaterial(), "cm of vinyl roll", job.estimatedCost());

            } else {
                throw new IllegalArgumentException("Unknown family: " + family);
            }
        } catch (NumberFormatException exception) {
            System.err.println("Error: quantity and work units must be whole numbers"
                    + " between 1 and 2147483647.");
            printUsage();
            System.exit(1);
        } catch (IllegalArgumentException exception) {
            System.err.println("Error: " + exception.getMessage());
            printUsage();
            System.exit(1);
        }
    }

    private static void printReport(Design design, String family,
                                    double minutes, double material,
                                    String materialUnit, double cost) {
        System.out.println("=== Makerspace quotation ===");
        System.out.println("Family: " + family);
        System.out.println("Design: " + design.getName());
        System.out.println("Quantity: " + design.getQuantity());
        System.out.println("Work units per item: " + design.getWorkUnitsPerItem());
        System.out.println("Total work units: " + design.getTotalWorkUnits());
        System.out.printf(Locale.ROOT, "Estimated time: %.0f minutes%n", minutes);
        System.out.printf(Locale.ROOT, "Required material: %.2f %s%n",
                material, materialUnit);
        System.out.printf(Locale.ROOT, "Estimated cost: %.2f credits%n", cost);
        System.out.println("Booking check: PASSED (quote only; no job was queued)");
    }

    private static void printUsage() {
        System.err.println("Families: printing, laser, vinyl");
        System.err.println("Examples of IntelliJ Program arguments:");
        System.err.println("  laser");
        System.err.println("  laser \"Desk sign\" 2 10");
        System.err.println("Leave arguments empty to use the printing demo.");
    }
}