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
package org.pushingpixels.substance.flamingo;

import java.awt.*;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.*;

import org.pushingpixels.flamingo.api.bcb.JBreadcrumbBar;
import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.popup.JCommandPopupMenu;
import org.pushingpixels.flamingo.api.common.popup.JPopupPanel;
import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.internal.ui.common.JRichTooltipPanel;
import org.pushingpixels.flamingo.internal.ui.common.popup.JColorSelectorPanel;
import org.pushingpixels.flamingo.internal.ui.ribbon.*;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuButton;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuPopupPanel;
import org.pushingpixels.lafplugin.LafComponentPlugin;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.flamingo.ribbon.ui.SubstanceRibbonBandBorder;
import org.pushingpixels.substance.internal.utils.*;
import org.pushingpixels.substance.internal.utils.border.SubstanceBorder;

/**
 * Plugin for Flamingo components.
 * 
 * @author Kirill Grouchnikov
 */
public class FlamingoPlugin implements LafComponentPlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.pushingpixels.lafplugin.LafComponentPlugin#getDefaults(java.lang.
	 * Object)
	 */
	public Object[] getDefaults(Object mSkin) {
		String UI_COMMON_CLASSNAME_PREFIX = "org.pushingpixels.substance.flamingo.common.ui.Substance";

		String UI_RIBBON_CLASSNAME_PREFIX = "org.pushingpixels.substance.flamingo.ribbon.ui.Substance";

		String UI_BCB_CLASSNAME_PREFIX = "org.pushingpixels.substance.flamingo.bcb.ui.Substance";

		FontSet fontSet = SubstanceLookAndFeel.getFontPolicy().getFontSet(
				"Substance", null);
		Font controlFont = fontSet.getControlFont();

		SubstanceSkin skin = (SubstanceSkin) mSkin;
		Border textBorder = new BorderUIResource(new SubstanceBorder());

		SubstanceColorScheme mainActiveScheme = skin
				.getActiveColorScheme(DecorationAreaType.NONE);
		Color backgroundColor = new ColorUIResource(mainActiveScheme
				.getBackgroundFillColor());
		Color disabledForegroundColor = SubstanceColorUtilities
				.getForegroundColor(mainActiveScheme);

		Object[] defaults = new Object[] {
				JCommandButtonPanel.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "CommandButtonPanelUI",

				JCommandButton.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "CommandButtonUI",

				JCommandMenuButton.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "CommandMenuButtonUI",

				JCommandToggleButton.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "CommandToggleButtonUI",

				JCommandToggleMenuButton.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "CommandToggleMenuButtonUI",

				JPopupPanel.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "PopupPanelUI",

				JScrollablePanel.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "ScrollablePanelUI",

				JCommandPopupMenu.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "CommandPopupMenuUI",

				JColorSelectorPanel.uiClassID,
				UI_COMMON_CLASSNAME_PREFIX + "ColorSelectorPanelUI",

				JBandControlPanel.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "BandControlPanelUI",

				JFlowBandControlPanel.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "FlowBandControlPanelUI",

				JRibbon.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonUI",

				JRibbonRootPane.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonRootPaneUI",

				JRibbonBand.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonBandUI",

				JRibbonGallery.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonGalleryUI",

				JRibbonApplicationMenuButton.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonApplicationMenuButtonUI",

				JRibbonApplicationMenuPopupPanel.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX
						+ "RibbonApplicationMenuPopupPanelUI",

				JRibbonTaskToggleButton.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonTaskToggleButtonUI",

				JRichTooltipPanel.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RichTooltipPanelUI",

				JRibbonComponent.uiClassID,
				UI_RIBBON_CLASSNAME_PREFIX + "RibbonComponentUI",

				JBreadcrumbBar.uiClassID,
				UI_BCB_CLASSNAME_PREFIX + "BreadcrumbBarUI",

				"BreadcrumbBar.font",
				controlFont,

				"IconPanel.font",
				fontSet.getTitleFont(),

				"CommandButtonPanel.font",
				controlFont.deriveFont(Font.BOLD, controlFont.getSize() + 1),

				"Ribbon.font",
				controlFont,

				"ControlPanel.border",
				null,

				"ControlPanel.background",
				backgroundColor,

				"CommandButton.popupActionIcon",
				new IconUIResource(SubstanceImageCreator
						.getDoubleArrowIconDelta(SubstanceSizeUtils
								.getControlFontSize(), -2, -1, -0.5f,
								SwingConstants.SOUTH, mainActiveScheme)),

				"PopupPanel.border", textBorder,

				"PopupGallery.background", backgroundColor,

				"Ribbon.border",
				new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1),

				"RibbonBand.border", new SubstanceRibbonBandBorder(),

				"RibbonBand.background", backgroundColor,

				"RibbonGallery.border",
				new BorderUIResource.EmptyBorderUIResource(2, 2, 2, 2),

				"RibbonGallery.margin", new Insets(3, 3, 3, 3),

				"ToggleButton.background", backgroundColor,

				"ToggleButton.disabledText", disabledForegroundColor };
		return defaults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pushingpixels.lafplugin.LafComponentPlugin#uninitialize()
	 */
	public void uninitialize() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pushingpixels.lafplugin.LafComponentPlugin#initialize()
	 */
	public void initialize() {
	}
}
