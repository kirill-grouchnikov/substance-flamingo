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
package org.pushingpixels.substance.flamingo.ribbon.gallery.oob;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
import org.pushingpixels.flamingo.api.common.StringValuePair;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.SkinInfo;

/**
 * In-ribbon gallery that contains all available <b>Substance</b> skins.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceSkinRibbonGallery {
	/**
	 * Adds an in-ribbon gallery that contains all available <b>Substance</b>
	 * skins to the specified ribbon band.
	 * 
	 * @param ribbonBand
	 *            Ribbon band that will contain the newly added skin gallery.
	 */
	public static void addSkinGallery(JRibbonBand ribbonBand) {
		Map<RibbonElementPriority, Integer> prefWidths = new HashMap<RibbonElementPriority, Integer>();
		prefWidths.put(RibbonElementPriority.LOW, 2);
		prefWidths.put(RibbonElementPriority.MEDIUM, 4);
		prefWidths.put(RibbonElementPriority.TOP, 8);

		List<StringValuePair<List<JCommandToggleButton>>> skinGroups = new ArrayList<StringValuePair<List<JCommandToggleButton>>>();
		List<JCommandToggleButton> skinButtons = new ArrayList<JCommandToggleButton>();

		Map<String, SkinInfo> skins = SubstanceLookAndFeel.getAllSkins();
		for (Map.Entry<String, SkinInfo> entry : skins.entrySet()) {
			try {
				final SubstanceSkin skin = (SubstanceSkin) Class.forName(
						entry.getValue().getClassName()).newInstance();
				ResizableIcon icon = new SkinResizableIcon(skin, 60, 40);
				JCommandToggleButton skinButton = new JCommandToggleButton(skin
						.getDisplayName(), icon);
				skinButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								SubstanceLookAndFeel.setSkin(skin);
							}
						});
					}
				});
				skinButtons.add(skinButton);
			} catch (Exception exc) {
			}
		}

		skinGroups.add(new StringValuePair<List<JCommandToggleButton>>("Skins",
				skinButtons));

		ribbonBand.addRibbonGallery("Skins", skinGroups, prefWidths, 5, 3,
				RibbonElementPriority.TOP);
	}
}
