package sdp.assignment2;

import java.util.Locale;
import java.util.UUID;

/** Separate demonstration so App's original quotation CLI stays unchanged. */
public final class WorkflowDemo {
    private WorkflowDemo() {
    }

    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                throw new IllegalArgumentException("Provide no arguments or one family.");
            }
            run(FactoryRegistry.select(args.length == 0 ? "printing" : args[0]));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            System.err.println("Error: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static <F extends Family> void run(FabricationFactory<F> factory) {
        FabricationService<F> service = new FabricationService<>(factory);
        Design design = new Design("Desk sign", 2, 10);
        JobQuote<F> quote = service.quoteJob(design);
        System.out.printf(Locale.ROOT, "%s quotation: %.2f credits%n",
                quote.job().familyName(), quote.cost());
        showState("After quote", service);
        UUID id = service.submitJob(design);
        showState("After submission", service);
        System.out.println("Cancelled: " + service.cancelJob(id));
        showState("After cancellation", service);
        System.out.println("Cancelled again: " + service.cancelJob(id));
        showState("After repeated cancellation", service);
    }

    private static void showState(String label, FabricationService<?> service) {
        System.out.printf(Locale.ROOT, "%s: queued=%d, available stock=%.2f%n",
                label, service.queuedJobs().size(), service.availableStock());
    }
}
