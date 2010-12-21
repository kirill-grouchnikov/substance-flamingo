/*
 * Copyright (c) 2005-2010 Substance Kirill Grouchnikov. All Rights Reserved.
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
 *  o Neither the name of Substance Kirill Grouchnikov nor the names of
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
package org.pushingpixels.substance.flamingo.utils;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

import javax.swing.*;

import org.pushingpixels.flamingo.api.common.AbstractCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.AbstractCommandButton.CommandButtonLocationOrderKind;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip.StripOrientation;
import org.pushingpixels.flamingo.api.common.model.PopupButtonModel;
import org.pushingpixels.flamingo.internal.utils.FlamingoUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.SubstanceFillPainter;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.flamingo.common.ui.ActionPopupTransitionAwareUI;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.utils.*;
import org.pushingpixels.substance.internal.utils.icon.TransitionAware;

/**
 * Delegate class for painting backgrounds of buttons in <b>Substance </b> look
 * and feel. This class is <b>for internal use only</b>.
 * 
 * @author Kirill Grouchnikov
 */
public class CommandButtonBackgroundDelegate {
	/**
	 * Cache for background images. Each time
	 * {@link #getBackground(AbstractButton, SubstanceButtonShaper, SubstanceFillPainter, int, int)}
	 * is called, it checks <code>this</code> map to see if it already contains
	 * such background. If so, the background from the map is returned.
	 */
	private static LazyResettableHashMap<BufferedImage> imageCache = new LazyResettableHashMap<BufferedImage>(
			"Substance.Flamingo.CommandButtonBackgroundDelegate");

