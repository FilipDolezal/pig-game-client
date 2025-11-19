cmake_minimum_required(VERSION 3.10)
project(sp-client)

# Find Java components
find_package(Java 14 REQUIRED COMPONENTS Development)
if (NOT Java_FOUND)
    message(FATAL_ERROR "Java not found.")
endif()
message(STATUS "Java compiler: ${Java_JAVAC_EXECUTABLE}")
message(STATUS "Java archiver: ${Java_JAR_EXECUTABLE}")
message(STATUS "Java runtime: ${Java_JAVA_EXECUTABLE}")

set(CMAKE_JAVA_TARGET_VERSION "14")
set(CMAKE_JAVA_SOURCE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/src")
set(CMAKE_JAVA_CLASSES_DIR "${CMAKE_CURRENT_BINARY_DIR}/classes")

# Find all Java source files
file(GLOB_RECURSE JAVA_SOURCES "${CMAKE_JAVA_SOURCE_DIR}/*.java")

# Define a stamp file for compilation
set(COMPILE_STAMP_FILE "${CMAKE_CURRENT_BINARY_DIR}/compile.stamp")

# Add a custom command for compilation
add_custom_command(
    OUTPUT ${COMPILE_STAMP_FILE}
    COMMAND ${CMAKE_COMMAND} -E make_directory ${CMAKE_JAVA_CLASSES_DIR}
    COMMAND ${Java_JAVAC_EXECUTABLE} -d ${CMAKE_JAVA_CLASSES_DIR} -source ${CMAKE_JAVA_TARGET_VERSION} -target ${CMAKE_JAVA_TARGET_VERSION} ${JAVA_SOURCES}
    COMMAND ${CMAKE_COMMAND} -E touch ${COMPILE_STAMP_FILE}
    DEPENDS ${JAVA_SOURCES}
    COMMENT "Compiling Java sources"
    VERBATIM
)

# Add a custom target for compilation
add_custom_target(compile DEPENDS ${COMPILE_STAMP_FILE})

# Define JAR properties
set(JAR_NAME "${PROJECT_NAME}")
set(JAR_FILE "${CMAKE_CURRENT_BINARY_DIR}/${JAR_NAME}.jar")
set(MAIN_CLASS "Main")

# Add a custom command for creating the JAR
add_custom_command(
    OUTPUT ${JAR_FILE}
    COMMAND ${Java_JAR_EXECUTABLE} cfe ${JAR_FILE} ${MAIN_CLASS} -C ${CMAKE_JAVA_CLASSES_DIR} .
    DEPENDS compile
    COMMENT "Creating JAR archive"
)

# Add a custom target for creating the JAR
add_custom_target(jar DEPENDS ${JAR_FILE})

# Add a target to run the application
add_custom_target(run
    COMMAND ${Java_JAVA_EXECUTABLE} -jar ${JAR_FILE}
    DEPENDS jar
    COMMENT "Running the application"
)

# Add a project-clean target
add_custom_target(project-clean
    COMMAND ${CMAKE_COMMAND} -E remove -f ${JAR_FILE}
    COMMAND ${CMAKE_COMMAND} -E remove_directory ${CMAKE_JAVA_CLASSES_DIR}
    COMMENT "Cleaning up build files"
)
