package ca.jrvs.apps.grep;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLmpIntImpl implements JavaGrepLmp {
  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String rootPath;
  private String outFile;
  private Pattern pattern;
  private Matcher matcher;
  private String regex;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    BasicConfigurator.configure();

    JavaGrepLmpIntImpl javaGrepLambdaImp= new JavaGrepLmpIntImpl();

    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {

      javaGrepLambdaImp.proccess();


    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error("Error: Unable to process", ex);

    }


  }


  /**
   * Top level search workflow
   *
   * @throws IOException
   */
  @Override
  public void proccess() throws IOException {
    List<String> lines = new ArrayList<>();
    listFiles(getRootPath()).forEach(root->readLines(root).filter(line-> containsPattern(line)).forEach(line -> lines.add(line)));
    writeToeFile(lines.stream());

  }

  /**
   * Traverse a given directory and return all files
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public Stream<File> listFiles(String rootDir) {
    try {
      return Files.walk(Paths.get(rootDir)).filter(Files::isRegularFile).map(Path::toFile);
    }catch (IOException e){
      logger.error("Error: ",e);
    }
    return null;
  }

  /**
   * Read a file and return all the lines
   * <p>
   * Explain FileReader BufferedReader charcter encoding
   *
   * @param inputFile file to read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public Stream<String> readLines(File inputFile) {
    try {
      return Files.lines(inputFile.toPath());
    } catch (IOException e) {
      logger.error("Error: ",e);
    }

    return null;
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
  public void writeToeFile(Stream<String> lines) throws IOException {
    FileOutputStream output = new FileOutputStream(getOutFile());
    OutputStreamWriter writer = new OutputStreamWriter(output);
    BufferedWriter buffer = new BufferedWriter(writer);


    lines.forEach(line -> {
      try {
        buffer.write(line);
        buffer.newLine();
      } catch (IOException e) {
        logger.error("Error: ",e);
      }
    });
    buffer.close();
  }


  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getOutFile() {
    return outFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  public Matcher getMatcher() {
    return matcher;
  }

  public void setMatcher(Matcher matcher) {
    this.matcher = matcher;
  }

}
