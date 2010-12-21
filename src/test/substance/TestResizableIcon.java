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
package test.substance;

import java.awt.*;

import javax.swing.JFrame;

import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.substance.flamingo.ribbon.gallery.oob.ColorSchemeResizableIcon;

public final class TestResizableIcon {

	/**
	 * Demo frame.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private static final class ImageCreatorFrame extends JFrame {
		/**
		 * Simple constructor. Creates all the icons.
		 */
		public ImageCreatorFrame() {
			super("Image creator demo");
			int width = 450;
			int height = 200;
			Dimension dim = new Dimension(width, height);
			this.setPreferredSize(dim);
			this.setSize(dim);
			this.setResizable(false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.Component#paint(java.awt.Graphics)
		 */
		@Override
		public final void paint(Graphics g) {
			ResizableIcon ri = new ColorSchemeResizableIcon(null, 32, 32);
			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			int xpos = 5;
			int[] dims = new int[] { 16, 20, 24, 28, 32, 36, 40, 44, 48 };
			for (int dim : dims) {
				int y = 45;
				ri.setDimension(new Dimension(dim, dim));
				g.setFont(new Font("Tahoma", Font.PLAIN, 11));
				g.setColor(Color.black);
				String sd = "" + dim;
				int len = g.getFontMetrics().stringWidth(sd);
				g.drawString("" + dim, (xpos + (dim - len) / 2), 42);
				ri.paintIcon(null, g, xpos, y);
				xpos += (dim + 12);
			}
		}
	}

	/**
	 * Main function for running <code>this</code> demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ImageCreatorFrame icf = new ImageCreatorFrame();
		icf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		icf.setVisible(true);
	}
}
