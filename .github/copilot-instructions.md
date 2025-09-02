# Jersey2-Akka-Java Development Instructions

Jersey2-Akka-Java is an example asynchronous REST API written in Java using Jersey 2.23 and Akka 2.3.15. This project demonstrates instantiating an Akka ActorSystem at server startup and using it to fulfill asynchronous Jersey REST service invocations.

**Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.**

## Working Effectively

### Prerequisites and Setup
- JDK 17+ is installed and working (despite README stating JDK 7.x requirement)
- Maven 3.x is available and working
- No additional setup required - Maven will download all dependencies automatically

### Build Commands (NEVER CANCEL - Use Long Timeouts)
- **Compile only**: `mvn clean compile` -- takes 20 seconds. NEVER CANCEL. Set timeout to 60+ seconds on first run due to dependency downloads.
- **Run tests**: `mvn test` -- takes 10 seconds. NEVER CANCEL. Set timeout to 30+ seconds.
- **WARNING**: `mvn clean package` and `mvn clean install` FAIL due to Maven War Plugin incompatibility with JDK 17. Do NOT attempt these commands.

### Development Workflow
1. **Always start by compiling**: `mvn clean compile`
2. **Run tests to verify changes**: `mvn test`  
3. **Start the application**: `mvn tomcat7:run`
4. **Test functionality manually** using the validation scenarios below

### Running the Application
- **Start server**: `mvn tomcat7:run` -- starts Tomcat on port 9090. NEVER CANCEL. Set timeout to 60+ seconds.
- **Base URL**: http://localhost:9090/
- **API endpoint**: http://localhost:9090/examples/doubler/{value}
- **Stop server**: Use Ctrl+C or stop the Maven process

## Validation Scenarios

**ALWAYS test these complete end-to-end scenarios after making changes:**

### Basic Functionality Test
```bash
# Start the application first
mvn tomcat7:run

# In another terminal, test the API:
curl http://localhost:9090/examples/doubler/2
# Expected output: {"results" : 4}

curl http://localhost:9090/examples/doubler/5  
# Expected output: {"results" : 10}

curl http://localhost:9090/examples/doubler/10
# Expected output: {"results" : 20}
```

### Complete Development Validation
1. **Build and test**: `mvn clean compile && mvn test`
2. **Start application**: `mvn tomcat7:run` 
3. **Verify API responses** using curl commands above
4. **Check logs** for any ERROR messages (warnings are expected)
5. **Stop application** when testing complete

## Common Issues and Limitations

### Known Compatibility Issues
- **WAR packaging fails**: The Maven War Plugin 2.6 is incompatible with JDK 17. Commands like `mvn clean package` and `mvn clean install` will fail with "ExceptionInInitializerError" related to TreeMap comparator access.
- **Solution**: Use `mvn tomcat7:run` for development and testing. The application runs correctly despite packaging issues.

### Expected Warnings
- Jersey validation warnings during startup are normal and do not affect functionality
- Platform encoding warnings during compilation are expected
- Deprecated API warnings in test compilation are expected

## Important Notes

### Timeout Requirements
- **Compilation**: NEVER CANCEL before 60 seconds (20s typical + dependency downloads)
- **Tests**: NEVER CANCEL before 30 seconds (10s typical)
- **Application startup**: NEVER CANCEL before 60 seconds (15s typical + startup time)

### Code Structure
- **Main application**: `src/main/java/com/ofallonfamily/jersey2akka/ExampleApplication.java`
- **REST service**: `src/main/java/com/ofallonfamily/jersey2akka/ExampleService.java` 
- **Actor implementation**: `src/main/java/com/ofallonfamily/jersey2akka/DoublingActor.java`
- **Test**: `src/test/java/com/ofallonfamily/jersey2akka/DoublerTest.java`
- **Configuration**: `src/main/resources/application.conf`

### Development Best Practices
- Always run `mvn clean compile` before testing changes
- Always run `mvn test` to verify unit tests pass
- Always manually test the REST API after code changes
- Use the tomcat7 plugin for local development - it works reliably
- Do not attempt WAR packaging unless you are prepared to handle Maven plugin compatibility issues

## Project Overview

### Key Components
- **Jersey 2.23**: JAX-RS implementation for REST services
- **Akka 2.3.15**: Actor framework for asynchronous processing  
- **Round-robin router**: Distributes work across 5 DoublingActor instances
- **Jackson JSON**: Handles JSON serialization/deserialization
- **Tomcat 7 Maven Plugin**: Provides embedded Tomcat for development

### Architecture
1. `ExampleApplication` extends `ResourceConfig` and sets up Akka ActorSystem
2. `ExampleService` provides REST endpoint that sends messages to Akka actors
3. `DoublingActor` processes integer doubling requests asynchronously
4. Jersey test framework provides integration testing capabilities

This is a learning/example project demonstrating Jersey + Akka integration patterns.

## Quick Reference

### Repository Structure
```
.
├── .github/
│   └── copilot-instructions.md    # This file
├── .gitignore
├── .travis.yml                    # CI configuration
├── README.md                      # Basic project information
├── pom.xml                        # Maven configuration
└── src/
    ├── main/
    │   ├── java/com/ofallonfamily/jersey2akka/
    │   │   ├── ExampleApplication.java    # Main application entry point
    │   │   ├── ExampleService.java        # REST endpoint implementation
    │   │   └── DoublingActor.java          # Akka actor for processing
    │   └── resources/
    │       └── application.conf            # Akka configuration
    └── test/
        └── java/com/ofallonfamily/jersey2akka/
            └── DoublerTest.java            # Integration test
```

### Expected Command Output Examples

#### Successful Compile
```
[INFO] BUILD SUCCESS
[INFO] Total time: 1.373 s
```

#### Successful Test
```
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

#### Application Startup (Final Line)
```
INFO: Starting ProtocolHandler ["http-bio-9090"]
```

#### API Response Examples
```bash
$ curl http://localhost:9090/examples/doubler/2
{
  "results" : 4
}
```