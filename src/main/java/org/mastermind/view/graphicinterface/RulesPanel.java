package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class RulesPanel extends AbstractPanel{

	public RulesPanel(Dimension dim){
		super(dim);
		initPanel();
		
	}

	public void initPanel(){
		
		setTitle(core.lang.get("rulesItem"));
		
		JTextArea rules = new JTextArea();
		rules.setBackground(Color.white);
		
		String rulesText = "";
		for(int i = 0;  core.lang.keyExist("rules" + "." + i); i++ ) {
			rulesText += core.lang.get("rules" + "." + i, true) + "\n";
		}
		
		rules.setText(rulesText);
		rules.setFont(arial);
		rules.setEditable(false);
		
		this.content.add(rules);
		
		this.panel.add(content, BorderLayout.CENTER);

	}

}
