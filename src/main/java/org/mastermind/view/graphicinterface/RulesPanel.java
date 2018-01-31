package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextArea;

import org.mastermind.core.Core;

public class RulesPanel extends AbstractPanel{

	public RulesPanel(Dimension dim){
		super(dim);
		
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
		
		
		initPanel();
		
	}

	public void initPanel(){
		
		setTitle(Core.lang.get("rulesItem"));
		
		JTextArea rules = new JTextArea();
		rules.setBackground(Color.white);
		
		String rulesText = "";
		for(int i = 0;  Core.lang.keyExist("rules" + "." + i); i++ ) {
			rulesText += Core.lang.get("rules" + "." + i, true) + "\n";
		}
		
		rules.setText(rulesText);
		rules.setFont(arial);
		rules.setEditable(false);
		
		this.content.add(rules);
		
		this.panel.add(content, BorderLayout.CENTER);

	}

}
