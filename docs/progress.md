# Development progress

## Stage 1 - Committed baseline

Verified commit `87a6142aa2380eadd334357752026f753c7b0026`
(`feat: implement initial fabrication jobs without factories`). It contains
the working App, Design, PrintingJob, LaserJob, VinylJob and initial-problems.md.
Before editing, `git diff HEAD -- src pom.xml docs` was empty. App directly
constructed jobs and repeated the booking check in each branch. Baseline CLI
runs confirmed the original quotation behavior. The earlier setup commit is
`7da12e7`.

## Stage 2 - Committed

Verified in commit `84a4cae18e81fac2b8585e8c313bc523caa2f249` before Stage 3.

Added under `src/main/java/sdp/assignment2/`:

- Family.java
- PrintingFamily.java
- LaserFamily.java
- VinylFamily.java
- FabricationJob.java

Modified in that same package:

- App.java: interface-typed selected job, one booking check, one report method;
  direct constructor selection remains.
- PrintingJob.java, LaserJob.java, VinylJob.java: typed interface implementation,
  design/display accessors, and Override annotations; formulas unchanged.

Added documentation: `docs/product-abstractions.md` and `docs/progress.md`.
Design.java, pom.xml and docs/initial-problems.md remain unchanged. The pre-existing
user edit to .gitignore was left untouched. No Git staging or commit was performed.
No later-stage components were added.

## Verification on 2026-09-30

Environment: PowerShell, Microsoft JDK 21.0.12.1, Maven bundled with IntelliJ.
No Maven wrapper, test sources, or test dependencies exist in this repository.
The original Assignment 2.docx is not present; root AGENTS.md was read completely.

Read-only inspection used `git status --short`, `git log --oneline -10`,
`git show --stat 87a6142`, `git show 87a6142:src/main/java/sdp/assignment2/App.java`,
`git diff HEAD -- src pom.xml docs`, Get-ChildItem, and Get-Content.
`rg` was unavailable, so PowerShell file enumeration was used.

Build commands actually executed (from the project root):

```powershell
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -version
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/javac.exe' --release 21 -d target/stage2-check/baseline (Get-ChildItem src/main/java/sdp/assignment2/*.java).FullName
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/javac.exe' '-J-Duser.home=C:/Users/duzel' --release 21 -d target/stage2-check/refactored (Get-ChildItem src/main/java/sdp/assignment2/*.java).FullName
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/javac.exe' --release 21 -d target/stage2-check/verified (Get-ChildItem src/main/java/sdp/assignment2/*.java).FullName
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -B test
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -B '-Dmaven.repo.local=target/stage2-check/m2' test
git diff --check
```

Initial sandboxed javac attempts produced runnable class files but exited with
`Fatal Error: Cannot close compiler resources` (exit 3 observed on the refactored
attempt). The verified compilation outside the sandbox succeeded with exit 0.
Default Maven initially failed to create `C:\.m2\repository`. The local-cache
retry hit sandbox network restrictions; the same command outside the sandbox
completed with BUILD SUCCESS (exit 0). No JUnit tests exist, so this is build
verification, not a claim that automated unit tests passed. `git diff --check`
passed; Git reported only LF/CRLF conversion warnings.

A temporary PowerShell harness under ignored `target/stage2-check/check.ps1`
launched the JDK's java.exe with `-cp <classes> sdp.assignment2.App` for each
case below. It captured stdout, stderr and process exit codes separately.
PowerShell blocked direct .ps1 execution, so the same script text was executed
as a script block without changing system execution policy:

```powershell
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/stage2-check/baseline target/stage2-check/baseline.json
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/stage2-check/refactored target/stage2-check/refactored.json
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/stage2-check/verified target/stage2-check/verified.json
```

All 12 smoke cases matched the baseline exactly, including both output streams
and exit code. These are CLI regression checks, not JUnit tests.

