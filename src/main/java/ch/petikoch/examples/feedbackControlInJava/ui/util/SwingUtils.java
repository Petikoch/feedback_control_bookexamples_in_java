package ch.petikoch.examples.feedbackControlInJava.ui.util;

import com.google.common.base.Throwables;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingUtils {

    public static void executeBlockingOnEdt(Runnable runnable) throws InvocationTargetException, InterruptedException {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeAndWait(runnable);
        }
    }

    public static void printStackTraceAndDisplayToUser(Throwable ex) {
        ex.printStackTrace();
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                null,
                Throwables.getStackTraceAsString(ex),
                "Opps...",
                JOptionPane.ERROR_MESSAGE));
    }

    // for testing
    public static void main(String[] args) {
        printStackTraceAndDisplayToUser(new NullPointerException());
    }
}
