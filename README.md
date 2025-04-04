![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-compile.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-test.yml/badge.svg)
![Compiles status](https://github.com/gnush/superdupermarket/actions/workflows/maven-package.yml/badge.svg)

# SuperDuperMarket

Manage the inventory of a market. Keep track of items that should be removed based on the expiration date or the quality.

## Dependencies

* Java >= 23

## Usage

### Execute manually

Run `mvn package` to package a jar with maven, then run
```
java -cp target/SuperDuperMarket-1.0.jar:/PATH/TO/picocli-4.7.6.jar code.challenge.Simulation [OPTIONS] [SIM]
```
to start a simulation.
Include the `picocli.jar` from your local maven repository, usually found under `$HOME/.m2`.

### Parameters

Use `--help` to print the parameter overview on your command line.

| Parameter      | Description                                                                                                           |
|----------------|-----------------------------------------------------------------------------------------------------------------------|
| -e, --exec     | The simulation to execute. Check table below for possible values. <br>Default: inventory                              |
| -n, --days     | Number of days to simulate <br>Default: 10                                                                            |
| -d, --startDay | The first day of the simulation. <br> Format: YYYY-MM-DD                                                              |
| -c, --clean    | Insufficient products will be removed from the inventory at the end of each simulation day                            |
| -s, --source   | Use the standard 'static', 'csv' or 'sqlite' source for the inventory. <br>Ignored when -f is set <br>Default: static |
| -f, --file     | The csv file or sqlite database to use. <br>-s is ignored if -f is set                                                |
| --delimiter    | The cell delimiter of the csv file. <br>Use when providing a .csv file with -f                                        |
| -h, --help     | Print a help message                                                                                                  |

| SIM        | Description                                                                                                 |
|------------|-------------------------------------------------------------------------------------------------------------|
| inventory  | Prints the status of the inventory on each day                                                              |
| quality    | Notifies about the daily quality changes of the inventory                                                   |
| autoremove | Notifies about the daily quality changes and automatically removes insufficient products from the inventory |

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
mvn exec:java -Dexec.mainClass="code.challenge.Simulation" -Dexec.args="[OPTIONS] [SIM]"
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