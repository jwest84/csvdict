<br>
<hr />
<h1>Generating Classes</h1>
<hr />
<b>The class generator (RowClassSourceGenerator) generates classes from CSV files. It is meant to be used as a command-line tool using the main() method, and is driven by a properties file which lists:</b>
<ul><li>the CSV files<br>
</li><li>the index of the key to be used when instancing the generated class that corresponds to the CSV file<br>
</li><li>whether the key is an int or a String.<br>
<b>There is a more detailed explanation of the properties file in the <a href='http://csvdict.googlecode.com/svn/trunk/csvdict/javadoc/com/sonomatech/csvdict/RowClassSourceGenerator.html'>javadoc</a> for RowClassSourceGenerator.</b><br><br>
Here is an example of a properties file:<b><br>
<pre><code>    species.csv = com.sti.fuelbed.calculators.SpeciesIV,0,int<br>
    woody_parameters.csv = com.sti.fuelbed.calculators.WoodyIV,0,int,1,String, _ByName<br>
</code></pre></b><br>
<b>Here is an example of invoking the class generator from an ant build script:</b>
<pre><code>          &lt;target name="-pre-compile"&gt;<br>
              &lt;java classname="com.sti.justice.csvdict.ClassSourceGenerator"&gt;<br>
              &lt;classpath&gt;<br>
                  &lt;pathelement location="lib\csvdict-1.0.jar"/&gt;<br>
              &lt;/classpath&gt;<br>
              &lt;arg line="generated\csg.properties csv generated" /&gt;<br>
              &lt;/java&gt;<br>
          &lt;/target&gt;<br>
</code></pre>
<br>
<b>Here is an example of a generated class:</b></li></ul>

<pre><code>package com.sti.fuelbed.calculators;<br>
<br>
import com.sti.justice.csvdict.CSVDict.CSVDictException;<br>
import com.sti.justice.csvdict.IntKeyCSVDict;<br>
import java.util.HashMap;<br>
<br>
<br>
public class BasalAccumIV {<br>
<br>
    private static HashMap cache = new HashMap();<br>
<br>
    public static BasalAccumIV getInstance(int MapID)<br>
        throws CSVDictException<br>
    {<br>
        BasalAccumIV instance = cache.get(MapID);<br>
        if (instance == null) {<br>
            instance = new BasalAccumIV(MapID);<br>
            cache.put(MapID, instance);<br>
        }<br>
        return instance;<br>
    }<br>
<br>
<br>
<br>
    public int MapID;<br>
    public float Wl;<br>
    public float SV;<br>
    public float PD;<br>
    public float HC;<br>
<br>
    private BasalAccumIV(int MapID)<br>
        throws CSVDictException<br>
    {<br>
        this.MapID = MapID;<br>
        IntKeyCSVDict dict = new IntKeyCSVDict("basalAccumulations_ivDb.csv", ',', 0);<br>
        Wl = dict.getFloat(MapID, 1);<br>
        SV = dict.getFloat(MapID, 2);<br>
        PD = dict.getFloat(MapID, 3);<br>
        HC = dict.getFloat(MapID, 4);<br>
    }<br>
}<br>
</code></pre>