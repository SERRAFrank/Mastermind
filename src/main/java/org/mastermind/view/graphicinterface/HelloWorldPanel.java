package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.mastermind.core.Core;

public class HelloWorldPanel extends AbstractPanel {

	public HelloWorldPanel(Dimension dim) {
		super(dim);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		initPanel();

	}

	@Override
	public void initPanel() {


		ImageIcon imgFile = new ImageIcon(Core.config.get("dir.img") + "hello.jpg");
		JLabel label = new JLabel(imgFile);
	    label.setHorizontalAlignment(SwingConstants.CENTER);

	    this.content.add(label, BorderLayout.CENTER);

		this.panel.add(content, BorderLayout.CENTER);

	}

}
