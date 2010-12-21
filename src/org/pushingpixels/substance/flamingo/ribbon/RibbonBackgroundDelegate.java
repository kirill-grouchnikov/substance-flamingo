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
package org.pushingpixels.substance.flamingo.ribbon;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.internal.ui.ribbon.JRibbonTaskToggleButton;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.SubstanceConstants.Side;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.flamingo.ribbon.ui.RibbonBorderShaper;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;
import org.pushingpixels.substance.internal.utils.*;

/**
 * Delegate class for painting backgrounds of buttons in Ribbon plugin.
 * 
 * @author Kirill Grouchnikov
 */
public class RibbonBackgroundDelegate {
	/**
	 * Cache for background images of ribbon buttons. Each time
	 * {@link #getTaskToggleButtonBackground(AbstractButton, int, int)} is
	 * called with <code>isRoundCorners</code> equal to <code>true</code>, it
	 * checks <code>this</code> map to see if it already contains such
	 * background. If so, the background from the map is returned.
	 */
	private static LazyResettableHashMap<BufferedImage> imageCache = new LazyResettableHashMap<BufferedImage>(
			"Substance.Flamingo.RibbonBackgroundDelegate");

	/**
	 * Retrieves background image for the specified ribbon button.
	 * 
	 * @param button
	 *            Button.
	 * @param width
	 *            Button width.
	 * @param height
	 *            Button height.
	 * @return Button background image.
	 */
	private static synchronized BufferedImage getTaskToggleButtonBackground(
			JRibbonTaskToggleButton button, int width, int height) {
		JRibbon ribbon = (JRibbon) SwingUtilities.getAncestorOfClass(
				JRibbon.class, button);
		TransitionAwareUI transitionAwareUI = (TransitionAwareUI) button
				.getUI();
		StateTransitionTracker stateTransitionTracker = transitionAwareUI
				.getTransitionTracker();
		ComponentState currState = ComponentState.getState(button
				.getActionModel(), button);
		StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker
				.getModelStateInfo();
		Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo
				.getStateContributionMap();

		SubstanceSkin skin = SubstanceCoreUtilities.getSkin(button);

		SubstanceColorScheme baseFillScheme = skin.getColorScheme(button,
				ColorSchemeAssociationKind.TAB, currState);
		SubstanceColorScheme baseBorderScheme = skin.getColorScheme(ribbon,
				ColorSchemeAssociationKind.TAB_BORDER, currState);

		SubstanceFillPainter fillPainter = skin.getFillPainter();
		SubstanceBorderPainter borderPainter = skin.getBorderPainter();

		JRibbon parent = (JRibbon) SwingUtilities.getAncestorOfClass(
				JRibbon.class, button);
		RibbonTask selectedTask = parent.getSelectedTask();
		AbstractRibbonBand band = (selectedTask.getBandCount() == 0) ? null
				: selectedTask.getBand(0);
		Color bgColor = (band != null) ? band.getBackground() : parent
				.getBackground();

		HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(width, height,
				baseFillScheme.getDisplayName(), baseBorderScheme
						.getDisplayName(), fillPainter.getDisplayName(),
				borderPainter.getDisplayName(), button.getParent()
						.getBackground().getRGB(), button.getActionModel()
						.isSelected(), button.getContextualGroupHueColor(),
				button.getActionModel().isSelected(), ribbon.isMinimized(),
				skin.getSelectedTabFadeStart(), skin.getSelectedTabFadeEnd(),
				bgColor);
		BufferedImage baseLayer = imageCache.get(baseKey);
		if (baseLayer == null) {
			baseLayer = getSingleLayer(button, width, height, ribbon,
					baseFillScheme, baseBorderScheme, fillPainter,
					borderPainter);

			imageCache.put(baseKey, baseLayer);
		}

		if (currState.isDisabled() || (activeStates.size() == 1)) {
			return baseLayer;
		}

		BufferedImage result = SubstanceCoreUtilities.getBlankImage(width,
				height);
		Graphics2D g2d = result.createGraphics();

		g2d.drawImage(baseLayer, 0, 0, null);

		for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates
				.entrySet()) {
			ComponentState activeState = activeEntry.getKey();
			if (activeState == currState)
				continue;

			float contribution = activeEntry.getValue().getContribution();
			if (contribution == 0.0f)
				continue;

			SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities
					.getColorScheme(button, ColorSchemeAssociationKind.TAB,
							activeState);
			SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities
					.getColorScheme(ribbon,
							ColorSchemeAssociationKind.TAB_BORDER, activeState);

			HashMapKey key = SubstanceCoreUtilities.getHashKey(width, height,
					fillScheme.getDisplayName(), borderScheme.getDisplayName(),
					fillPainter.getDisplayName(), borderPainter
							.getDisplayName(), button.getParent()
							.getBackground().getRGB(), button.getActionModel()
							.isSelected(), button.getContextualGroupHueColor(),
					button.getActionModel().isSelected(), ribbon.isMinimized(),
					skin.getSelectedTabFadeStart(), skin
							.getSelectedTabFadeEnd(), bgColor);

			BufferedImage layer = imageCache.get(key);
			if (layer == null) {
				layer = getSingleLayer(button, width, height, ribbon,
						fillScheme, borderScheme, fillPainter, borderPainter);

				imageCache.put(key, layer);
			}

			g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
			g2d.drawImage(layer, 0, 0, null);
		}

