package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mastermind.core.Core;

public class ScoresPanel extends AbstractPanel{

	private Map<String, int[]> scoresList;
	
	public ScoresPanel(Dimension dim, Map<String, int[]> list){
		super(dim);
		this.scoresList = list;
		
		initPanel();
		
		
	}

	public void initPanel(){
		
		
		setTitle(core.lang.get("scoreItem"));

	    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		int i = 0;
		
		for(Entry<String, int[]> list : scoresList.entrySet()) {
		
			String playerName = list.getKey();
			String value = printScores(list.getValue());
			JLabel scoreLabel = new JLabel( playerName + " : " +  value );
			scoreLabel.setFont(arial);
			scoreLabel.setPreferredSize(new Dimension(440, 40));
			content.add(scoreLabel);

			i++;

		}
		
		
		this.panel.add(content, BorderLayout.CENTER);	
	}
	
	
	private String printScores(int[] o ) {
		int w = o[0];
		int l = o[1];
		double p = 0.;
		try{
			p =   (w / ((double)w + (double)l)) * 100;
		}catch(Exception e) {}

		p = Math.round(p * Math.pow(10,2)) / Math.pow(10,2);
		return "  " + w + " " +  Core.lang.get("victory") + " / " +  l + " " + Core.lang.get("defeat") +  " ( " + p + "% )";
	}


}
