package org.mastermind.view.graphicinterface.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.view.graphicinterface.GameComponent;
import org.mastermind.view.graphicinterface.GameGFX;

public class GamePanel extends AbstractPanel {

	/** Instance du Controller */
	protected Controller controller;

	/** Longueur de la combinaison */
	protected int combinationLenght = Core.config.getInt("game.lenght");

	/** Tour en cours */
	protected int currentTurn;

	/** Nompbre de tours maximum */
	protected int maxTurn = Core.config.getInt("game.turns");

	protected Map<String, GameComponent> labelList = new HashMap<String, GameComponent>();

	protected JButton submitButton = new JButton(Core.lang.get("text.submit"));

	protected ImageIcon infoIcon = GameGFX.INFO.getIcon();

	protected String proposText;

	protected String comparText;

	private String editable;

	private int currentRound = 1;

	private List<Object> acceptedValues = new ArrayList<Object>();

	private List<Object> returnedField = new ArrayList<Object>();

	private boolean uniqueValue;

	private List<Object> comparValues;

	private boolean moreLess;

	private JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT));

	private JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	private boolean stopGame = false;


	/**
	 * Instantiates a new game panel.
	 *
	 * @param dim the dim
	 * @param ctrl the ctrl
	 */
	public GamePanel(Dimension dim, Controller ctrl) {
		super(dim);

		this.controller = ctrl;



		JButton infoButton = new JButton(new ImageIcon(Core.config.get("dir.img") + "game.info.png"));
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, info, "" , JOptionPane.INFORMATION_MESSAGE, infoIcon);
			}
		});
		//	infoButton.setPreferredSize(new Dimension(30,30));
		//	infoButton.setHorizontalAlignment(JButton.RIGHT);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.add(infoButton);

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				submit();
			}
		});

		initPanel();

	}

	@Override
	public void initPanel() {
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
	}


	protected void infoPanel() {
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		JLabel acceptedValuesLabel = new JLabel(Core.lang.get("newGameSetting.acceptedValues"));
		acceptedValuesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		info.add( new JLabel(Core.lang.get("newGameSetting.acceptedValues") ) );

		GameComponent acceptedValuesPanel = new GameComponent(acceptedValues.size());
		acceptedValuesPanel.setValues(acceptedValues);
		acceptedValuesPanel.setEnabled(false);
		acceptedValuesPanel.setDimension(new Dimension(30,30));
		acceptedValuesPanel.setFont(GameGFX.COMICS20.getFont());
		info.add(acceptedValuesPanel.toPanel());
		if(editable.equals("compar")) {
			for(int i = 0; i < 3 ; i++) {
				info.add(new JLabel( acceptedValues.get(i) + " : " + Core.lang.get("text.compar." + ((moreLess)?"moreLess":"mastermind") + "." + i) ) );
			}
		}
		info.add( new JLabel(Core.lang.get("text.combinasionLenght") ) );
		info.add( new JLabel(Core.lang.get("newGameSetting.uniqueValue." + (uniqueValue ? "Yes" : "No"))) ) ;




	}

	protected void drawPanel() {

		content.removeAll();
		JPanel list = new JPanel();
		list.setLayout(new GridLayout(maxTurn, 1));

		for (int i = currentRound; i >= 1; i--) {
			JPanel history = new JPanel();
			history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
			history.setBackground((i == currentRound) ? Color.WHITE : Color.LIGHT_GRAY);

			TitledBorder titled = BorderFactory
					.createTitledBorder(Core.lang.get("text.nbrRound") + " " + i + " / " + this.maxTurn);
			titled.setTitleJustification(TitledBorder.CENTER);
			titled.setTitlePosition(TitledBorder.DEFAULT_POSITION);
			history.setBorder(titled);

			if (i == currentRound && returnedField.size() == combinationLenght) {
				labelList.get(i + "." + editable).setValues(returnedField);
			}


			JPanel propos = labelList.get(i + ".propos").toPanel();
			JPanel compar = labelList.get(i + ".compar").toPanel();


			if (i == currentRound) {
				JLabel proposTextLabel = new JLabel(proposText);
				proposTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				history.add(proposTextLabel);

				propos.setBackground(Color.WHITE);
			} else {
				propos.setBackground(Color.LIGHT_GRAY);
			}
			history.add(propos);

			if((i == currentRound)) 
				compar.setBackground(Color.WHITE);
			else
				compar.setBackground(Color.LIGHT_GRAY);


			if (editable.equals("compar")) {
				if(i == currentRound) {
					JLabel comparTextLabel = new JLabel(comparText);
					comparTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
					history.add(comparTextLabel);
				}
				history.add(compar);
			}else {
				if(i != currentRound) {
					if (moreLess) {
						history.add(compar);
					}else {
						history.add(new JLabel(getReturnText(labelList.get(i + ".compar").getValues())));
					}

				}
			}



			list.add(history);

		}

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(575, 400));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		content.add(scrollPane);

		panel.add(infoPanel, BorderLayout.NORTH);
		panel.add(content, BorderLayout.CENTER);
		panel.add(submitButton, BorderLayout.SOUTH);

		content.revalidate();
	}

	protected void submit() {
		String id = currentRound + "." + editable;

		returnedField = labelList.get(id).getValues();

		boolean testUniqueValue = true;

		if(editable.equals("propos") && uniqueValue) {
			//création d'un set pour avoir des valeurs uniques 
			Set<Object> uniqueInput = new HashSet<Object>(returnedField);
			testUniqueValue = ( uniqueInput.size() == returnedField.size());
		}

		boolean r = false;
		if(testUniqueValue) 
			r = controller.setInput(editable, returnedField);

		if(r) {
			labelList.get(id).setEnabled(false);
		} else {
			JOptionPane.showMessageDialog(null, Core.lang.get("input.error"), "Erreur", JOptionPane.ERROR_MESSAGE, GameGFX.ERROR.getIcon());
		}
	}


	@Override
	public void updateInput(String p) {

	}

	@Override
	public void updateInitGame(String s, List<Object> l, List<Object> r,  boolean ml, boolean u) {
		this.editable = s;
		this.acceptedValues = l;
		this.comparValues = r;
		this.uniqueValue = u;
		this.moreLess = ml;
		infoPanel();
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

		} else  {
			proposText = Core.lang.get("output.proposCombination");
			comparText = Core.lang.get("input.setComparaison");
			comparPanel.setPossibleValue(acceptedValues, uniqueValue);
			comparPanel.setEnabled(true);
			propPanel.setEnabled(false);
		}

		labelList.put(currentRound + ".propos", propPanel);
		labelList.put(currentRound + ".compar", comparPanel);

		drawPanel();
	}

	private String getReturnText(List<Object> compar) {
		String returnText = "";
		int v = 0;
		int o = 0;
		for (Object obj : compar) {
			if (obj.equals(comparValues.get(1)))
				o++;
			else if (obj.equals(comparValues.get(2)))
				v++;
		}

		returnText = Core.lang.get("text.gameReturnMasterMind");
		returnText = returnText.replaceAll("\\{0\\}", String.valueOf(v));
		returnText = returnText.replaceAll("\\{1\\}", String.valueOf(o));


		if(Core.DEBUG() && moreLess) {
			Core.debug(compar);
		}

		return returnText;
	}

	@Override
	public void updateOutputCompar(List<Object> compar) {
		if(!stopGame)
			labelList.get(currentRound + ".compar").setValues(compar);

	}

	@Override
	public void updateOutputPropos(List<Object> o) {
		if(!stopGame) {
			if(o.contains(-1)) {
				JOptionPane.showMessageDialog(null, Core.lang.get("impossible.error"), "Erreur", JOptionPane.ERROR_MESSAGE, GameGFX.ERROR.getIcon());
				stopGame = true;
			}else {
				labelList.get(currentRound + ".propos").setValues(o);
			}
		}
	}

	@Override
	public void updateRound(int o) {
		this.currentRound = o;
		addLabelLine();

	}

	@Override
	public void updateEndGame(String t, boolean winner) {
	}

}