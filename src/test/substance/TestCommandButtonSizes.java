package test.substance;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeSilver2007Skin;

import test.svg.transcoded.font_x_generic;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class TestCommandButtonSizes extends JFrame {
	public TestCommandButtonSizes() {
		super("Command button sizes");
		FormLayout fl = new FormLayout(
				"right:pref, 2dlu, center:pref, 2dlu, left:pref, 4dlu, right:pref, 2dlu, center:pref, 2dlu, left:pref");
		DefaultFormBuilder builder = new DefaultFormBuilder(fl);
		builder.setDefaultDialogBorder();

		ResizableIcon svgIcon = new font_x_generic();
		svgIcon.setDimension(new Dimension(16, 16));

		// Core Swing button with an icon and text
		builder.append("core");
		JButton buttonCore1 = new JButton("s", svgIcon);
		JLabel sizeCore1 = new JLabel();
		wireLabelToComponent(buttonCore1, sizeCore1);
		builder.append(buttonCore1, sizeCore1);

		// Flamingo MEDIUM action command button with an icon and text
		builder.append("command medium action");
		JCommandButton commandButton1 = new JCommandButton("c", svgIcon);
		commandButton1.setDisplayState(CommandButtonDisplayState.MEDIUM);
		commandButton1.setFlat(false);
		JLabel sizeCommand1 = new JLabel();
		wireLabelToComponent(commandButton1, sizeCommand1);
		builder.append(commandButton1, sizeCommand1);

		// Flamingo MEDIUM split command button with an icon and text
		builder.append("command medium split");
		JCommandButton commandButton2 = new JCommandButton("c", svgIcon);
		commandButton2.setDisplayState(CommandButtonDisplayState.MEDIUM);
		commandButton2.setFlat(false);
		commandButton2
				.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		JLabel sizeCommand2 = new JLabel();
		wireLabelToComponent(commandButton2, sizeCommand2);
		builder.append(commandButton2, sizeCommand2);

		// Flamingo MEDIUM command toggle button with an icon and text
		builder.append("command toggle medium");
		JCommandToggleButton commandToggleButton1 = new JCommandToggleButton(
				"ct", svgIcon);
		commandToggleButton1.setDisplayState(CommandButtonDisplayState.MEDIUM);
		commandToggleButton1.setFlat(false);
		JLabel sizeCommandToggle1 = new JLabel();
		wireLabelToComponent(commandToggleButton1, sizeCommandToggle1);
		builder.append(commandToggleButton1, sizeCommandToggle1);

		// Core Swing button with an icon
		builder.append("core");
		JButton buttonCore2 = new JButton(svgIcon);
		JLabel sizeCore2 = new JLabel();
		wireLabelToComponent(buttonCore2, sizeCore2);
		builder.append(buttonCore2, sizeCore2);

		// Flamingo MEDIUM command toggle button with an icon
		builder.append("command toggle medium");
		JCommandToggleButton commandToggleButton2 = new JCommandToggleButton(
				svgIcon);
		commandToggleButton2.setDisplayState(CommandButtonDisplayState.MEDIUM);
		commandToggleButton2.setFlat(false);
		JLabel sizeCommandToggle2 = new JLabel();
		wireLabelToComponent(commandToggleButton2, sizeCommandToggle2);
		builder.append(commandToggleButton2, sizeCommandToggle2);

		// Flamingo MEDIUM action command button with an icon
		builder.append("command medium action");
		JCommandButton commandButton3 = new JCommandButton(svgIcon);
		commandButton3.setDisplayState(CommandButtonDisplayState.MEDIUM);
		commandButton3.setFlat(false);
		JLabel sizeCommand3 = new JLabel();
		wireLabelToComponent(commandButton3, sizeCommand3);
		builder.append(commandButton3, sizeCommand3);

		// Flamingo SMALL action command button with an icon and text
		builder.append("command small action");
		JCommandButton commandButton4 = new JCommandButton("c", svgIcon);
		commandButton4.setDisplayState(CommandButtonDisplayState.SMALL);
		commandButton4.setFlat(false);
		JLabel sizeCommand4 = new JLabel();
		wireLabelToComponent(commandButton4, sizeCommand4);
		builder.append(commandButton4, sizeCommand4);

		// Flamingo SMALL action command button with an icon and text
		builder.append("command small action, gap scale 0.5");
		JCommandButton commandButton5 = new JCommandButton("c", svgIcon);
		commandButton5.setDisplayState(CommandButtonDisplayState.SMALL);
		commandButton5.setGapScaleFactor(0.5);
		commandButton5.setFlat(false);
		JLabel sizeCommand5 = new JLabel();
		wireLabelToComponent(commandButton5, sizeCommand5);
		builder.append(commandButton5, sizeCommand5);

		this.add(builder.getPanel(), BorderLayout.CENTER);

		this.setSize(600, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void wireLabelToComponent(final Component comp, final JLabel label) {
		comp.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				label.setText(comp.getWidth() + ":" + comp.getHeight());
			}
		});
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new OfficeSilver2007Skin());
				new TestCommandButtonSizes().setVisible(true);
			}
		});
	}

}
