# Stage 7 - Collaborating operations

FabricationService keeps using one typed factory and its retained machine and
material catalog. Family selection stays in FactoryRegistry at initialization.

- `quoteJob(Design)` prepares a job through the factory/Creator and asks the
  catalog for its quotation cost. It returns `JobQuote<F>` containing the job and
  quoted cost. It neither reserves material nor enqueues work. A quote is not a
  promise of available stock or machine capacity.
- `submitJob(Design)` prepares a fresh job, reserves its material, then enqueues
  it. If stock reservation fails, the machine is never called. If enqueue throws
  a RuntimeException, the reservation is released before the exception propagates.
  Only a successful submission is recorded and returns a UUID.
- `cancelJob(UUID)` looks up a job submitted to this service. Only successful
  removal from the machine queue permits releasing its reservation. The service
  then forgets that submission. Unknown, other-service or repeated IDs return
  false without releasing stock. Null IDs are rejected.

The UUID is the cancellation handle. Existing catalog reservations already store
the reserved quantity, so no second reservation model is needed. Queue membership
represents the only cancellable state; there is no running/completed state yet.
Two submissions of the same design create separate jobs and IDs.

## Contracts and limits

Machine.enqueue must reject before mutation. Catalog.reserve must likewise fail
without mutation, quoteCost must be read-only, and release must reliably remove
an existing reservation once. Original implementations meet these contracts.
Rollback handles a machine's reported rejection; it cannot repair an arbitrary
custom implementation that mutates and then throws, or whose release fails.
The service assumes exclusive ownership of its products, a fresh job per factory
creation, and single-threaded in-memory use. There is no durable transaction,
hardware execution, concurrent access or recovery from process failure.

JobQuote changes quoteJob's return type from FabricationJob<F>. Java callers now
use `.job()` to inspect the job and `.cost()` for the catalog quotation. Rates
still live in the original jobs; catalogs delegate to those rates. App consumes
the quote but preserves its previous printed output and command-line behavior.
Historical Stage 5 examples use the earlier return type.

## Run the shared scenario

In IntelliJ run `sdp.assignment2.WorkflowDemo.main` with no arguments or one of
`printing`, `laser`, `vinyl`. The same generic workflow quotes, submits, cancels,
then attempts cancellation again. It prints queue size and available stock after
each step. For the default design:

| Family | Quote | Stock before | Stock submitted | Stock cancelled |
| --- | --- | --- | --- | --- |
| Printing | 8.50 | 1000 grams | 950 grams | 1000 grams |
| Laser | 7.00 | 20 sheets | 19 sheets | 20 sheets |
| Vinyl | 2.70 | 2000 cm | 1920 cm | 2000 cm |

Queue size changes 0 -> 1 -> 0 and stays 0 after repeated cancellation. Run
`sdp.assignment2.App.main` for the original quotation-only CLI.
