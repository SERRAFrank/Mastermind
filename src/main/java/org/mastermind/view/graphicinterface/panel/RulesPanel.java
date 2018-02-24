package org.mastermind.view.graphicinterface.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.mastermind.core.Core;

public class RulesPanel extends AbstractPanel {

	public RulesPanel(Dimension dim) {
		super(dim);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		initPanel();

	}

	@Override
	public void initPanel() {

		setTitle(Core.lang.get("graphic.menuRulesItem"));

		JTextArea rules = new JTextArea();
		rules.setBackground(Color.white);

		String rulesText = "";
		for (String s : Core.lang.getArray("text.rules") )
			rulesText += s + "\n";

		rules.setText(rulesText);
		rules.setEditable(false);
		
		rules.setLineWrap(true); 
		rules.setWrapStyleWord(true); 

		JScrollPane scrollrules = new JScrollPane(rules);
		scrollrules.setPreferredSize(new Dimension(500, 500));
		scrollrules.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollrules.setBorder(null);

		this.content.add(scrollrules);

		this.panel.add(content, BorderLayout.CENTER);

	}

}
