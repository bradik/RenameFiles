package com.bradik.RenameFiles;

import com.bradik.RenameFiles.Timer.Progress;
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

    private CmdOptionReder options;

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

        Path dirIn = Paths.get(options.getDirIn());
        Path dirOut = Paths.get(options.getDirOut());
        Path logFile = options.getLogFile() == null ? null : Paths.get(options.getLogFile());


        List<Path> pathList = Files.walk(dirIn).filter(path -> path.toFile().isFile()).collect(Collectors.toList());

        FileLog fileLog = new FileLog(logFile);

        Map<Path, Path> map = new LinkedHashMap<>();

        for (Path sourse : pathList) {
            if (logFile != null && sourse.getFileName().equals(logFile.getFileName()))
                continue;

            FileNameChanger nameChanger = new FileNameChanger(sourse);

            for (Map.Entry<String, String> entry : options.getListChange().entrySet()) {

                nameChanger.replace(entry.getKey(), entry.getValue(), options.isUseRegExp());
            }

            nameChanger.createFileName(options.getNewFileName());

            Path target = nameChanger.getResolvePath(dirIn, dirOut);

//            fileLog.println(sourse.toString());
//            fileLog.println(target.toString());

            map.put(sourse, target);

        }

        Progress progress = new Progress(map.size());

        Path sourse, target;
        for (Map.Entry<Path, Path> entry : map.entrySet()) {

            sourse = entry.getKey();
            target = entry.getValue();

            if (dirIn.equals(dirOut)) {

                //Files.move(sourse,target, StandardCopyOption.ATOMIC_MOVE);
            } else {

                if (!Files.exists(target.getParent()))
                    Files.createDirectories(target.getParent());

                Files.copy(sourse,target, StandardCopyOption.REPLACE_EXISTING);

            }

            progress.add().show();
        }


        fileLog.close();
    }

    public boolean init(String[] args) {

        try {
            options = CmdOptionReder.getInstance().read(args).validation();

        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            return false;
        }

        return true;
    }

}
