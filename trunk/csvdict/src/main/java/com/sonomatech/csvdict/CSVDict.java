/*
Copyright (c) 2012, Sonoma Technology, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions 
are met:

    * Redistributions of source code must retain the above copyright 
      notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright 
      notice, this list of conditions and the following disclaimer in 
      the documentation and/or other materials provided with the 
      distribution.

    * Neither the name of Sonoma Technology, Inc. nor the names of 
      its contributors may be used to endorse or promote products 
      derived from this software without specific prior written 
      permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF 
SUCH DAMAGE. 
*/



package com.sonomatech.csvdict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Turns a CSV file into an in-memory table that is queried using a key value and
 * a position. The key value selects the row in the table and the position
 * selects the column. A header row in the CSV file containing field names is required
 */
public class CSVDict
{
    private static final char COMMENT_CHAR = '#';

    protected LinkedHashMap<String, String[]> dict;
    private int  nCols;
    private String keyFieldName;
    private String csvFileName = "unknown";
    private String[] fieldNames;
    private HashMap<String, Integer> nameToPosMap;



    public class CSVDictException extends Exception
    {
        private static final long serialVersionUID = 0L;
        
        public CSVDictException(Exception e) { super(e); }
        public CSVDictException(String m) { super(m); }
    }



    /**
     * The constructor reads the CSV file into a LinkedHashMap of string arrays.
     *
     * @param csvfn the name of the CSV file
     * @param delimiter the column delimiter in the CSV file
     * @param keyIndex the zero-based index of the column that is to be used as
     *                 the key
     * @param cl the CSV file must be on this class loader's classpath
     * @throws CSVDict.CSVDictException
     */
    public CSVDict(String csvfn, char delimiter, int keyIndex, ClassLoader cl)
        throws CSVDictException
    {
        csvFileName = csvfn;
        InputStream is = cl.getResourceAsStream(csvFileName);
        if (is == null)
            throw new CSVDictException("class loader can't load " + csvFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            loadCSV(br, delimiter, keyIndex);
            br.close();
            is.close();
        } catch (Exception e) {
            throw new CSVDictException(e);
        }
    }
    
