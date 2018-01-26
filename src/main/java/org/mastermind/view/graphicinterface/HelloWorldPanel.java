package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class HelloWorldPanel extends AbstractPanel{

	public HelloWorldPanel(Dimension dim){
		super(dim);
		initPanel();
		
	}

	public void initPanel(){
		ImageIcon imgFile = new ImageIcon(core.config.get("imgDir") + "/hello.jpg");

		this.content.add(new JLabel(imgFile), BorderLayout.CENTER);
		
		this.panel.add(content, BorderLayout.CENTER);

	}


}
