# Oral-defense practice notes

Use these prompts to practice in your own words, with the code open. Reading
these notes is not evidence that you can explain or modify the design.

## Why Factory Method?

Open JobCreator. prepareJob is the shared final workflow: validate, create, check
the booking limit, return. createJob is protected and overridden by subclasses.
The workflow uses the returned product's estimatedMinutes, so creation is part of
real business logic. Contrast that with FactoryRegistry.select, which merely
maps a runtime name to a factory. Identify where PrintingFactory delegates to
PrintingJobCreator.prepareJob and why the preparation policy is not bypassed.

## Why Abstract Factory and product families?

Open FabricationFactory: it creates three related product types. A printing job
requests grams of PLA; its machine checks a spool limit; its catalog reserves
PLA grams. Laser sheets and vinyl lengths require different rules. Trace one
factory's three products and explain why selecting them independently is risky.

## What enforces compatibility?

Explain F extends Family and show Machine<PrintingFamily> accepting only a
FabricationJob<PrintingFamily>. Open the compiler test's matching and mismatched
calls. Explain that the service gets its products from one factory and stores
them without setters. Also state the limit: a programmer can still write a
misbehaving implementation; generic labels do not prove correct material units.

## Where does runtime selection happen?

Trace App -> FactoryRegistry -> generic runQuotation -> FabricationService.
The wildcard means the family is initially unknown to App; the helper captures
that single type as F. Explain why the service needs no switch when another
family is added. No raw types or unchecked casts are needed.

## Trace each business operation

Quotation creates a prepared job and asks the catalog for a cost, without stock
or queue changes. Submission reserves before enqueueing and releases if the
machine rejects. Cancellation releases only after successful removal and deletes
the submission ID, so it cannot release twice. Walk through the rollback test
and state its contract: enqueue must throw before changing the queue.

## What changed for embroidery?

Six classes were added: marker, job, machine, catalog, Creator and factory. The
registry gained one case; App gained a family name in usage text. The service
and interfaces did not change. Show the real 86a3450..0af0397 diff and extension
report. Explain thread meters, hooping time and the 6.75-credit default quote.

## Read the UML and demonstrate evidence

Locate the Client, Factory Method Creator, Abstract Factory and each product
interface in final-system.svg. Explain implementation triangles versus class
inheritance triangles and creation dependencies versus retained references.
Run Maven test and WorkflowDemo with embroidery, then another family. Explain
why 29 tests includes real workflow/negative tests and not just getters.
Be ready to describe the no-factory problems from commit 87a6142 and the current
single-threaded/in-memory limitations without claiming production guarantees.
