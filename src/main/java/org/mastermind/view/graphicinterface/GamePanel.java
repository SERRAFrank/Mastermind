package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.model.scores.Score;



public class GamePanel extends AbstractPanel {

	/** tour en cours */
	protected int currentTurn;

	protected int maxTurn;


	/** Instance des scores */
	protected Score score = Score.getInstance();

	protected Controller controller; 

	protected int combinationLenght = core.config.getInt("combinationLenght");

	/** Combinaison proposée à comparer */
	protected JTextField[] proposJTextField = new JTextField[combinationLenght];

	/** Reponse de la comparaison de la proposition de chaine et  de la cachée */
	protected JTextField[]  comparJTextField = new JTextField[combinationLenght];

	protected String phase;

	protected JPanel propPanel = new JPanel();
	protected JPanel reponsePanel = new JPanel();

	protected JLabel roundLabel = new JLabel();

	protected JOptionPane popup = new JOptionPane();

	protected JButton submitButton = new JButton("Submit");

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
		
		initPanel();
		
	}

	@Override
	public void initPanel(){
		setTitle(Core.lang.get("rulesItem"));

		GridLayout gl = new GridLayout(3,1 );
		content.setLayout(gl);	

		propPanel.setBorder(BorderFactory.createTitledBorder(""));	
		reponsePanel.setBorder(BorderFactory.createTitledBorder(""));

		Thread t = new Thread() {
			@Override
			public void run() {
				content.removeAll();

				content.add(roundLabel);
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
	public void updateOutput(String phase, List<?> o) {
		JTextField[] output;

		if(phase.equals("compar"))
			output = comparJTextField;
		else
			output = proposJTextField;

		for(int i = 0; i < combinationLenght; i++) {
			output[i].setText(o.get(i).toString());
		}

	}

	@Override
	public void updateOutput(String s, String o) {
		switch(s) {
		case "round":
			roundLabel.setText(o);
			break;
		}

	}

	@Override
	public void updateEndGame(String t, boolean winner) {


	}

}