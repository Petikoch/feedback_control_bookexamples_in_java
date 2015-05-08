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
package ch.petikoch.examples.feedbackControlInJava;

import ch.petikoch.examples.feedbackControlInJava.plotting.JPanelDisplayer;
import com.google.common.io.Files;
import groovy.ui.Console;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FeedbackControlInJavaMain {

    public static final String EXAMPLES_DIR = "./src/main/java/ch/petikoch/examples/feedbackControlInJava/examples";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Console groovyConsole = new Console();

                groovyConsole.run();
                JFrame frame = (JFrame) groovyConsole.getFrame();
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                groovyConsole.setCurrentFileChooserDir(new File(EXAMPLES_DIR));
                groovyConsole.getInputEditor().getTextEditor().setText(readCh13Example());
                groovyConsole.setDetachedOutput(false);

                JSplitPane splitPane = groovyConsole.getSplitPane();
                Component outputComponent = splitPane.getRightComponent();
                splitPane.remove(outputComponent);

                JSplitPane newLowerSplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                newLowerSplitPanel.setLeftComponent(outputComponent);
                JPanel chartPanelHolder = new JPanel(new BorderLayout());
                chartPanelHolder.add(new JLabel("Run the code to see the chart", SwingConstants.CENTER), BorderLayout.CENTER);
                newLowerSplitPanel.setRightComponent(chartPanelHolder);

                JPanelDisplayer.PANEL_HOLDER = chartPanelHolder;

                splitPane.setRightComponent(newLowerSplitPanel);

                EventQueue.invokeLater(() -> newLowerSplitPanel.setDividerLocation(0.5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String readCh13Example() throws IOException {
        return Files.toString(new File(EXAMPLES_DIR, "Ch13_cache_closedloop_jumps.java"), Charset.forName("UTF-8"));
    }

}