| Program arguments | Actual result | Exit |
| --- | --- | --- |
| (none) | Printing: 65 minutes, 50.00 grams of PLA, 8.50 credits | 0 |
| printing | Same printing quotation | 0 |
| laser | 15 minutes, 1.00 material sheets, 7.00 credits | 0 |
| vinyl | 11 minutes, 80.00 cm of vinyl roll, 2.70 credits | 0 |
| laser "Desk sign" 2 10 | Same laser quotation | 0 |
| printing "Large sign" 10 10 | 120-minute booking-limit error and usage | 1 |
| laser "Desk sign" 0 10 | Quantity must be greater than zero; usage | 1 |
| laser "Desk sign" abc 10 | Whole-number input error and usage | 1 |
| unknown | Unknown family error and usage | 1 |
| laser "Desk sign" | Argument-count error and usage | 1 |
| laser "Desk sign" 2 0 | Work units must be greater than zero; usage | 1 |
| " LASER " | Same laser quotation after normalization | 0 |

## Run in IntelliJ

Use Project SDK Microsoft JDK 21 and run `sdp.assignment2.App.main` using the
gutter Run button. Under Run > Edit Configurations, leave Program arguments
empty for printing or enter `laser`, `vinyl`, or `laser "Desk sign" 2 10`.
Use the Maven tool window's Lifecycle > test to repeat the Maven build check.

Suggested commit: `refactor: introduce typed product interfaces and family markers`

## Stage 3 - Committed

Verified commit `035911dd0dd8c314859cae8dfb578da294f742d3` before Stage 4.

Started from a clean working tree at `84a4cae`. Re-read root AGENTS.md and inspected
the current sources, pom.xml, Git status and history. JDK remains Microsoft
21.0.12.1; Maven remains IntelliJ's bundled Maven, with no wrapper or existing
automated test sources.

Added `src/main/java/sdp/assignment2/JobCreator.java`, PrintingJobCreator.java,
LaserJobCreator.java and VinylJobCreator.java. Updated App.java to select a creator
and call its final preparation workflow. The booking policy moved entirely into
JobCreator. Added `docs/factory-method.md` and updated this progress document.
Existing products, Design, family markers, build configuration and historical
Part A/Stage 2 explanations are unchanged. No later-stage features were added.

Commands and actual results:

```powershell
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage3-check/before.json
# After refactoring and repeating the Maven command:
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage3-check/after.json
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' --class-path target/classes target/stage3-check/CreatorChecks.java
git diff --check
```

Both offline Maven builds succeeded outside the sandbox; the second compiled all
14 source files for Java 21. No JUnit tests exist. The same 12 CLI smoke cases
listed above matched the fresh Stage 2 capture exactly in stdout, stderr and exit
code (comparison of the two JSON captures).

A temporary Java check under ignored target/stage3-check verified all three
concrete product types, null-design rejection, acceptance at exactly 120 minutes,
and rejection at 121 minutes: six checks passed. Its first sandboxed source-launch
attempt could not resolve the project classes; the same command outside the
sandbox passed. These temporary checks are not a committed JUnit suite.
`git diff --check` passed with only line-ending conversion warnings.

IntelliJ: run `sdp.assignment2.App.main` with JDK 21. Use no arguments, `printing`,
`laser`, `vinyl`, or `laser "Desk sign" 2 10`. To see rejection use
`printing "Large sign" 10 10`. Quotation output and error behavior are preserved.

Suggested commit: `feat: introduce Factory Method for validated job preparation`

## Stage 4 - Committed

Verified commit `c04d5be528f5a1b4d54ef7ea27826395d94f45d4` before Stage 5.

Started from clean Git status at `035911d`; read root AGENTS.md, current Java
sources, pom.xml and Git history. No nested source/docs AGENTS.md files were found.
The existing Java 21 Maven setup and package sdp.assignment2 are preserved.

Added under src/main/java/sdp/assignment2:

- Machine.java and MaterialCatalog.java: typed product contracts.
- PrintingMachine.java, LaserMachine.java, VinylMachine.java: real queues and
  material-based per-job capacity limits.
- PrintingMaterials.java, LaserMaterials.java, VinylMaterials.java: stock,
  quotation delegation and reversible reservations.
- JobQueue.java and MaterialStock.java: package-private shared state mechanics.

Added docs/original-products.md and updated docs/progress.md. Existing Java files,
including App, Design, jobs and creators, and pom.xml were not modified.
Pricing remains owned by jobs; catalogs delegate quotation instead of copying
rates. Family assumptions and component limitations are documented in
original-products.md. No Abstract Factory, service or fourth family was added.

Commands executed from the project root:

```powershell
# Capture existing Stage 3 output before adding files:
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage4-check/before.json
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage4-check/after.json
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' --class-path target/classes target/stage4-check/ProductChecks.java
git diff --check
```

