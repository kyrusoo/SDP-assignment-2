# Stage 9 - Embroidery extension

Original-system baseline: `86a3450de6166faed4607fd55514164608853bb8`
(`test: verify original workflows compatibility and architecture`). Verified
committed with clean status before extension edits. Extension commit:
`0af039741d46bd33c81aab428a78061e1f4aa671`.

## Simulation behavior

Let U be total work units and Q the item quantity. Embroidery interprets work
units as stitch-pattern complexity. Thread = U * 0.5 meters. Minutes =
ceil(4 + Q * 3 + U * 0.75), accounting for setup, hooping each item and stitching.
Cost = thread * 0.30 + minutes * 0.15 credits. The job owns all pricing rates.
These are fictional classroom assumptions.

The machine accepts at most 100 meters per job from a loaded spool. The material
catalog starts with 500 meters, permits fractional meters and uses existing
reservation accounting. EmbroideryFactory delegates job preparation to
EmbroideryJobCreator, preserving the shared booking policy.

Default quotation: 25 minutes, 10 meters, 6.75 credits. The full scenario changes
stock 500 -> 490 -> 500 and queue size 0 -> 1 -> 0. Repeated cancellation returns
false and leaves stock unchanged.

## Exact added paths

- src/main/java/sdp/assignment2/EmbroideryFamily.java
- src/main/java/sdp/assignment2/EmbroideryJob.java
- src/main/java/sdp/assignment2/EmbroideryMachine.java
- src/main/java/sdp/assignment2/EmbroideryMaterials.java
- src/main/java/sdp/assignment2/EmbroideryJobCreator.java
- src/main/java/sdp/assignment2/EmbroideryFactory.java
- src/test/java/sdp/assignment2/EmbroideryTest.java
- docs/fourth-family.md

## Exact modified paths

- src/main/java/sdp/assignment2/FactoryRegistry.java: one embroidery selection case.
- src/main/java/sdp/assignment2/App.java: add embroidery to CLI usage text only.
- docs/progress.md: record baseline, implementation and verification.

FabricationService, all product/factory interfaces, existing families, shared
queue/stock code, WorkflowDemo and the existing tests are unchanged. No business
workflow changes were required. The App edit is display text, not workflow logic.
Stage 10 updates the README and adds final four-family UML separately; those
documentation edits are not part of the extension comparison below.

The original inventory included untracked additions. In Stage 10 it was verified
against `git diff --name-status 86a3450de6166faed4607fd55514164608853bb8 0af039741d46bd33c81aab428a78061e1f4aa671`:
the exact eight added and three modified paths above match the actual commit.
Git ancestry also confirms the original baseline precedes the extension.

## Verification and running

Maven test: 29 tests, 0 failures, 0 errors, 0 skipped (24 original + 5 extension).
EmbroideryTest covers all product creation and quotation values, normalized
runtime selection, a complete service scenario including repeated cancellation,
Creator booking rejection and machine capacity rejection.

Both App and WorkflowDemo were run with embroidery and exited successfully.
In IntelliJ use JDK 21 and Program arguments `embroidery` for either main class.
Use Maven Lifecycle > test to run the complete suite.
