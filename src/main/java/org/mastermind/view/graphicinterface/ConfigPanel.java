package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FilenameUtils;
import org.mastermind.core.Core;

public class ConfigPanel extends AbstractPanel {

	protected JPanel generalPan = new JPanel();
	protected JPanel gamePan = new JPanel();
	protected JPanel systemPan = new JPanel();

	protected JTabbedPane tabbedPan = new JTabbedPane(JTabbedPane.LEFT);

	protected JTabbedPane gameTypeTabPan = new JTabbedPane(JTabbedPane.TOP);

	public ConfigPanel(Dimension dim) {
		super(dim);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		initGeneralPan();
		initGamePan();
		initSystemPan();

		initPanel();

	}

	@Override
	protected void initPanel() {

		// onglet interface
		JLabel interfaceLabel = new JLabel(Core.lang.get("generalPan"));
		interfaceLabel.setIcon(new ImageIcon(Core.config.get("dir.img") + "/icon-interface.png"));
		interfaceLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
		tabbedPan.addTab(null, generalPan);
		tabbedPan.setTabComponentAt(0, interfaceLabel);

		// onglet jeu
		JLabel gameLabel = new JLabel(Core.lang.get("gamePan"));
		gameLabel.setIcon(new ImageIcon(Core.config.get("dir.img") + "/icon-game.png"));
		gameLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
		tabbedPan.addTab(null, gamePan);
		tabbedPan.setTabComponentAt(1, gameLabel);

		// onglet lang
		JLabel langLabel = new JLabel(Core.lang.get("systemPan"));
		langLabel.setIcon(new ImageIcon(Core.config.get("dir.img") + "/icon-lang.png"));
		langLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
		tabbedPan.addTab(null, systemPan);
		tabbedPan.setTabComponentAt(2, langLabel);

		content.add(tabbedPan);

		panel.add(content, BorderLayout.CENTER);

	}