    /**
     * The constructor reads the CSV file into a LinkedHashMap of string arrays.
     *
     * @param csvfp full path to the csv file
     * @param delimiter the column delimiter in the CSV file
     * @param keyIndex the zero-based index of the column that is to be used as
     *                 the key
     * @throws CSVDict.CSVDictException
     */   
    public CSVDict(String csvfp, char delimiter, int keyIndex)
        throws CSVDictException
    {     
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvfp));
            loadCSV(br, delimiter, keyIndex);
            br.close();
        } catch (Exception e) {
            throw new CSVDictException(e);
        }
    }
    
    /**
     * The constructor reads the CSV file into a LinkedHashMap of string arrays.
     * 
     * @param br an initialized buffered reader of the CSV file
     * @param delimiter the column delimiter in the CSV file
     * @param keyIndex the zero-based index of the column that is to be used as
     *                 the key
     * @throws CSVDict.CSVDictException
     */
    public CSVDict(BufferedReader br, char delimiter, int keyIndex)
        throws CSVDictException
    {
        loadCSV(br, delimiter, keyIndex);
    }



    private String getLine(BufferedReader br)
        throws CSVDictException
    {
        String inLine;
        try {
            inLine = br.readLine();
            while (inLine != null && (inLine.isEmpty() || inLine.charAt(0) == COMMENT_CHAR || inLine.trim().isEmpty()))
                inLine = br.readLine();
        } catch (IOException ioe) {
            throw new CSVDictException(ioe);
        }
        return inLine;
    }



    private String[] parseLine(BufferedReader br, String delimRegex)
        throws CSVDictException
    {
        String inLine = getLine(br);
        if (inLine == null)
            return null;
        
        return inLine.split(delimRegex); 
    }



    private void loadCSV(BufferedReader br, char delimiter, int keyIndex)
        throws CSVDictException
    {
        //dict = new LinkedHashMap<String, String[]>();
        dict = new LinkedHashMap<>();
        String delimStr = new Character(delimiter).toString();
        String delimRegex = delimStr + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";        

        // read away header line and validate key index
        fieldNames = parseLine(br, delimRegex);
        // build name-to-pos map
        nameToPosMap = new HashMap<String, Integer>();
        for (int pos = 0; pos < fieldNames.length; pos++)
            nameToPosMap.put(fieldNames[pos], pos);
        nCols = fieldNames.length;
        if (keyIndex >= nCols || keyIndex < 0)
            throw new RuntimeException("trying to key CSV file with " + nCols + " columns on column " + (keyIndex + 1));
        keyFieldName = fieldNames[keyIndex];

        // read rows into Hashtable using column at keyIndex as the key column
        String[] row = null;
        while ((row = parseLine(br, delimRegex)) != null)
            dict.put(row[keyIndex], row);
    }



    /**
     * This method is for accessing the value as a String 
     * 
     * @param key the value of the key field that selects the row
     * @param pos the zero-based position in the row of the desired value
     * @return the selected cell in the CSV file as a String, or null if cell
     *         is empty
     * @throws CSVDictException if no row is found that matches key
     *         value or pos is greater than number of columns
     */
    public String getString(String key, int pos)
        throws CSVDictException
    {
        if (pos >= nCols)
            throw new CSVDictException(new IllegalArgumentException("pos (" + pos + ") >= number of columns (" + nCols + ")"));

        String[] selectedRow = dict.get(key);
        if (selectedRow == null)
            throw new CSVDictException("no row in " + csvFileName + " with " + keyFieldName + " value " + key);

        return selectedRow[pos];
    }
    
    
    
     /**
     * This method is for accessing the value as a Float.
     *
     * @param key the value of the key field that selects the row
     * @param pos the zero-based position in the row of the desired value
     * @return the selected cell in the CSV file as a Float, or null if cell is
     *         empty
     * @throws CSVDictException if no row is found that matches key
     *         value or pos is greater than number of columns or
     *         val cannot be interpreted as a float
     */
    public Float getFloat(String key, int pos)
        throws CSVDictException
    {
        String str = getString(key, pos);
        if (str == null || str.trim().isEmpty())
            return Float.NaN;
        try {
            return Float.valueOf(str);
        } catch (NumberFormatException e) {
            return Float.NaN;
        }
    }



     /**
     * This method is for accessing the value as an int.
     *
     * @param key the value of the key field that selects the row
     * @param pos the position in the row of the desired value
     * @return the selected cell in the CSV file as an int, or null
     *         if selected cell is empty
     * @throws CSVDictException if no row is found that matches key
     *         value or pos is greater than number of columnsor val
     *         cannot be interpreted as an int
     */
    public Integer getInteger(String key, int pos)
        throws CSVDictException
    {
        String str = getString(key, pos);
        if (str == null || str.trim().isEmpty())
            return Integer.MAX_VALUE;
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }



    /**
     * @return the number of rows in the CSV file not including the header row
     */
    public int getRowCount()
    {
        return dict.size();
    }



    /**
     *
     * @return the field names form the header row in the CSV file
     */
    public String[] getFieldNames()
    {
        return fieldNames;
    }

    /**
     * Gets the specified column from the CSV file
     * @param colPos zero-based column number
     * @return the values of the specified column as a String array
     */
    public String[] getColumn(int colPos)
    {
        if (colPos >= fieldNames.length)
            return null;
        String[] col = new String[dict.size()];
        int rNum = 0;
        for (String[] row : dict.values())
            col[rNum++] = row[colPos];
        return col;
    }
    
    /**
     * Gets the specified column from the CSV file
     * @param fieldName the field name in the header row specifying the column to get
     * @return the values of the specified column as a String array
     */
    public String[] getColumn(String fieldName)
    {
        int colPos = 0;
        while((!(fieldNames[colPos].equals(fieldName))) && colPos < fieldNames.length)
            colPos += 1;
        return getColumn(colPos);
    }
    
    /**
     * Gets the specified column from the CSV file, and filters the list 
     * based on whether or not the row contains the filterValue in the filterColumn.
     * An example would be, select all values in column fieldName where values in Column filterColumn == filterValue.
     * @param fieldName the field name in the header row specifying the column to get
     * @param filterColumn the field name in the header row specifying 
     * the column which contains the filterValue
     * @param filterValue the value in the column, columnWithFilterValues, on which to filter
     * @return the values of the specified column as a String array
     */
    public String[] getFilteredColumn(String fieldName, String filterColumn, String filterValue)
    {
        int colPos = getColPos(fieldName);
        int filterColPos = getColPos(filterColumn);
        return getFilteredColumn(colPos, filterColPos, filterValue);
    }
    
    /**
     * Gets the specified column from the CSV file, 
     * filtered for rows which contain the filterValue in the filterCol.
     * See getFilteredColumn with String arguments for an example.
     * @param colPos zero-based column number
     * @param filterColPos zero-based column number
     * @param filterValue the value in the column, filterColPos, on which to filter
     * @return the values of the specified column as a String array
     */
    public String[] getFilteredColumn(int colPos, int filterColPos, String filterValue)
    {
        if (colPos >= fieldNames.length || filterColPos >= fieldNames.length){
            return null;
        }
            
        List<String> col = new ArrayList<String>();
        for (String[] row : dict.values()){
            if(row[filterColPos].equals(filterValue)){
                col.add(row[colPos]);
            }
        }
        
        String[] items = col.toArray(new String[col.size()]);
        return items;
    }
    
    
    /**
     * Gets the col number of the given field
     * 
     * @param fieldName
     * @return the position of the column
     */
    public int getColPos(String fieldName)
    {
        Integer pos = nameToPosMap.get(fieldName);
        return pos == null ? -1 : pos;
    }
}