		g2d.dispose();
		return result;
	}

	private static BufferedImage getSingleLayer(JRibbonTaskToggleButton button,
			int width, int height, JRibbon ribbon,
			SubstanceColorScheme fillScheme, SubstanceColorScheme borderScheme,
			SubstanceFillPainter fillPainter,
			SubstanceBorderPainter borderPainter) {
		Set<Side> bottom = new HashSet<Side>();
		bottom.add(Side.BOTTOM);

		Color contextualGroupHueColor = button.getContextualGroupHueColor();
		if (contextualGroupHueColor != null) {
			fillScheme = ShiftColorScheme.getShiftedScheme(fillScheme,
					contextualGroupHueColor,
					RibbonContextualTaskGroup.HUE_ALPHA, null, 0.0f);
		}

		float radius = RibbonBorderShaper.getRibbonToggleButtonRadius(button);
		int borderDelta = (int) Math.ceil(2.0 * SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(button)));
		int borderInsets = (int) Math.floor(SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(button)) / 2.0);
		GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(width,
				height + 2 + borderDelta, radius, bottom, borderInsets);

		BufferedImage result = SubstanceCoreUtilities.getBlankImage(width,
				height + 2);
		Graphics2D graphics = result.createGraphics();
		fillPainter.paintContourBackground(graphics, button, width, height + 2
				+ borderDelta, contour, false, fillScheme, true);

		int borderThickness = (int) SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(button));
		GeneralPath contourInner = SubstanceOutlineUtilities.getBaseOutline(
				width, height + 2 + borderDelta, radius, bottom,
				borderThickness + borderInsets);

		borderPainter.paintBorder(graphics, button, width, height + 2, contour,
				contourInner, borderScheme);
		graphics.dispose();

		if (button.getActionModel().isSelected()
				&& (button.getContextualGroupHueColor() == null)) {
			int fw = result.getWidth();
			int fh = result.getHeight();
			BufferedImage fade = SubstanceCoreUtilities.getBlankImage(fw, fh);
			Graphics2D fadeGraphics = fade.createGraphics();
			JRibbon parent = (JRibbon) SwingUtilities.getAncestorOfClass(
					JRibbon.class, button);
			RibbonTask selectedTask = parent.getSelectedTask();
			AbstractRibbonBand band = (selectedTask.getBandCount() == 0) ? null
					: selectedTask.getBand(0);
			if (band != null) {
				fadeGraphics.setColor(band.getBackground());
			} else {
				fadeGraphics.setColor(parent.getBackground());
			}
			fadeGraphics.fillRect(0, 0, fw, fh);
			SubstanceSkin skin = SubstanceCoreUtilities.getSkin(button);
			if (skin.getWatermark() != null)
				skin.getWatermark().drawWatermarkImage(fadeGraphics, button, 0,
						0, fw, fh);

			borderPainter.paintBorder(fadeGraphics, button, width, height + 2,
					contour, contourInner, borderScheme);

			result = SubstanceCoreUtilities.blendImagesVertical(result, fade,
					skin.getSelectedTabFadeStart(), skin
							.getSelectedTabFadeEnd());
		}
		return result;
	}

	/**
	 * Updates background of the specified button.
	 * 
	 * @param g
	 *            Graphic context.
	 * @param button
	 *            Button to update.
	 * @param cycleCount
	 *            Cycle count for transition effects.
	 */
	public void updateTaskToggleButtonBackground(Graphics g,
			JRibbonTaskToggleButton button) {
		Graphics2D g2d = (Graphics2D) g.create();

		int width = button.getWidth();
		int height = button.getHeight();

		BufferedImage ribbonBackground = getTaskToggleButtonBackground(button,
				width, height);

		TransitionAwareUI ui = (TransitionAwareUI) button.getUI();
		StateTransitionTracker stateTransitionTracker = ui
				.getTransitionTracker();

		float extraActionAlpha = 0.0f;
		for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : stateTransitionTracker
				.getModelStateInfo().getStateContributionMap().entrySet()) {
			ComponentState activeState = activeEntry.getKey();
			if (activeState.isDisabled())
				continue;
			if (activeState == ComponentState.ENABLED)
				continue;
			extraActionAlpha += activeEntry.getValue().getContribution();
		}

		g2d.setComposite(LafWidgetUtilities.getAlphaComposite(button,
				extraActionAlpha, g));
		g2d.drawImage(ribbonBackground, 0, 0, null);

		g2d.dispose();
	}
}
