package io.github.zebMcCorkle.FontInstaller;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Copyright (c) 2016 Zeb McCorkle
 *
 * All Rights Reserved
 */
public class FontInstallerGui {
    private JList fontList;
    private JButton addFontButton;
    private JButton removeFontButton;
    private JPanel panel;

    private FontInstaller installer;

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public FontInstallerGui() throws IOException, URISyntaxException {
        InputStream rfexein = getClass().getResource("/RegisterFont.exe").openStream();
        File rfexe = new File("RegisterFont.exe");
        FileOutputStream rfexeout = new FileOutputStream(rfexe);

        copyStream(rfexein, rfexeout);

        rfexeout.close();
        rfexein.close();
        rfexeout.flush();

        rfexe.deleteOnExit();

        AutoUpdate au = new AutoUpdate();
        au.run();
        if (!au.uptodate) {
            int response = JOptionPane.showConfirmDialog(panel, "There is an update available, would you like to download it?\nYou are currently on " + au.VERSION + ", latest is " + au.newVersion, "Update Available!", JOptionPane.YES_NO_OPTION);
            if (response == 0) {
                Desktop.getDesktop().browse(new URI(au.updateUrl));
                System.exit(1);
            }
        }

        installer = new FontInstaller();

        addFontButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Font Files", "ttf", "otf");
                fileChooser.setFileFilter(filter);
                fileChooser.setMultiSelectionEnabled(true);
                int returnVal = fileChooser.showOpenDialog(panel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    for (File f : fileChooser.getSelectedFiles()) {
                        try {
                            Font font = new Font(FontType.valueOf(FilenameUtils.getExtension(f.toString()).toUpperCase()), FilenameUtils.getBaseName(f.toString()));
                            Files.copy(f.toPath(), Paths.get(installer.directory.getAbsolutePath() + "\\" + font.getName() + "." + font.getFontType().toString()));
                            installer._addFont(font);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    try {
                        installer.update();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                update();
            }
        });
        removeFontButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Font font = (Font) fontList.getSelectedValue();
                    installer.removeFont(font);
                    Files.delete(Paths.get(installer.directory.getAbsolutePath() + "\\" + font.getName() + "." + font.getFontType().toString()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                update();
            }
        });

        File startupFile = new File(System.getenv("appdata") + "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\FontInstaller.jar");

        if (startupFile.exists()) {
            Files.delete(startupFile.toPath());
        }

        File jarFile = new File(FontInstallerGui.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        try {
            Files.copy(jarFile.toPath(), startupFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        update();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        JFrame frame = new JFrame("Font Installer");
        frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));
        frame.setContentPane(new FontInstallerGui().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.pack();
        frame.setVisible(true);
    }

    private void update() {
        DefaultListModel model = new DefaultListModel();
        for (Font font : installer.fonts) {
            model.addElement(font);
        }
        fontList.setModel(model);
    }

    protected void finalize() {
        new File("RegisterFont.exe").delete();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        fontList = new JList();
        panel.add(fontList, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        addFontButton = new JButton();
        addFontButton.setText("Add Font");
        panel.add(addFontButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeFontButton = new JButton();
        removeFontButton.setText("Remove Font");
        panel.add(removeFontButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
