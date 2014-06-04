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

import java.io.File;

/**
 * obtain extensions from files
 *
 *
 * @author tkonopka
 */
public class FileExtensionGetter {

    /**
     * get the last extension for a file
     *
     * @param f
     *
     * a file
     *
     * @return
     *
     * a string of the file extension (i.e. characters after the last dot)
     *
     * e.g hello.txt -> txt, hello.txt.gz -> gz, hello -> [empty string]
     *
     */
    public static String getExtension(File f) {
        if (f == null) {
            return null;
        }
        return(getExtension(f.getName()));        
    }
    
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }        
        int lastdot = filename.lastIndexOf(".");
        if (lastdot >= 0 && lastdot < filename.length() - 1) {
            return (filename.substring(lastdot + 1, filename.length()));
        }
        return "";
    }

    public static String[] getExtensionSplit(String filename) {

        String[] ans = {"", "", ""};

        if (filename == null) {
            return ans;
        }

        // first check for compression extension
        if (filename.endsWith(".zip") || filename.endsWith(".bz2")) {
            ans[2] = filename.substring(filename.length() - 3, filename.length());
            filename = filename.substring(0, filename.length() - 4);
        } else if (filename.endsWith(".gz")) {
            ans[2] = "gz";
            filename = filename.substring(0, filename.length() - 3);
        }

        // find conventional extension
        int lastdot = filename.lastIndexOf(".");

        // give up if there is no extension
        if (lastdot < 0) {
            ans[0] = filename;
            return ans;
        }

        // there is an extension, so get it
        ans[0] = filename.substring(0, lastdot);
        ans[1] = filename.substring(lastdot + 1, filename.length());

        return ans;
    }

    /**
     * splits a file name into three parts: name, extension, and another
     * extension if the file is compressed.
     *
     *
     * @param f
     *
     * a file
     *
     * @return
     *
     * Some examples:
     *
     * hello.txt.gz -> (hello) (txt) (gz) bye.log -> (bye) (log) ()
     * something.very.complicated -> (something.very) (complicated) ()
     * archive.zip -> (archive) () (zip)
     *
     */
    public static String[] getExtensionSplit(File f) {

        String[] ans = {"", "", ""};

        if (f == null) {
            return ans;
        }
        return(getExtensionSplit(f.getName()));
    }
}
