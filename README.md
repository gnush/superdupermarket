![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-compile.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-test.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-package.yml/badge.svg)

# SuperDuperMarket

Manage the inventory of a market. Keep track of items that should be removed based on the expiration date or the quality.

## Dependencies

* Java >= 23

## Usage

### Execute manually

1. Run `mvn package` to package a jar with maven.
2. Run
```
java -cp target/SuperDuperMarket-1.0.jar:/path/to/picocli.jar code.challenge.SIMULATION [-n DAYS] [-s DATASOURCE] [-f FILE]
```
to start a simulation.

### Parameters

| Parameter  | Description                                                                                |
|------------|--------------------------------------------------------------------------------------------|
| SIMULATION | The entry point to run                                                                     |
| DAYS       | Number of days to simulate <br>Default: 10                                                 |
| DATASOURCE | Use the standard 'static', 'csv' or 'sqlite' source for the inventory. <br>Default: static |
| FILE       | The csv file or sqlite database to use. <br>-s is ignored if -f is set                     |

| SIMULATION                                | Description                                                                                                 |
|-------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| ProductInventorySimulation                | Prints the status of the inventory on each day                                                              |
| ProductInventoryQualityObserverSimulation | Notifies about the daily quality changes of the inventory                                                   |
| ProductInventoryObserverSimulation        | Notifies about the daily quality changes and automatically removes insufficient products from the inventory |


### Execute with maven

#### Default execution configurations
Run
```
exec:exec@ID
```
to start a simulation with standard parameters.

| ID                             | Equivalent SIMULATION                     |
|--------------------------------|-------------------------------------------|
| inventory-simulation           | ProductInventorySimulation                |
| quality-simulation             | ProductInventoryQualityObserverSimulation |
| remove-insufficient-simulation | ProductInventoryObserverSimulation        |


#### Custom executions with maven

Run
```
mvn exec:java -Dexec.mainClass="code.challenge.SIMULATION" -Dexec.args="[-n DAYS] [-s DATASOURCE] [-f FILE]"
```
to start a simulation.

## Product CSV Format

1. Product Category
2. Number of additional arguments (If products should be removed based on more than the generic product properties)
3. Additional arguments (One cell for each additional argument)
4. Product label
5. Currency Identifiert (Currently only EUR and USD are supported)
6. Currency amount
7. Product quality
8. Expiration date in YYYY-MM-DD format (may be omitted if product does not expire)