Maven compiled 24 source files and completed BUILD SUCCESS using Microsoft
JDK 21 outside the sandbox and the existing offline cache. No JUnit suite exists.
All 12 CLI cases above matched the Stage 3 capture exactly (stdout, stderr and
exit codes). The ignored temporary ProductChecks.java passed 51 assertions:
each family's quote has no mutation; reserve/release accounting; duplicate
reservation/queue rejection; ordered immutable queue snapshots; physical capacity
rejection without queue mutation; removal and repeated removal; repeated/unknown
release; empty/exact stock; and invalid initial stock. These are executable
component checks, not committed JUnit tests. git diff --check passed.

IntelliJ: run sdp.assignment2.App.main with JDK 21 and no arguments, printing,
laser, vinyl, or `laser "Desk sign" 2 10`. Output remains the quotation demo.
The new products can be exercised using the example in original-products.md;
they are not yet connected to App. No Git staging or commit was performed.

Suggested commit: `feat: implement original machines and material catalogs`

## Stage 5 - Committed

Verified commit `41b46f05fc2ebf1964278af0d2253cb00b1e1ab4` before Stage 6.

Started from clean status at c04d5be. Read root AGENTS.md completely, inspected
current contracts, App, Creator, concrete products, pom.xml and Git history.
No nested src/docs instructions were found. Java 21 and Maven remain unchanged.

Added under src/main/java/sdp/assignment2: FabricationFactory.java,
PrintingFactory.java, LaserFactory.java, VinylFactory.java, FabricationService.java.
Updated App.java to select a factory and quote through the service. Added
docs/abstract-factory.md and updated this progress file. Existing products,
creators, rates and CLI parsing/error/report code are unchanged. No registry,
submission/cancellation workflow or fourth family was added.

Actual commands:

```powershell
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage5-check/before.json
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage5-check/after.json
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' --class-path target/classes target/stage5-check/CompositionChecks.java
git diff --check
```

Maven compiled 29 Java files and reported BUILD SUCCESS. Maven and the Java source
launcher ran outside the sandbox using the existing offline cache/JDK. There are
still no JUnit tests. All 12 CLI cases matched the before capture exactly in both
output streams and exit code. The ignored temporary composition checker passed
24 assertions covering all three families, booking rejection through factories,
non-mutating quotes, independent production products, retained factory products,
once-only machine/catalog creation and immutable snapshots. These checks are not
a committed JUnit suite. git diff --check passed. The invalid generic example in
the documentation was not separately compiled.

IntelliJ: run sdp.assignment2.App.main with JDK 21. Leave Program arguments empty
or use printing, laser, vinyl, or `laser "Desk sign" 2 10`. The same quotations
now pass through Abstract Factory and FabricationService. No Git staging or
commit was performed.

Suggested commit: `feat: add Abstract Factory with typed family composition`

## Stage 6 - Committed

Verified commit `09884230ad2259663f2a72d049761c8b388d2191` before Stage 7.

Started from clean status at 41b46f0. Read root AGENTS.md completely, inspected
App, factory/service contracts, pom.xml and Git history. No nested source/docs
instructions were found. The existing Java 21 Maven setup is preserved.

Added src/main/java/sdp/assignment2/FactoryRegistry.java. Updated App.java to
delegate selection/normalization and use a generic runQuotation helper that
captures the selected factory type. Added docs/runtime-selection.md and updated
this progress file. Service, products, creators, formulas and CLI report/error
handling are unchanged. No Stage 7 operations were added.

Commands executed:

```powershell
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage6-check/before.json
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage6-check/after.json
git diff --check
```

Maven compiled 30 files for Java 21 and reported BUILD SUCCESS, using the existing
offline cache outside the sandbox. There are no existing JUnit tests. All 12 CLI
smoke cases listed earlier matched the fresh before capture exactly in stdout,
stderr and exit code, including each original family, default arguments,
normalized family input, invalid input and booking rejection. JSON comparison
reported no differences. git diff --check passed with line-ending warnings only.

IntelliJ: run sdp.assignment2.App.main with JDK 21; leave arguments empty or enter
printing, laser, vinyl, or `laser "Desk sign" 2 10`. Invalid `unknown` still exits
with code 1 and usage. No staging or commit was performed.

