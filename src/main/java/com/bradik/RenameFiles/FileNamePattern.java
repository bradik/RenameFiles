package com.bradik.RenameFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brad on 09.06.2017.
 */
public class FileNamePattern {

    private String pattern;
    private Pattern p2Path = Pattern.compile("[\\d_\\-]*(?<suffix>[A-zА-я\\s.,!*\\-_)(]*\\s*[!\\]\\[\\d]*)\\.(?<ext>\\p{Alnum}*$)");
    private Map<String, Boolean> replacements = new HashMap<>();
    private String dataTimePattern = null;


    public FileNamePattern(String spattern) {

        Pattern pTime = Pattern.compile("([MmDdYyKkSs\\-_\\s]+)");

        pattern = spattern;
        Pattern p = Pattern.compile("\\{([\\s\\p{Alnum}_\\-+]*)\\}");
        Matcher m = p.matcher(pattern);
        while (m.find()) {
            String key = m.group(1);
            boolean isDataTime = pTime.matcher(key).matches();

            replacements.put(key, isDataTime);

            if (isDataTime)
                pattern = pattern.replace(m.group(), "{dataTime}");
        }
    }

    public String getDataTimePattern() {

        if (dataTimePattern != null)
            return dataTimePattern;

        dataTimePattern = "";

        for (Map.Entry<String, Boolean> entry : replacements.entrySet()) {
            if (entry.getValue()) {
                dataTimePattern = entry.getKey();
                break;
            }
        }

        return dataTimePattern;
    }

    public Path replacePath(Path path, Path dirIn, Path dirOut) throws IOException {

        String fileName = path.getFileName().toString();
        String suffix = "";
        String ext = "";
        String dataTime = "";

        String newFileName = pattern;

        Matcher m = p2Path.matcher(fileName);
        if (m.find()) {
            suffix = m.group("suffix");
            ext = m.group("ext");
        }

        if (!getDataTimePattern().isEmpty()) {
            SimpleDateFormat df = new SimpleDateFormat(getDataTimePattern());
            dataTime = df.format(Files.getLastModifiedTime(path).toMillis());

            newFileName = newFileName.replace("{dataTime}", dataTime);
        }

        if (replacements.get("suffix") != null)
            newFileName = newFileName.replace("{suffix}", suffix);

        if (replacements.get("ext") != null)
            newFileName = newFileName.replace("{ext}", ext);

        int iDot = newFileName.lastIndexOf(".");
        if (iDot != -1)
            newFileName = newFileName.substring(0, iDot).trim() + newFileName.substring(iDot);


        Path newPath = Paths.get(path.getParent().toString(), newFileName);

        if (!dirIn.equals(dirOut))
            newPath = dirOut.resolve(dirIn.relativize(newPath));

        return newPath;
    }

}
