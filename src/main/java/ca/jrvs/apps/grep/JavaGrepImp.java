package ca.jrvs.apps.grep;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.*;

public class JavaGrepImp implements JavaGrep {

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String regex;
  private String rootPath;
  private String outFile;
  private Pattern pattern;
  private Matcher matcher;


  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    BasicConfigurator.configure();
    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {

      javaGrepImp.proccess();


    } catch (Exception ex) {
      javaGrepImp.logger.error("Error: Unable to process", ex);

    }

  }


  public String getRootPath() {
    return this.rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }


  public String getRegex() {
    return this.regex;
  }


  public void setRegex(String regex) {
    this.regex = regex;

  }

  public String getOutFile() {
    return this.outFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;

  }

  /**
   * Top level search workflow
   *
   * @throws IOException
   */
  @Override
  public void proccess() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    List<File> files = listFiles(getRootPath());
    for (File file : files){
      for (String line: readLines(file)){
        if (containsPattern(line)){
          matchedLines.add(line);
        }
      }
    }
    writeToeFile(matchedLines);

  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {
    File rootDirPath = new File(rootDir);
    List<File> listfiles = new ArrayList<File>();
    for (File dir : rootDirPath.listFiles()) {
      if (dir.isDirectory()) {
        for (File files : dir.listFiles()) {
          listfiles.add(files);
        }
      }
    }
    return listfiles;
  }

  /**
   * Read a file and return all the lines Explain fileReader: BufferedReader: character encoding:
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) {
    BufferedReader reader;
    List<String> lines = new ArrayList<String>();
    try {
      reader = new BufferedReader(new FileReader(inputFile));
      String line = reader.readLine();

      while (line != null) {
        lines.add(line);
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

  /**
   * check if a line contains the regex patter (passed by user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {

    pattern = Pattern.compile(getRegex());
    matcher = pattern.matcher(line);
    return matcher.matches();
  }

  /**
   * Write lines to a file Explore: FileoOutputStream OutputStreamWriter BufferedWriter
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToeFile(List<String> lines) throws IOException {
    FileOutputStream output = new FileOutputStream(getOutFile());
    OutputStreamWriter writer = new OutputStreamWriter(output);
    BufferedWriter buffer = new BufferedWriter(writer);

    for (String line : lines) {
      buffer.write(line);
      buffer.newLine();
    }
    buffer.close();
  }
}


