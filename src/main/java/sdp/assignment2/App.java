package sdp.assignment2;

import java.util.Locale;

public class App {

    public static void main(String[] args) {
        try {
            if (args.length != 0 && args.length != 1 && args.length != 4) {
                throw new IllegalArgumentException(
                        "Provide no arguments, one family, or all four arguments.");
            }

            String family = args.length == 0
                    ? "printing"
                    : args[0];

            Design design = args.length == 4
                    ? new Design(args[1], Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]))
                    : new Design("Desk sign", 2, 10);

            runQuotation(FactoryRegistry.select(family), design);
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

    private static <F extends Family> void runQuotation(
            FabricationFactory<F> factory, Design design) {
        FabricationService<F> service = new FabricationService<>(factory);
        FabricationJob<F> job = service.quoteJob(design);
        printReport(job);
    }

    private static void printReport(FabricationJob<?> job) {
        Design design = job.getDesign();
        System.out.println("=== Makerspace quotation ===");
        System.out.println("Family: " + job.familyName());
        System.out.println("Design: " + design.getName());
        System.out.println("Quantity: " + design.getQuantity());
        System.out.println("Work units per item: " + design.getWorkUnitsPerItem());
        System.out.println("Total work units: " + design.getTotalWorkUnits());
        System.out.printf(Locale.ROOT, "Estimated time: %.0f minutes%n", job.estimatedMinutes());
        System.out.printf(Locale.ROOT, "Required material: %.2f %s%n",
                job.requiredMaterial(), job.materialUnit());
        System.out.printf(Locale.ROOT, "Estimated cost: %.2f credits%n", job.estimatedCost());
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
