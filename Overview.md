<br>
<hr />
<h1>Motivation</h1>
<hr />

<b><i>The developers at STI hope that other developers will find this simple package useful.</i></b>

This library was conceived as part of a scientific modeling project. The models that were coded make use of various empirical data sets -- measurements and calculated values resulting from measurements. The data can change from time to time when new measurements or new methods of calculation are provided by scientists. CSV files seemed to be a useful means of representing the data. The scientists are themselves familiar with editing CSV files, and would not have to touch the code.<br>
<br>
<b>Key-based Record Access</b><br>
In the course of designing a framework for these models a common pattern emerged of accessing a record (row) in a csv file based on the value of a certain field (column) in that file. There are various open-source java CSV libraries available, but since none facilitates this pattern, we created CSVDict.<br>
<br>
<b>Auto-named Field Access</b><br>
Some of the CSV files have many columns. It is tedious to count columns in order to access the desired cell. Also, to increase code readability there would have to be a proliferation of constants to represent the column numbers of fields in the CSV files. Thus the idea arose of a code generator that could create java classes that used the CSV column header values as member data variable names.<br>
<br><br><br><br>
<hr />
<h1>Features</h1>
<hr />

<ul><li><b>CSV file loading</b> -  can be loaded via pathname or as a resource<br>
</li><li><b>Key-based Record Access</b> - as described above and on <a href='http://code.google.com/p/csvdict'>home page</a>, keys can be ints or Strings<br>
</li><li><b>Auto-Named Field Access</b> - as described above and on <a href='http://code.google.com/p/csvdict'>home page</a>, field names are column header values<br>
</li><li><b>Type-specific accessors</b> - values are requested as String, Integer, or Float<br>
</li><li><b>Miscellaneous convenience methods</b>
<ul><li>get whole column by name or position<br>
</li><li>get column filtered on another column's value<br>
</li><li>get all field names<br>
</li><li>get column position from field name<br>
</li><li>get number of rows<br>
<br><br>
</li></ul></li><li><h3><a href='UserGuide.md'>User Guide</a></h3>
<br><br>
<a href='http://www.sonomatech.com'><img src='http://lh3.googleusercontent.com/-532qqKyC4Xs/UMoXfKf0TOI/AAAAAAAAApc/yp7IIMJS5DQ/s218/STI_logo_web.jpg' /></a>