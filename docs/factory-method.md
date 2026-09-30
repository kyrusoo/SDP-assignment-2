# Stage 3 - Factory Method

App selects a `JobCreator<?>` from the command-line family and calls
`prepareJob(design)`. It no longer constructs concrete jobs or owns the booking
policy. Its parsing, error handling and interface-based reporting are unchanged.

`JobCreator<F>` is the abstract Creator. Its final `prepareJob` workflow:

1. Rejects a null design. Design already validates its immutable fields when
   constructed, so those checks do not need to be copied here.
2. Calls the protected abstract `createJob(Design)` factory method.
3. Uses the returned product's estimated time to reject jobs above 120 minutes.
4. Returns the prepared job for quotation; it does not queue anything.

`final` prevents subclasses from replacing this preparation workflow.
PrintingJobCreator, LaserJobCreator and VinylJobCreator override only the creation
step, returning their respective jobs. The family parameter is carried from
`JobCreator<F>` to `FabricationJob<F>` without casts.

This is Factory Method because the inherited business workflow calls a method
whose implementation is supplied by a subclass. A static selection function
would choose and construct products in one function; it would not use an
overridden creation step. App's if/else selects a creator at initialization;
that selection is not itself the Factory Method.

The Creator does meaningful work beyond returning a new object: it enforces the
booking policy using the product's behavior. There is one owner of this policy,
and each family's existing time calculation determines whether a job is accepted.
The 120-minute rule and all prices remain classroom simulation assumptions.

App still knows the concrete creator classes, so a new family still requires a
selection branch. Abstract Factory and collaborating machines/material catalogs
are later stages. The Stage 2 document describes the earlier historical state.