	/**
	 * Panneau Interface
	 */
	private void initGeneralPan() {

		generalPan.setBackground(Color.white);
		generalPan.setPreferredSize(new Dimension(400, 300));

		JPanel panNbrTurns = new JPanel();
		panNbrTurns.setBackground(Color.white);
		panNbrTurns.setPreferredSize(new Dimension(350, 80));
		panNbrTurns.setLayout(new BoxLayout(panNbrTurns, BoxLayout.Y_AXIS));
		panNbrTurns.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setNbrTurns")));

		// Slider
		JSlider sliderNbrTurns = new JSlider(1, 10);
		sliderNbrTurns.setValue(Core.config.getInt("game.turns"));
		sliderNbrTurns.setMajorTickSpacing(1);
		// sliderCombinationLenght.setMinorTickSpacing(1);
		sliderNbrTurns.setPaintTicks(true);
		sliderNbrTurns.setPaintLabels(true);

		// Ajout du listener
		sliderNbrTurns.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int nbrTurns = ((JSlider) e.getSource()).getValue();
				Core.config.set("game.turns", nbrTurns);
				Core.config.updateConfigFile();

				String value = Core.lang.get("nbrTurn") + nbrTurns;
			}
		});

		panNbrTurns.add(sliderNbrTurns);

		// Slider
		JSlider sliderCombinationLenght = new JSlider(1, 10);
		sliderCombinationLenght.setValue(Core.config.getInt("game.lenght"));
		sliderCombinationLenght.setMajorTickSpacing(1);
		// sliderCombinationLenght.setMinorTickSpacing(1);
		sliderCombinationLenght.setPaintTicks(true);
		sliderCombinationLenght.setPaintLabels(true);
		// Ajout du listener
		sliderCombinationLenght.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				int lenght = ((JSlider) e.getSource()).getValue();
				Core.config.set("game.lenght", lenght);
				initGamePan();
				Core.config.updateConfigFile();

			}
		});

		// Longueur de la combinaison

		JPanel panCombinationLenght = new JPanel();
		panCombinationLenght.setBackground(Color.white);
		panCombinationLenght.setPreferredSize(new Dimension(350, 80));
		panCombinationLenght.setLayout(new BoxLayout(panCombinationLenght, BoxLayout.Y_AXIS));
		panCombinationLenght.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setCombinationLenght")));
		panCombinationLenght.add(sliderCombinationLenght);

		generalPan.add(panNbrTurns);
		generalPan.add(panCombinationLenght);

	}

	/**
	 * Panneau Jeu
	 */
	private void initGamePan() {
		gamePan.setBackground(Color.white);
		gamePan.setPreferredSize(new Dimension(400, 500));
		gameTypeTabPan.removeAll();

		for (int i = 0; i < Core.config.getArray("game.type").length; i++) {
			final String gameType = Core.config.getArray("game.type")[i];
			JLabel gameTypeLabel = new JLabel(gameType);
			JPanel gameTypePanel = new JPanel();
			gameTypePanel.setBackground(Color.white);
			gameTypePanel.setPreferredSize(new Dimension(350, 400));
			gameTypePanel.setLayout(new GridLayout(11, 1));

			
			JPanel labPanel = new JPanel();
			labPanel.add(new JLabel(Core.lang.get(gameType + ".configValues")));
			gameTypePanel.add(labPanel);
			
			for (int j = 0; j < 10; j++) {
				final String v;
				if (j < Core.config.getArray(gameType + ".acceptedInputValues").length)
					v = Core.config.getArray(gameType + ".acceptedInputValues")[j];
				else
					v = null;

				final JLabel data = new JLabel(v);
				data.setOpaque(true);
				data.setPreferredSize(new Dimension(150, 30));
				data.setHorizontalAlignment(SwingConstants.CENTER);
				data.setBorder(LineBorder.createGrayLineBorder());

				if (j < Core.config.getInt("game.lenght")) {
					data.setEnabled(true);
					data.setBackground(Color.WHITE);
				} else {
					data.setEnabled(false);
					data.setBackground(Color.LIGHT_GRAY);
				}

				if(v != null && data.isEnabled()){
					if (Core.config.get(gameType + ".acceptedInputType").equals("Color")) {
						data.setBackground(Color.decode(v));
						data.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								PickupPanel colorPanel = new PickupPanel(null, "", Color.decode(v));
								Color c = colorPanel.showDialog();
								if (c != null) {
									String hex = "#" + Integer.toHexString(c.getRGB()).substring(2);
									if(Core.config.getList(gameType + ".acceptedInputValues").contains(hex) && !data.getText().equals(hex)) {
										JOptionPane.showMessageDialog(null, "Cette valeur est déjà présente", "Erreur", JOptionPane.ERROR_MESSAGE);
									}else {
										List<Object> acceptedInputValues = Core.config.getList(gameType + ".acceptedInputValues");
										int id = acceptedInputValues.indexOf(data.getText());
										acceptedInputValues.set(id, hex);
										Core.config.set(gameType + ".acceptedInputValues", acceptedInputValues);
										Core.config.updateConfigFile();
										
										data.setBackground(c);
										data.setText(hex);

									}
								}

							}
						});
					}
					/*else {
						data.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								String value = JOptionPane.showInputDialog(null, "Veuillez décliner votre identité !", "Gendarmerie nationale !", JOptionPane.QUESTION_MESSAGE);
								if (value != null) {
									if(Core.config.getList(gameType + ".acceptedInputValues").contains(value) && !data.getText().equals(value)) {
										JOptionPane.showMessageDialog(null, "Cette valeur est déjà présente", "Erreur", JOptionPane.ERROR_MESSAGE);
									}else {
										data.setText(value);
									}
								}

							}
						});
						
					}
					*/
				}
				
				JPanel dataPanel = new JPanel();
				dataPanel.add(data);

				gameTypePanel.add(dataPanel);

			}

			// gameTypePanel.add(valueList);

			gameTypeTabPan.addTab(null, gameTypePanel);
			gameTypeTabPan.setTabComponentAt(i, gameTypeLabel);
		}

		gamePan.add(gameTypeTabPan);

	}

	/**
	 * Panneau Langue
	 */
	private void initSystemPan() {
		systemPan.setBackground(Color.white);
		systemPan.setPreferredSize(new Dimension(400, 300));

		// Mode de vue par defaut
		JPanel panView = new JPanel();
		panView.setBackground(Color.white);
		panView.setLayout(new BoxLayout(panView, BoxLayout.Y_AXIS));
		panView.setPreferredSize(new Dimension(350, 80));
		panView.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setViewMode")));

		// ComboBox
		JComboBox<String> viewComboBox = new JComboBox<String>();
		viewComboBox.addItem("console");
		viewComboBox.addItem("graphic");
		viewComboBox.setSelectedItem(Core.config.get("view"));
		viewComboBox.setMaximumSize(new Dimension(200, 30));

		// Ajout du listener
		viewComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Core.config.set("view", e.getItem().toString());
				Core.config.updateConfigFile();
			}
		});

		panView.add(viewComboBox);

		// Activation du Debug Mode
		JPanel panDebug = new JPanel();
		panDebug.setBackground(Color.white);
		panDebug.setPreferredSize(new Dimension(350, 80));
		panDebug.setLayout(new BoxLayout(panDebug, BoxLayout.Y_AXIS));
		panDebug.setBorder(BorderFactory.createTitledBorder(Core.lang.get("debugMode")));

		// CheckBox
		JCheckBox debugModeBox = new JCheckBox(Core.lang.get("setDebugMode"));
		debugModeBox.setSelected(Core.DEBUG());
		panDebug.add(debugModeBox);

		// Ajout du listener
		debugModeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.config.set("DEBUG", ((AbstractButton) e.getSource()).isSelected());
				Core.config.updateConfigFile();
			}
		});

		// Definition de la langue
		JPanel panLang = new JPanel();
		panLang.setBackground(Color.white);
		panLang.setPreferredSize(new Dimension(350, 80));
		panLang.setLayout(new BoxLayout(panLang, BoxLayout.Y_AXIS));
		panLang.setBorder(BorderFactory.createTitledBorder(Core.lang.get("setLang")));

		// ComboBox
		JComboBox<String> langComboBox = new JComboBox<String>();
		langComboBox.setMaximumSize(new Dimension(200, 30));

		File langDir = new File(Core.config.get("dir.language"));

		int i = 0;
		int selectedID = 0;
		for (String langFile : langDir.list()) {
			// liste des fichiers langues dans le dossier languageDir
			if (FilenameUtils.isExtension(langFile, "lang")) {
				String baseName = FilenameUtils.getBaseName(langFile);
				langComboBox.addItem(baseName);
				if (baseName.equals(Core.config.get("lang.default")))
					selectedID = i;

				i++;
			}
		}

		langComboBox.setSelectedIndex(selectedID);

		langComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Core.config.set("lang.default", e.getItem().toString());
				Core.config.updateConfigFile();
			}
		});

		panLang.add(langComboBox);

		systemPan.add(panLang);
		systemPan.add(panView);
		systemPan.add(panDebug);

	}

}

