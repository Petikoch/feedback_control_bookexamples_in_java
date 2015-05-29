/**
 * Copyright 2015 Peti Koch, All rights reserved.
 *
 * Project Info:
 * https://github.com/Petikoch/feedback_control_bookexamples_in_java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.petikoch.examples.feedbackControlInJava.ui.plotting;

import com.google.common.base.Preconditions;
import net.jcip.annotations.ThreadSafe;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;

@ThreadSafe
public class JPanelDisplayer {

    private static JPanel PANEL_HOLDER = null;

    public static void usePanelHolder(JPanel holder) {
        Preconditions.checkState(SwingUtilities.isEventDispatchThread());

        JPanelDisplayer.PANEL_HOLDER = holder;
    }

    public static void displayPanel(JPanel panel) {
        displayPanel(panel, "Feedback Control", 800, 600);
    }

    public static void displayPanel(JPanel panel, String frameTitel, int panelWidth, int panelHeight) {
        Preconditions.checkState(SwingUtilities.isEventDispatchThread());

        if (PANEL_HOLDER == null) {
            ApplicationFrame applicationFrame = new ApplicationFrame(frameTitel);
            panel.setPreferredSize(new java.awt.Dimension(panelWidth, panelHeight));
            applicationFrame.setContentPane(panel);
            applicationFrame.pack();
            RefineryUtilities.centerFrameOnScreen(applicationFrame);
            applicationFrame.setVisible(true);
        } else {
            setInPanelHolder(panel);
        }
    }

    public static void clearDisplay() {
        Preconditions.checkState(SwingUtilities.isEventDispatchThread());

        if (PANEL_HOLDER != null) {
            setInPanelHolder(new JLabel("Please wait...", SwingConstants.CENTER));
        }
    }

    private static void setInPanelHolder(Component component) {
        Preconditions.checkState(SwingUtilities.isEventDispatchThread());

        PANEL_HOLDER.removeAll();
        PANEL_HOLDER.setLayout(new BorderLayout());
        PANEL_HOLDER.add(component, BorderLayout.CENTER);

        PANEL_HOLDER.revalidate();
        PANEL_HOLDER.repaint();
    }
}
