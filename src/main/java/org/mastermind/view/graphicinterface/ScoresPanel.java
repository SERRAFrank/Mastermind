package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.mastermind.core.Core;

public class ScoresPanel extends AbstractPanel{

	private Map<String, int[]> scoresList;
	
	public ScoresPanel(Dimension dim, Map<String, int[]> list){
		super(dim);
		
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
		
		this.scoresList = list;
		
		initPanel();
		
		
	}

	public void initPanel(){
		
		
		setTitle(Core.lang.get("scoreItem"));

	    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

	    
	    
		JTextArea scoreJTextArea = new JTextArea();
		scoreJTextArea.setBackground(Color.white);
		scoreJTextArea.setFont(arial);
		
		String scoreText = "";
		
		for(Entry<String, int[]> list : scoresList.entrySet()) {
			String playerName = list.getKey();
			String value = printScores(list.getValue());
			scoreText += playerName + " : " +  value + "\n\n";
		}
		
		scoreJTextArea.setText(scoreText);
		scoreJTextArea.setEditable(false);
		
		JScrollPane scrollScore = new JScrollPane(scoreJTextArea);
		scrollScore.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollScore.setBorder(null);
		
		
		this.content.add(scrollScore);
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
