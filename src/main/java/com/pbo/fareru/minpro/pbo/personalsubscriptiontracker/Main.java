package com.pbo.fareru.minpro.pbo.personalsubscriptiontracker;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.pbo.fareru.minpro.pbo.controller.LanggananController;
import com.pbo.fareru.minpro.pbo.view.LanggananView;
import com.pbo.fareru.minpro.pbo.view.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Exception ignored) { }
        }));

        boolean modeCli = false;
        if (args != null && args.length > 0) {
            for (String arg : args) {
                if ("--cli".equalsIgnoreCase(arg) || "-c".equalsIgnoreCase(arg)) {
                    modeCli = true;
                    break;
                }
            }
        }

        if (modeCli) {
            LanggananView view = new LanggananView();
            LanggananController controller = new LanggananController(view);
            controller.jalankanAplikasi();
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }

            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            });
        }
    }
}
