[![JDK](http://img.shields.io/badge/JDK-v8.0-yellow.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License](http://img.shields.io/badge/License-Apache_2-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# akka-demo

This is a simple demonstration of [Akka](https://akka.io/).

The artifect will scan the current folder to parse files with extension '.txt' or '.log' and count the number of words in files.

## Build from Source
### Prerequisite
- JDK 8
- Gradle > 4.4

### Build project

	gradle build

### Build executable jar

	gradle fatJar

## License

The Spring Framework is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).