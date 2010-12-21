/*
 * Copyright (c) 2005-2010 Flamingo / Substance Kirill Grouchnikov. All Rights Reserved.
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
package org.pushingpixels.substance.flamingo.ribbon.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonGalleryUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.JRibbonGallery;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.flamingo.common.TransitionAwareResizableIcon;
import org.pushingpixels.substance.flamingo.common.ui.ActionPopupTransitionAwareUI;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.utils.*;

/**
 * UI delegate for {@link JRibbonGallery} component under Substance
 * look-and-feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceRibbonGalleryUI extends BasicRibbonGalleryUI {
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent comp) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
		return new SubstanceRibbonGalleryUI();
	}

	/**
	 * Creates new UI delegate.
	 */
	private SubstanceRibbonGalleryUI() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.ribbon.ui.BasicRibbonGalleryUI#paintRibbonGalleryBorder
	 * (java.awt.Graphics)
	 */
	@Override
	protected void paintRibbonGalleryBorder(Graphics graphics) {
		Graphics2D g2d = (Graphics2D) graphics;
		SubstanceColorScheme borderColorScheme = SubstanceColorSchemeUtilities
				.getColorScheme(this.ribbonGallery,
						ColorSchemeAssociationKind.BORDER,
						ComponentState.ENABLED);
		SubstanceImageCreator.paintBorder(this.ribbonGallery, g2d,
				this.margin.left, this.margin.top, this.ribbonGallery
						.getWidth()
						- this.margin.left - this.margin.right,
				this.ribbonGallery.getHeight() - this.margin.top
						- this.margin.bottom, SubstanceSizeUtils
						.getClassicButtonCornerRadius(SubstanceSizeUtils
								.getComponentFontSize(this.ribbonGallery)),
				borderColorScheme);
		g2d.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.ribbon.ui.BasicRibbonGalleryUI#createExpandButton()
	 */
	@Override
	protected ExpandCommandButton createExpandButton() {
		final ExpandCommandButton button = super.createExpandButton();
		final int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
		int arrowIconHeight = (int) SubstanceSizeUtils
				.getSmallArrowIconHeight(fontSize) + 3;
		int arrowIconWidth = (int) SubstanceSizeUtils
				.getSmallArrowIconWidth(fontSize);
		final ResizableIcon arrowIcon = new TransitionAwareResizableIcon(
				button,
				new TransitionAwareResizableIcon.StateTransitionTrackerDelegate() {
					@Override
					public StateTransitionTracker getStateTransitionTracker() {
						return ((ActionPopupTransitionAwareUI) button.getUI())
								.getActionTransitionTracker();
					}
				}, new TransitionAwareResizableIcon.Delegate() {
					@Override
					public Icon getColorSchemeIcon(SubstanceColorScheme scheme,
							int width, int height) {
						return SubstanceImageCreator
								.getDoubleArrowIcon(
										SubstanceSizeUtils
												.getComponentFontSize(button),
										width,
										height,
										SubstanceSizeUtils
												.getDoubleArrowStrokeWidth(fontSize),
										SwingConstants.SOUTH, scheme);
					}
				}, new Dimension(arrowIconWidth, arrowIconHeight));
		button.setIcon(arrowIcon);
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.ribbon.ui.BasicRibbonGalleryUI#createScrollDownButton
	 * ()
	 */
	@Override
	protected JCommandButton createScrollDownButton() {
		final JCommandButton button = super.createScrollDownButton();
		final int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
		int arrowIconHeight = (int) SubstanceSizeUtils
				.getSmallArrowIconHeight(fontSize);
		int arrowIconWidth = (int) SubstanceSizeUtils
				.getSmallArrowIconWidth(fontSize);
		final ResizableIcon arrowIcon = new TransitionAwareResizableIcon(
				button,
				new TransitionAwareResizableIcon.StateTransitionTrackerDelegate() {
					@Override
					public StateTransitionTracker getStateTransitionTracker() {
						return ((ActionPopupTransitionAwareUI) button.getUI())
								.getActionTransitionTracker();
					}
				}, new TransitionAwareResizableIcon.Delegate() {
					@Override
					public Icon getColorSchemeIcon(SubstanceColorScheme scheme,
							int width, int height) {
						return SubstanceImageCreator.getArrowIcon(width,
								height, SubstanceSizeUtils
										.getDoubleArrowStrokeWidth(fontSize),
								SwingConstants.SOUTH, scheme);
					}
				}, new Dimension(arrowIconWidth, arrowIconHeight));
		button.setIcon(arrowIcon);
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.flamingo.ribbon.ui.BasicRibbonGalleryUI#createScrollUpButton()
	 */
	@Override
	protected JCommandButton createScrollUpButton() {
		final JCommandButton button = super.createScrollUpButton();
		final int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
		int arrowIconHeight = (int) SubstanceSizeUtils
				.getSmallArrowIconHeight(fontSize);
		int arrowIconWidth = (int) SubstanceSizeUtils
				.getSmallArrowIconWidth(fontSize);
		ResizableIcon arrowIcon = new TransitionAwareResizableIcon(
				button,
				new TransitionAwareResizableIcon.StateTransitionTrackerDelegate() {
					@Override
					public StateTransitionTracker getStateTransitionTracker() {
						return ((ActionPopupTransitionAwareUI) button.getUI())
								.getActionTransitionTracker();
					}
				}, new TransitionAwareResizableIcon.Delegate() {
					@Override
					public Icon getColorSchemeIcon(SubstanceColorScheme scheme,
							int width, int height) {
						return SubstanceImageCreator.getArrowIcon(width,
								height, SubstanceSizeUtils
										.getDoubleArrowStrokeWidth(fontSize),
								SwingConstants.NORTH, scheme);
					}
				}, new Dimension(arrowIconWidth, arrowIconHeight));
		button.setIcon(arrowIcon);
		return button;
	}
}