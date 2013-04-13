# Maven Custom Logging

This is a maven extension that allows some customization of the maven console output.

## Features
- Remove the log threshold from the output (ie. `[INFO]`)
- Prepend a timestamp to each log line
- Coloring of whole lines or segments of lines using the [JANSI](https://github.com/fusesource/jansi) library
- Alter text output or remove output

## Setup
- Clone the project and build using Maven
- Copy the JAR `maven-custom-logging.jar` to your `$MAVEN_HOME/lib/ext` directory
- Run any maven build you would like and you should see some colors!

## How to use my own configuration?
There are 3 different ways for you to supply a custom configuration file:
- supply this system property `custom.logging.configuration` at the commandline, with a value pointing to your config file
- supply the environment variable `MAVEN_CUSTOM_LOGGING_CONFIG` and put the path to your config file as it's value
- put config file `custom-logging.yml` in your `$MAVEN_HOME/conf` directory

#### Note: [Sample Configuration File][1]

## FAQ
- Lines I am attempting to color are not being colored?
  - This can be caused by your regex pattern being incorrect
  - This can be caused by a maven plugin that is running in your build decided to not use Maven's Logger
  - This can be caused by a maven plugin that has switched out the `System.out` to a different instance

- What colors/options are available for the `render` configuration?
  - Colors and Options available are [here][2]
  - Examples of render syntax is [here][3]

- How can I see phases of the filtering of the log lines?
  - If you specify the `-X` switch at the commandline this will put Maven in debug mode and A LOT of information will be printed.
  - This will show the before and after of each log line we filtered

- How can I temporary shutoff the log filtering?
  - Just supply `custom.logging.off` system property and you should see all the original maven output

## Maven versions Tested and working
  - [x] 3.0.4
  - [x] 3.0.5




  [1]: https://github.com/born2snipe/maven-custom-logging/blob/master/src/main/resources/config/default.yml "sample"
  [2]: https://github.com/fusesource/jansi/blob/master/jansi/src/main/java/org/fusesource/jansi/AnsiRenderer.java#L127 "colors"
  [3]: https://github.com/fusesource/jansi/blob/master/jansi/src/test/java/org/fusesource/jansi/AnsiRendererTest.java "syntax"
