jersey2-akka-java
=================

An example asynchronous REST API written in Java using Jersey 2 and Akka 2.3

[![CI](https://github.com/pofallon/jersey2-akka-java/workflows/CI/badge.svg)](https://github.com/pofallon/jersey2-akka-java/actions/workflows/ci.yml)
[![Dependabot enabled](https://img.shields.io/badge/Dependabot-enabled-brightgreen.svg)](https://github.com/dependabot)

Key concepts
------------
* Instantiating an Akka ActorSystem at server startup and injecting it into each request
* Fulfilling an asynchronous Jersey REST service invocation using Akka actors

How to run the example
----------------------
1. Clone this repository
2. Run `mvn cargo:run`
3. Visit `http://localhost:9090/examples/doubler/2` via curl or your favorite browser

The application uses the Cargo Maven plugin to run Apache Tomcat 10.1.43, which provides full support for Jakarta EE and modern Java versions.

**Note:** The legacy `mvn tomcat7:run` command is still available but uses the older Tomcat 7.0.47 which has compatibility issues with modern Jakarta EE libraries and may not work correctly.

Prerequisites
-------------
* JDK 17.x
* Maven 3.x
