package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;

public class JavaGrepLambdaImp extends JavaGrepImp {


  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    BasicConfigurator.configure();
    JavaGrepLambdaImp javaGrepLambdaImp= new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try {

      javaGrepLambdaImp.proccess();
    } catch (Exception ex) {
      javaGrepLambdaImp.logger.error("Error: Unable to process", ex);

    }
  }

  @Override
  public List<File> listFiles(String rootDir){
      List<File> dir = new ArrayList<>();
      try{
        dir = Files.walk(Paths.get(rootDir)).filter(Files::isRegularFile).map(Path::toFile).collect(
            Collectors.toList());
      }catch (IOException e) {
        e.printStackTrace();
      }

    return dir;
  }

  @Override
  public List<String> readLines(File inputFile){
    List<String> inputLines = new ArrayList<>();
    try {
     inputLines = Files.lines(inputFile.toPath()).collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return inputLines;
  }


}
