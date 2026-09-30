# Makerspace Factory Patterns

Java 21 / Maven classroom simulation of a university makerspace. Work units,
credits, rates, stock and machine limits are fictional. The final system includes
printing, laser, vinyl and embroidery.

## Product families

| Product | Printing | Laser | Vinyl | Embroidery |
| --- | --- | --- | --- | --- |
| FabricationJob | PrintingJob | LaserJob | VinylJob | EmbroideryJob |
| Machine | PrintingMachine | LaserMachine | VinylMachine | EmbroideryMachine |
| MaterialCatalog | PrintingMaterials | LaserMaterials | VinylMaterials | EmbroideryMaterials |

JobCreator's final preparation workflow calls its subclass-overridden createJob
and enforces the booking limit: this is Factory Method. FabricationFactory creates
three related product types; each concrete factory delegates job preparation to
its Creator: this is Abstract Factory.

FabricationService retains one factory-created machine and catalog and uses only
interfaces. A shared family parameter prevents ordinary mixed-family calls.
FactoryRegistry selects the family from external input at initialization.
Generics cannot prove arbitrary implementations behave correctly; tests also
exercise concrete collaborations and interface-only test implementations.

## Build and test

Use a JDK 21 and Maven (there is no wrapper):

~~~powershell
mvn test
java -cp target/classes sdp.assignment2.App laser
java -cp target/classes sdp.assignment2.App laser "Desk sign" 2 10
java -cp target/classes sdp.assignment2.WorkflowDemo vinyl
~~~

On this machine Maven and Java are not on PATH. The commands actually used are:

~~~powershell
$env:JAVA_HOME = 'C:/Users/duzel/.jdks/ms-21.0.12.1'
& 'C:/Program Files/JetBrains/IntelliJ IDEA 2026.2.2/plugins/maven-plugin/lib/maven3/bin/mvn.cmd' -B '-Dmaven.repo.local=target/stage2-check/m2' test
& 'C:/Users/duzel/.jdks/ms-21.0.12.1/bin/java.exe' -cp target/classes sdp.assignment2.App laser
~~~

The local-cache option is an environment workaround, not a project requirement.
The first build needs access to Maven Central for plugins and test dependencies.
JUnit is test-only. The JavaCompiler compatibility test requires a full JDK.

In IntelliJ, reload Maven after opening pom.xml, set Project SDK and Maven runner
JRE to JDK 21, and run Maven Lifecycle > test (or the src/test/java directory).
Run App.main for quotations or WorkflowDemo.main for the complete scenario.

## Run behavior

App accepts no arguments (printing demo), one family, or four arguments:
family, design name, quantity, work units per item. Family names are trimmed and
case-insensitive. Unknown families, invalid input and jobs above 120 minutes
print an error/usage and exit 1.

Default design: Desk sign, quantity 2, work units 10.

| Family | Minutes | Material | Credits |
| --- | --- | --- | --- |
| printing | 65 | 50 grams PLA | 8.50 |
| laser | 15 | 1 sheet | 7.00 |
| vinyl | 11 | 80 cm vinyl | 2.70 |
| embroidery | 25 | 10 meters thread | 6.75 |

WorkflowDemo accepts no arguments or one family. Quotation does not mutate stock
or queues. Submission reserves material then enqueues; rejection rolls back the
reservation. Cancellation removes an eligible job and releases once. Repeated or
unknown cancellation returns false. Operations are single-threaded, in memory,
with no actual machine execution. Custom products must honor the documented
mutation/failure contracts.

## Automated evidence and UML

Final verification: **29 JUnit tests, 0 failures, 0 errors, 0 skipped**.

- OriginalFamiliesTest: 18 tests, covering all nine factory products, all original
  workflows, selection, booking boundaries, stock failure, capacity rejection,
  reservation/queue safety and negative scenarios.
- ServiceAbstractionTest: 5 tests using only test implementations of product and
  factory interfaces, including catalog-driven pricing, retention and rollback.
- FamilyCompatibilityTest: 1 test compiling a matching-family control and
  rejecting the equivalent mixed-family call with the same JDK/classpath.

Reports are generated in target/surefire-reports. Test count refers to JUnit
methods, not individual assertions. Earlier temporary checks are historical
evidence, not part of this suite. EmbroideryTest adds 5 tests for product creation,
selection, the complete workflow, booking rejection and machine capacity.

[Editable UML](docs/final-system.puml) |
[SVG diagram](docs/final-system.svg) |
[PNG diagram](docs/final-system.png)

The diagram labels both patterns, the service as Client, the product interfaces,
all twelve concrete products, creators, factories and their relationships.
Use the SVG for zooming. Dashed triangle arrows implement interfaces; solid
triangle arrows extend a class. Other arrows indicate retained references or
creation/use dependencies. Queue/stock helper internals are omitted for clarity.

## Development record

See [progress](docs/progress.md), [initial problems](docs/initial-problems.md),
[Factory Method](docs/factory-method.md), [Abstract Factory](docs/abstract-factory.md),
[runtime selection](docs/runtime-selection.md), and
[business operations](docs/business-operations.md).

Stage documents describe their historical API at the time. The current quoteJob
returns JobQuote<F>; use job() for product details and cost() for the quotation.
The no-factory baseline remains in commit 87a6142. The original system was
completed at 86a3450 and embroidery added afterward at 0af0397.

See the [verified extension report](docs/fourth-family.md),
[requirements audit](docs/requirements-audit.md), and
[oral-defense notes](docs/oral-defense.md). The historical three-family UML
remains in docs/original-system.puml. Stage 10 documentation awaits review/commit.
