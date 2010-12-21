package test.substance;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.common.AsynchronousLoadListener;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButton.CommandButtonKind;
import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
import org.pushingpixels.flamingo.api.svg.SvgBatikResizableIcon;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.OfficeSilver2007Skin;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class TestSingleCommandButton extends JFrame {

	public TestSingleCommandButton() {
		FormLayout lm = new FormLayout("left:pref, 4dlu, left:pref", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(lm);
		builder.setDefaultDialogBorder();

		SvgBatikResizableIcon svgIcon = SvgBatikResizableIcon.getSvgIcon(
				TestCommandButtonSizes.class.getClassLoader().getResource(
						"test/svg/font-x-generic.svg"), new Dimension(16, 16));
		svgIcon.addAsynchronousLoadListener(new AsynchronousLoadListener() {
			@Override
			public void completed(boolean success) {
				if (success)
					repaint();
			}
		});
		JCommandButton cb = new JCommandButton("Command", svgIcon);
		cb.setCommandButtonKind(CommandButtonKind.ACTION_AND_POPUP_MAIN_ACTION);
		cb.setDisplayState(CommandButtonDisplayState.MEDIUM);
		cb.setFlat(false);

		JCommandToggleButton ctb = new JCommandToggleButton("ComToggle",
				svgIcon);
		ctb.setDisplayState(CommandButtonDisplayState.MEDIUM);
		ctb.setFlat(false);

		builder.append(cb, ctb);

		ImageIcon icon = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("test/substance/edit-paste.png"));
		JButton b = new JButton("Regular", icon);
		JToggleButton tb = new JToggleButton("RegToggle", icon);
		
		builder.append(b, tb);

		this.add(builder.getPanel(), BorderLayout.CENTER);

		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SubstanceLookAndFeel.setSkin(new OfficeSilver2007Skin());
				new TestSingleCommandButton().setVisible(true);
			}
		});
	}

}
