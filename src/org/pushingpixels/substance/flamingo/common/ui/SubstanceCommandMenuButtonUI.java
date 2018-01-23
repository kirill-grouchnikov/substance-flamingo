/*
 * Copyright (c) 2005-2018 Flamingo / Substance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.substance.flamingo.common.ui;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumSet;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.pushingpixels.flamingo.api.common.JCommandMenuButton;
import org.pushingpixels.flamingo.api.common.RolloverActionListener;
import org.pushingpixels.flamingo.internal.utils.KeyTipRenderingUtilities;
import org.pushingpixels.substance.api.SubstanceCortex;
import org.pushingpixels.substance.api.SubstanceSlices;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

/**
 * UI for {@link JCommandMenuButton} components in <b>Substance</b> look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceCommandMenuButtonUI extends SubstanceCommandButtonUI {
    /**
     * Rollover menu mouse listener.
     */
    protected MouseListener rolloverMenuMouseListener;

    public static ComponentUI createUI(JComponent c) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(c);
        SubstanceCortex.ComponentScope.setButtonStraightSides(c,
                EnumSet.allOf(SubstanceSlices.Side.class));
        return new SubstanceCommandMenuButtonUI((JCommandMenuButton) c);
    }

    public SubstanceCommandMenuButtonUI(JCommandMenuButton button) {
        super(button);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jvnet.flamingo.common.ui.BasicCommandButtonUI#installListeners()
     */
    @Override
    protected void installListeners() {
        super.installListeners();

        this.rolloverMenuMouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (commandButton.isEnabled()) {
                    int modifiers = 0;
                    AWTEvent currentEvent = EventQueue.getCurrentEvent();
                    if (currentEvent instanceof InputEvent) {
                        modifiers = ((InputEvent) currentEvent).getModifiers();
                    } else if (currentEvent instanceof ActionEvent) {
                        modifiers = ((ActionEvent) currentEvent).getModifiers();
                    }
                    fireRolloverActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                            commandButton.getActionModel().getActionCommand(),
                            EventQueue.getMostRecentEventTime(), modifiers));

                    processPopupAction();
                }
            }
        };
        this.commandButton.addMouseListener(this.rolloverMenuMouseListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jvnet.flamingo.common.ui.BasicCommandButtonUI#uninstallListeners()
     */
    @Override
    protected void uninstallListeners() {
        this.commandButton.removeMouseListener(this.rolloverMenuMouseListener);
        this.rolloverMenuMouseListener = null;

        super.uninstallListeners();
    }

    /**
     * Fires the rollover action on all registered handlers.
     * 
     * @param e
     *            Event object.
     */
    protected void fireRolloverActionPerformed(ActionEvent e) {
        // Guaranteed to return a non-null array
        RolloverActionListener[] listeners = commandButton
                .getListeners(RolloverActionListener.class);
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 1; i >= 0; i--) {
            (listeners[i]).actionPerformed(e);
        }
    }

    @Override
    public void update(Graphics g, JComponent c) {
        super.update(g, c);

        JCommandMenuButton menuButton = (JCommandMenuButton) c;
        KeyTipRenderingUtilities.renderMenuButtonKeyTips(g, menuButton, layoutManager);
    }
}
