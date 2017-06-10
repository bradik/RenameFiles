package com.bradik.RenameFiles;

import org.apache.commons.cli.ParseException;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Brad on 08.06.2017.
 */
public class Solution {

    private static Solution instance = new Solution();

    private Path dirIn;
    private Path dirOut;
    private Path logFile;
    private String pattern;

    public static void main(String[] args) throws IOException {

        if (!getInstance().init(args)) {
            System.exit(0);
        }

        getInstance().run();
    }

    public static Solution getInstance() {

        return instance;
    }

    public void run() throws IOException {


        FileNamePattern fileNamePattern = new FileNamePattern(pattern);


        List<Path> pathList = Files.walk(dirIn).filter(path -> path.toFile().isFile()).collect(Collectors.toList());

        FileLog fileLog = new FileLog(logFile);


        for (Path sourse : pathList) {
            if (logFile!=null&&sourse.getFileName().equals(logFile.getFileName()))
                continue;

            Path target = fileNamePattern.replacePath(sourse, dirIn, dirOut);

            fileLog.println(sourse.toString());
            fileLog.println(target.toString());

            if (dirIn.equals(dirOut)){

                //Files.move(sourse,target, StandardCopyOption.ATOMIC_MOVE);
            }else{

//                if (!Files.exists(target.getParent()))
//                    Files.createDirectories(target.getParent());
//
//                Files.copy(sourse,target, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        fileLog.close();
    }

    public boolean init(String[] args) {

        CmdOptionReder optionReder;

        try {
            optionReder = CmdOptionReder.getInstance().read(args);

        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            return false;
        }

        if (!optionReder.validation())
            return false;

        pattern = optionReder.getPattern();
        dirIn = Paths.get(optionReder.getDirIn());
        dirOut = Paths.get(optionReder.getDirOut());
        logFile = optionReder.getLogFile() == null ? null : Paths.get(optionReder.getLogFile());

        return true;
    }

}
