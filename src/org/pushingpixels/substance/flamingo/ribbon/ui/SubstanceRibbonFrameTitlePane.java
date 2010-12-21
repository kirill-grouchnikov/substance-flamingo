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
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pushingpixels.flamingo.api.ribbon.*;
import org.pushingpixels.flamingo.internal.ui.ribbon.BasicRibbonUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.RibbonUI;
import org.pushingpixels.flamingo.internal.ui.ribbon.appmenu.JRibbonApplicationMenuButton;
import org.pushingpixels.flamingo.internal.utils.FlamingoUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.*;
import org.pushingpixels.substance.api.painter.border.SubstanceBorderPainter;
import org.pushingpixels.substance.api.painter.fill.MatteFillPainter;
import org.pushingpixels.substance.internal.painter.DecorationPainterUtils;
import org.pushingpixels.substance.internal.painter.SeparatorPainterUtils;
import org.pushingpixels.substance.internal.ui.SubstanceRootPaneUI;
import org.pushingpixels.substance.internal.utils.*;

/**
 * Custom title pane for {@link JRibbonFrame} running under Substance
 * look-and-feel.
 * 
 * @author Kirill Grouchnikov
 */
public class SubstanceRibbonFrameTitlePane extends SubstanceTitlePane {
	/**
	 * Custom component to paint the header of a single contextual task group.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class SubstanceContextualGroupComponent extends JComponent {
		/**
		 * The associated contextual task group.
		 */
		RibbonContextualTaskGroup taskGroup;

		/**
		 * Creates the new task group header component.
		 * 
		 * @param taskGroup
		 *            The associated contextual task group.
		 */
		public SubstanceContextualGroupComponent(
				RibbonContextualTaskGroup taskGroup) {
			this.taskGroup = taskGroup;
			this.setOpaque(false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent(Graphics g) {
			int width = this.getWidth();
			int height = this.getHeight();
			Color hueColor = this.taskGroup.getHueColor();

			Graphics2D g2d = (Graphics2D) g.create();
			Paint paint = new GradientPaint(0, 0, SubstanceColorUtilities
					.getAlphaColor(hueColor, 0), 0, height,
					SubstanceColorUtilities.getAlphaColor(hueColor,
							(int) (255 * RibbonContextualTaskGroup.HUE_ALPHA)));
			g2d = (Graphics2D) g.create();
			// translucent gradient paint
			g2d.setPaint(paint);
			g2d.fillRect(0, 0, width, height);
			// and a solid line at the bottom
			g2d.setColor(hueColor);
			g2d.drawLine(1, height - 1, width, height - 1);

			JRibbon ribbon = getRibbon();

			SubstanceColorScheme scheme = SubstanceCoreUtilities.getSkin(
					rootPane).getEnabledColorScheme(
					DecorationAreaType.PRIMARY_TITLE_PANE);

			// task group title
			FontMetrics fm = this.getFontMetrics(ribbon.getFont());
			int yOffset = (height - fm.getHeight()) / 2;
			RenderingUtils.installDesktopHints(g2d, this);

			int offset = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils
					.getComponentFontSize(this), 5, 2, 1, false);
			if (getComponentOrientation().isLeftToRight()) {
				SubstanceTextUtilities.paintText(g2d, this, new Rectangle(
						offset, yOffset, width, height - yOffset),
						this.taskGroup.getTitle(), -1, ribbon.getFont(),
						SubstanceColorUtilities.getForegroundColor(scheme),
						null);
			} else {
				SubstanceTextUtilities.paintText(g2d, this, new Rectangle(width
						- offset
						- g2d.getFontMetrics().stringWidth(
								this.taskGroup.getTitle()), yOffset, width,
						height - yOffset), this.taskGroup.getTitle(), -1,
						ribbon.getFont(), SubstanceColorUtilities
								.getForegroundColor(scheme), null);
			}

			// left separator
			SeparatorPainterUtils.paintSeparator(ribbon, g2d, 2, height,
					SwingConstants.VERTICAL, false, height / 3, 0, true);

			// right separator
			g2d.translate(width - 1, 0);
			SeparatorPainterUtils.paintSeparator(ribbon, g2d, 2, height,
					SwingConstants.VERTICAL, false, height / 3, 0, true);

			g2d.dispose();
		}
	}

