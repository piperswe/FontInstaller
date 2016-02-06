package io.github.zebMcCorkle.FontInstaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
 * Copyright (c) 2016 Zeb McCorkle
 *
 * All Rights Reserved
 */
public class FontInstaller {
    public File directory = new File(System.getenv("appdata") + "\\fontinstaller");
    public File fontsFile = new File(directory.getAbsolutePath() + "\\fonts.txt");
    public List<Font> fonts = new ArrayList<>();

    public FontInstaller() throws IOException {
        System.out.println(directory.getAbsolutePath());

        if (!directory.isDirectory()) {
            directory.mkdir();
        }
        if (!fontsFile.exists()) {
            fontsFile.createNewFile();
        }

        update();
    }

    public void addFont(Font f) throws IOException {
        _addFont(f);
        update();
    }

    public void _addFont(Font f) throws IOException {
        fonts.add(f);
        FileWriter writer = new FileWriter(fontsFile, true);
        writer.append(f.getName() + "." + f.getFontType() + System.getProperty("line.separator"));
        writer.close();
    }

    public void removeFont(Font f) throws IOException {
        _removeFont(f);
        update();
    }

    public void _removeFont(Font f) throws IOException {
        fonts.remove(f);
        System.out.print(IOUtils.toString(Runtime.getRuntime().exec("RegisterFont rem " + directory.getAbsolutePath() + "\\" + f.getName() + "." + f.getFontType()).getInputStream()));

        Scanner fontsFileScanner = new Scanner(fontsFile);
        StringBuilder builder = new StringBuilder();
        while (fontsFileScanner.hasNextLine()) {
            String line = fontsFileScanner.nextLine();
            if (!line.equals(f.getName() + "." + f.getFontType())) builder.append(line + "\n");
        }
        fontsFileScanner.close();
        FileWriter writer = new FileWriter(fontsFile);
        writer.append(builder.toString());
        writer.close();
    }

    public void update() throws IOException {
        fonts.clear();
        Scanner fontsFileScanner = new Scanner(fontsFile);

        StringBuilder command = new StringBuilder("RegisterFont add");

        while (fontsFileScanner.hasNextLine()) {
            String filename = fontsFileScanner.nextLine();
            Font f = new Font(FontType.valueOf(FilenameUtils.getExtension(filename).toUpperCase()), FilenameUtils.getBaseName(filename));
            fonts.add(f);
            command.append(" " + directory.getAbsolutePath() + "\\" + f.getName() + "." + f.getFontType());
        }

        Collections.sort(fonts);

        fontsFileScanner.close();

        String cmd = command.toString();

        System.out.print(IOUtils.toString(Runtime.getRuntime().exec(cmd).getInputStream()));
    }
}
