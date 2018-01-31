package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FilenameUtils;
import org.mastermind.core.Core;
import org.mastermind.view.graphicinterface.rangeslider.RangeSlider;



public class ConfigPanel extends AbstractPanel {

	protected JPanel interfacePan = new JPanel();
	protected JPanel gamePan = new JPanel();
	protected JPanel langPan = new JPanel();

	protected JTabbedPane tabbedPan = new JTabbedPane(JTabbedPane.LEFT);

	public ConfigPanel(Dimension dim) {
		super(dim);
		
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		initInterfacePan();
		initGamePan();
		initLangPan();

		initPanel();

	}


	protected void initPanel() {

		// onglet interface
		JLabel interfaceLabel = new JLabel(Core.lang.get("interfacePan"));
		interfaceLabel.setIcon(new ImageIcon( Core.config.get("imgDir") + "/icon-interface.png"));
		interfaceLabel.setHorizontalTextPosition(JLabel.TRAILING);
		tabbedPan.addTab(null, interfacePan);
		tabbedPan.setTabComponentAt(0, interfaceLabel);		

		// onglet jeu
		JLabel gameLabel = new JLabel(Core.lang.get("gamePan"));
		gameLabel.setIcon(new ImageIcon( Core.config.get("imgDir") + "/icon-game.png"));
		gameLabel.setHorizontalTextPosition(JLabel.TRAILING);
		tabbedPan.addTab(null, gamePan);
		tabbedPan.setTabComponentAt(1, gameLabel);		

		// onglet lang
		JLabel langLabel = new JLabel(Core.lang.get("langPan"));
		langLabel.setIcon(new ImageIcon( Core.config.get("imgDir") + "/icon-lang.png"));
		langLabel.setHorizontalTextPosition(JLabel.TRAILING);
		tabbedPan.addTab(null, langPan);
		tabbedPan.setTabComponentAt(2, langLabel);

		content.add(tabbedPan);

		panel.add(content, BorderLayout.CENTER);	

	}