	/**
	 * Retrieves the background for the specified button.
	 * 
	 * @param commandButton
	 *            Button.
	 * @param shaper
	 *            Button shaper.
	 * @param fillPainter
	 *            Button fill painter.
	 * @param borderPainter
	 *            Button border painter.
	 * @param width
	 *            Button width.
	 * @param height
	 *            Button height.
	 * @return Button background.
	 */
	public static BufferedImage getFullAlphaBackground(
			AbstractCommandButton commandButton, ButtonModel buttonModel,
			SubstanceFillPainter fillPainter,
			SubstanceBorderPainter borderPainter, int width, int height,
			StateTransitionTracker stateTransitionTracker,
			boolean ignoreSelections) {
		StateTransitionTracker.ModelStateInfo modelStateInfo = (stateTransitionTracker == null) ? null
				: stateTransitionTracker.getModelStateInfo();
		Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = (modelStateInfo == null) ? null
				: (ignoreSelections ? modelStateInfo
						.getStateNoSelectionContributionMap() : modelStateInfo
						.getStateContributionMap());
		ComponentState currState = (modelStateInfo == null) ? ComponentState
				.getState(buttonModel, commandButton)
				: (ignoreSelections ? modelStateInfo
						.getCurrModelStateNoSelection() : modelStateInfo
						.getCurrModelState());

		SubstanceColorScheme baseFillScheme = SubstanceColorSchemeUtilities
				.getColorScheme(commandButton, currState);
		SubstanceColorScheme baseBorderScheme = SubstanceColorSchemeUtilities
				.getColorScheme(commandButton,
						ColorSchemeAssociationKind.BORDER, currState);

		float radius = SubstanceSizeUtils
				.getClassicButtonCornerRadius(SubstanceSizeUtils
						.getComponentFontSize(commandButton));

		Set<SubstanceConstants.Side> straightSides = SubstanceCoreUtilities
				.getSides(commandButton,
						SubstanceLookAndFeel.BUTTON_SIDE_PROPERTY);

		// special handling for location order
		AbstractCommandButton.CommandButtonLocationOrderKind locationOrderKind = commandButton
				.getLocationOrderKind();
		int dx = 0;
		int dy = 0;
		int dw = 0;
		int dh = 0;
		boolean isVertical = false;
		if ((locationOrderKind != null)
				&& (locationOrderKind != AbstractCommandButton.CommandButtonLocationOrderKind.ONLY)) {
			Component parent = commandButton.getParent();
			if ((parent instanceof JCommandButtonStrip)
					&& (((JCommandButtonStrip) parent).getOrientation() == StripOrientation.VERTICAL)) {
				isVertical = true;
				switch (locationOrderKind) {
				case FIRST:
					dh = commandButton.getHeight() / 2;
					break;
				case MIDDLE:
					dy = -commandButton.getHeight() / 2;
					dh = commandButton.getHeight();
					break;
				case LAST:
					dy = -commandButton.getHeight() / 2;
					dh = commandButton.getHeight() / 2;
				}
			} else {
				boolean ltr = commandButton.getComponentOrientation()
						.isLeftToRight();
				if (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.MIDDLE) {
					dx = -commandButton.getWidth() / 2;
					dw = commandButton.getWidth();
				} else {
					boolean curveOnLeft = (ltr && (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.FIRST))
							|| (!ltr && (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.LAST));
					if (curveOnLeft) {
						dw = commandButton.getWidth() / 2;
					} else {
						dx = -commandButton.getWidth() / 2;
						dw = commandButton.getWidth() / 2;
					}
				}
			}
		}

		HashMapKey baseKey = SubstanceCoreUtilities.getHashKey(currState,
				width, height, baseFillScheme.getDisplayName(),
				baseBorderScheme.getDisplayName(),
				fillPainter.getDisplayName(), borderPainter.getDisplayName(),
				commandButton.getClass().getName(), radius, straightSides,
				SubstanceSizeUtils.getComponentFontSize(commandButton),
				locationOrderKind, dx, dy, dw, dh, isVertical);

		BufferedImage baseLayer = imageCache.get(baseKey);
		if (baseLayer == null) {
			baseLayer = getSingleLayer(commandButton, fillPainter,
					borderPainter, width, height, baseFillScheme,
					baseBorderScheme, radius, straightSides, locationOrderKind,
					dx, dy, dw, dh, isVertical);

			imageCache.put(baseKey, baseLayer);
		}

		if (currState.isDisabled() || (activeStates == null)
				|| (activeStates.size() == 1)) {
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
					.getColorScheme(commandButton, activeState);
			SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities
					.getColorScheme(commandButton,
							ColorSchemeAssociationKind.BORDER, activeState);

			HashMapKey key = SubstanceCoreUtilities.getHashKey(activeState,
					width, height, fillScheme.getDisplayName(), borderScheme
							.getDisplayName(), fillPainter.getDisplayName(),
					borderPainter.getDisplayName(), commandButton.getClass()
							.getName(), radius, straightSides,
					SubstanceSizeUtils.getComponentFontSize(commandButton),
					locationOrderKind, dx, dy, dw, dh, isVertical);

			BufferedImage layer = imageCache.get(key);
			if (layer == null) {
				layer = getSingleLayer(commandButton, fillPainter,
						borderPainter, width, height, fillScheme, borderScheme,
						radius, straightSides, locationOrderKind, dx, dy, dw,
						dh, isVertical);

				imageCache.put(key, layer);
			}

			g2d.setComposite(AlphaComposite.SrcOver.derive(contribution));
			g2d.drawImage(layer, 0, 0, null);
		}

		g2d.dispose();
		return result;
	}

	private static BufferedImage getSingleLayer(
			AbstractCommandButton commandButton,
			SubstanceFillPainter fillPainter,
			SubstanceBorderPainter borderPainter,
			int width,
			int height,
			SubstanceColorScheme fillScheme,
			SubstanceColorScheme borderScheme,
			float radius,
			Set<SubstanceConstants.Side> straightSides,
			AbstractCommandButton.CommandButtonLocationOrderKind locationOrderKind,
			int dx, int dy, int dw, int dh, boolean isVertical) {
		int borderDelta = (int) Math.floor(SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(commandButton)) / 2.0);

