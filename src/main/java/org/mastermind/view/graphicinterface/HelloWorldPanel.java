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
		setTitle(core.lang.get("hello"));


		this.content.add(new JLabel(new ImageIcon(core.lang.get("helloWorld.img"))), BorderLayout.CENTER);
		
		this.panel.add(content, BorderLayout.CENTER);

	}


}
