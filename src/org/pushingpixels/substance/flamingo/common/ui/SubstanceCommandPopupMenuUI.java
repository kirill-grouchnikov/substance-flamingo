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
package org.pushingpixels.substance.flamingo.common.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.internal.ui.common.popup.BasicCommandPopupMenuUI;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants.MenuGutterFillKind;
import org.pushingpixels.substance.internal.painter.SeparatorPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceBorder;

/**
 * UI for {@link JCommandPopupMenu} components in <b>Substance</b> look and
 * feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceCommandPopupMenuUI extends BasicCommandPopupMenuUI {
	public static ComponentUI createUI(JComponent c) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(c);
		return new SubstanceCommandPopupMenuUI();
	}

	@Override
	protected JPanel createMenuPanel() {
		return new SubstanceMenuPanel();
	}

	protected static class SubstanceMenuPanel extends MenuPanel {
		@Override
		protected void paintIconGutterSeparator(Graphics g) {
			Graphics2D g2d = (Graphics2D) g.create();
			int sepX = this.getSeparatorX();
			g2d.translate(sepX, 0);
			SeparatorPainterUtils.paintSeparator(this, g2d, 2,
					this.getHeight(), JSeparator.VERTICAL, true, 0, 0, false);
			g2d.dispose();
		}

		@Override
		protected void paintIconGutterBackground(Graphics g) {
			Graphics2D g2d = (Graphics2D) g.create();
			MenuGutterFillKind fillKind = SubstanceCoreUtilities
					.getMenuGutterFillKind();
			if (fillKind != MenuGutterFillKind.NONE) {
				SubstanceColorScheme scheme = SubstanceColorSchemeUtilities
						.getColorScheme(this, ComponentState.ENABLED);
				Color leftColor = ((fillKind == MenuGutterFillKind.SOFT_FILL) || (fillKind == MenuGutterFillKind.HARD)) ? scheme
						.getUltraLightColor()
						: scheme.getLightColor();
				Color rightColor = ((fillKind == MenuGutterFillKind.SOFT_FILL) || (fillKind == MenuGutterFillKind.SOFT)) ? scheme
						.getUltraLightColor()
						: scheme.getLightColor();
				g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this,
						0.7f, g));

				int sepX = this.getSeparatorX();
				if (this.getComponentOrientation().isLeftToRight()) {
					GradientPaint gp = new GradientPaint(0, 0, leftColor,
							sepX + 2, 0, rightColor);
					g2d.setPaint(gp);
					g2d.fillRect(0, 0, sepX, this.getHeight());
				} else {
					GradientPaint gp = new GradientPaint(sepX, 0, leftColor,
							this.getWidth(), 0, rightColor);
					g2d.setPaint(gp);
					g2d.fillRect(sepX + 2, 0, this.getWidth() - sepX, this
							.getHeight());
				}
			}
			g2d.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.flamingo.common.ui.BasicCommandPopupMenuUI#
	 * createScrollableButtonPanel()
	 */
	@Override
	protected ScrollableCommandButtonPanel createScrollableButtonPanel() {
		ScrollableCommandButtonPanel result = super
				.createScrollableButtonPanel();
		result.setBorder(new SubstanceBorder(new Insets(0, 0, 1, 0)));
		return result;
	}
}
