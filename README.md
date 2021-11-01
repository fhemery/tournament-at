# tournament Project

This project is an example of Hexagonal architecture put in place
for [Agile Tour Bordeaux 2021](http://agiletourbordeaux.fr/). It showcases what __hexagonal architecture__ looks like
and how to test the application.

This project uses [Quarkus](https://quarkus.io/) and [Kotlin](https://kotlinlang.org/)

It requires Java 11+ installed

## Launch app in dev mode

```shell script
./gradlew quarkusDev
```

## Testing

The `src/test` folder contains several ways of testing the app :

* `acceptance` corresponds to the tests that are testing the hexagon. Input are the _commands_ and _queries_. The limits
  are the _output ports_, that are stubbed
* `integration` corresponds to the full integration test, from the API to the real mongo database. Use it preferably
  with the CLI within quarkusDev task (`r` to launch tests)
* `controllerToPorts` are tests starting with a direct API function call (Resource manual instance) until
  the `output ports`. They are just a try, I don't believe much in them.
* `architecture` are tests run with [Archunit](https://www.archunit.org) framework to guarantee code organization

