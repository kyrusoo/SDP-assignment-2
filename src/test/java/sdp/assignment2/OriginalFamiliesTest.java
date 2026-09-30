package sdp.assignment2;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class OriginalFamiliesTest {
    private final Design design = new Design("Desk sign", 2, 10);

    @Test void printingProductsAndWorkflow() {
        verify(new PrintingFactory(), PrintingJob.class, PrintingMachine.class,
                PrintingMaterials.class, 65, 50, 8.50, 1000);
    }
    @Test void laserProductsAndWorkflow() {
        verify(new LaserFactory(), LaserJob.class, LaserMachine.class,
                LaserMaterials.class, 15, 1, 7, 20);
    }
    @Test void vinylProductsAndWorkflow() {
        verify(new VinylFactory(), VinylJob.class, VinylMachine.class,
                VinylMaterials.class, 11, 80, 2.70, 2000);
    }
    private <F extends Family> void verify(FabricationFactory<F> factory,
            Class<?> jobType, Class<?> machineType, Class<?> catalogType,
            double minutes, double amount, double cost, double stock) {
        assertInstanceOf(jobType, factory.createJob(design));
        assertInstanceOf(machineType, factory.createMachine());
        assertInstanceOf(catalogType, factory.createMaterialCatalog());
        FabricationService<F> service = new FabricationService<>(factory);
        JobQuote<F> quote = service.quoteJob(design);
        assertEquals(minutes, quote.job().estimatedMinutes());
        assertEquals(amount, quote.job().requiredMaterial());
        assertEquals(cost, quote.cost(), 0.000001);
        assertEquals(stock, service.availableStock());
        assertTrue(service.queuedJobs().isEmpty());
        UUID id = service.submitJob(design);
        assertEquals(stock - amount, service.availableStock());
        assertEquals(1, service.queuedJobs().size());
        assertTrue(service.cancelJob(id));
        assertEquals(stock, service.availableStock());
        assertTrue(service.queuedJobs().isEmpty());
    }
    @Test void printingSelectedAtRuntime() {
        assertInstanceOf(PrintingJob.class, FactoryRegistry.select("printing").createJob(design));
    }
    @Test void laserSelectionNormalizesCaseAndWhitespace() {
        assertInstanceOf(LaserJob.class, FactoryRegistry.select(" LASER ").createJob(design));
    }
    @Test void vinylSelectedAtRuntime() {
        assertInstanceOf(VinylJob.class, FactoryRegistry.select("vinyl").createJob(design));
    }
    @Test void unknownFamilyRejected() {
        assertEquals("Unknown family: unknown",
                assertThrows(IllegalArgumentException.class,
                        () -> FactoryRegistry.select("unknown")).getMessage());
    }
    @Test void invalidDesignRejectedBeforeSubmission() {
        assertThrows(IllegalArgumentException.class, () -> new Design("Sign", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new Design(" ", 1, 10));
        assertThrows(IllegalArgumentException.class, () -> new Design("Sign", 1, -1));
    }
    @Test void factoriesKeepCreatorBookingPolicy() {
        for (FabricationFactory<?> factory : new FabricationFactory<?>[] {
                new PrintingFactory(), new LaserFactory(), new VinylFactory()}) {
            assertThrows(IllegalArgumentException.class,
                    () -> factory.createJob(new Design("Huge", 10000, 10000)));
        }
    }
    @Test void bookingBoundaryAcceptedButNextMinuteRejected() {
        VinylFactory factory = new VinylFactory();
        assertEquals(120, factory.createJob(new Design("Boundary", 1, 464)).estimatedMinutes());
        assertThrows(IllegalArgumentException.class,
                () -> factory.createJob(new Design("Above", 1, 465)));
    }
    @Test void repeatedCancellationDoesNotInflateStock() {
        var service = new FabricationService<>(new PrintingFactory());
        UUID id = service.submitJob(design);
        assertTrue(service.cancelJob(id));
        assertFalse(service.cancelJob(id));
        assertEquals(1000, service.availableStock());
    }
    @Test void foreignAndUnknownIdsDoNotCancelJobs() {
        var first = new FabricationService<>(new PrintingFactory());
        var second = new FabricationService<>(new PrintingFactory());
        UUID id = first.submitJob(design);
        assertFalse(second.cancelJob(id));
        assertFalse(first.cancelJob(UUID.randomUUID()));
        assertEquals(950, first.availableStock());
        assertEquals(1, first.queuedJobs().size());
        assertEquals(1000, second.availableStock());
    }
    @Test void identicalDesignsRemainSeparateSubmissions() {
        var service = new FabricationService<>(new PrintingFactory());
        UUID first = service.submitJob(design), second = service.submitJob(design);
        assertNotEquals(first, second);
        assertEquals(900, service.availableStock());
        assertTrue(service.cancelJob(first));
        assertEquals(950, service.availableStock());
        assertEquals(1, service.queuedJobs().size());
        assertTrue(service.cancelJob(second));
        assertEquals(1000, service.availableStock());
    }
    @Test void insufficientStockLeavesExistingQueueUntouched() {
        var service = new FabricationService<>(new LaserFactory());
        for (int i = 0; i < 20; i++) service.submitJob(design);
        var before = service.queuedJobs();
        assertThrows(IllegalStateException.class, () -> service.submitJob(design));
        assertEquals(0, service.availableStock());
        assertEquals(before, service.queuedJobs());
    }
    @Test void realMachineCapacityRejectionRollsBackReservation() {
        var service = new FabricationService<>(new VinylFactory());
        assertThrows(IllegalArgumentException.class,
                () -> service.submitJob(new Design("Long roll", 1, 101)));
        assertEquals(2000, service.availableStock());
        assertTrue(service.queuedJobs().isEmpty());
    }
    @Test void queueSnapshotsCannotMutateService() {
        var service = new FabricationService<>(new PrintingFactory());
        var snapshot = service.queuedJobs();
        service.submitJob(design);
        assertTrue(snapshot.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> service.queuedJobs().clear());
        assertEquals(1, service.queuedJobs().size());
    }
    @Test void catalogDuplicateReservationAndReleaseAreSafe() {
        var materials = new LaserMaterials();
        var job = new LaserJob(design);
        materials.reserve(job);
        assertThrows(IllegalStateException.class, () -> materials.reserve(job));
        assertEquals(19, materials.availableStock());
        assertTrue(materials.release(job));
        assertFalse(materials.release(job));
        assertEquals(20, materials.availableStock());
    }
    @Test void duplicateEnqueueDoesNotChangeQueue() {
        var machine = new PrintingMachine();
        var job = new PrintingJob(design);
        machine.enqueue(job);
        assertThrows(IllegalStateException.class, () -> machine.enqueue(job));
        assertEquals(1, machine.queuedJobs().size());
    }
}
