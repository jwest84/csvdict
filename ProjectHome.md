
---

# **[CSVDict](Overview.md)** #
  * developed by [Sonoma Technology](http://www.sonomatech.com)

---

### CSVDict is a small Java library for querying CSV files, comprised of 3 classes ###
  * [CSVDict](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/CSVDict.html)       - parses CSV files and stores them in a dictionary structure that allows specifying a record (a row) using a value of a specified key column (assumes unique string keys). Cell values are retrieved using the key and a column position number.
  * [IntKeyCSVDict](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/IntKeyCSVDict.html) - subclass of `CSVDict` that uses integer keys.
  * [RowClassSourceGenerator](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/RowClassSourceGenerator.html) - a Java program to generate classes from CSV files which allow accessing cells using the CSV header values instead of column numbers.

**Consider the SQL query:**

```
    SELECT
       CrownShapeFactor,
       PDWood 
    FROM
       botanical_species
    WHERE
       ScientificName = 'Athyrium filix-femina'
```

**Making this query using the**CSVDict**class looks like this:**

```
    final int    scientificNameCol      = 3;  // these are zero-based CSV column numbers
    final int    crownShapeFactorCol    = 4;
    final int    pdWoodCol              = 7;

    // load the CSV file, specifying comma as delimiter and scientificNameCol as key column
    CSVDict speciesData = new CSVDict("botanical_species.csv", ',', scientificNameCol);

    // this is the key value which we will use to select the CSV row
    final String ladyFernScientificName = "Athyrium filix-femina";

    // here we access cells by key value (selects the row) and column number
    float ladyfernCSF = speciesData.getFloat(ladyFernScientificName, crownShapeFactorCol);
    float ladyfernPDWood = speciesData.getFloat(ladyFernScientificName, pdWoodCol);
```

**The csvdict package also includes a source code generator that encodes access to a row of a CSV file in a java class, using column names as member variable names. This mechanism allows a row to be accessed by key value; and a cell within the row, by the column name.  Using one of these looks like this:**

```
    GeneratedSpecies ladyfernData = GeneratedSpecies.getInstance("Athyrium filix-femina");
    float ladyfernCSF = ladyfernData.CrownShapeFactor;
    float ladyfernPDWood = ladyfernData.PDWood;
```

**So with the generated class, the need to count columns in CSV files is eliminated.  Moreover, examining the** `ladyfernData` **variable in an IDE will yield a select box containing all column names:**

![https://lh5.googleusercontent.com/-Jst7Vfo1Lxo/UMjd27URLOI/AAAAAAAAAo8/-LkOGyGrB0Y/s912/screen_3.png](https://lh5.googleusercontent.com/-Jst7Vfo1Lxo/UMjd27URLOI/AAAAAAAAAo8/-LkOGyGrB0Y/s912/screen_3.png)

### [javadoc](http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/index.html) ###