	/**
	 * The taskbar panel that holds the {@link JRibbon#getTaskbarComponents()}.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class TaskbarPanel extends JPanel {
		/**
		 * Creates the new taskbar panel.
		 */
		public TaskbarPanel() {
			super(new TaskbarLayout());
			this.setOpaque(false);
			int insets = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils
					.getComponentFontSize(this), 2, 3, 1, false);
			this.setBorder(new EmptyBorder(insets, insets, insets, insets));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent(Graphics g) {
		}

		/**
		 * Returns the outline of this taskbar panel.
		 * 
		 * @param insets
		 *            Insets.
		 * @return The outline of this taskbar panel.
		 */
		protected Shape getOutline(double insets) {
			double width = this.getWidth() - 2 * insets - 1;
			double height = this.getHeight() - 2 * insets - 1;
			float radius = (float) height;

			if (getComponentCount() == 0) {
				return null;
			} else {
				GeneralPath outline = new GeneralPath();
				JRibbonApplicationMenuButton menuButton = FlamingoUtilities
						.getApplicationMenuButton(SwingUtilities
								.getWindowAncestor(this));

				boolean ltr = getComponentOrientation().isLeftToRight();

				double alpha = Math.asin((radius - insets / 2)
						/ (radius + insets / 2));

				if (ltr) {
					// top right
					outline.moveTo(insets + width - height / 2, insets);

					// right arc
					outline
							.append(new Arc2D.Double(insets + width - height,
									insets, height, height, 90, -180,
									Arc2D.OPEN), true);
					// bottom left
					outline.lineTo(insets, insets + height);
					if (menuButton != null) {
						double arcSpan = 90;
						if (insets != 0) {
							arcSpan = 180.0 * alpha / Math.PI;
						}
						outline.append(new Arc2D.Double(insets - 2 * height,
								insets, 2 * height, 2 * height, 0, arcSpan,
								Arc2D.OPEN), true);
					} else {
						outline.lineTo(insets, insets);
					}
				} else {
					// top left corner
					outline.moveTo(insets + height / 2, 0);
					// left arc
					outline.append(new Arc2D.Double(insets, 0, height, height,
							90, 180, Arc2D.OPEN), true);
					// bottom right corner
					outline.lineTo(width - 1, insets + height);
					if (menuButton != null) {
						double arcSpan = -90;
						if (insets != 0) {
							arcSpan = -180.0 * alpha / Math.PI;
						}
						outline.append(new Arc2D.Double(width - 1, 0,
								2 * height, 2 * height, 180, arcSpan,
								Arc2D.OPEN), true);
					} else {
						outline.lineTo(width - 1, 0);
					}
				}
				outline.closePath();

				return outline;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
		@Override
		public Dimension getPreferredSize() {
			Dimension result = super.getPreferredSize();
			return new Dimension(result.width + result.height / 2,
					result.height);
		}
	}

	/**
	 * Maps the currently visible contextual task groups to the respective child
	 * components of this title pane.
	 */
	protected Map<RibbonContextualTaskGroup, SubstanceContextualGroupComponent> taskComponentMap;

	/**
	 * Listener to sync {@link #taskComponentMap}.
	 */
	protected ChangeListener ribbonFrameChangeListener;

	/**
	 * Panel for the taskbar components.
	 */
	protected TaskbarPanel taskbarPanel;

	/**
	 * Creates a new title pane for {@link JRibbonFrame}.
	 * 
	 * @param root
	 *            Root pane.
	 * @param ui
	 *            UI delegate.
	 */
	public SubstanceRibbonFrameTitlePane(JRootPane root, SubstanceRootPaneUI ui) {
		super(root, ui);
		this.taskComponentMap = new HashMap<RibbonContextualTaskGroup, SubstanceContextualGroupComponent>();
		this.taskbarPanel = new TaskbarPanel();
		this.markExtraComponent(this.taskbarPanel, ExtraComponentKind.LEADING);
		this.add(this.taskbarPanel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.substance.utils.SubstanceTitlePane#createLayout()
	 */
	@Override
	protected LayoutManager createLayout() {
		return new RibbonFrameTitlePaneLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.substance.utils.SubstanceTitlePane#addNotify()
	 */
	@Override
	public void addNotify() {
		super.addNotify();

		JRibbon ribbon = this.getRibbon();
		ribbon.putClientProperty(BasicRibbonUI.IS_USING_TITLE_PANE,
				Boolean.TRUE);

		this.syncRibbonState();

		this.ribbonFrameChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				syncRibbonState();
			}
		};
		ribbon.addChangeListener(this.ribbonFrameChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvnet.substance.utils.SubstanceTitlePane#removeNotify()
	 */
	@Override
	public void removeNotify() {
		JRibbon ribbon = this.getRibbon();
		ribbon.putClientProperty(BasicRibbonUI.IS_USING_TITLE_PANE, null);

		for (SubstanceContextualGroupComponent groupComp : this.taskComponentMap
				.values()) {
			this.remove(groupComp);
		}

		ribbon.removeChangeListener(this.ribbonFrameChangeListener);
		this.ribbonFrameChangeListener = null;

		super.removeNotify();
	}

	/**
	 * Synchronizes the child components for ribbon state (visible contextual
	 * task groups and taskbar components).
	 */
	protected void syncRibbonState() {
		// Contextual task groups
		for (SubstanceContextualGroupComponent groupComp : this.taskComponentMap
				.values()) {
			this.remove(groupComp);
		}
		this.taskComponentMap.clear();
		JRibbon ribbon = this.getRibbon();
		for (int i = 0; i < ribbon.getContextualTaskGroupCount(); i++) {
			RibbonContextualTaskGroup group = ribbon.getContextualTaskGroup(i);
			if (!ribbon.isVisible(group))
				continue;
			SubstanceContextualGroupComponent taskGroupComponent = new SubstanceContextualGroupComponent(
					group);
			taskGroupComponent.applyComponentOrientation(this.getRibbon()
					.getComponentOrientation());
			this.add(taskGroupComponent);
			this.taskComponentMap.put(group, taskGroupComponent);
			this.markExtraComponent(taskGroupComponent,
					ExtraComponentKind.TRAILING);
		}
		// Taskbar components
		this.taskbarPanel.removeAll();
		this.taskbarPanel.setPreferredSize(null);
		for (Component taskbarComp : ribbon.getTaskbarComponents()) {
			this.taskbarPanel.add(taskbarComp);
		}
	}

	/**
	 * Custom layout manager for the title panes of {@link JRibbonFrame} under
	 * decorated mode.
	 * 
	 * @author Kirill Grouchnikov
	 */
	protected class RibbonFrameTitlePaneLayout extends TitlePaneLayout {
		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.jvnet.substance.utils.SubstanceTitlePane.TitlePaneLayout#
		 * layoutContainer(java.awt.Container)
		 */
		@Override
		public void layoutContainer(Container c) {
			super.layoutContainer(c);

			JRibbon ribbon = getRibbon();
			boolean ltr = ribbon.getComponentOrientation().isLeftToRight();
			RibbonUI ribbonUI = ribbon.getUI();

			if (ltr) {
				// headers of contextual task groups
				for (Map.Entry<RibbonContextualTaskGroup, SubstanceContextualGroupComponent> entry : taskComponentMap
						.entrySet()) {
					Rectangle taskGroupBounds = ribbonUI
							.getContextualTaskGroupBounds(entry.getKey());
					// make sure that the header bounds do not overlap with the
					// min / max / close buttons
					int minTrailingX = c.getWidth();

					for (int i = 0; i < c.getComponentCount(); i++) {
						Component child = c.getComponent(i);
						if (!child.isVisible())
							continue;
						if (child instanceof JComponent) {
							ExtraComponentKind kind = (ExtraComponentKind) ((JComponent) child)
									.getClientProperty(EXTRA_COMPONENT_KIND);
							if (kind == ExtraComponentKind.LEADING)
								continue;
							if (child instanceof SubstanceContextualGroupComponent)
								continue;

							minTrailingX = Math.min(child.getX(), minTrailingX);
						}
					}

					int width = taskGroupBounds.width;
					if (taskGroupBounds.x + width > (minTrailingX - 5)) {
						width = minTrailingX - 5 - taskGroupBounds.x;
					}
					entry.getValue().setBounds(
							new Rectangle(taskGroupBounds.x, 0, width, c
									.getHeight()));
					// hide headers when the task toggle buttons
					// are wrapped with visible scroller buttons
					entry.getValue().setVisible(
							!ribbonUI.isShowingScrollsForTaskToggleButtons());
				}

				// taskbar panel
				taskbarPanel.setVisible(true);
				Dimension pref = taskbarPanel.getPreferredSize();
				if (taskbarPanel.getComponentCount() == 0) {
					// fix for issue 38 on Flamingo - if there are no
					// taskbar components, don't push the title to the right
					pref.width = 0;
				}

				SubstanceRibbonRootPaneUI rootPaneUI = (SubstanceRibbonRootPaneUI) getRootPane()
						.getUI();
				JRibbonApplicationMenuButton menuButton = rootPaneUI.applicationMenuButton;

				if (menuButton != null) {
					if (menuButton.isVisible()) {
						int maxLeadingX = menuButton.getX()
								+ menuButton.getWidth() + 2
								* getTaskBarLayoutGap(taskbarPanel);
						if (taskbarPanel.isVisible()) {
							taskbarPanel.setBounds(maxLeadingX, 0, pref.width,
									c.getHeight());
						}
						menuBar.setVisible(false);
					} else {
						if (taskbarPanel.isVisible()) {
							if (pref.width == 0) {
								taskbarPanel.setBounds(menuBar.getX()
										+ menuBar.getWidth(), 0, pref.width, c
										.getHeight());
							} else {
								taskbarPanel.setBounds(menuBar.getX()
										+ menuBar.getWidth() + 5, 0,
										pref.width, c.getHeight());
							}
						}
						menuBar.setVisible(true);
					}
				} else {
					menuBar.setVisible(true);
				}
			} else {
				// headers of contextual task groups
				for (Map.Entry<RibbonContextualTaskGroup, SubstanceContextualGroupComponent> entry : taskComponentMap
						.entrySet()) {
					Rectangle taskGroupBounds = ribbonUI
							.getContextualTaskGroupBounds(entry.getKey());
					// make sure that the header bounds do not overlap with the
					// min / max / close buttons
					int maxTrailingX = 0;

					for (int i = 0; i < c.getComponentCount(); i++) {
						Component child = c.getComponent(i);
						if (!child.isVisible())
							continue;
						if (child instanceof JComponent) {
							ExtraComponentKind kind = (ExtraComponentKind) ((JComponent) child)
									.getClientProperty(EXTRA_COMPONENT_KIND);
							if (kind == ExtraComponentKind.LEADING)
								continue;
							if (child instanceof SubstanceContextualGroupComponent)
								continue;

							maxTrailingX = Math.max(child.getX()
									+ child.getWidth(), maxTrailingX);
						}
					}

					int width = taskGroupBounds.width;
					int x = taskGroupBounds.x;
					if (taskGroupBounds.x < (maxTrailingX + 5)) {
						int diff = maxTrailingX + 5 - taskGroupBounds.x;
						x += diff;
						width -= diff;
					}
					entry.getValue().setBounds(
							new Rectangle(x, 0, width, c.getHeight()));
					// hide headers when the task toggle buttons
					// are wrapped with visible scroller buttons
					entry.getValue().setVisible(
							!ribbonUI.isShowingScrollsForTaskToggleButtons());
				}

				// taskbar panel
				taskbarPanel.setVisible(true);
				Dimension pref = taskbarPanel.getPreferredSize();
				if (taskbarPanel.getComponentCount() == 0) {
					// fix for issue 38 on Flamingo - if there are no
					// taskbar components, don't push the title to the left
					pref.width = 0;
				}

				SubstanceRibbonRootPaneUI rootPaneUI = (SubstanceRibbonRootPaneUI) getRootPane()
						.getUI();
				JRibbonApplicationMenuButton menuButton = rootPaneUI.applicationMenuButton;

				if (menuButton != null) {
					if (menuButton.isVisible()) {
						int maxLeadingX = menuButton.getX() - 2
								* getTaskBarLayoutGap(taskbarPanel);
						if (taskbarPanel.isVisible()) {
							taskbarPanel.setBounds(maxLeadingX - pref.width, 0,
									pref.width, c.getHeight());
						}
						menuBar.setVisible(false);
					} else {
						if (taskbarPanel.isVisible()) {
							if (pref.width == 0) {
								taskbarPanel.setBounds(menuBar.getX(), 0,
										pref.width, c.getHeight());
							} else {
								taskbarPanel.setBounds(menuBar.getX() - 5
										- pref.width, 0, pref.width, c
										.getHeight());
							}
						}
						menuBar.setVisible(true);
					}
				} else {
					menuBar.setVisible(true);
				}
			}
		}
	}

	/**
	 * Layout for the task bar.
	 * 
	 * @author Kirill Grouchnikov
	 */
	private class TaskbarLayout implements LayoutManager {
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
		 * java.awt.Component)
		 */
		public void addLayoutComponent(String name, Component c) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
		 */
		public void removeLayoutComponent(Component c) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
		 */
		public Dimension preferredLayoutSize(Container c) {
			Insets ins = c.getInsets();
			int pw = 0;
			int gap = getTaskBarLayoutGap(c);
			for (Component regComp : getRibbon().getTaskbarComponents()) {
				pw += regComp.getPreferredSize().width;
				pw += gap;
			}
			return new Dimension(pw + ins.left + ins.right, c.getParent()
					.getHeight());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
		 */
		public Dimension minimumLayoutSize(Container c) {
			return this.preferredLayoutSize(c);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
		 */
		public void layoutContainer(Container c) {
			Insets ins = c.getInsets();
			int gap = getTaskBarLayoutGap(c);
			boolean ltr = getComponentOrientation().isLeftToRight();
			int x = ltr ? ins.left : c.getWidth() - ins.right;
			for (Component regComp : getRibbon().getTaskbarComponents()) {
				int pw = regComp.getPreferredSize().width;
				if (ltr) {
					regComp.setBounds(x, ins.top, pw, c.getHeight() - ins.top
							- ins.bottom);
					x += (pw + gap);
				} else {
					regComp.setBounds(x - pw, ins.top, pw, c.getHeight()
							- ins.top - ins.bottom);
					x -= (pw + gap);
				}
			}
		}
	}

	/**
	 * Retrieves the {@link JRibbon} component of the associated
	 * {@link JRibbonFrame}.
	 * 
	 * @return {@link JRibbon} component of the associated {@link JRibbonFrame}.
	 */
	private JRibbon getRibbon() {
		JRibbonFrame ribbonFrame = (JRibbonFrame) SwingUtilities
				.getWindowAncestor(this);
		JRibbon ribbon = ribbonFrame.getRibbon();
		return ribbon;
	}

	/**
	 * Returns the layout gap of the taskbar panel.
	 * 
	 * @param c
	 *            Container.
	 * @return Layout gap of the taskbar panel.
	 */
	private int getTaskBarLayoutGap(Container c) {
		return SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils
				.getComponentFontSize(c), 1, 6, 1, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jvnet.substance.utils.SubstanceTitlePane#paintComponent(java.awt.
	 * Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		if (taskbarPanel.getWidth() != 0) {
			g2d.translate(taskbarPanel.getX(), taskbarPanel.getY());
			// paint the outline of the taskbar panel to complete
			// the correct appearance in the area behind the application
			// menu button
			// g2d.clipRect(0, 0, 20, 100);
			paintTaskBarPanelOutline(g2d, this.taskbarPanel);
			g2d.translate(-taskbarPanel.getX(), -taskbarPanel.getY());
		}

		if (SubstanceLookAndFeel.getCurrentSkin(this).getOverlayPainters(
				DecorationAreaType.PRIMARY_TITLE_PANE).isEmpty()) {
			// g2d.translate(0, this.getHeight() - 1);
			SubstanceColorScheme compScheme = SubstanceColorSchemeUtilities
					.getColorScheme(this, ColorSchemeAssociationKind.SEPARATOR,
							ComponentState.ENABLED);
			Color sepColor = compScheme.isDark() ? SeparatorPainterUtils
					.getSeparatorShadowColor(compScheme)
					: SeparatorPainterUtils.getSeparatorDarkColor(compScheme);
			g2d.setColor(sepColor);
			g2d.drawLine(0, this.getHeight() - 1, this.getWidth(), this
					.getHeight() - 1);
			// SeparatorPainterUtils.paintSeparator(this, g2d, this.getWidth(),
			// 0,
			// JSeparator.HORIZONTAL, false, 0);
		}
		g2d.dispose();
	}

	/**
	 * Paints the outline of the taskbar panel.
	 * 
	 * @param g
	 *            Graphics context.
	 * @param taskbarPanel
	 *            Taskbar panel.
	 */
	protected static void paintTaskBarPanelOutline(Graphics g,
			TaskbarPanel taskbarPanel) {
		int borderDelta = (int) Math.floor(SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(taskbarPanel)) / 2.0);
		int borderThickness = (int) SubstanceSizeUtils
				.getBorderStrokeWidth(SubstanceSizeUtils
						.getComponentFontSize(taskbarPanel));

		Shape contour = taskbarPanel.getOutline(borderDelta);
		Shape contourInner = taskbarPanel.getOutline(borderDelta
				+ borderThickness);

		SubstanceColorScheme colorScheme = SubstanceColorSchemeUtilities
				.getColorScheme(taskbarPanel, ComponentState.ENABLED);
		SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities
				.getColorScheme(taskbarPanel,
						ColorSchemeAssociationKind.BORDER,
						ComponentState.ENABLED);
		SubstanceBorderPainter borderPainter = SubstanceCoreUtilities
				.getBorderPainter(taskbarPanel);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.6f));
		if (contour != null) {
			Shape clip = g2d.getClip();
			g2d.clip(contour);
			DecorationPainterUtils.paintDecorationBackground(g2d, taskbarPanel,
					true);
			g2d.setComposite(LafWidgetUtilities.getAlphaComposite(taskbarPanel,
					0.3f, g));
			MatteFillPainter.INSTANCE.paintContourBackground(g2d, taskbarPanel,
					taskbarPanel.getWidth(), taskbarPanel.getHeight(), contour
							.getBounds(), false, colorScheme, false);
			g2d.setComposite(LafWidgetUtilities.getAlphaComposite(taskbarPanel,
					1.0f, g));
			g2d.setClip(clip);

		}
		borderPainter.paintBorder(g2d, taskbarPanel, taskbarPanel.getWidth(),
				taskbarPanel.getHeight(), contour, contourInner, borderScheme);
		g2d.dispose();
	}
}
