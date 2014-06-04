/*
 * Copyright 2012 Tomasz Konopka.
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

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * writes an rle-encoded signal to an output stream
 *
 * @author tkonopka
 */
public class RleWriter {

    /**
     * writes an Rle-encoded version of the data to an output stream.
     *
     * @param outstream
     *
     * where data will be written to. It needs to be initialized. It is not
     * closed at the end.
     *
     * @param data
     *
     * an array with data
     *
     * @param header
     *
     * determines if "length\tvaluelabel" is written on the first line
     *
     * @param valuelabel
     *
     * the label on the second column of the output. This is ignored if header
     * is false.
     *
     */
    public static void write(OutputStream outstream, int[] data, boolean header, String valuelabel) throws IOException {

        // abandom if data is empty
        if (data == null) {
            return;
        }
        int datalen = data.length;
        if (datalen < 1) {
            return;
        }

        // The string builder will store intermediate results.
        // Writing to the outputstream will only occur in large chunks
        StringBuilder sb = new StringBuilder(1048576);

        if (header) {
            sb.append("length\t").append(valuelabel).append("\n");
        }

        int runlength = 1;
        for (int i = 1; i < datalen; i++) {
            if (data[i] != data[i - 1]) {
                // record a run                
                sb.append(runlength).append("\t").append(data[i - 1]).append("\n");
                runlength = 1;
                // if sb get's too big, write it into the outstream
                if (sb.length() > 1040000) {
                    outstream.write(sb.toString().getBytes());
                    sb = new StringBuilder(1048576);
                }
            } else {
                runlength++;
            }
        }

        // record the last run
        sb.append(runlength).append("\t").append(data[datalen - 1]).append("\n");

        // finally write the string to the output
        outstream.write(sb.toString().getBytes());

    }

    /**
     * writes an Rle using 'a0' as the standard header label for the value
     * column
     *
     * @param data
     * @param header
     */
    public static void write(OutputStream outstream, int[] data, boolean header) throws IOException {
        write(outstream, data, header, "a0");
    }

    /**
     * writes an Rle-encoded signal from non-integer numbers.
     *
     * @param outstream
     *
     * where the Rle-encoded signal will be sent. It is assumed this is
     * initialized. It will not be closed at the end.
     *
     * @param data
     * @param header
     *
     * if true, output will contain a header row "length\tvaluelabel"
     *
     * @param valuelabel
     *
     * a string giving a label to the second column of the rle signal
     *
     * @param dblformat
     *
     * format for how numbers will appear. See java DecimalFormat for details.
     *
     * @throws IOException
     */
    public static void write(OutputStream outstream, double[] data,
            boolean header, String valuelabel, DecimalFormat dblformat) throws IOException {

        // abandom if data is empty
        if (data == null) {
            return;
        }
        int datalen = data.length;
        if (datalen < 1) {
            return;
        }

        // make an output stream to hold the output prior to sending to outstream
        StringBuilder sb = new StringBuilder(1048576);

        if (header) {
            sb.append("length\t").append(valuelabel).append("\n");
        }


        int runlength = 1;
        for (int i = 1; i < datalen; i++) {
            if (data[i] != data[i - 1]) {
                // record a run using the specified format
                if (dblformat == null) {
                    sb.append(runlength).append("\t").append(data[i - 1]).append("\n");
                } else {
                    sb.append(runlength).append("\t").append(dblformat.format(data[i - 1])).append("\n");
                }
                if (sb.length() > 1040000) {
                    outstream.write(sb.toString().getBytes());
                    sb = new StringBuilder(1048576);
                }
                runlength = 1;
            } else {
                runlength++;
            }
        }

        // record the last run
        sb.append(runlength).append("\t").append(data[datalen - 1]).append("\n");

        // finally write the string to the output
        outstream.write(sb.toString().getBytes());

    }

    /**
     * writes an Rle with default header value label "a0"
     * 
     * @param outstream
     * @param data
     * @param header
     * @param dblformat
     * @throws IOException 
     */
    public static void write(OutputStream outstream, double[] data,
            boolean header, DecimalFormat dblformat) throws IOException {
        write(outstream, data, header, "a0", dblformat);
    }

    /**
     * write an Rle using standard header labels and standard encoding for double number
     * 
     * @param outstream
     * @param data
     * @param header
     * @throws IOException 
     */
    public static void write(OutputStream outstream, double[] data,
            boolean header) throws IOException {
        write(outstream, data, header, "a0", null);
    }
    
    /**
     * writes an Rle using standard encoding for double numbers
     * 
     * @param outstream
     * @param data
     * @param header
     * @param valuelabel
     * @throws IOException 
     */
    public static void write(OutputStream outstream, double[] data,
            boolean header, String valuelabel) throws IOException {
        write(outstream, data, header, valuelabel, null);
    }
}