Suggested commit: `feat: select fabrication families from command-line arguments`

## Stage 7 - Committed

Verified commit `3506e8aff1a27930c9f97ec53f5e3520b6f90616` before Stage 8.

Started from clean status at 0988423 after reading root AGENTS.md and inspecting
service, queue/stock implementations, product contracts, App, pom.xml and history.
No nested source/docs instructions were found. Java 21 and Maven are unchanged.

Modified FabricationService.java (three operations and submitted-job tracking),
App.java (consume JobQuote), Machine.java and MaterialCatalog.java (document
failure/mutation contracts). Added JobQuote.java and WorkflowDemo.java in
src/main/java/sdp/assignment2. Added docs/business-operations.md and updated this
progress file. Existing factories, registry, creators, calculations and stock/
queue implementations are unchanged. No Stage 8 JUnit/UML work was started.

Actual commands:

```powershell
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage7-check/before.json
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& ([scriptblock]::Create((Get-Content target/stage2-check/check.ps1 -Raw))) target/classes target/stage7-check/after.json
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' --class-path target/classes target/stage7-check/WorkflowChecks.java
foreach ($family in @('printing','laser','vinyl')) {
    & 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -cp target/classes sdp.assignment2.WorkflowDemo $family
}
git diff --check
```

Maven compiled 32 Java files and reported BUILD SUCCESS. Maven and the source
launcher ran outside the sandbox with the existing cache/JDK. There are no JUnit
tests yet. All 12 App smoke cases matched previous stdout, stderr and exit code.
All three WorkflowDemo runs exited 0 and showed the stock/queue transitions in
business-operations.md. The ignored temporary WorkflowChecks.java passed 26
assertions: all original families, quote non-mutation, successful submission,
unknown/repeated cancellation, full release, insufficient stock without enqueue,
rollback for a rejecting test machine and real vinyl capacity rejection, separate
submissions of identical designs, and booking rejection without mutation.
These temporary checks are not a committed JUnit suite. git diff --check passed.

IntelliJ: run WorkflowDemo.main with JDK 21 and printing, laser or vinyl for the
three-operation scenario. App.main still accepts the original quotation arguments,
including `laser "Desk sign" 2 10`. No Git staging or commit was performed.

Suggested commit: `feat: implement quotation submission and cancellation workflows`

## Stage 8 - Committed

Verified commit `86a3450de6166faed4607fd55514164608853bb8` before Stage 9.

Started from clean Git status at 3506e8a. Read root instructions, production
sources, build configuration, ignore rules and history. The no-factory baseline
87a6142 remains in history. No nested source/docs instructions were found.

Changed pom.xml to add test-scoped JUnit Jupiter 5.11.4 and pin Surefire 3.5.4.
Added these files under src/test/java/sdp/assignment2:

- OriginalFamiliesTest.java: 18 JUnit tests for original factories, workflows,
  runtime selection, booking limits, stock failure and queue/reservation safety.
- ServiceAbstractionTest.java: 5 JUnit tests with independent test implementations
  of all product interfaces and the factory, including failed enqueue rollback.
- FamilyCompatibilityTest.java: 1 JavaCompiler test with a successful matching
  control and rejected mixed-family call using identical compiler options.

Added docs/original-system.puml and its SVG/PNG exports. Updated README.md and
this progress file. Production Java and .gitignore are unchanged. TestFamily is
only a test fixture, not the production fourth-family extension.

Executed:

```powershell
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -B '-Dmaven.repo.local=target/stage2-check/m2' test
Invoke-WebRequest -UseBasicParsing 'https://repo.maven.apache.org/maven2/net/sourceforge/plantuml/plantuml/1.2024.8/plantuml-1.2024.8.jar' -OutFile target/stage8-tools/plantuml.jar
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -jar target/stage8-tools/plantuml.jar -tsvg docs/original-system.puml
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -jar target/stage8-tools/plantuml.jar -tpng docs/original-system.puml
git diff --check
```

Maven completed BUILD SUCCESS: 24 tests, 0 failures, 0 errors, 0 skipped.
Verified Surefire text reports under target/surefire-reports. The compiler test
ran successfully with the full Microsoft JDK 21; it was not skipped. Test/plugin
downloads and rendering ran outside the sandbox. No global software was installed.
PlantUML was not found installed; a local ignored tool JAR was downloaded and its
Smetana layout rendered SVG and PNG successfully (exit 0). Inspected the PNG;
the SVG supports zooming into the full class diagram. git diff --check passed.

