package ca.jrvs.apps.grep;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface JavaGrep {
  /**
   * Top level search workflow
   * @throws java.io.IOException
   */
  void proccess() throws IOException;

  /**
   * Traverse a given directory and return all files
   * @param rootDir input directory
   * @return files under the rootDir
   */
  List<File> listFiles(String rootDir);

  /**
   * Read a file and return all the lines
   *
   * Explain
   * FileReader
   * BufferedReader
   * charcter encoding
   *
   * @param inputFile file to read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  List<String> readLines(File inputFile);



  /**
   * check if a line contains the regex patter (passed by user)
   * @param line input string
   * @return true if there is a match
   */
  boolean containsPattern(String line);

  /**
   * Write lines to a file
   * Explore:
   * FileoOutputStream
   * OutputStreamWriter
   * BufferedWriter
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  void writeToeFile(List<String> lines) throws IOException;



}
