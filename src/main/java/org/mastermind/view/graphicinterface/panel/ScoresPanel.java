package org.mastermind.view.graphicinterface.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.mastermind.core.Core;
import org.mastermind.view.graphicinterface.GameGFX;

public class ScoresPanel extends AbstractPanel {

	private Map<String, int[]> scoresList;

	public ScoresPanel(Dimension dim) {
		super(dim);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		this.scoresList = Core.score.getScoresList();

		initPanel();

	}

	@Override
	public void initPanel() {

		setTitle(Core.lang.get("graphic.menuScoreItem"));

		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JTextArea scoreJTextArea = new JTextArea();
		scoreJTextArea.setBackground(Color.white);
		scoreJTextArea.setFont(GameGFX.ARIAL.getFont());

		String scoreText = "";

		for (Entry<String, int[]> list : scoresList.entrySet()) {
			String playerName = list.getKey();
			String value = printScores(list.getValue());
			scoreText += playerName + " : " + value + "\n\n";
		}

		scoreJTextArea.setText(scoreText);
		scoreJTextArea.setEditable(false);
		

		JScrollPane scrollScore = new JScrollPane(scoreJTextArea);
		scrollScore.setPreferredSize(new Dimension(500, 500));
		scrollScore.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollScore.setBorder(null);

		this.content.add(scrollScore);
		this.panel.add(content, BorderLayout.CENTER);
	}

	private String printScores(int[] o) {
		int w = o[0];
		int l = o[1];
		double p = 0.;
		try {
			p = (w / ((double) w + (double) l)) * 100;
		} catch (Exception e) {
		}

		p = Math.round(p * Math.pow(10, 2)) / Math.pow(10, 2);
		return "  " + w + " " + Core.lang.get("text.victory") + " / " + l + " " + Core.lang.get("text.defeat") + " ( " + p
				+ "% )";
	}

}
