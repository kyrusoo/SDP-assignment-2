# Stage 5 - Abstract Factory and typed composition

FabricationFactory<F> creates the three related product types: a FabricationJob<F>,
a Machine<F> and a MaterialCatalog<F>. PrintingFactory, LaserFactory and
VinylFactory each supply one complete family. Machine and catalog creation return
fresh instances, so separate services using a production factory have independent
queues and stock.

Job creation delegates to the existing matching JobCreator.prepareJob workflow.
That workflow still validates the input and enforces the booking limit using its
overridden creation method. Abstract Factory therefore groups compatible products
while Factory Method continues to control job preparation.

FabricationService<F> accepts one factory, creates its machine and catalog once
in the constructor, and retains them in private final fields. It accepts no
replacement products and exposes no product setters. Its stock query and immutable
queue snapshot allow state observation without exposing mutable products.
All service dependencies are interfaces; it names no concrete families.

For now quoteJob creates a prepared job through the factory. The job retains the
original quotation formulas; quoting does not reserve stock or enqueue work.
The machine and catalog are retained for later collaborating operations. Submission,
cancellation and their transaction rules are not implemented in this stage.

App selects a factory in its existing initialization branches and constructs the
service using Java's inferred wildcard capture. No raw types or unchecked casts
are needed. Centralizing selection into a registry remains Stage 6.

## Matching types

This is valid Java:

```java
FabricationFactory<PrintingFamily> factory = new PrintingFactory();
FabricationService<PrintingFamily> service = new FabricationService<>(factory);
FabricationJob<PrintingFamily> job = service.quoteJob(new Design("Sign", 2, 10));
Machine<PrintingFamily> machine = factory.createMachine();
machine.enqueue(job);
```

This intentionally invalid example belongs only in documentation:

```java
Machine<LaserFamily> machine = new LaserFactory().createMachine();
FabricationJob<PrintingFamily> job =
        new PrintingFactory().createJob(new Design("Sign", 2, 10));
machine.enqueue(job); // Does not compile: PrintingFamily is not LaserFamily.
```

The same family parameter links all factory return types and the service fields.
Production factories consistently supply the corresponding implementations.
Generics prevent ordinary mixed-family calls, but cannot prove a custom factory
behaves honestly, uses correct units, or avoids sharing state. Behavioral checks
remain necessary. Temporary composition checks verified all three production
families, unchanged booking enforcement, quote non-mutation, separate production
stock/queues, and once-only creation and retention using a counting factory.
The invalid snippet above was documented, not compiler-tested in this stage.
