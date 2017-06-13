package com.bradik.RenameFiles;

import org.apache.commons.cli.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Brad on 09.06.2017.
 */
public class CmdOptionReder {

    private static CmdOptionReder instance = new CmdOptionReder();

    private Options options;

    //-chL "^[^\s]*\s*[\d_.-]*" "" "[720]" "" -regExp -nn "VID_{yyyy_MM_dd-kkmmss} {name}.{ext}" -in "e:\source" -out "e:\destination" -log result.log
    private Map<String,String> listChange;
    private boolean useRegExp;
    private String newFileName;
    private String dirIn;
    private String dirOut;
    private String logFile;


    private CmdOptionReder() {

        listChange = new LinkedHashMap<>();

        options = new Options().
                addOption(
                        Option.builder("chl").
                        longOpt("changeList").
                        desc("list to replacement").
                        hasArgs().valueSeparator(' ').
                        build()).
                addOption("", "regExp", false, "use reg exp to replacement").
                addOption(
                        Option.builder("nn").
                                longOpt("NewName").
                                desc("new name").
                                hasArg().
                                required().
                                build()).
                addOption(
                        Option.builder("in").
                                longOpt("inDir").
                                desc("source directory").
                                hasArg().
                                required().
                                build()).
                addOption("out", "outDir", true, "destination directory (can be empty)").
                addOption("log", "logFile", true, "file for logging (can be empty)");

    }

    public static CmdOptionReder getInstance() {
        return instance;
    }

    public CmdOptionReder read(String[] args) throws ParseException {

        // create the parser
        CommandLineParser parser = new DefaultParser();


        // parse the command line arguments
        CommandLine line = parser.parse(options, args);

        if (line.hasOption("changeList")) {
            String[] strings = line.getOptionValues("changeList");
            for (int i = 0; i < strings.length-1 ; i+=2) {
                listChange.put(strings[i],strings[i+1]);
            }
        }

        useRegExp = line.hasOption("regExp");

        if (line.hasOption("NewName")) {
            newFileName = line.getOptionValue("NewName");
        }

        if (line.hasOption("inDir")) {
            dirIn = line.getOptionValue("inDir");
        }

        if (line.hasOption("outDir")) {
            dirOut = line.getOptionValue("outDir");
        }

        if (line.hasOption("logFile")) {
            logFile = line.getOptionValue("logFile");
        }

        return this;
    }

    public CmdOptionReder validation() throws ParseException {

        dirIn = isDirCorrect(dirIn);

        if (dirIn.isEmpty())
            throw new ParseException(String.format("parameter '%s' is not correct","-in(-inDir)"));

        dirOut = isDirCorrect(dirOut);

        if (dirOut.isEmpty())
            dirOut = dirIn;

        if (logFile != null) {
            Path pathLogFile = Paths.get(logFile);
            if (pathLogFile.getParent() == null) {
                pathLogFile = Paths.get(dirOut.toString(), logFile);
            }
            logFile = pathLogFile.toAbsolutePath().toString();
        }

        return this;
    }

    private String isDirCorrect(String dir) {

        if (dir == null)
            return "";

        Path dirInPath = Paths.get(dir);
        if (Files.notExists(dirInPath) || !Files.isDirectory(dirInPath))
            return "";

        return dirInPath.toAbsolutePath().toString();
    }

    public Map<String, String> getListChange() {
        return listChange;
    }

    public boolean isUseRegExp() {
        return useRegExp;
    }

    public String getNewFileName() {
        return newFileName;
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
