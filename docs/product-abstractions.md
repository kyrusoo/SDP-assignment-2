# Stage 2 - Product abstractions

`FabricationJob<F extends Family>` describes what every fabrication job can
provide: its design, display name, material unit, estimated time, material
requirement, and cost. PrintingJob, LaserJob, and VinylJob implement this same
contract while retaining their own original calculation rules.

App now holds its selected job as `FabricationJob<?>`. The question mark means
"a job from some family"; reporting does not need to know which family it is.
Java calls the methods implemented by the actual selected job. This lets App
apply the 120-minute booking check once and call one `printReport` method.
The report gets its labels and values from the same job instead of receiving
separate arguments that could accidentally describe different families.

## Family type labels

`Family` is an empty marker interface. PrintingFamily, LaserFamily, and
VinylFamily are final marker classes with private constructors: they are type
labels, not objects that the application needs to instantiate.
For example, PrintingJob implements `FabricationJob<PrintingFamily>`.
This differs from `FabricationJob<LaserFamily>` in Java's type system.
The display strings are only for output; they do not establish compatibility.

These labels prepare for typed collaboration in later stages. There are no
machines or material catalogs yet, so this stage does not implement the final
family compatibility architecture. A type label alone does not prove that an
implementation behaves correctly.

## What remains

App still directly constructs all three concrete jobs in its selection branches.
Changing constructors or adding a family still requires changing App. The booking
policy also remains in App. Neither Factory Method nor Abstract Factory has been
implemented. Stage 3 will address creation and preparation using Factory Method;
it is outside this change.

Design, CLI parsing, errors, exit codes, report formatting, and formulas are
unchanged. Quotes still do not queue jobs. Rates, credits, work units, and the
booking limit remain fictional classroom simulation assumptions.

`initial-problems.md` is preserved as the historical description of Part A.
See `progress.md` for the actual baseline commit and verification evidence.