class PickupPanel extends JDialog {

	private Color color;

	private boolean sendData = false;

	private List<JLabel> buttonsList = new ArrayList<JLabel>();

	private JPanel content = new JPanel();

	public PickupPanel(JFrame parent, String title, Color c) {
		super(parent, true);

		color = c;
		this.setTitle(title);
		this.setSize(350, 350);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(new BorderLayout());

		initContent();

		JPanel control = new JPanel();
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData = true;
				setVisible(false);
				dispose();
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

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);

	}

	private void initContent() {
		this.content.setLayout(new GridLayout(6, 6));

		float pas = 1.f / 30.f;

		for (float h = 1.f; h > 0.f; h = h - pas) {
			buttonsList.add(initLabel(Color.getHSBColor(h, 1, 1)));
		}
		for (float b = 1.f; b >= 0.f; b = b - (1.f / 5.f)) {
			buttonsList.add(initLabel(Color.getHSBColor(0, 0, b)));
		}

		for (JLabel l : buttonsList)
			this.content.add(l);

	}

	private JLabel initLabel(Color c) {
		final JLabel cButton = new JLabel();
		// cButton.setSize(new Dimension(20,20));
		cButton.setOpaque(true);
		cButton.setBackground(c);
		if (color.equals(c))
			cButton.setBorder(LineBorder.createGrayLineBorder());

		cButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				color = cButton.getBackground();
				cButton.setBorder(LineBorder.createGrayLineBorder());
				for (JLabel b : buttonsList) {
					if (color != b.getBackground())
						b.setBorder(null);
				}
			}
		});
		return cButton;
	}

	public Color showDialog() {
		this.sendData = false;
		this.setVisible(true);
		return (this.sendData) ? color : null;
	}

}