IntelliJ: reload Maven, select JDK 21 for the project and Maven runner, and run
Lifecycle > test or the test directory. Run App.main with `laser "Desk sign" 2 10`
for quotation or WorkflowDemo.main with printing, laser or vinyl for operations.

Suggested commit: `test: verify original workflows compatibility and architecture`

## Stage 9 - Committed

Verified extension commit `0af039741d46bd33c81aab428a78061e1f4aa671` in Stage 10.

Started from clean status at the committed Stage 8 baseline above. Read root
AGENTS.md, relevant products, factory/registry and pom.xml; checked Git history
and nested instructions. Added the six Embroidery classes and EmbroideryTest.
Modified only FactoryRegistry registration and App usage text in existing Java.
Added docs/fourth-family.md with the full baseline hash and exact changed paths;
updated this progress file. Service, interfaces and original tests are unchanged.

Executed with Microsoft JDK 21 outside the sandbox:

```powershell
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -cp target/classes sdp.assignment2.App embroidery
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -cp target/classes sdp.assignment2.WorkflowDemo embroidery
git diff --check
git diff --name-only 86a3450de6166faed4607fd55514164608853bb8
git status --short
```

BUILD SUCCESS: 29 JUnit tests, 0 failures/errors/skips. Both demos exited 0.
Quotation: 25 minutes, 10 meters thread, 6.75 credits. Workflow stock:
500 -> 490 -> 500, remaining 500 after repeated cancellation. git diff --check
passed. File inventory includes untracked additions. Extension commit comparison
will be checked after the student commits; no extension hash is invented.

IntelliJ: run App.main or WorkflowDemo.main with JDK 21 and arguments embroidery;
run Lifecycle > test for all tests. No staging/commit was performed.

Suggested commit: `feat: add embroidery family without changing business workflows`

## Stage 10 - Implemented, awaiting review/commit

Started from clean status at 0af0397. Re-read root instructions and inspected
current code, all test sources, documentation and actual commit statistics.
Assignment 2.docx is absent; audit scope is the supplied AGENTS.md summary.
Verified the extension's exact eight added/three modified paths against the
86a3450..0af0397 commit comparison and updated fourth-family.md accordingly.

Updated README.md (four-family matrix, results, final diagram and audit links),
docs/fourth-family.md and docs/progress.md. Added docs/final-system.puml, its
SVG/PNG exports, docs/requirements-audit.md and docs/oral-defense.md. Preserved
the original three-family UML. No production code, tests, build or ignore rules
changed. Nine substantive development commits exist excluding setup and this
uncommitted documentation; the no-factory baseline and later extension survive.

Commands actually executed include:

```powershell
git log --reverse --format='%h %s' --stat
git show 87a6142:src/main/java/sdp/assignment2/App.java
git diff --name-status 86a3450 HEAD
git merge-base --is-ancestor 87a6142 86a3450
git merge-base --is-ancestor 86a3450 0af0397
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -o -B '-Dmaven.repo.local=target/stage2-check/m2' test
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -jar target/stage8-tools/plantuml.jar -tsvg docs/final-system.puml
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -jar target/stage8-tools/plantuml.jar -tpng docs/final-system.puml
git diff --check
```

Both ancestry checks returned 0. Maven BUILD SUCCESS: 29 tests, 0 failures,
0 errors, 0 skipped, including the JavaCompiler compatibility control. Rendering
completed successfully using the existing local PlantUML tool; inspected the PNG.
Maven/rendering used JDK 21 outside the sandbox. git diff --check passed.

The audit maps objective and Parts A-J individually to classes, named tests,
documents and actual commits. Remaining review items: check against the absent
original DOCX, practice the oral explanation, and review/commit these final docs.
Simulation and custom-product contract limitations are explicitly documented.

IntelliJ: JDK 21; Maven Lifecycle > test. Run App.main for quotation or
WorkflowDemo.main for all operations, using printing, laser, vinyl or embroidery.
No Git staging or commit was performed.

Suggested commit: `docs: finalize UML and assignment requirements evidence`

No further implementation stage is planned. Final student review/commit remains.
