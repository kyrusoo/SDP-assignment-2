# Part A - Problems in the initial implementation

This version deliberately uses neither Factory Method nor Abstract Factory.
App creates PrintingJob, LaserJob, and VinylJob directly.

## 1. The client depends on concrete classes

App names all three concrete job classes and calls their constructors.
Changes to those constructors can require changes in App.

## 2. Adding a family requires changing the client

A new family needs another branch in App, another constructor call,
and another copy of the job-processing workflow.

## 3. Business validation is duplicated

The 120-minute booking-limit check appears separately in all three branches.
Changing how booking validation works requires editing multiple places.

## 4. Family selection and business processing are mixed

The same if/else chain chooses a concrete job, applies the booking policy,
and prepares the quotation output. More families make this chain longer.

## Scope of this version

This is a quotation demo only. Passing the booking check does not queue a job.
The job classes represent three families of one product type.
Machines, material catalogs, interfaces, factories, and real submission and
cancellation workflows belong to later development stages.

## Simulation assumptions

Work units represent an invented size/complexity measure, not a physical unit.
All rates, prices in credits, and the 120-minute limit are classroom examples,
not real equipment specifications or prices.