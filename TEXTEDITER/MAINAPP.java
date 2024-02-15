package TEXTEDITER;

import javax.swing.SwingUtilities;

/**
 * MAINAPP
 */
public class MAINAPP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIMAIN gui = new GUIMAIN();
            gui.setVisible(true);
        });
    }
}