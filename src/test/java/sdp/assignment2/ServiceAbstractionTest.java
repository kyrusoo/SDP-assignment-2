package sdp.assignment2;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ServiceAbstractionTest {
    // Test-only marker, not a production fourth family.
    static final class TestFamily implements Family {}
    record TestJob(Design getDesign) implements FabricationJob<TestFamily> {
        public String familyName() { return "Test"; }
        public String materialUnit() { return "units"; }
        public double estimatedMinutes() { return 1; }
        public double requiredMaterial() { return 3; }
        public double estimatedCost() { return 99; }
    }
    static final class TestMachine implements Machine<TestFamily> {
        final List<FabricationJob<TestFamily>> jobs = new ArrayList<>();
        boolean reject, removable = true;
        int enqueueCalls;
        public void enqueue(FabricationJob<TestFamily> job) {
            enqueueCalls++;
            if (reject) throw new IllegalStateException("Unavailable");
            jobs.add(job);
        }
        public boolean remove(FabricationJob<TestFamily> job) {
            return removable && jobs.remove(job);
        }
        public List<FabricationJob<TestFamily>> queuedJobs() { return List.copyOf(jobs); }
    }
    static final class TestMaterials implements MaterialCatalog<TestFamily> {
        double stock = 10;
        int quotes, releases;
        final Map<FabricationJob<TestFamily>, Double> reserved = new IdentityHashMap<>();
        public double availableStock() { return stock; }
        public double quoteCost(FabricationJob<TestFamily> job) { quotes++; return 7; }
        public void reserve(FabricationJob<TestFamily> job) {
            if (stock < job.requiredMaterial()) throw new IllegalStateException("No stock");
            reserved.put(job, job.requiredMaterial());
            stock -= job.requiredMaterial();
        }
        public boolean release(FabricationJob<TestFamily> job) {
            Double amount = reserved.remove(job);
            if (amount == null) return false;
            stock += amount;
            releases++;
            return true;
        }
    }
    static final class TestFactory implements FabricationFactory<TestFamily> {
        final TestMachine machine = new TestMachine();
        final TestMaterials materials = new TestMaterials();
        int machineCalls, catalogCalls;
        public FabricationJob<TestFamily> createJob(Design design) { return new TestJob(design); }
        public Machine<TestFamily> createMachine() { machineCalls++; return machine; }
        public MaterialCatalog<TestFamily> createMaterialCatalog() { catalogCalls++; return materials; }
    }
    private final Design design = new Design("Test", 1, 1);

    @Test void clientWorksThroughOnlyTestProductImplementations() {
        TestFactory factory = new TestFactory();
        var service = new FabricationService<>(factory);
        assertEquals(7, service.quoteJob(design).cost()); // Catalog, not job's 99.
        assertEquals(1, factory.materials.quotes);
        assertEquals(10, service.availableStock());
        assertTrue(service.queuedJobs().isEmpty());
        UUID id = service.submitJob(design);
        assertEquals(7, service.availableStock());
        assertEquals(1, service.queuedJobs().size());
        assertTrue(service.cancelJob(id));
        assertEquals(10, service.availableStock());
        assertEquals(1, factory.materials.releases);
    }
    @Test void machineRejectionRollsBackReservation() {
        TestFactory factory = new TestFactory();
        factory.machine.reject = true;
        var service = new FabricationService<>(factory);
        assertEquals("Unavailable", assertThrows(IllegalStateException.class,
                () -> service.submitJob(design)).getMessage());
        assertEquals(10, service.availableStock());
        assertTrue(factory.materials.reserved.isEmpty());
        assertTrue(service.queuedJobs().isEmpty());
        assertEquals(1, factory.materials.releases);
    }
    @Test void stockFailureNeverCallsMachine() {
        TestFactory factory = new TestFactory();
        factory.materials.stock = 0;
        var service = new FabricationService<>(factory);
        assertThrows(IllegalStateException.class, () -> service.submitJob(design));
        assertEquals(0, factory.machine.enqueueCalls);
        assertEquals(0, factory.materials.releases);
        assertTrue(factory.materials.reserved.isEmpty());
    }
    @Test void nonRemovableJobKeepsReservation() {
        TestFactory factory = new TestFactory();
        var service = new FabricationService<>(factory);
        UUID id = service.submitJob(design);
        factory.machine.removable = false;
        assertFalse(service.cancelJob(id));
        assertEquals(7, service.availableStock());
        assertEquals(0, factory.materials.releases);
        factory.machine.removable = true;
        assertTrue(service.cancelJob(id));
        assertFalse(service.cancelJob(id));
        assertEquals(1, factory.materials.releases);
    }
    @Test void productsCreatedOnceAndRetainedAcrossOperations() {
        TestFactory factory = new TestFactory();
        var service = new FabricationService<>(factory);
        service.quoteJob(design);
        UUID first = service.submitJob(design);
        service.submitJob(design);
        service.cancelJob(first);
        assertEquals(1, factory.machineCalls);
        assertEquals(1, factory.catalogCalls);
        assertEquals(7, service.availableStock());
        assertEquals(factory.machine.jobs, service.queuedJobs());
    }
}
