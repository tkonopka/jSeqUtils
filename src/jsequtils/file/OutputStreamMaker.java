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

import apachecommons.compressors.bzip2.BZip2CompressorOutputStream;
import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * A maker of various output streams.
 * 
 *  Note: found similar factory methods in the apachecommons classes. Here, 
 * this maker allows using strings such as "stdin" or "stdout". This allows using
 * a unified factory/maker for both file and streaming applications.
 * 
 * @author tkonopka
 */
public class OutputStreamMaker {

    /**
     * creates an appropriate outputstream based on the filename
     *
     * @param filename
     * @return
     *
     * System.out if filename is "stdout" otherwise, the output stream is either
     * a gzip, bzip2 or fileoutput stream
     *
     * @throws FileNotFoundException
     * @throws IOException *
     */
    public static OutputStream makeOutputStream(String filename) throws FileNotFoundException, IOException {
        if (filename.equals("stdout")) {
            return System.out;
        }
        return makeOutputStream(new File(filename));
    }

    /**
     * creates an appropriate outputstream given directory name and filename
     *
     * @param directory
     * @param filename
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static OutputStream makeOutputStream(String directory, String filename) throws FileNotFoundException, IOException {
        if (filename.equals("stdout")) {
            return System.out;
        }
        return makeOutputStream(new File(directory, filename));
    }

    /**
     *
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static OutputStream makeOutputStream(File f) throws FileNotFoundException, IOException {

        if (f == null) {
            return System.out;
        }

        String fextension = FileExtensionGetter.getExtension(f);
        if (fextension == null) {
            return new BufferedOutputStream(new FileOutputStream(f));
        } else {
            if (fextension.equals("gz")) {
                return new GZIPOutputStream(new FileOutputStream(f));
            } else if (fextension.equals("bz2")) {
                return new BZip2CompressorOutputStream(new FileOutputStream(f));
            } else {
                return new BufferedOutputStream(new FileOutputStream(f));
            }
        }
    }
}
