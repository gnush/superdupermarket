![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-compile.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-test.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-package.yml/badge.svg)

# SuperDuperMarket

Simulate the inventory management of a store.

Keep track of commodities that should be removed based on the expiration date or the quality.
Commodities may change in quality over time. Commodity quality change is based on rules.
There is a generic rule set predefined for unrecognized commodities, as well as specialised rule sets for cheese, wine and sets of interlocking building bricks.
Additional rule sets can be created by adding new classes or can be dynamically constructed in a modular way.

Execute a simulation spanning multiple days.

## Requires

* Java >= 23

## Usage

### Execute manually

#### Without runtime dependencies

Run `mvn package` to package a jar with maven, then run
```
java --enable-native-access=ALL-UNNAMED -cp target/SuperDuperMarket-1.0.0.jar:/PATH/TO/picocli-4.7.6.jar io.github.gnush.Simulation [OPTIONS]
```
to start a simulation.
Include the `picocli.jar` from your local maven repository, usually found under `$HOME/.m2`.

#### Bundled Jar

Run `mvn compile assembly:single` to package a jar including the runtime dependencies with maven, then run
```
java --enable-native-access=ALL-UNNAMED -jar target/SuperDuperMarket-1.0.0-jar-with-dependencies.jar [OPTIONS]
```
to start a simulation.

### Parameters

Use `--help` to print the parameter overview on your command line.

| Parameter      | Description                                                                                             |
|----------------|---------------------------------------------------------------------------------------------------------|
| -e, --exec     | The simulation to execute. Check table below for possible values. <br>Default: inventory                |
| -n, --days     | Number of days to simulate <br>Default: 10                                                              |
| -d, --startDay | The first day of the simulation. <br> Format: YYYY-MM-DD                                                |
| -c, --clean    | Insufficient Commodities will be removed from the inventory at the end of each simulation day           |
| -s, --source   | Use the standard 'static', 'csv', 'sqlite' or 'hibernate' source for the inventory. <br>Default: static |
| -f, --file     | The csv file or sqlite database to use. <br>Use in combination with -s csv\|sqlite                      |
| --delimiter    | The cell delimiter of the csv file. <br>Use when providing a .csv file with -f                          |
| -h, --help     | Print a help message                                                                                    |

| SIM        | Description                                                                                                    |
|------------|----------------------------------------------------------------------------------------------------------------|
| inventory  | Prints the status of the inventory on each day                                                                 |
| quality    | Notifies about the daily quality changes of the inventory                                                      |
| autoremove | Notifies about the daily quality changes and automatically removes insufficient Commodities from the inventory |

### Populate Hibernate Database

Use `io.github.gnush.PopulateDatabase` to populate the database used by hibernate with default data.
Supply the argument `fresh` to purge the existing commodity entries.

### Execute with maven

#### Default execution configurations
Run
```
mvn exec:exec@SIM
```
to start the respective simulation with default parameters.

#### Custom executions with maven

Run
```
mvn exec:java -Dexec.mainClass="io.github.gnush.Simulation" -Dexec.args="[OPTIONS]"
```
to start a simulation.

## Commodity CSV Format

1. Commodity Category
2. Number of additional arguments (Can be used if commodities should be removed based on more than the generic rules)
3. Additional arguments (One cell for each additional argument)
4. Commodity label
5. Currency Identifier (Currently EUR and USD are supported)
6. Currency amount
7. Commodity quality
8. Expiration date in YYYY-MM-DD format (can be omitted if commodity does not expire)