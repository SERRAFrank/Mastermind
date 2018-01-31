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
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.MaskFormatter;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;



public class GamePanel extends AbstractPanel {

	/** Instance du Controller */
	protected Controller controller; 

	/** Longueur de la combinaison */
	protected int combinationLenght = Core.config.getInt("combinationLenght");


	/** Tour en cours */
	protected int currentTurn;
	
	/** Nompbre de tours maximum */
	protected int maxTurn = Core.config.getInt("gameTurns");

	/** Combinaison proposée à comparer */
	protected JTextField[] proposJTextField = new JTextField[combinationLenght]; 

	/** Reponse de la comparaison de la proposition de chaine et  de la cachée */
	protected JTextField[]  comparJComboBox = new JTextField[combinationLenght]; 

	/** Phase de jeu */
	protected String phase;

	protected JPanel propPanel = new JPanel();
	protected JPanel comparPanel = new JPanel();
	protected JPanel roundBarPanel = new JPanel();

	protected JProgressBar roundBar = new JProgressBar();

	protected JButton submitButton = new JButton(Core.lang.get("submit"));

	protected String proposText;
	protected String comparText;

	public GamePanel(Dimension dim, Controller ctrl){
		super(dim);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);


		this.controller = ctrl;

		for(int i = 0 ; i < ( combinationLenght ); i++) {
			proposJTextField[i] = fieldMaker("0123456789");
			comparJComboBox[i] = fieldMaker("+-=");

			propPanel.add( proposJTextField[i]);
			comparPanel.add( comparJComboBox[i]);
		}

		roundBar.setMinimum(0);
		roundBar.setMaximum(maxTurn);
		roundBar.setValue(1);
		roundBar.setStringPainted(true);
		roundBar.setString(Core.lang.get("nbrRound") + " 1 / " + this.maxTurn);

		initPanel();

	}

	@Override
	public void initPanel(){

		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

		propPanel.setBorder(BorderFactory.createTitledBorder(""));	
		comparPanel.setBorder(BorderFactory.createTitledBorder(""));



		Thread t = new Thread() {
			@Override
			public void run() {


				content.removeAll();

				content.add(roundBar);
				content.add(propPanel);
				content.add(comparPanel);

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
		String value = "";
		JTextField[] returnedField;

		if(phase.equals("propos"))
			returnedField = proposJTextField;
		else
			returnedField = comparJComboBox;

		for(JTextField v : returnedField)
			value += v.getText().trim() + " ";

		if(!controller.setInput(phase, value)  ) {
			JOptionPane.showMessageDialog(null, Core.lang.get("input.error"), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField fieldMaker(String f) {
		MaskFormatter format = null;
		try {
			format = new MaskFormatter("*");
			format.setValidCharacters(f);
		} catch (Exception e) {
			Core.error(e);
		}

		final JTextField field = new JFormattedTextField (format);
		field.setHorizontalAlignment(SwingConstants.CENTER);
		field.setFont(comics30);
		field.setPreferredSize(new Dimension(50, 50));
		field.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						field.selectAll();
					}
				});
			}
		});


		return field;
	}

	@Override
	public void updateInput(String p) {
		boolean proposIsEnable = true;

		this.phase = p; 
		if (phase.equals("propos")) {
			proposText = Core.lang.get("input.proposCombination");
			comparText = Core.lang.get("output.setComparaison");
			proposIsEnable = true;

		}else {
			proposText = Core.lang.get("output.proposCombination");
			comparText = Core.lang.get("input.setComparaison ");
			proposIsEnable = false;
		}

		propPanel.setBorder(BorderFactory.createTitledBorder(proposText));	
		comparPanel.setBorder(BorderFactory.createTitledBorder(comparText));

		for(int i = 0 ; i < ( combinationLenght ); i++) {
			proposJTextField[i].setEnabled(proposIsEnable);
			proposJTextField[i].setToolTipText(Core.lang.get("toolTipText" + this.phase));
			
			comparJComboBox[i].setEnabled(!proposIsEnable);
		}
	}

	@Override
	public void updateOutput(String phase, Object o) {
		switch(phase) {
		case "compar":
			for(int i = 0; i < combinationLenght; i++) {
				JTextField box = comparJComboBox[i];

				Color color = Color.RED;
				String value = ((List<?>) o).get(i).toString();
				if(value.equals("="))
					color = Color.BLUE;
				box.setBackground(color);
				box.setText(((List<?>) o).get(i).toString());
				
			}
			break;
		case "propos":
			for(int i = 0; i < combinationLenght; i++)
				proposJTextField[i].setText(((List<?>) o).get(i).toString());

			break;

		case "round":
			roundBar.setString(Core.lang.get("nbrRound") + " " + o + " / " + this.maxTurn);
			roundBar.setValue((int) o);
			break;
		}

	}


	@Override
	public void updateEndGame(String t, boolean winner) {


	}

}