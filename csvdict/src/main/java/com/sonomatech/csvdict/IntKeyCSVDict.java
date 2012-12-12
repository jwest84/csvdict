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
import java.io.FileNotFoundException;
import java.util.Iterator;



/**
 * A subclass of CSVDict that uses integer keys.
 */
public class IntKeyCSVDict extends CSVDict
{
    /**
     * Create the IntKeyCSVDict from a BufferedReader initialized to read from
     * the CSV file.
     *
     * @param br the reader
     * @param delimiter the delimiter character in the CSV file
     * @param keyIndex the zero-based index of the column that is to be used as the key
     * @throws com.sti.justice.csvdict.CSVDict.CSVDictException
     */
    public IntKeyCSVDict(BufferedReader br, char delimiter, int keyIndex)
        throws CSVDictException
    {
        super(br, delimiter, keyIndex);
        keyIsInt();
    }

    /**
     * Create the IntKeyCSVDict from the specified CSV file.
     *
     * @param csvFileName the name of the CSV file (file must be on classpath)
     * @param delimiter the delimiter character in the CSV file
     * @param keyIndex the zero-based index of the column that is to be used as the key
     * @throws com.sti.justice.csvdict.CSVDict.CSVDictException
     */
    public IntKeyCSVDict(String csvFileName, char delimiter, int keyIndex)
        throws CSVDictException
    {
        super(csvFileName, delimiter, keyIndex);
        keyIsInt();
    }

    private boolean keyIsInt()
        throws CSVDictException
    {
        Iterator<String> dictKeysIter = dict.keySet().iterator();
        if (dictKeysIter.hasNext())
            try {
                Integer.parseInt(dictKeysIter.next());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Trying to instance a IntKeyCSVDict with non-int key");
            }
        else
            throw new CSVDictException(new FileNotFoundException("Trying to instance a IntKeyCSVDict with empty CSV file"));
        return true;
    }


    /**
     * This method is for accessing the value as a String
     *
     * @param key the value of the key field that selects the row
     * @param pos the zero-based position in the row of the desired value
     * @return the selected cell in the CSV file as a String, or null if cell
     *         is empty
     * @throws com.sti.justive.csvdict.CSVDict.CSVDictException if no row is found
     *         that matches key value or pos is greater than number of columns
     */
    public String getString(int key, int pos)
        throws CSVDictException
    {
        return getString(key + "", pos);
    }




    /**
     * This method is for accessing the value as a Float
     *
     * @param key the value of the key field that selects the row
     * @param pos the zero-based position in the row of the desired value
     * @return the selected cell in the CSV file as a Float, or null if cell
     *         is empty
     * @throws com.sti.justice.csvdict.CSVDict.CSVDictException if no row is found
     *         that matches key value or pos is greater than number of columns or
     *         val cannot be interpreted as a float
     */
    public Float getFloat(int key, int pos)
        throws CSVDict.CSVDictException
    {
        return getFloat(key + "", pos);
    }




    /**
     * This method is for accessing the value as a Float
     *
     * @param key the value of the key field that selects the row
     * @param pos the zero-based position in the row of the desired value
     * @return the selected cell in the CSV file as an Integer, or null if cell
     *         is empty
     * @throws com.sti.justice.csvdict.CSVDict.CSVDictException if no row is found
     *         that matches key value or pos is greater than number of columns or
     *         val cannot be interpreted as an integer
     */
    public Integer getInteger(int key, int pos)
        throws CSVDict.CSVDictException
    {
        return getInteger(key + "", pos);
    }
}
