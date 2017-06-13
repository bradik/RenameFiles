package com.bradik.RenameFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brad on 13.06.2017.
 */
public class FileNameChanger {
    private Path path;
    private String fileName;
    private String name;
    private String ext;

    public FileNameChanger(Path path) {

        this.path = path;

        String name = path.getFileName().toString();

        int dot = name.lastIndexOf('.');
        if (dot == -1) {
            this.name = name;
            this.ext = "";
        } else {
            this.name = name.substring(0, dot);
            this.ext = name.substring(++dot);
        }
    }

    public void createFileName(String pattern) {
        //VID_{yyyy_MM_dd-kkmmss} {name}.{ext}

        fileName = pattern;

        Pattern p = Pattern.compile("\\{([\\s\\p{Alnum}_\\-+]*)\\}");
        Matcher m = p.matcher(pattern);
        while (m.find()) {
            String key = m.group(1);
            if ("name".equalsIgnoreCase(key)) {
                fileName = fileName.replace(m.group(0),name);
            } else if ("ext".equalsIgnoreCase(key)) {
                fileName = fileName.replace(m.group(0),ext);
            } else if(key.matches("([MmDdYyKkSs\\-_\\s]+)"))  {
                SimpleDateFormat df = new SimpleDateFormat(key);
                try {

                    fileName = fileName.replace(m.group(0),df.format(Files.getLastModifiedTime(path).toMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void replace(String target, String replacement, Boolean regExp) {

        if (!regExp)
            name = name.replace(target, replacement);
        else
            name = name.replaceAll(target, replacement);
    }

    public Path getResolvePath(Path dirIn, Path dirOut) {

        Path newPath = Paths.get(path.getParent().toString(), fileName);

        if (!dirIn.equals(dirOut))
            newPath = dirOut.resolve(dirIn.relativize(newPath));


        return newPath;
    }
}
