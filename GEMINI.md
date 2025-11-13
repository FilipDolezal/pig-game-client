# Gemini Project: sp-client

## Project Overview

This project is the client-side implementation of a client-server application for playing the game "Pig". The client is a Java Swing application.

The server-side implementation is located in a separate project. The requirements for the client are located in the `docs` directory.

## Building and Running

This project does not use a build management tool like Maven or Gradle. It can be built and run using an IDE like IntelliJ IDEA or from the command line.

### From IntelliJ IDEA

1.  Open the project in IntelliJ IDEA.
2.  The IDE should automatically detect the project structure.
3.  Create a run configuration for the `com.example.spclient.Main` class.
4.  Build and run the project.

### From the Command Line

1.  **Compile the code:**
    ```bash
    javac -d out src/main/java/com/example/spclient/*.java
    ```
2.  **Run the application:**
    ```bash
    java -cp out com.example.spclient.Main
    ```

## Development Conventions

The project follows standard Java conventions. The source code is located in `src/main/java`, resources in `src/main/resources`, and tests in `src/test/java`.

The application uses the Swing framework for the graphical user interface.