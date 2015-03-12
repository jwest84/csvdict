# Introduction #

---


### CSVDict is a small Java library for querying CSV files, comprised of 3 classes ###
  * [CSVDict](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/CSVDict.html)       - parses CSV files and stores them in a dictionary structure that allows specifying a record (a row) using a value of a specified key column (assumes unique string keys). Cell values are retrieved using the key and a column position number.
  * [IntKeyCSVDict](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/IntKeyCSVDict.html) - subclass of `CSVDict` that uses integer keys.
  * [RowClassSourceGenerator](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/RowClassSourceGenerator.html) - a Java program to generate classes from CSV files which allow accessing cells using the CSV header values instead of column numbers.

---

[Basic Examples](BasicExamples.md)

[Using Generated Classes](UseGenClasses.md)

[Generating Classes](GenClasses.md)