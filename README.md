![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-compile.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-test.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-package.yml/badge.svg)

# SuperDuperMarket

Manage the inventory of a market. Keep track of items that should be removed based on the expiration date or the quality.

## Dependencies

Java >= 23

## How to run

Run `mvn package` to package a jar with maven.

Run 
```
java -cp target/SuperDuperMarket-1.0.jar code.challenge.SIMULATION_ENTRY [DATASOURCE [FILE]]
```
to start a simulation.

| SIMULATION_ENTRY                         | Objective                                                                                                   |
| ---------------------------------------- |-------------------------------------------------------------------------------------------------------------|
|ProductInventorySimulation                | Prints the status of the inventory on each day                                                              |
|ProductInventoryQualityObserverSimulation | Notifies about the daily quality changes of the inventory                                                   |
|ProductInventoryObserverSimulation        | Notifies about the daily quality changes and automatically removes insufficient products from the inventory |

TODO: add optional parameters explanation