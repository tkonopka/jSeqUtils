jSeqUtils
=========

jSeqUtils is java library for sequencing-related projects. It is meant to be a lightweight collection of general-purpose data structures and algorithms that may be relevant for applications in the field of genomics. The library grew gradually as I found myself reusing bits and pieces of code between projects. 

The library currently contains two main parts. The first contains functions for handling files, particularly to allow as easy a means to access compressed files as uncompressed files. This part makes use of some code from the Apache Common compress package.

The second part of the library contains data structures and helper function for holding sequence information - genomes, genomic positions,  genomic intervals, genomic variants, etc. 


Source
------

The library is developed under Netbeans. The github repo contains the full Netbeans project, including source code and javadoc documentation for all classes.

Versions
--------

The repo also has a folder with compiled jar files with dated versions of the library. If you'd like to use the library in a project, these jars are all you need.


Licence
-------


The code is released under the Apache 2.0 licence.
