# Stage 4 - Original machines and material catalogs

The three original families now each have a job, machine and material catalog.
`Machine<F>` accepts only `FabricationJob<F>` and exposes enqueue, removal and
an immutable queue snapshot. `MaterialCatalog<F>` quotes, reserves and releases
material for the same typed job. For example, a PrintingJob cannot be passed to
a LaserMachine through these typed APIs. No strings, casts or reflection identify
families. Types do not prove that a custom implementation reports honest values.

## Simulation assumptions

| Family | Stock unit | Default stock | Machine capacity per job |
| --- | --- | --- | --- |
| Printing | grams of PLA | 1000 | 250 grams from a loaded spool |
| Laser | whole material sheets | 20 | 4 sheets in a loading batch |
| Vinyl | centimeters of fixed-width roll | 2000 | 400 cm in a feed run |

Machines use the job's material requirement to enforce these limits. Laser
machines and catalogs additionally require whole sheet amounts. Printing and
vinyl allow fractional units. These are small classroom assumptions, not real
equipment specifications. A machine's physical capacity is distinct from the
Creator's 120-minute booking policy; the booking policy remains solely in
JobCreator. Direct machine calls do not run that preparation workflow.

## Queue behavior

Enqueue appends to an ordered in-memory queue. Invalid material amounts,
capacity violations and duplicate job objects are rejected before mutation.
Removal returns true once and false when the object is absent. Queue snapshots
cannot be edited by callers. Two separate job objects with identical designs
are separate jobs; reusing the very same object while queued is a duplicate.
There is no execution, completed-job status or queue-size limit in this stage.

## Stock and reservation behavior

Catalogs can use their default stock or receive an initial amount in a constructor.
Initial stock must be finite and nonnegative; sheets must also be whole.
Each catalog owns a private map from job identity to the amount reserved.
Available stock equals initial stock minus outstanding reservations.
Reserve rejects duplicate reservations, nonpositive/nonfinite requirements,
fractional sheets and insufficient stock without changing state.
Release removes an existing reservation once. Missing or repeated release returns
false and cannot inflate stock. Release uses the recorded amount, rather than
recalculating the job's requirement. Null jobs are rejected.

There is no replenishment or permanent material consumption yet. These components
are single-threaded and in memory. One catalog represents one stock pool.

## Ownership and scope

The existing job classes remain the authoritative owners of the original
simulation prices and calculations. `quoteCost(job)` delegates to
`job.estimatedCost()` without changing stock. It returns the full quotation,
including machine time, not just a material subtotal. Rates are not copied into
catalogs or machines, and no pricing responsibility moved in this stage.

Two small package-private helpers avoid duplicating state-management rules:
JobQueue owns queue mechanics; MaterialStock owns reservation accounting.
Concrete products own family capacities, default stock and whole-unit rules.

App remains the unchanged quotation demonstration. There is no factory or service
composing these products yet. Queue and reservation operations are independent;
atomic submission and rollback belong to the later service workflow. This stage
does not claim to implement that workflow or its compatibility guarantee.

Example component use in Java (not a submission service):

```java
FabricationJob<PrintingFamily> job =
        new PrintingJobCreator().prepareJob(new Design("Desk sign", 2, 10));
MaterialCatalog<PrintingFamily> materials = new PrintingMaterials();
Machine<PrintingFamily> machine = new PrintingMachine();
double cost = materials.quoteCost(job); // 8.50; stock unchanged
materials.reserve(job);                // 950 grams available
machine.enqueue(job);                 // one queued job
machine.remove(job);
materials.release(job);                // 1000 grams available again
```
