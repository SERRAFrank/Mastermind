package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;

public class NewGameSettingDialog extends JDialog {

	private JComboBox gameTypeCombo = new JComboBox();
	private JComboBox gameModeCombo = new JComboBox();

	private JLabel gameTypeLabel = new JLabel(Core.lang.get("selectGameTypeTitle"));
	private JLabel gameModeLabel = new JLabel(Core.lang.get("selectGameModeTitle"));
	private JLabel gameSettingLabel = new JLabel(Core.lang.get("gameSettingLabel"));


	private JCheckBox gameSettingCheckBoxUniqueValue = new JCheckBox(Core.lang.get("newGameSetting.setUniqueValue"));
	private JRadioButton  gameSettingCheckBoxMoreOrLessTrue = new JRadioButton (Core.lang.get("newGameSetting.moreLess.True"));
	private JRadioButton  gameSettingCheckBoxMoreOrLessFalse = new JRadioButton (Core.lang.get("newGameSetting.moreLess.False"));
	private ButtonGroup gameSettingCheckBoxMoreOrLessButtonGroup = new ButtonGroup();

	private boolean sendData;

	private String[] gameTypeValue = Core.config.getArray("game.type");
	private String[] gameModeValue = Core.config.getArray("game.mode");

	private List<String> gameTypeList = new ArrayList<String>();
	private List<String> gameModeList = new ArrayList<String>();

	private JPanel content = new JPanel();

	private Controller controller;


	private String gameMode = null;
	private String gameType = null;
	private boolean moreLess = false;
	private boolean uniqueValue = false;

	public NewGameSettingDialog(JFrame parent, String title, boolean modal, Controller ctrl) {
		super(parent, title, modal);

		controller = ctrl;
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		this.setSize(450, 260);

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.initComponent();

		this.setVisible(true);

	}

	private void initComponent() {
		JPanel gameTypePanel = new JPanel();
		gameTypePanel.setPreferredSize(new Dimension(425, 50));
		gameTypePanel.setLayout(new BoxLayout(gameTypePanel, BoxLayout.Y_AXIS));
		gameTypePanel.setBorder(BorderFactory.createTitledBorder((Core.lang.get("text.gameType"))));

		//gameTypeCombo.setPreferredSize(new Dimension(400, 30));
		for (String gt : gameTypeValue) {
			gameTypeList.add(gt);
			gameTypeCombo.addItem(Core.lang.get("text.gameType." + gt));
		}
		gameTypeCombo.setSelectedIndex(0);

		gameTypeCombo.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						updateComponents();
					}
				} );

		gameTypePanel.add(gameTypeCombo);

		JPanel gameModePanel = new JPanel();
		gameModePanel.setLayout(new BoxLayout(gameModePanel, BoxLayout.Y_AXIS));
		gameModePanel.setPreferredSize(new Dimension(425, 50));
		gameModePanel.setBorder(BorderFactory.createTitledBorder((Core.lang.get("text.gameMode"))));

		//gameModeCombo.setPreferredSize(new Dimension(400, 30));
		for (String gm : gameModeValue) {
			gameModeList.add(gm);
			gameModeCombo.addItem(Core.lang.get("text.gameMode." + gm));
		}
		gameModeCombo.setSelectedIndex(0);

		gameModeCombo.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						updateComponents();
					}
				} );
		

		gameModePanel.add(gameModeCombo);		

		JPanel gameSettingPanel = new JPanel();
		gameSettingPanel.setLayout(new BoxLayout(gameSettingPanel, BoxLayout.LINE_AXIS));
		gameSettingPanel.setPreferredSize(new Dimension(425, 80));
		gameSettingPanel.setBorder(BorderFactory.createTitledBorder((Core.lang.get("newGameSetting.gameSetting"))));

		
		gameSettingCheckBoxMoreOrLessButtonGroup.add(gameSettingCheckBoxMoreOrLessTrue);
		gameSettingCheckBoxMoreOrLessButtonGroup.add(gameSettingCheckBoxMoreOrLessFalse);


		JPanel gameSettingCheckBoxMoreOrLess = new JPanel();
		gameSettingCheckBoxMoreOrLess.setPreferredSize(new Dimension(200, 80));
		gameSettingCheckBoxMoreOrLess.setLayout(new BoxLayout(gameSettingCheckBoxMoreOrLess, BoxLayout.Y_AXIS));
		gameSettingCheckBoxMoreOrLess.add(gameSettingCheckBoxMoreOrLessTrue);
		gameSettingCheckBoxMoreOrLess.add(gameSettingCheckBoxMoreOrLessFalse);


		gameSettingPanel.add(gameSettingCheckBoxMoreOrLess);
		gameSettingPanel.add(gameSettingCheckBoxUniqueValue);	



		JPanel control = new JPanel();
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moreLess = gameSettingCheckBoxMoreOrLessTrue.isSelected();
				uniqueValue = gameSettingCheckBoxUniqueValue.isSelected();
				
				controller.setGameMode(gameMode, gameType, moreLess, uniqueValue);
				setVisible(false);

			}
		});

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});

		control.add(okBouton);
		control.add(cancelBouton);

		content.add(gameModePanel);
		content.add(gameTypePanel);
		content.add(gameSettingPanel);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
		
		updateComponents();

	}

	private void updateComponents() {
		gameType = gameTypeList.get(gameTypeCombo.getSelectedIndex());
		gameMode = gameModeList.get(gameModeCombo.getSelectedIndex());
		
		
		gameSettingCheckBoxUniqueValue.setSelected(Core.config.getBoolean((gameType + ".uniqueValue") ));
		gameSettingCheckBoxUniqueValue.setEnabled(!Core.config.containsKey(gameType + ".uniqueValue"));
		
		gameSettingCheckBoxMoreOrLessTrue.setEnabled(!Core.config.containsKey(gameType + ".moreLess"));
		gameSettingCheckBoxMoreOrLessFalse.setEnabled(!Core.config.containsKey(gameType + ".moreLess"));
		gameSettingCheckBoxMoreOrLessTrue.setSelected(Core.config.getBoolean(gameType + ".moreLess"));
		gameSettingCheckBoxMoreOrLessFalse.setSelected(!Core.config.getBoolean(gameType + ".moreLess"));
		
		
		moreLess = gameSettingCheckBoxMoreOrLessTrue.isSelected();

		uniqueValue = gameSettingCheckBoxUniqueValue.isSelected();
		
	}

}
