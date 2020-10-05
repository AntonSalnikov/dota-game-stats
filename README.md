bayes-dota
==========

This is the [task](TASK.md).

**Java version** - *11*

**Spring Boot version** - *2.2.6.RELEASE* 


## How to build the service

To build the project run - ```mvn clean install```.

To build project without tests run - ```mvn clean install -Dskip.unit.tests=true -Dskip.integration.tests=true```


## To run application locally

1. Setup environment by starting PostgreSQL ```$ cd docker-compose/dev``` and ```$ docker-compose up```
2. Run application from shell ```java -jar target/dota-game.jar```
