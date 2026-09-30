# Final requirements audit

Audited against the assignment summary in root AGENTS.md. Assignment 2.docx is
not present in the workspace, so this is not an independent check of the original
brief's exact wording. Current implementation commit: 0af0397; final documentation
is awaiting review/commit. All Java classes named below are in
`src/main/java/sdp/assignment2`; tests are in `src/test/java/sdp/assignment2`.

## Objective and domain

The README's 3-by-4 matrix identifies twelve concrete products, including nine
original products. FabricationJob, Machine and MaterialCatalog are distinct
product types. Jobs calculate requirements; machines maintain queues and enforce
material limits; catalogs manage stock/reservations. Printing uses PLA grams,
laser uses whole sheets, vinyl uses roll length and embroidery uses thread
meters plus hooping time. These behavioral differences are documented in
original-products.md and fourth-family.md. The Client needs a consistent family
and shared preparation policy, which motivates both patterns.

## Part A - Without factories: evidenced

Commit 87a6142 contains the working App, Design and original three jobs. Inspected
its actual App source: direct constructors and three repeated booking checks are
present. docs/initial-problems.md records concrete-class coupling, client changes
for new families, duplicated validation and mixed selection/processing. This
commit survives as an ancestor of the original-system baseline and extension.
The setup commit 7da12e7 is not treated as the working baseline.

## Part B - Factory Method: evidenced

FabricationJob is the Product. PrintingJob, LaserJob and VinylJob are the original
concrete products. JobCreator.prepareJob validates input, invokes its protected
abstract createJob and rejects estimated time above 120 minutes. The three
original JobCreator subclasses override creation. Embroidery adds a fourth.
All concrete fabrication factories call prepareJob, so the pattern remains active.
OriginalFamiliesTest.factoriesKeepCreatorBookingPolicy and
bookingBoundaryAcceptedButNextMinuteRejected verify business behavior.
FactoryRegistry.select is a static selection utility, not this pattern.

## Part C - Abstract Factory: evidenced

FabricationFactory<F> declares createJob, createMachine and createMaterialCatalog.
PrintingFactory, LaserFactory and VinylFactory supply the original families;
EmbroideryFactory supplies the extension. OriginalFamiliesTest's three
*ProductsAndWorkflow methods assert all nine original concrete product types
and then exercise collaboration. EmbroideryTest.factoryCreatesAllEmbroideryProductsAndQuotes
covers all three extension products.

## Part D - Compatibility: evidenced within stated limits

FabricationFactory<F>, FabricationService<F>, FabricationJob<F>, Machine<F> and
MaterialCatalog<F> share one bounded family type. The service accepts one factory
and retains its machine/catalog in private final fields with no replacement
setters. Production factories return matching products. No runtime family-string
check or unchecked cast establishes compatibility.
FamilyCompatibilityTest.matchingFamilyCompilesAndMixedFamilyDoesNot successfully
compiles a printing/printing call and rejects printing/laser with identical
compiler options and classpath. All original workflow tests also verify matching
units, stock and costs. Generics cannot validate arbitrary custom implementations'
units, behavior or ownership; that limitation is explicitly documented.

## Part E - Runtime selection: evidenced

FactoryRegistry.select uses the command-line family name. App.runQuotation and
WorkflowDemo.run capture the selected factory's generic type. FabricationService
contains no family-name selection. OriginalFamiliesTest runtime-selection methods
and unknownFamilyRejected, plus EmbroideryTest.registrySelectsEmbroideryWithNormalizedInput,
cover selection and rejection. App keeps default and four-argument behavior.

## Part F - Three operations: evidenced

FabricationService.quoteJob collaborates with a prepared job and catalog to return
JobQuote without mutation. submitJob reserves material and enqueues the job,
releasing on enqueue rejection. cancelJob removes an eligible queued job before
release and forgets its submission ID. WorkflowDemo demonstrates the same scenario
for every family. Tests verify all three operations, repeat/foreign cancellation,
stock failure, physical capacity rejection and rollback. In particular:
ServiceAbstractionTest.machineRejectionRollsBackReservation,
stockFailureNeverCallsMachine and nonRemovableJobKeepsReservation verify failure
ordering using independent test products.

## Part G - Fourth family: evidenced

Original-system commit 86a3450 precedes extension commit 0af0397. The exact commit
comparison matches docs/fourth-family.md: six production classes, one test file
and the report added; registry, App usage text and progress modified. Service,
interfaces and existing business workflows did not change. EmbroideryTest runs
the complete quote/submit/cancel scenario through the unchanged service.

## Part H - UML: artifacts complete; student explanation remains

docs/final-system.puml and its rendered SVG/PNG show all four families, product
interfaces, concrete products, Creator/subclasses, abstract/concrete factories,
Client and relationships. Pattern roles are marked. The service is the pattern
Client; App/WorkflowDemo are entry points. Generic family bindings are explained
in the diagram note. The original three-family diagram remains preserved.
The student still needs to demonstrate understanding of these relationships;
generating a diagram and notes does not establish that understanding.

## Part I - Automated tests: evidenced

Final Maven run: 29 tests, 0 failures, 0 errors, 0 skipped. Surefire reports under
target/surefire-reports record 18 OriginalFamiliesTest, 5 ServiceAbstractionTest,
1 FamilyCompatibilityTest and 5 EmbroideryTest methods. This exceeds the required
15 without counting assertions or temporary smoke harnesses as extra tests.
Coverage includes all original families and concrete products, compatibility,
runtime selection, all three operations, more than two negative cases, extension
behavior and a Client running solely on interface test implementations.
ServiceAbstractionTest.clientWorksThroughOnlyTestProductImplementations even
returns a catalog price different from the fake job's price to check delegation.

## Part J - Meaningful Git history: evidenced

Inspected commit file statistics and stage content. Nine substantive development
commits exist, excluding the setup commit and this uncommitted documentation:

| Commit | Actual milestone |
| --- | --- |
| 87a6142 | Working no-factory system and initial problems |
| 84a4cae | Typed product interfaces and family markers |
| 035911d | Creator workflow and concrete creators |
| c04d5be | Machines, material catalogs and state behavior |
| 41b46f0 | Abstract Factory and typed service |
| 0988423 | Runtime factory registry |
| 3506e8a | Collaborating business operations |
| 86a3450 | Original-system automated tests and UML |
| 0af0397 | Fourth-family extension and tests |

Read-only merge-base checks returned exit 0 for 87a6142 -> 86a3450 and
86a3450 -> 0af0397. Thus development stages are preserved, and extension follows
the completed original system rather than being retroactively split from it.

## Remaining limitations and review items

- Original DOCX unavailable: verify this audit against the instructor's original
  brief before submission; no additional rubric rules have been invented.
- Oral explanation must be practiced by the student.
- The simulation is single-threaded, in memory and has no hardware execution.
  Rollback assumes enqueue rejects before mutation and release succeeds; arbitrary
  broken custom implementations and process failure are not transactional.
- Final documentation is uncommitted until the student reviews and saves it.
  No implementation gap was found against the supplied requirement summary.
