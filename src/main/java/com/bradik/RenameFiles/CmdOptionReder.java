package com.bradik.RenameFiles;

import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Brad on 09.06.2017.
 */
public class CmdOptionReder {

    private static CmdOptionReder instance = new CmdOptionReder();

    private Options options;

    //-p VID_{yyyy_MM_dd-kkmmss}{suffix}.{ext} -in "e:\Mi5\Video\Танцы\Бачата" -out "e:\Mi5\Video\Танцы new" -log result.log
    private String pattern;
    private String dirIn;
    private String dirOut;
    private String logFile;


    private CmdOptionReder() {

        options = new Options().
                addOption("p", true, "pattern to replacement").
                addOption("in", true, "source directory").
                addOption("out", true, "destination directory").
                addOption("log", true, "file for logging");


    }

    public static CmdOptionReder getInstance() {
        return instance;
    }

    public CmdOptionReder read(String[] args) throws ParseException {

        // create the parser
        CommandLineParser parser = new DefaultParser();


        // parse the command line arguments
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("p")) {
            pattern = line.getOptionValue("p");
        }

        if (line.hasOption("in")) {
            dirIn = line.getOptionValue("in");
        }

        if (line.hasOption("out")) {
            dirOut = line.getOptionValue("out");
        }

        if (line.hasOption("log")) {
            logFile = line.getOptionValue("log");
        }

        return this;
    }

    public boolean validation(){

        if (pattern==null)
            return false;

        dirIn = isDirCorrect(dirIn);

        if (dirIn.isEmpty())
            return false;

        dirOut = isDirCorrect(dirOut);

        if (dirOut.isEmpty())
            dirOut = dirIn;

        if (logFile!=null){
            Path pathLogFile = Paths.get(logFile);
            if (pathLogFile.getParent()==null){
                pathLogFile = Paths.get(dirOut.toString(),logFile);
            }
            logFile = pathLogFile.toAbsolutePath().toString();
        }

        return true;
    }

    private String isDirCorrect(String dir){

        if (dir==null)
            return "";

        Path dirInPath = Paths.get(dir);
        if (Files.notExists(dirInPath)||!Files.isDirectory(dirInPath))
            return "";

        return dirInPath.toAbsolutePath().toString();
    }

    public String getPattern() {
        return pattern;
    }

    public String getDirIn() {
        return dirIn;
    }

    public String getDirOut() {
        return dirOut;
    }

    public String getLogFile() {
        return logFile;
    }
}
