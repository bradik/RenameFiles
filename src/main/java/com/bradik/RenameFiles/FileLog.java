package com.bradik.RenameFiles;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Brad on 09.06.2017.
 */
public class FileLog implements Closeable {

    private PrintWriter writer;

    public FileLog(Path path, Charset charset) throws IOException {

        if (path == null)
            return;

        //cp866//windows-1251//UTF-8
        writer = new PrintWriter(Files.newBufferedWriter(path, charset));
    }

    public FileLog(Path path) throws IOException {

        this(path, Charset.forName("cp866"));
    }

    public void println(String s) {

        System.out.println(s);

        if (writer != null)
            writer.println(s);
    }

    @Override
    public void close() throws IOException {
        if (writer != null)
            writer.close();
    }
}
