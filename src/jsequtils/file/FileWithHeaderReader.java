/*
 * Copyright 2013 Tomasz Konopka.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsequtils.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 *
 * The objective of the class is to separate header from data coming from a
 * reader (eg when reading from a file). Lines starting with some string at the
 * beginning of the stream are treated as a header. All subsequent lines are
 * treated as normal data lines that can be accessed via readLine() as in a
 * BufferedReader.
 *
 *
 * @author Tomasz Konopka
 */
public class FileWithHeaderReader extends BufferedReader {

    // headerdef is a string that marks header lines, e.g. # in vcf files
    private final String headerdef;
    // headerlines will store each header line from a file
    final ArrayList<String> headerlines = new ArrayList<String>();

    /**
     * General constructor for reader of file with a header. Works based 
     * on a BufferedReader.
     * 
     * The constructor already starts reading, and skips lines that start with 
     * some string. The header lines are stored for future lookup.
     * 
     * @param in
     * 
     * input reader.
     * 
     * @param headerdef
     * 
     * definition of header lines
     * 
     * @param saveheader     
     * 
     * true to keep header lines in memory for future lookup. false to just ignore them. 
     * 
     * @throws IOException
     */
    public FileWithHeaderReader(Reader in, String headerdef, boolean saveheader) throws IOException {
        super(in);
        this.headerdef = headerdef;

        int hlen = headerdef.length();
        char[] cbuf = new char[hlen];
        super.mark(hlen + 4);
        int nowread = super.read(cbuf);
        while (nowread == hlen && nowread > -1 && headerdef.equals(new String(cbuf))) {
            String s = new String(cbuf) + super.readLine();
            if (saveheader) {
                headerlines.add(s);
            }
            super.mark(hlen + 4);
            nowread = super.read(cbuf);
        }

        // backtrack a little so that next readLine return a data row
        super.reset();
    }
    
    /**
     * Creates a reader separating header and data.
     *
     * With this constructor, the header is kept in memory until explicitly
     * cleared.
     *
     * @param in
     * @param headerdef
     * @throws IOException
     */
    public FileWithHeaderReader(Reader in, String headerdef) throws IOException {
        this(in, headerdef, true);
    }

    /**
     * Use this to get rid of the saved header. Once this is called, the header
     * will no longer be accessible.
     *
     */
    public void clearHeader() {
        headerlines.clear();
        headerlines.trimToSize();
    }

    /**
     *
     * @return
     *
     * number of header lines observed by this reader.
     *
     */
    public int getNumHeaderLines() {
        return headerlines.size();
    }

    /**
     *
     * @param index
     * @return
     *
     * the header line stored at a particular index
     *
     */
    public String getHeaderLine(int index) {
        if (index < headerlines.size() && index >= 0) {
            return headerlines.get(index);
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     *
     * the string that was used to skip the header in this reader. All header
     * lines should start with this string.
     *
     */
    public String getHeaderDef() {
        return headerdef;
    }

    @Override
    public String readLine() throws IOException {
        return super.readLine();
    }
}
