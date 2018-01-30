package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.model.scores.Score;



public class GamePanel extends AbstractPanel {

	/** tour en cours */
	protected int currentTurn;


	/** Instance des scores */
	protected Score score = Score.getInstance();

	protected Controller controller; 

	protected int combinationLenght = core.config.getInt("combinationLenght");


	protected int maxTurn = core.config.getInt("gameTurns");

	/** Combinaison proposée à comparer */
	protected JTextField[] proposJTextField = new JTextField[combinationLenght];

	/** Reponse de la comparaison de la proposition de chaine et  de la cachée */
	protected JTextField[]  comparJTextField = new JTextField[combinationLenght];

	protected String phase;

	protected JPanel propPanel = new JPanel();
	protected JPanel reponsePanel = new JPanel();
	protected JPanel roundBarPanel = new JPanel();

	protected JProgressBar roundBar = new JProgressBar();

	protected JOptionPane popup = new JOptionPane();

	protected JButton submitButton = new JButton(core.lang.get("submit"));

	protected String proposText;
	protected String reponseText;

	public GamePanel(Dimension dim, Controller ctrl){
		super(dim);		
		this.controller = ctrl;

		for(int i = 0 ; i < ( combinationLenght ); i++) {
			proposJTextField[i] = fieldMaker();
			comparJTextField[i] = fieldMaker();

			propPanel.add( proposJTextField[i]);
			reponsePanel.add( comparJTextField[i]);
		}
		
		roundBar.setMinimum(0);
		roundBar.setMaximum(maxTurn);
		roundBar.setValue(1);
		roundBar.setStringPainted(true);
		roundBar.setString(core.lang.get("nbrRound") + " 1 / " + this.maxTurn);
		
		initPanel();

	}

	@Override
	public void initPanel(){
	
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

		propPanel.setBorder(BorderFactory.createTitledBorder(""));	
		reponsePanel.setBorder(BorderFactory.createTitledBorder(""));
		


		Thread t = new Thread() {
			@Override
			public void run() {
				
				
				content.removeAll();

				content.add(roundBar);
				content.add(propPanel);
				content.add(reponsePanel);

				submitButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent event){
						submit();
					}
				});
				panel.add(content, BorderLayout.CENTER);
				panel.add(submitButton, BorderLayout.SOUTH);

				content.revalidate();
			}
		};
		t.start();

	}

	protected void submit() {
		JTextField[] input;

		if(phase.equals("propos"))
			input = proposJTextField;
		else
			input = comparJTextField;

		String value = "";
		for(JTextField v : input)
			value += v.getText().trim() + " ";

		if(!controller.setInput(phase, value)  ) {
			JOptionPane.showMessageDialog(null, Core.lang.get("input.error"), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField fieldMaker() {
		JTextField field = new JTextField();
		field.setHorizontalAlignment(SwingConstants.CENTER);
		field.setFont(comics30);
		field.setPreferredSize(new Dimension(50, 50));
		return field;
	}


	@Override
	public void updateInput(String p) {
		boolean proposIsEnable = true;

		phase = p; 
		if (phase.equals("propos")) {
			proposText = Core.lang.get("input.proposCombination");
			reponseText = Core.lang.get("output.setComparaison");
			proposIsEnable = true;

		}else {
			proposText = Core.lang.get("output.proposCombination");
			reponseText = Core.lang.get("input.setComparaison ");
			proposIsEnable = false;
		}

		propPanel.setBorder(BorderFactory.createTitledBorder(proposText));	
		reponsePanel.setBorder(BorderFactory.createTitledBorder(reponseText));

		for(int i = 0 ; i < ( combinationLenght ); i++) {
			proposJTextField[i].setEnabled(proposIsEnable);
			comparJTextField[i].setEnabled(!proposIsEnable);
		}
	}

	@Override
	public void updateOutput(String phase, Object o) {
		switch(phase) {
		case "compar":
			for(int i = 0; i < combinationLenght; i++) {
				Color color = Color.RED;
				String value = ((List) o).get(i).toString();
				
				if(value.equals("="))
					color = Color.BLUE;
				
				comparJTextField[i].setDisabledTextColor(color);
				comparJTextField[i].setText(value);
			}
			break;
		case "propos":
			for(int i = 0; i < combinationLenght; i++)
				proposJTextField[i].setText(((List) o).get(i).toString());
			break;

		case "round":
			roundBar.setString(core.lang.get("nbrRound") + " " + o + " / " + this.maxTurn);
			roundBar.setValue((int) o);
			break;
		}

	}


	@Override
	public void updateEndGame(String t, boolean winner) {


	}

}