<br>
<hr />
<h1>Using Generated Classes</h1>
<ul><li>A generated class contains one record (one CSV row) of data<br>
</li><li>The examples below show how to accomplish the same tasks as the <a href='BasicExamples.md'>basic examples</a> using generated classes instead of <code>CSVDict</code>.<br>
<hr />
<br><br>
<b>The generated classes define simple accessors to the CSV file. The field to use as the key is hard-coded by the class generator, and then the desired value of the key is the single argument to the pseudoconstructor of the generated class:</b>
<pre><code>    int keyVal = getFooID();<br>
    GeneratedFooAccessor fooData = GeneratedFooAccessor.getInstance(keyVal);<br>
</code></pre>
<b>The instance of the generated class contains the values from the selected row of the CSV file, which<br>
are accessed directly, using the actual names from the header row in the CSV file:</b>
<pre><code>    float adjustedFooFactor = baseFoo * fooData.AdjustmentFactor; // "AdjustmentFactor" is from the CSV header<br>
</code></pre>
<b>Get some info from a specified record (row).<br>
<pre><code>    public float[] getStationLocation(String stationID)<br>
        throws CSVDictException<br>
    {<br>
        GeneratedStationInfo station = GeneratedStationInfo.getInstance(stationID);<br>
        float[] location = new float[2];<br>
<br>
        // Note that "LatDegrees" and "LonDegress" are the actual<br>
        // column namesfrom the header row in the CSV file that was<br>
        // used to generate GeneratedStationInfo.  This feature<br>
        // becomes important when dealing with "wide" CSV files.<br>
        location[0] = stationInfo.LatDegrees;<br>
        location[1] = stationInfo.LonDegrees;<br>
      <br>
        return location;<br>
    }<br>
</code></pre></b><br><br>
<b>Get some info for all records.</b><br>
The code for this method is exactly the same as the example in <a href='BasicExamples.md'>basic examples</a>.  But note that in this case, when <code>getStationLocation()</code> is called, the <code>getInstance()</code> method of the generated class is called.  In most cases it would be more efficient to use CSVDict to process all records because for each new record, <code>getInstance()</code> is using CSVDict to read the entire CSV file.<br>
<pre><code>    public Map&lt;String, float[]&gt; getAllLocations()<br>
        throws CSVDictException<br>
    {<br>
        Map&lt;String, float[]&gt; locations = new HashMap&lt;String, float[]&gt;();<br>
        for (String stationID : stationInfo.getColumn("StationID")) {<br>
<br>
            // calling method from previous example<br>
            float[] stationLoc = getStationLocation(stationID);<br>
<br>
            locations.put(stationID, stationLoc);<br>
        }<br>
        return locations;<br>
    }<br>
</code></pre>