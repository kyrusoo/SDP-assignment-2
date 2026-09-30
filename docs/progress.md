# Development progress

## Stage 1 - Committed baseline

Verified commit `87a6142aa2380eadd334357752026f753c7b0026`
(`feat: implement initial fabrication jobs without factories`). It contains
the working App, Design, PrintingJob, LaserJob, VinylJob and initial-problems.md.
Before editing, `git diff HEAD -- src pom.xml docs` was empty. App directly
constructed jobs and repeated the booking check in each branch. Baseline CLI
runs confirmed the original quotation behavior. The earlier setup commit is
`7da12e7`.

## Stage 2 - Implemented, awaiting review/commit

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

Next stage: Stage 3 - Factory Method. Not started.