		Shape contour = SubstanceOutlineUtilities.getBaseOutline(width + dw,
				height + dh, radius, straightSides, borderDelta);
		BufferedImage newBackground = SubstanceCoreUtilities.getBlankImage(
				width, height);
		Graphics2D finalGraphics = (Graphics2D) newBackground.getGraphics();
		finalGraphics.translate(dx, dy);
		fillPainter.paintContourBackground(finalGraphics, commandButton, width
				+ dw, height + dh, contour, false, fillScheme, true);

		int borderThickness = (int) SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(commandButton));
		Shape contourInner = SubstanceOutlineUtilities.getBaseOutline(width
				+ dw, height + dh, radius, straightSides, borderDelta
				+ borderThickness);
		borderPainter.paintBorder(finalGraphics, commandButton, width + dw,
				height + dh, contour, contourInner, borderScheme);

		if (isVertical) {
			if ((locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.FIRST)
					|| (locationOrderKind == CommandButtonLocationOrderKind.MIDDLE)) {
				// outer/inner line at the bottom
				int y = -dy + commandButton.getHeight() - borderDelta - 2
						* borderThickness;
				int xs = borderDelta;
				int xe = commandButton.getWidth() - 2 * borderDelta;
				Shape upper = new Line2D.Double(xs + borderThickness, y, xe - 2
						* borderThickness, y);
				y += borderThickness;
				Shape lower = new Line2D.Double(xs, y, xe, y);
				borderPainter.paintBorder(finalGraphics, commandButton, width
						+ dw, height + dh, lower, upper, borderScheme);
			}

			// special case for MIDDLE and LAST location order kinds
			if ((locationOrderKind == CommandButtonLocationOrderKind.MIDDLE)
					|| (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.LAST)) {
				// inner line at the top
				int y = -dy + borderDelta;
				int xs = borderDelta;
				int xe = commandButton.getWidth() - 2 * borderDelta;
				Shape upper = new Line2D.Double(xs + borderThickness, y, xe - 2
						* borderThickness, y);
				borderPainter.paintBorder(finalGraphics, commandButton, width
						+ dw, height + dh, null, upper, borderScheme);
			}
		} else {
			// special case for leftmost (not FIRST!!!) and MIDDLE location
			// order
			// kinds
			boolean ltr = commandButton.getComponentOrientation()
					.isLeftToRight();
			boolean leftmost = (ltr && (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.FIRST))
					|| (!ltr && (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.LAST));
			if (leftmost
					|| (locationOrderKind == CommandButtonLocationOrderKind.MIDDLE)) {
				// outer / inner line at the right
				int x = -dx + commandButton.getWidth() - borderDelta - 2
						* borderThickness;
				int ys = borderDelta;
				int ye = commandButton.getHeight() - 2 * borderDelta;
				Shape upper = new Line2D.Double(x, ys + borderThickness, x, ye
						- 2 * borderThickness);
				x += borderThickness;
				Shape lower = new Line2D.Double(x, ys, x, ye);
				borderPainter.paintBorder(finalGraphics, commandButton, width
						+ dw, height + dh, lower, upper, borderScheme);
			}

			// special case for MIDDLE and LAST location order kinds
			boolean rightmost = (ltr && (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.LAST))
					|| (!ltr && (locationOrderKind == AbstractCommandButton.CommandButtonLocationOrderKind.FIRST));
			if ((locationOrderKind == CommandButtonLocationOrderKind.MIDDLE)
					|| rightmost) {
				// inner line at the left
				int x = -dx + borderDelta;
				int ys = borderDelta;
				int ye = commandButton.getHeight() - 2 * borderDelta;
				Shape upper = new Line2D.Double(x, ys + borderThickness, x, ye
						- 2 * borderThickness);
				borderPainter.paintBorder(finalGraphics, commandButton, width
						+ dw, height + dh, null, upper, borderScheme);
			}
		}
		return newBackground;
	}

	public static BufferedImage getCombinedCommandButtonBackground(
			AbstractCommandButton commandButton, ButtonModel actionModel,
			Rectangle actionArea, PopupButtonModel popupModel,
			Rectangle popupArea) {
		ButtonModel backgroundModel = new DefaultButtonModel();
		backgroundModel.setEnabled(actionModel.isEnabled()
				&& popupModel.isEnabled());

		SubstanceFillPainter fillPainter = SubstanceCoreUtilities
				.getFillPainter(commandButton);
		SubstanceBorderPainter borderPainter = SubstanceCoreUtilities
				.getBorderPainter(commandButton);

		// layer number one - background with the combined enabled
		// state of both models. Full opacity
		// System.out.println("Background layer");
		BufferedImage fullAlphaBackground = CommandButtonBackgroundDelegate
				.getFullAlphaBackground(commandButton, backgroundModel,
						fillPainter, borderPainter, commandButton.getWidth(),
						commandButton.getHeight(), null, false);

		BufferedImage layers = SubstanceCoreUtilities
				.getBlankImage(fullAlphaBackground.getWidth(),
						fullAlphaBackground.getHeight());
		Graphics2D combinedGraphics = layers.createGraphics();
		combinedGraphics.drawImage(fullAlphaBackground, 0, 0, null);

		ActionPopupTransitionAwareUI ui = (ActionPopupTransitionAwareUI) commandButton
				.getUI();

		if (actionModel.isEnabled() && popupModel.isEnabled()) {
			// layer number two - background with the combined rollover state
			// of both models. Opacity 60%.
			backgroundModel.setRollover(actionModel.isRollover()
					|| popupModel.isRollover() || popupModel.isPopupShowing());
			// System.out.println(actionModel.isRollover() + ":"
			// + popupModel.isRollover());
			combinedGraphics.setComposite(AlphaComposite.SrcOver.derive(0.6f));
			// System.out.println("Rollover layer");
			BufferedImage rolloverBackground = CommandButtonBackgroundDelegate
					.getFullAlphaBackground(commandButton, backgroundModel,
							fillPainter, borderPainter, commandButton
									.getWidth(), commandButton.getHeight(), ui
									.getTransitionTracker(), false);
			combinedGraphics.drawImage(rolloverBackground, 0, 0, null);
		}

		// Shape currClip = combinedGraphics.getClip();
		if ((actionArea != null) && !actionArea.isEmpty()) {
			// layer number three - action area with its model. Opacity 40%
			// for enabled popup area, 100% for disabled popup area
			Graphics2D graphicsAction = (Graphics2D) combinedGraphics.create();
			// System.out.println(actionArea);
			graphicsAction.clip(actionArea);
			// System.out.println(graphicsAction.getClipBounds());
			float actionAlpha = 0.4f;
			if ((popupModel != null) && !popupModel.isEnabled())
				actionAlpha = 1.0f;
			if (!actionModel.isEnabled())
				actionAlpha = 0.0f;
			graphicsAction.setComposite(AlphaComposite.SrcOver
					.derive(actionAlpha));
			BufferedImage actionAreaBackground = CommandButtonBackgroundDelegate
					.getFullAlphaBackground(commandButton, null, fillPainter,
							borderPainter, commandButton.getWidth(),
							commandButton.getHeight(), ui
									.getActionTransitionTracker(), false);
			graphicsAction.drawImage(actionAreaBackground, 0, 0, null);
			// graphicsAction.setColor(Color.red);
			// graphicsAction.fill(toFill);
			graphicsAction.dispose();
		}
		// combinedGraphics.setClip(currClip);
		if ((popupArea != null) && !popupArea.isEmpty()) {
			// layer number four - popup area with its model. Opacity 40%
			// for enabled action area, 100% for disabled action area
			Graphics2D graphicsPopup = (Graphics2D) combinedGraphics.create();
			// System.out.println(popupArea);
			graphicsPopup.clip(popupArea);
			// System.out.println(graphicsPopup.getClipBounds());
			float popupAlpha = 0.4f;
			if (!actionModel.isEnabled())
				popupAlpha = 1.0f;
			if ((popupModel != null) && !popupModel.isEnabled())
				popupAlpha = 0.0f;
			graphicsPopup.setComposite(AlphaComposite.SrcOver
					.derive(popupAlpha));
			// System.out.println(popupAlpha + ":"
			// + ComponentState.getState(popupModel, this.commandButton));
			BufferedImage popupAreaBackground = CommandButtonBackgroundDelegate
					.getFullAlphaBackground(commandButton, null, fillPainter,
							borderPainter, commandButton.getWidth(),
							commandButton.getHeight(), ui
									.getPopupTransitionTracker(), false);
			graphicsPopup.drawImage(popupAreaBackground, 0, 0, null);
			// graphicsPopup.setColor(Color.blue);
			// graphicsPopup.fill(toFill);
			graphicsPopup.dispose();
		}
		combinedGraphics.dispose();
		// System.out.println(imageCache.size());
		return layers;
	}

	/**
	 * Returns the memory usage string.
	 * 
	 * @return Memory usage string.
	 */
	static String getMemoryUsage() {
		StringBuffer sb = new StringBuffer();
		sb.append("SubstanceBackgroundDelegate: \n");
		sb.append("\t" + imageCache.size() + " regular");
		// + pairwiseBackgrounds.size() + " pairwise");
		return sb.toString();
	}

	public static void paintThemedCommandButtonIcon(Graphics2D g,
			Rectangle iconRect, AbstractCommandButton commandButton,
			Icon regular, ButtonModel model,
			StateTransitionTracker stateTransitionTracker) {
		Icon themed = SubstanceCoreUtilities.getThemedIcon(commandButton,
				regular);

		boolean useRegularVersion = model.isArmed()
				|| model.isPressed()
				|| model.isSelected()
				|| regular.getClass()
						.isAnnotationPresent(TransitionAware.class);
		Graphics2D g2d = (Graphics2D) g.create();
		if (useRegularVersion) {
			regular.paintIcon(commandButton, g2d, iconRect.x, iconRect.y);
		} else {
			if (stateTransitionTracker != null) {
				float alpha = stateTransitionTracker.getActiveStrength();
				if (alpha < 1.0f) {
					// paint the themed image full opaque on a separate image
					BufferedImage themedImage = FlamingoUtilities
							.getBlankImage(themed.getIconWidth(), themed
									.getIconHeight());
					themed.paintIcon(commandButton, themedImage
							.createGraphics(), 0, 0);
					// and paint that image translucently
					g2d.setComposite(LafWidgetUtilities.getAlphaComposite(
							commandButton, 1.0f - alpha, g));
					g2d.drawImage(themedImage, iconRect.x, iconRect.y, null);
				}

				if (alpha > 0.0f) {
					// paint the regular image full opaque on a separate image
					BufferedImage regularImage = FlamingoUtilities
							.getBlankImage(regular.getIconWidth(), regular
									.getIconHeight());
					regular.paintIcon(commandButton, regularImage
							.createGraphics(), 0, 0);
					// and paint that image translucently
					g2d.setComposite(LafWidgetUtilities.getAlphaComposite(
							commandButton, alpha, g));
					g2d.drawImage(regularImage, iconRect.x, iconRect.y, null);
				}
			} else {
				if (model.isRollover()) {
					regular.paintIcon(commandButton, g2d, iconRect.x,
							iconRect.y);
				} else {
					themed
							.paintIcon(commandButton, g2d, iconRect.x,
									iconRect.y);
				}
			}
		}
		g2d.dispose();
	}
}
