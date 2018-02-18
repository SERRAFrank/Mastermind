package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

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

	protected Map<String, GameComponent> labelList = new HashMap<String, GameComponent>();

	protected JButton submitButton = new JButton(Core.lang.get("submit"));

	protected String proposText;
	protected String comparText;

	private String editable ;

	private int currentRound = 1;

	private List<Object> acceptedValues = new ArrayList<Object>();

	private List<Object> returnedField = new ArrayList<Object>();

	private boolean uniqueValue;
	
	private String gameType;

	private String returnText = "";

	private List<Object> comparValues;

	public GamePanel(Dimension dim, Controller ctrl){
		super(dim);

		this.controller = ctrl;

		submitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				submit();
			}
		});


		initPanel();

	}

	@Override
	public void initPanel(){

		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

	}


	protected void drawPanel() {
		content.removeAll();

		JPanel list = new JPanel();
		list.setLayout(new GridLayout(maxTurn,1));




		for(int i = currentRound ; i >= 1 ; i--) {
			JPanel history = new JPanel();
			history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
			history.setBackground( (i == currentRound ) ? Color.WHITE  : Color.LIGHT_GRAY );


			TitledBorder titled = BorderFactory.createTitledBorder(Core.lang.get("nbrRound") + " " + i + " / " + this.maxTurn);
			titled.setTitleJustification(TitledBorder.CENTER);
			titled.setTitlePosition(TitledBorder.DEFAULT_POSITION);
			history.setBorder(titled);


			if(i != currentRound ) {
				for(int j = 0; j <  combinationLenght; j++) {
					Color color = ( labelList.get(i+".compar").getValue(j).equals("=") ) ? Color.BLUE : Color.RED ;
					labelList.get(i+".propos").setColor(j,color);
					labelList.get(i+".compar").setColor(j,color);
				}
			}else if(returnedField.size() == combinationLenght ) {
				labelList.get(i+"."+editable).setValues(returnedField);
			}

			JPanel propos = labelList.get(i+".propos").toPanel();
			JPanel compar = labelList.get(i+".compar").toPanel();


			if(i == currentRound ) {
				history.add(new JLabel(proposText));
				propos.setBackground(Color.WHITE);
			}else {
				propos.setBackground(Color.LIGHT_GRAY);
			}
			history.add(propos);



			if(editable.equals("compar") || i != currentRound) {
				if(i == currentRound ) {
					history.add(new JLabel(comparText));
					compar.setBackground(Color.WHITE);
				}else {
					compar.setBackground(Color.LIGHT_GRAY);
				}

				if(gameType.equals("hiddenComb"))
					history.add(compar);
				else
					history.add(new JLabel(returnText));
			}
			list.add(history);

		}




		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(400, 350));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		content.add(scrollPane);
		panel.add(content, BorderLayout.CENTER);
		panel.add(submitButton, BorderLayout.SOUTH);

		content.revalidate();

	}

	protected void submit() {
		String id = currentRound + "." + editable;

		returnedField = labelList.get(id).toList();

		if(controller.setInput(editable, returnedField)  )  {
			labelList.get(id).setEnabled(false);
		}else {
			JOptionPane.showMessageDialog(null, Core.lang.get("input.error"), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}


	@Override
	public void updateInput(String p) {
		for(int i = 0 ; i < ( combinationLenght ); i++) {
			//labelList.get(currentRound + "." + p).get(i).setToolTipText(Core.lang.get("toolTipText" + this.editable));
		}

		drawPanel();

	}

	@Override
	public void updateInitGame(String s, List<Object> l, List<Object> r,  boolean u, String gt) {
		this.editable = s;
		this.acceptedValues = l;
		this.comparValues = r;
		this.uniqueValue = u;
		this.gameType = gt;
		addLabelLine();


	}


	private void addLabelLine() {

		GameComponent propPanel = new GameComponent(combinationLenght);
		GameComponent comparPanel = new GameComponent(combinationLenght);


		if (editable.equals("propos")) {
			proposText = Core.lang.get("input.proposCombination");
			comparText = Core.lang.get("output.setComparaison");
			propPanel.setPossibleValue(acceptedValues, uniqueValue);
			propPanel.setEnabled(true);
			comparPanel.setEnabled(false);

		}else if (editable.equals("compar")) {
			proposText = Core.lang.get("output.proposCombination");
			comparText = Core.lang.get("input.setComparaison");
			comparPanel.setPossibleValue(acceptedValues, uniqueValue);
			comparPanel.setEnabled(true);
			propPanel.setEnabled(false);
		}


		labelList.put(currentRound + ".propos" , propPanel );
		labelList.put(currentRound + ".compar" , comparPanel );


		drawPanel();
	}


	@Override
	public void updateOutputCompar( List<Object> compar) {
		/*
		for(int i = 0; i < combinationLenght; i++) {

			Color color = Color.RED;
			String value = ((List<?>) o).get(i).toString();
			if(value.equals("="))
				color = Color.BLUE;

			labelList.get(currentRound + ".compar").setColor(i, color);
			labelList.get(currentRound + ".compar").setText(i, value);

		}
		 */
		
		int v = 0;
		int o = 0;
		for(Object obj : compar) {
			if(obj.equals("o"))
				o++;
			if(obj.equals("v"))
				v++;
		}
		
		returnText = o + " present(s) et " + v + " bien placé(s)";
		
		labelList.get(currentRound + ".compar").setValues(compar);

	}

	@Override
	public void updateOutputPropos( List<Object> o) {	
		/*
		for(int i = 0; i < combinationLenght; i++) {
			String value = ((List<?>) o).get(i).toString();
			labelList.get(currentRound + ".propos").setText(i, value);
		}
		 */
		labelList.get(currentRound + ".propos").setValues(o);
	}


	@Override
	public void updateRound( int o) {
		this.currentRound = o;
		addLabelLine();

	}


	@Override
	public void updateEndGame(String t, boolean winner) {


	}

}