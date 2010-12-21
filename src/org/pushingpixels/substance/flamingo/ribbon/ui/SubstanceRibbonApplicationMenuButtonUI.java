/*
 * Copyright (c) 2005-2010 Substance Flamingo / Substance Kirill Grouchnikov. All Rights Reserved.
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
 *  o Neither the name of Substance Flamingo Kirill Grouchnikov nor the names of 
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
package org.pushingpixels.substance.flamingo.ribbon.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.BasicRibbonApplicationMenuButtonUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuButton;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.animation.effects.GhostingListener;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.flamingo.common.ui.ActionPopupTransitionAwareUI;
import org.pushingpixels.substance.flamingo.utils.*;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

/**
 * UI for {@link JRibbonApplicationMenuButton} components in <b>Substance</b>
 * look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceRibbonApplicationMenuButtonUI extends
		BasicRibbonApplicationMenuButtonUI implements
		ActionPopupTransitionAwareUI {
	/**
	 * Model change listener for ghost image effects.
	 */
	private GhostingListener substanceModelChangeListener;

	/**
	 * Tracker for visual state transitions.
	 */
	protected CommandButtonVisualStateTracker substanceVisualStateTracker;

	public static ComponentUI createUI(JComponent c) {
		return new SubstanceRibbonApplicationMenuButtonUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.common.ui.BasicCommandButtonUI#installListeners()
	 */
	@Override
	protected void installListeners() {
		super.installListeners();

		this.substanceVisualStateTracker = new CommandButtonVisualStateTracker();
		this.substanceVisualStateTracker.installListeners(this.commandButton);

		this.substanceModelChangeListener = new GhostingListener(
				this.commandButton, this.commandButton.getActionModel());
		this.substanceModelChangeListener.registerListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.common.ui.BasicCommandButtonUI#uninstallListeners()
	 */
	@Override
	protected void uninstallListeners() {
		this.substanceVisualStateTracker.uninstallListeners(this.commandButton);
		this.substanceVisualStateTracker = null;

		this.substanceModelChangeListener.unregisterListeners();
		this.substanceModelChangeListener = null;

		super.uninstallListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.basic.BasicButtonUI#paint(java.awt.Graphics,
	 * javax.swing.JComponent)
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		JRibbonApplicationMenuButton b = (JRibbonApplicationMenuButton) c;

		this.layoutInfo = this.layoutManager.getLayoutInfo(this.commandButton,
				g);
		commandButton.putClientProperty("icon.bounds", layoutInfo.iconRect);

		Graphics2D g2d = (Graphics2D) g.create();
		SubstanceFillPainter fillPainter = SubstanceCoreUtilities
				.getFillPainter(commandButton);
		SubstanceBorderPainter borderPainter = SubstanceCoreUtilities
				.getBorderPainter(commandButton);
		BufferedImage fullAlphaBackground = RibbonApplicationMenuButtonBackgroundDelegate
				.getFullAlphaBackground(b, fillPainter, borderPainter,
						commandButton.getWidth(), commandButton.getHeight());
		g2d.drawImage(fullAlphaBackground, 0, 0, null);

		// Paint the icon
		Icon icon = b.getIcon();
		if (icon != null) {
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			Rectangle iconRect = new Rectangle((c.getWidth() - iconWidth) / 2,
					(c.getHeight() - iconHeight) / 2, iconWidth, iconHeight);
			paintButtonIcon(g2d, iconRect);
		}

		g2d.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.common.ui.BasicCommandButtonUI#paintButtonIcon(java
	 * .awt.Graphics, java.awt.Rectangle)
	 */
	@Override
	protected void paintButtonIcon(Graphics g, Rectangle iconRect) {
		Icon regular = this.applicationMenuButton.isEnabled() ? this.applicationMenuButton
				.getIcon()
				: this.applicationMenuButton.getDisabledIcon();
		if (regular == null)
			return;
		boolean useThemed = SubstanceCoreUtilities
				.useThemedDefaultIcon(this.applicationMenuButton);

		if (regular != null) {
			Graphics2D g2d = (Graphics2D) g.create();

			GhostPaintingUtils.paintGhostIcon(g2d, this.applicationMenuButton,
					regular, iconRect);
			g2d.setComposite(LafWidgetUtilities.getAlphaComposite(
					this.applicationMenuButton, g));

			if (!useThemed) {
				regular.paintIcon(this.applicationMenuButton, g2d, iconRect.x,
						iconRect.y);
			} else {
				CommandButtonBackgroundDelegate.paintThemedCommandButtonIcon(
						g2d, iconRect, this.applicationMenuButton, regular,
						this.applicationMenuButton.getPopupModel(),
						this.substanceVisualStateTracker
								.getPopupStateTransitionTracker());
			}
			g2d.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.ribbon.ui.appmenu.BasicRibbonApplicationMenuButtonUI
	 * #update(java.awt.Graphics, javax.swing.JComponent)
	 */
	@Override
	public void update(Graphics g, JComponent c) {
		this.paint(g, c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.substance.SubstanceButtonUI#contains(javax.swing.JComponent,
	 * int, int)
	 */
	@Override
	public boolean contains(JComponent c, int x, int y) {
		// allow clicking anywhere in the area (around the button
		// round outline as well) to activate the button.
		return (x >= 0) && (x < c.getWidth()) && (y >= 0)
				&& (y < c.getHeight());
	}

	@Override
	public StateTransitionTracker getActionTransitionTracker() {
		return this.substanceVisualStateTracker
				.getActionStateTransitionTracker();
	}

	@Override
	public StateTransitionTracker getPopupTransitionTracker() {
		return this.substanceVisualStateTracker
				.getPopupStateTransitionTracker();
	}

	@Override
	public StateTransitionTracker getTransitionTracker() {
		return this.substanceVisualStateTracker
				.getPopupStateTransitionTracker();
	}

	@Override
	public boolean isInside(MouseEvent me) {
		return true;
	}
}
