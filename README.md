[![JDK](http://img.shields.io/badge/JDK-v8.0-yellow.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[![License](http://img.shields.io/badge/License-Apache_2-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# akka-demo

This is a simple demonstration of [Akka](https://akka.io/).

The artifect will scan the current folder to parse files with extension '.txt' or '.log' and count the number of words in files.

## Build from Source
### Prerequisite
- JDK 8
- Gradle > 4.4

### Get the Source Code

```
git clone https://github.com/ycchen1981/akka-demo.git
cd akka-demo
```

### Build from the Command Line
To compile, test, build all jars, distribution zips, and docs use:

Linux:
```
./gradlew build
```

Windows:
```
gradlew.bat build
```

### Build executable jar
```
gradle fatJar
```

Once built successfully, you could find the artifact **akka-demo-[version]-SNAPSHOT.jar** in the folder **./akka-demo/build/libs**.
Copy this artifact to the target folder and execute it to scan files:

```
java -jar ./akka-demo-0.0.1.jar

2 file(s) will be processed and displayed respectively.
The file name: meeting memo.txt, results =>
        vmware = 2
        vsan = 2
......
The file name: intel white paper.txt, results =>
        iteration = 32
        intel = 17
        2000 = 11
......
All 2 file(s) were processed and displayed respectively.
[INFO] [09/17/2018 11:09:15.402] [WordCount-akka.actor.default-dispatcher-3] [akka://WordCount/user/terminator] akka://WordCount/user/fileScanner has terminated, shutting down system
```

## License

The Spring Framework is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).