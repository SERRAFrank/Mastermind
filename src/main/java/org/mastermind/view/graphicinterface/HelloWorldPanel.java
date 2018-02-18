package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.mastermind.core.Core;

public class HelloWorldPanel extends AbstractPanel{

	public HelloWorldPanel(Dimension dim){
		super(dim);
		
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
		
		initPanel();
		
	}

	public void initPanel(){
		ImageIcon imgFile = new ImageIcon(Core.config.get("dir.img") + "hello.jpg");

		this.content.add(new JLabel(imgFile), BorderLayout.CENTER);
		
		this.panel.add(content, BorderLayout.CENTER);

	}


}
