<br>
<hr />
<h1>Basic Examples Using CSVDict</h1>
<ul><li>Note that some examples rely on previous examples.<br>
</li><li>Using IntKeyCSVDict is the same as using CSVDict except that the accessor methods take an int instead of a String as the first (key) argument.<br>
<hr />
<br><br>
<b>Create a static CSVDict for a relatively large CSV file.<br>
<pre><code>    private static CSVDict stationInfo;<br>
    <br>
    public CSVDict getStationInfo(File stationInfoCSVFile)<br>
        throws CSVDictException<br>
    {<br>
        if (stationInfo == null) {<br>
            stationInfo = new CSVDict(stationInfoCSVFile.getAbsolutePath(), ',', 0);<br>
        }<br>
        return stationInfo;<br>
    }<br>
  <br>
</code></pre></b><br><br>
<b>Get some info from a specified record (row).<br>
<pre><code>    private static final int LATITUDE_POS = 4;<br>
    private static final int LONGITUDE_POS = 5;<br>
<br>
    public float[] getStationLocation(String stationID)<br>
        throws CSVDictException<br>
    {<br>
        float[] location = new float[2];<br>
        location[0] = stationInfo.getFloat(stationID, LATITUDE_POS);<br>
        location[1] = stationInfo.getFloat(stationID, LONGITUDE_POS);      <br>
        return location;<br>
    }<br>
</code></pre></b><br><br>
