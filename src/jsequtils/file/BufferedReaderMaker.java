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

import apachecommons.compressors.bzip2.BZip2CompressorInputStream;
import apachecommons.compressors.gzip.GzipCompressorInputStream;
import java.io.*;

/**
 * allows to create a BufferedReader from a stdin, normal text file, gzip, or
 * bzip2 file depending on the filename extension
 *
 * Note: I think in java language this would be called a factory, but old code
 * has this class called a 'maker' not a 'factory' so let's not change the name.
 * 
 * Note: found similar factory methods in the apachecommons classes. Here, 
 * this maker allows using strings such as "stdin" or "stdout". This allows using
 * a unified factory/maker for both file and streaming applications.
 * 
 * @author tkonopka
 */
public final class BufferedReaderMaker {

    /**
     *
     * @param filename
     *
     * name of file to read from
     *
     * @return
     *
     * a BufferedReader ready to read from the given file. The BufferedReader is
     * created using different constructors depending on the extension of the
     * file
     *
     * @throws IOException
     */
    public static BufferedReader makeBufferedReader(String filename) throws IOException {
        // read from standard input in exceptional cases
        if (filename == null) {
            return new BufferedReader(new InputStreamReader(System.in));
        }
        if (filename.equals("stdin")) {
            return new BufferedReader(new InputStreamReader(System.in));
        }

        return makeBufferedReader(new File(filename));
    }

    /**
     *
     * @param directory
     *
     * location of file to read from
     *
     * @param filename
     *
     * name of file to read from
     *
     * @return
     *
     * a BufferedReader ready to read from the given file. The BufferedReader is
     * created using different constructors depending on the extension of the
     * file
     *
     * @throws IOException
     */
    public static BufferedReader makeBufferedReader(String directory, String filename) throws IOException {
        // read from standard input in exceptional cases
        if (filename == null) {
            return new BufferedReader(new InputStreamReader(System.in));
        }
        if (filename.equals("stdin")) {
            return new BufferedReader(new InputStreamReader(System.in));
        }

        return makeBufferedReader(new File(directory, filename));
    }

    /**
     *
     * @param f
     *
     * a file to read from
     *
     * @return
     *
     * a BufferedReader ready to read from the given file. The BufferedReader is
     * created using different constructors depending on the extension of the
     * file
     *
     * @throws IOException
     */
    public static BufferedReader makeBufferedReader(File f) throws IOException {
        if (f == null) {
            return new BufferedReader(new InputStreamReader(System.in));
        }

        String fextension = FileExtensionGetter.getExtension(f);
        if (fextension == null) {
            return new BufferedReader(new FileReader(f));
        } else {
            if (fextension.equals("gz")) {
                return new BufferedReader(new InputStreamReader(new GzipCompressorInputStream(new FileInputStream(f), true)));
            } else if (fextension.equals("bz2")) {
                return new BufferedReader(new InputStreamReader(new BZip2CompressorInputStream(new FileInputStream(f), true)));
            } else {
                return new BufferedReader(new FileReader(f));
            }
        }
    }
    
    public static FileWithHeaderReader makeFileWHeaderReader(File f, String headerdef) throws IOException {
        if (f == null) {
            return new FileWithHeaderReader(new InputStreamReader(System.in), headerdef);
        }

        String fextension = FileExtensionGetter.getExtension(f);
        if (fextension == null) {
            return new FileWithHeaderReader(new FileReader(f), headerdef);
        } else {
            if (fextension.equals("gz")) {
                return new FileWithHeaderReader(new InputStreamReader(new GzipCompressorInputStream(new FileInputStream(f), true)), headerdef);
            } else if (fextension.equals("bz2")) {
                return new FileWithHeaderReader(new InputStreamReader(new BZip2CompressorInputStream(new FileInputStream(f), true)), headerdef);
            } else {
                return new FileWithHeaderReader(new FileReader(f), headerdef);
            }
        }
    }
}
