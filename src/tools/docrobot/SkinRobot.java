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
package tools.docrobot;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.timing.Pause;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;

import test.substance.ribbon.NewCheckRibbon;

/**
 * The base class for taking screenshots of skins for Substance documentation.
 * 
 * @author Kirill Grouchnikov
 */
public abstract class SkinRobot {
	/**
	 * The associated Substance skin.
	 */
	protected SubstanceSkin skin;

	/**
	 * The screenshot filename.
	 */
	protected String screenshotFilename;

	/**
	 * The frame instance.
	 */
	protected NewCheckRibbon ribbonFrame;

	/**
	 * Creates the new screenshot robot.
	 * 
	 * @param skin
	 *            Substance skin.
	 * @param screenshotFilename
	 *            The screenshot filename.
	 */
	public SkinRobot(SubstanceSkin skin, String screenshotFilename) {
		this.skin = skin;
		this.screenshotFilename = screenshotFilename;
	}

	/**
	 * Runs the screenshot process.
	 */
	public void run() {
		long start = System.currentTimeMillis();

		Robot robot = BasicRobot.robotWithNewAwtHierarchy();

		// set skin
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				SubstanceLookAndFeel.setSkin(skin);
				JFrame.setDefaultLookAndFeelDecorated(true);
			}
		});
		robot.waitForIdle();

		// create the frame and set the icon image
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				ribbonFrame = new NewCheckRibbon();
				ribbonFrame.configureRibbon();
				ribbonFrame.applyComponentOrientation(ComponentOrientation
						.getOrientation(Locale.getDefault()));
				Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment()
						.getMaximumWindowBounds();
				ribbonFrame.setPreferredSize(new Dimension(r.width,
						r.height / 2));
				ribbonFrame.setMinimumSize(new Dimension(100, r.height / 3));
				ribbonFrame.pack();
				ribbonFrame.setLocation(r.x, r.y);
				ribbonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				ribbonFrame.setVisible(true);
			}
		});
		robot.waitForIdle();

		robot.moveMouse(new Point(0, 0));
		robot.waitForIdle();

		// wait for a second
		Pause.pause(1000);

		// make the first screenshot
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				makeScreenshot();
			}
		});
		robot.waitForIdle();

		// dispose the frame
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				ribbonFrame.dispose();
			}
		});
		robot.waitForIdle();

		long end = System.currentTimeMillis();
		System.out.println(this.getClass().getSimpleName() + " : "
				+ (end - start) + "ms");
	}

	/**
	 * Creates the screenshot and saves it on the disk.
	 * 
	 * @param count
	 *            Sequence number for the screenshot.
	 */
	public void makeScreenshot() {
		BufferedImage bi = new BufferedImage(ribbonFrame.getWidth(),
				ribbonFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		ribbonFrame.paint(g);

		BufferedImage finalIm = new BufferedImage(500, 200,
				BufferedImage.TYPE_INT_ARGB);
		finalIm.getGraphics().drawImage(bi, 0, 0, null);

		try {
			File output = new File(this.screenshotFilename + ".png");
			output.getParentFile().mkdirs();
			ImageIO.write(finalIm, "png", output);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
