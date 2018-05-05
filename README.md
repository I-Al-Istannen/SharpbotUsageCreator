## About

This is a small kotlin program to generate a markdown file containing descriptions
of all commands in sharpbot.


## How it works

It isn't quite complex, as it just clones the sharpbot repo, finds all command js files
and reads the `exports.info` file every command has.

As some commands have more than one export or use some makeshift functions that build
that section, some tweaks may need to be applied. These are currently:

* Run `Nashorn` on the object as Json is not exactly very happy with some quirks of the
  language
* Run predefined transformers on the more complex classes that bring it into a state 
  `Nashorn` can evaluate

After that the resulting `JsonObject` is thrown at some `CommandFormatter` that takes
care of building Markdown from it, using a pseudo Kotlin DSL to make generating it
easier.


## Usage

### Requirements
The program requires `Java 9` or newer, as only these versions support `ES6`.

### Invocation
`java -jar <Path to downloaded/built jar>`  

This will clone the repository to a temp directory, run the program and delete the 
directory again.

### Options
* `java -jar <Path to downloaded/build jar> --help`  
   Shows the help

* `java -jar <Path to downloaded/build jar> --no-redownload-in-temp`  
   Tells the program to clone the repo in the current folder and NOT delete it after
   the program is done. This allows you to re-run it without making new API-Requests to
   Github.

* `java -jar <Path to downloaded/build jar> --toc`  
   Prepends a Table of contents to the output
