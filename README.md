# Utilities

A help library for commonly used function

## Libraries

* floating-point-comparison - helper for double number comparisons
* writer - helper for saving data to different file formats
* reader - helper for reading data in different file formats
* file - helper for file system operations
* cmd - helper for program execution in terminal
* format - helper for different types formatting to string output
* string-formatter - helper for string formatting
* benchmark - provides benchmarking functionality

## Install

To include libraries add the following to your `:dependencies`:

### Leiningen
```clj
[org.iiasa/utilities-clj "1.1.0-SNAPSHOT"]
```

### Maven

```xml
<dependency>
  <groupId>org.iiasa</groupId>
  <artifactId>utilities-clj</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

examples:

(use 'utilities-clj.floating-point-comparison)
(double= 1.0 1.000000000000001)
=> true
(double= [1.0 0] [1.000000000000001 1])
=> false

(use 'utilities-clj.writer)
(csv-file parent-dir file.csv [["a_11" "a_12" "a_13"] ["a_21" "a_22" "a_23"]])

(use 'utilities-clj.reader)
(read-file file.asc)

(use 'utilities-clj.file)
(walk source-folder #".*\.asc")

(use 'utilities-clj.cmd)
(terminal {:short-desc "Cmd app example"
           :args '("argument")
           :args-desc [["argument" "Cmd argument"]]
           :execute ((fn[_](println ("Hello, world!"))))})

## References
  [API Documentation] [doc] folder at the root of this distribution

## License

Licensed under [MIT](http://opensource.org/licenses/MIT)