	/**
	 * Panneau Interface
	 */
	private void initInterfacePan() {

		interfacePan.setBackground(Color.white);
		interfacePan.setPreferredSize(new Dimension(400, 300));

		//Mode de vue par defaut
		JPanel panView = new JPanel();
		panView.setBackground(Color.white);
		panView.setLayout(new BoxLayout(panView, BoxLayout.Y_AXIS));
		panView.setPreferredSize(new Dimension(350, 80));
		panView.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setViewMode")));

		//ComboBox
		JComboBox<String> viewComboBox = new JComboBox<String>();
		viewComboBox.addItem("console");
		viewComboBox.addItem("graphic");
		viewComboBox.setSelectedItem(Core.config.get("view"));
		viewComboBox.setMaximumSize(new Dimension(200,30));

		//Ajout du listener
		viewComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent  e) {
				Core.config.set("view", e.getItem().toString());
				Core.config.updateConfigFile();
			}
		});

		panView.add(viewComboBox);

		//Activation du Debug Mode
		JPanel panDebug = new JPanel();
		panDebug.setBackground(Color.white);
		panDebug.setPreferredSize(new Dimension(350, 80));
		panDebug.setLayout(new BoxLayout(panDebug, BoxLayout.Y_AXIS));
		panDebug.setBorder(BorderFactory.createTitledBorder(Core.lang.get("debugMode")));

		//CheckBox
		JCheckBox debugModeBox = new JCheckBox(Core.lang.get("setDebugMode"));
		debugModeBox.setSelected(Core.config.DEBUG);
		panDebug.add(debugModeBox);

		//Ajout du listener
		debugModeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent  e) {
				Core.config.set("DEBUG", ((JCheckBox)e.getSource()).isSelected()?"true" : "false");
				Core.config.updateConfigFile();
			}
		});

		interfacePan.add(panView);	
		interfacePan.add(panDebug);	

	}

	/**
	 * Panneau Jeu
	 */
	private void initGamePan() {
		gamePan.setBackground(Color.white);
		gamePan.setPreferredSize(new Dimension(400, 300));

		//Longueur de la combinaison
		final JLabel nbrTurnValue = new JLabel(Core.lang.get("nbrTurn") + Core.config.get("gameTurns"));

		JPanel panNbrTurns = new JPanel();
		panNbrTurns.setBackground(Color.white);
		panNbrTurns.setPreferredSize(new Dimension(350, 80));
		panNbrTurns.setLayout(new BoxLayout(panNbrTurns, BoxLayout.Y_AXIS));
		panNbrTurns.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setNbrTurns")));		

		//Slider
		JSlider sliderNbrTurns = new JSlider(1, 15);
		sliderNbrTurns.setValue(Core.config.getInt("combinationLenght"));
		sliderNbrTurns.setMaximumSize(new Dimension(200,30));

		//Ajout du listener
		sliderNbrTurns.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				String nbrTurns = String.valueOf( ((JSlider)e.getSource()).getValue() );
				Core.config.set("gameTurns", nbrTurns);
				Core.config.updateConfigFile();            	

				String value = Core.lang.get("nbrTurn") + nbrTurns;
				nbrTurnValue.setText(value);
			}
		});


		//Longueur de la combinaison
		final JLabel combinationLenghtValue = new JLabel(Core.lang.get("setLenght") + Core.config.get("combinationLenght"));

		JPanel panCombinationLenght = new JPanel();
		panCombinationLenght.setBackground(Color.white);
		panCombinationLenght.setPreferredSize(new Dimension(350, 80));
		panCombinationLenght.setLayout(new BoxLayout(panCombinationLenght, BoxLayout.Y_AXIS));
		panCombinationLenght.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setCombinationLenght")));
		
		//Slider
		JSlider sliderCombinationLenght = new JSlider(1, 9);
		sliderCombinationLenght.setValue(Core.config.getInt("combinationLenght"));

		//Ajout du listener
		sliderCombinationLenght.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				String lenght = String.valueOf( ((JSlider)e.getSource()).getValue() );
				Core.config.set("combinationLenght", lenght);
				Core.config.updateConfigFile();            	

				combinationLenghtValue.setText(Core.lang.get("setLenght") + lenght);

			}
		});


		panCombinationLenght.add(combinationLenghtValue);
		panCombinationLenght.add(sliderCombinationLenght);

		// bornes de la combinaison
		final JLabel rangeSliderValueMin = new JLabel(Core.lang.get("setCombMin") + Core.config.get("combinationNumbersMin"));
		final JLabel rangeSliderValueMax = new JLabel(Core.lang.get("setCombMax") + Core.config.get("combinationNumbersMax"));


		JPanel panCombinationRange = new JPanel();
		panCombinationRange.setBackground(Color.white);
		panCombinationRange.setPreferredSize(new Dimension(350, 80));
		panCombinationRange.setLayout(new BoxLayout(panCombinationRange, BoxLayout.Y_AXIS));
		panCombinationRange.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setCombinationRange")));

		//Range
		RangeSlider slider =  new RangeSlider();
		slider.setMinimum(0);
		slider.setMaximum(9);

		slider.setValue(Core.config.getInt("combinationNumbersMin"));
		slider.setUpperValue(Core.config.getInt("combinationNumbersMax"));

		//Ajout du listener
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				Core.config.set("combinationNumbersMin", String.valueOf(slider.getValue()));
				Core.config.set("combinationNumbersMax", String.valueOf(slider.getUpperValue()));

				Core.config.updateConfigFile();                

				rangeSliderValueMin.setText(Core.lang.get("setCombMin") + String.valueOf(slider.getValue()));
				rangeSliderValueMax.setText(Core.lang.get("setCombMax") + String.valueOf(slider.getUpperValue()));
			}
		});

		panCombinationRange.add(rangeSliderValueMin);
		panCombinationRange.add(rangeSliderValueMax);
		panCombinationRange.add(slider);

		gamePan.add(panCombinationLenght);		    
		gamePan.add(panCombinationRange);		    

	}

	/**
	 * Panneau Langue
	 */
	private void initLangPan() {
		langPan.setBackground(Color.white);
		langPan.setPreferredSize(new Dimension(400, 300));

		//Definition de la langue 
		JPanel panLang = new JPanel();
		panLang.setBackground(Color.white);
		panLang.setPreferredSize(new Dimension(350, 80));
		panLang.setLayout(new BoxLayout(panLang, BoxLayout.Y_AXIS));
		panLang.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setLang")));
		
		//ComboBox
		JComboBox<String> langComboBox = new JComboBox<String>();
		langComboBox.setMaximumSize(new Dimension(200,30));
		
		File langDir = new File(Core.config.get("languageDir"));

		int i = 0;
		int selectedID = 0;
		for(String langFile :  langDir.list()) {
			// liste des fichiers langues dans le dossier languageDir			
			if(FilenameUtils.isExtension(langFile, "lang")) {
				String baseName = FilenameUtils.getBaseName(langFile);
				langComboBox.addItem(baseName);		
				if(baseName.equals(Core.config.get("defaultLanguage")))
					selectedID = i;

				i++;
			}
		}
		
		langComboBox.setSelectedIndex(selectedID);

		langComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent  e) {
				Core.config.set("defaultLanguage", e.getItem().toString());
				Core.config.updateConfigFile();
			}
		});

		panLang.add(langComboBox);

		langPan.add(panLang);		

	}

}
