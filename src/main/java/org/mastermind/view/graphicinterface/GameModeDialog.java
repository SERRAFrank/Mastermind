package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;

public class GameModeDialog extends JDialog {

	private JComboBox gameTypeCombo = new JComboBox();
	private JComboBox gameModeCombo = new JComboBox();

	private JLabel gameTypeLabel = new JLabel(Core.lang.get("setGameType"));
	private JLabel gameModeLabel = new JLabel(Core.lang.get("selectGameMode.Title"));

	private String[] gameTypeValue = Core.config.getArray("game.type");
	private String[] gameModeValue = Core.config.getArray("game.mode");
	private boolean sendData;
	private JPanel content = new JPanel();

	private Controller controller;


	public GameModeDialog(JFrame parent,  String title, boolean modal, Controller ctrl){
		super(parent, title, modal);

		controller = ctrl;
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		this.setSize(450, 250);

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		this.initComponent();

		this.setVisible(true);



	}

	private void initComponent() {
		JPanel gameTypePanel = new JPanel();
		gameTypePanel.setPreferredSize(new Dimension(425, 80));
		gameTypePanel.setBorder(BorderFactory.createTitledBorder((Core.lang.get("gameType"))));

		gameTypeCombo.setPreferredSize(new Dimension(400, 30));
		for(String gameType : gameTypeValue )
			gameTypeCombo.addItem(Core.lang.get("gameType." + gameType ));
		
		gameTypePanel.add(gameTypeLabel);
		gameTypePanel.add(gameTypeCombo);

		
		JPanel gameModePanel = new JPanel();
		gameModePanel.setPreferredSize(new Dimension(425, 80));
		gameModePanel.setBorder(BorderFactory.createTitledBorder((Core.lang.get("gameMode"))));

		gameModeCombo.setPreferredSize(new Dimension(400, 30));
		for(String gameMode : gameModeValue )
			gameModeCombo.addItem(Core.lang.get("gameMode." + gameMode ));

		gameModePanel.add(gameModeLabel);
		gameModePanel.add(gameModeCombo);


		JPanel control = new JPanel();
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String gm = null;
				for(String gameMode : gameModeValue ) {
					if(Core.lang.get("gameMode." + gameMode ).equals(gameModeCombo.getSelectedItem()))
						gm = gameMode;
				}

				String gt = null;
				for(String gameType : gameTypeValue ) {
					if(Core.lang.get("gameType." + gameType ).equals(gameTypeCombo.getSelectedItem()))
						gt = gameType;
				}
				controller.setGameMode(gm, gt, false, true);
				
				setVisible(false);

			}      
		});

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}      
		});

		control.add(okBouton);
		control.add(cancelBouton);

		content.add(gameModePanel);
		content.add(gameTypePanel);
		
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);

	}
}
