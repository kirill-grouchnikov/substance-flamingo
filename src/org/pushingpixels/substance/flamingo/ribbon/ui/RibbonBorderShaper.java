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

import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

/**
 * Utility class for computing outlines of ribbons and ribbon toggle buttons.
 * 
 * @author Kirill Grouchnikov
 */
public class RibbonBorderShaper {
	public static float getRibbonToggleButtonRadius(JComponent comp) {
		return SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils
				.getComponentFontSize(comp), 5.0f, 2, 1.0f);
	}

	public static GeneralPath getRibbonBorderOutline(JRibbon ribbon,
			int startX, int endX, int startSelectedX, int endSelectedX,
			int topY, int bandTopY, int bottomY, float radius) {

		int height = bottomY - topY;
		GeneralPath result = new GeneralPath();
		// float radius3 = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils
		// .getComponentFontSize(ribbon), 3.0f, 3, 1.0f);
		float radius3 = (float) (radius / (1.5 * Math.pow(height, 0.5)));
		float buttonRadius = getRibbonToggleButtonRadius(ribbon);
		float buttonRadius3 = (float) (buttonRadius / (1.5 * Math.pow(height,
				0.5)));

		// start in the top left corner at the end of the curve
		result.moveTo(startX + radius, bandTopY);

		// move to the bottom start of the selected tab and curve up
		result.lineTo(startSelectedX - radius, bandTopY);
		result.quadTo(startSelectedX - radius3, bandTopY - radius3,
				startSelectedX, bandTopY - radius);

		// move to the top start of the selected tab and curve right
		result.lineTo(startSelectedX, topY + buttonRadius);
		result.quadTo(startSelectedX + buttonRadius3, topY + buttonRadius3,
				startSelectedX + buttonRadius, topY);

		// move to the top end of the selected tab and curve down
		result.lineTo(endSelectedX - buttonRadius - 1, topY);
		result.quadTo(endSelectedX + buttonRadius3 - 1, topY + buttonRadius3,
				endSelectedX - 1, topY + buttonRadius);

		// move to the bottom end of the selected tab and curve right
		result.lineTo(endSelectedX - 1, bandTopY - radius);
		result.quadTo(endSelectedX + radius3 - 1, bandTopY - radius3,
				endSelectedX + radius - 1, bandTopY);

		// move to the top right corner and curve down
		result.lineTo(endX - radius - 1, bandTopY);
		result.quadTo(endX - radius3 - 1, bandTopY + radius3, endX - 1,
				bandTopY + radius);

		// move to the bottom right corner and curve left
		result.lineTo(endX - 1, bottomY - radius - 1);
		result.quadTo(endX - radius3 - 1, bottomY - 1 - radius3, endX - radius
				- 1, bottomY - 1);

		// move to the bottom left corner and curve up
		result.lineTo(startX + radius, bottomY - 1);
		result.quadTo(startX + radius3, bottomY - 1 - radius3, startX, bottomY
				- radius - 1);

		// move to the top left corner and curve right
		result.lineTo(startX, bandTopY + radius);
		result.quadTo(startX + radius3, bandTopY + radius3, startX + radius,
				bandTopY);

		return result;
	}
}
