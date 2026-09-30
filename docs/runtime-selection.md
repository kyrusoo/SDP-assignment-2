# Stage 6 - Runtime factory selection

FactoryRegistry.select receives the external family name and returns a
FabricationFactory<?>. It trims whitespace, normalizes case with Locale.ROOT,
and selects PrintingFactory, LaserFactory or VinylFactory. Unsupported names
produce the same `Unknown family: ...` error as before. A null Java argument is
rejected explicitly; normal command-line arguments are never null.

App owns CLI parsing: no arguments still means printing with the default design;
one argument selects a family; four arguments specify family, name, quantity and
work units. Design validation still happens before factory selection, preserving
error precedence. Blank/unknown family names are errors, not default selections.

App's generic runQuotation helper captures the unknown family from the selected
factory as `F extends Family`. Inside that helper, FabricationFactory<F>,
FabricationService<F> and FabricationJob<F> share the same type. No raw types,
unchecked casts or family checks are needed in the business workflow.

Only initialization selects concrete factories. FabricationService is unchanged
and has no dependency on the registry or family strings. All three families run
through the same helper, service and report method. Adding another family later
will require extending the registry, not the service workflow.

The registry is a static selection utility; it is not the Factory Method pattern.
Factory Method remains JobCreator's overridden createJob used by prepareJob.
Abstract Factory remains the factory interface that produces related products.

In IntelliJ run sdp.assignment2.App.main with JDK 21. Try no arguments, `printing`,
`laser`, `vinyl`, `laser "Desk sign" 2 10`, or `unknown`. Quotation values, usage,
errors and exit codes are preserved. Submission/cancellation remain Stage 7.
