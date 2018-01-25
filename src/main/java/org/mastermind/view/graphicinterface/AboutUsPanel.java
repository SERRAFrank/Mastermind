package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;

public class AboutUsPanel  extends AbstractPanel{

	public AboutUsPanel(Dimension dim){
		super(dim);
		initPanel();
		
	}

	public void initPanel(){
		
		setTitle(core.lang.get("aboutUsItem"));
		
		JTextArea rules = new JTextArea();
		rules.setBackground(Color.white);
		
		String aboutUsText =  core.lang.get("aboutUs");
		
		rules.setText(aboutUsText);
		rules.setFont(arial);
		rules.setEditable(false);
		
		this.content.add(rules);
		
		this.panel.add(content, BorderLayout.CENTER);

	}

}
