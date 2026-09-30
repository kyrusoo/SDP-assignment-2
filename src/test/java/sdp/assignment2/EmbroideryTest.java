package sdp.assignment2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmbroideryTest {
    private final Design design = new Design("Badge", 2, 10);

    @Test void factoryCreatesAllEmbroideryProductsAndQuotes() {
        var factory = new EmbroideryFactory();
        var job = factory.createJob(design);
        assertInstanceOf(EmbroideryJob.class, job);
        assertInstanceOf(EmbroideryMachine.class, factory.createMachine());
        assertInstanceOf(EmbroideryMaterials.class, factory.createMaterialCatalog());
        assertEquals(25, job.estimatedMinutes());
        assertEquals(10, job.requiredMaterial());
        assertEquals(6.75, job.estimatedCost(), 0.000001);
    }

    @Test void registrySelectsEmbroideryWithNormalizedInput() {
        assertInstanceOf(EmbroideryJob.class,
                FactoryRegistry.select(" EMBROIDERY ").createJob(design));
    }

    @Test void unchangedServiceRunsCompleteEmbroideryScenario() {
        var service = new FabricationService<>(new EmbroideryFactory());
        assertEquals(6.75, service.quoteJob(design).cost(), 0.000001);
        assertEquals(500, service.availableStock());
        assertTrue(service.queuedJobs().isEmpty());
        var id = service.submitJob(design);
        assertEquals(490, service.availableStock());
        assertEquals(1, service.queuedJobs().size());
        assertTrue(service.cancelJob(id));
        assertEquals(500, service.availableStock());
        assertTrue(service.queuedJobs().isEmpty());
        assertFalse(service.cancelJob(id));
        assertEquals(500, service.availableStock());
    }

    @Test void creatorStillRejectsOverLimitEmbroidery() {
        assertThrows(IllegalArgumentException.class,
                () -> new EmbroideryFactory().createJob(new Design("Large", 1, 200)));
    }

    @Test void machineRejectsThreadBeyondLoadedSpoolWithoutQueueMutation() {
        var machine = new EmbroideryMachine();
        assertThrows(IllegalArgumentException.class,
                () -> machine.enqueue(new EmbroideryJob(new Design("Large", 1, 201))));
        assertTrue(machine.queuedJobs().isEmpty());
    }
}
