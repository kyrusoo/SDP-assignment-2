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

## Stage 7 - Implemented, awaiting review/commit

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

Next stage: Stage 8 - Original-system automated tests and UML baseline. Not started.
