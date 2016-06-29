# Wage Calculator

Sample web tool for calculating salaries from hour marking data. Parses a CSV file containing the hour markings and calculates
wages for every employee. Might be available in http://192.81.223.24:8080.

## Design Goals
How to make it as simple and robust as possible?
- Time is managed in units of 15 minutes, which happens to work with the CSV format.
- Money is handled in a custom type, with only minimal operations, no decimal numbers in the whole application.
- More lines for test than the code, at least without counting the comments.
- No fancy UI, just the simplest possible thing to get this online.
- No state (no db or anything, but also every variable is immutable).

## Running Instructions
Start Scalatra server over jetty (requires Scala, sbt and internet connection for downloading dependencies):
```
~: cd Wage-calculator
Wage-calculator: ./sbt
> jetty:start
```
Run tests
```
Wage-calculator: sbt test
```
