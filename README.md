Maven JMeter Plugin
===================

The Maven JMeter Plugin allows you to automate JMeter tests in Maven.


Basic Usage
-----

### Add the plugin to your project

* Add the plugin to the build section of your pom's project :

    <plugin>
        <groupId>com.mtvi.arc</groupId>
        <artifactId>jmeter-maven-plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
        <configuration>
            <executorHome>${project.build.directory}/jmeter</executorHome>
            <executorLogs>${project.build.directory}/jmeter-results</executorLogs>
            <sourcePath>${jmeter.common.int.tests.path}</sourcePath>
        </configuration>
        <executions>
            <execution>
                <id>jmeter</id>
                <phase>integration-test</phase>
                <goals>
                    <goal>jmeter</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
