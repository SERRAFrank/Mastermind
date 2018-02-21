package org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigPanel.
 */
public class ConfigPanel extends AbstractPanel {

	/**  Panneau Général. */ 
	protected JPanel generalPan = new JPanel();

	/**  Panneau Parametre de jeu. */ 
	protected JPanel gamePan = new JPanel();

	/**  Panneau System. */ 
	protected JPanel systemPan = new JPanel();

	/**  Onglets. */
	protected JTabbedPane tabbedPan = new JTabbedPane(JTabbedPane.LEFT);

	/**  onglets Type de jeu. */
	protected JTabbedPane gameTypeTabPan = new JTabbedPane(JTabbedPane.TOP);

	/**
	 * Instantiates a new config panel.
	 *
	 * @param dim the dim
	 */
	public ConfigPanel(Dimension dim) {
		super(dim);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		initGeneralPan();
		initGamePan();
		initSystemPan();

		initPanel();

	}

	/* (non-Javadoc)
	 * @see org.mastermind.view.graphicinterface.AbstractPanel#initPanel()
	 */
	@Override
	protected void initPanel() {

		// onglet interface
		JLabel interfaceLabel = new JLabel(Core.lang.get("config.generalPanTitle"));
		interfaceLabel.setIcon(new ImageIcon(Core.config.get("dir.img") + "/icon-general.png"));
		interfaceLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		tabbedPan.addTab(null, generalPan);
		tabbedPan.setTabComponentAt(0, interfaceLabel);

		// onglet jeu
		JLabel gameLabel = new JLabel(Core.lang.get("config.gamePanTitle"));
		gameLabel.setIcon(new ImageIcon(Core.config.get("dir.img") + "/icon-game.png"));
		gameLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
		tabbedPan.addTab(null, gamePan);
		tabbedPan.setTabComponentAt(1, gameLabel);

		// onglet lang
		JLabel langLabel = new JLabel(Core.lang.get("config.systemPanTitle"));
		langLabel.setIcon(new ImageIcon(Core.config.get("dir.img") + "/icon-system.png"));
		langLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
		tabbedPan.addTab(null, systemPan);
		tabbedPan.setTabComponentAt(2, langLabel);

		content.add(tabbedPan);

		panel.add(content, BorderLayout.CENTER);

	}

	/**
	 * Panneau Interface.
	 */
	private void initGeneralPan() {

		generalPan.setBackground(Color.WHITE);
		generalPan.setPreferredSize(new Dimension(400, 300));

		JPanel panNbrTurns = new JPanel();
		panNbrTurns.setBackground(Color.WHITE);
		panNbrTurns.setPreferredSize(new Dimension(350, 80));
		panNbrTurns.setLayout(new BoxLayout(panNbrTurns, BoxLayout.Y_AXIS));
		panNbrTurns.setBorder(BorderFactory.createTitledBorder(Core.lang.get("config.setNbrTurnsTitle")));

		// Slider
		JSlider sliderNbrTurns = new JSlider(1, 10);
		sliderNbrTurns.setValue(Core.config.getInt("game.turns"));
		sliderNbrTurns.setBackground(Color.WHITE);
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

				String value = Core.lang.get("text.nbrTurn") + nbrTurns;
			}
		});

		panNbrTurns.add(sliderNbrTurns);

		// Slider de longueur de cominaison
		int sliderCombinationLenghtMax = Integer.MAX_VALUE ;
		for(String gt : Core.config.getArray("game.type")) {
			if(Core.config.getArray(gt + ".acceptedInputValues").length < sliderCombinationLenghtMax)
				sliderCombinationLenghtMax = Core.config.getArray(gt + ".acceptedInputValues").length -1;
		}

		JSlider sliderCombinationLenght = new JSlider(1, sliderCombinationLenghtMax);
		sliderCombinationLenght.setBackground(Color.WHITE);
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
		panCombinationLenght.setBackground(Color.WHITE);
		panCombinationLenght.setPreferredSize(new Dimension(350, 80));
		panCombinationLenght.setLayout(new BoxLayout(panCombinationLenght, BoxLayout.Y_AXIS));
		panCombinationLenght.setBorder(BorderFactory.createTitledBorder(Core.lang.get("config.setCombinationLenghtTitle")));
		panCombinationLenght.add(sliderCombinationLenght);

		generalPan.add(panNbrTurns);
		generalPan.add(panCombinationLenght);

	}

	/**
	 * Panneau Jeu.
	 */
	private void initGamePan() {
		gamePan.setBackground(Color.WHITE);
		gamePan.setPreferredSize(new Dimension(400, 500));
		gameTypeTabPan.removeAll();

		//boucle pour les types de jeu
		for (int i = 0; i < Core.config.getArray("game.type").length; i++) {
			final String gameType = Core.config.getArray("game.type")[i];

			JLabel gameTypeLabel = new JLabel(Core.lang.get("config.gameTypePan." + gameType)) ;

			JPanel gameTypePanel = new JPanel();
			gameTypePanel.setBackground(Color.WHITE);
			gameTypePanel.setPreferredSize(new Dimension(350, 400));
			gameTypePanel.setLayout(new GridLayout(11, 1));

			JPanel labPanel = new JPanel();
			labPanel.add(new JLabel(Core.lang.get("config.setValues")));
			gameTypePanel.add(labPanel);

			// boucle des valeurs de combinaison
			for (int j = 0; j < 10; j++) {
				String v;

				if (j < Core.config.getArray(gameType + ".acceptedInputValues").length)
					v = Core.config.getArray(gameType + ".acceptedInputValues")[j];
				else
					v = null;

				JLabel data = new JLabel(v);
				data.setOpaque(true);
				data.setPreferredSize(new Dimension(150, 30));
				data.setHorizontalAlignment(SwingConstants.CENTER);
				data.setBorder(LineBorder.createGrayLineBorder());
				data.setBackground(Color.WHITE);

				if(v != null){
					// si les combinaisons dont des couleurs
					if (Core.config.get(gameType + ".acceptedInputType").equals("Color")) {
						data.setBackground(Color.decode(v));
						// Ecouteur de clique
						data.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								JLabel data = (JLabel) e.getSource();

								// Ouvertur du selectionneur de couleur
								PickupPanel colorPanel = new PickupPanel(null, "", Color.decode(data.getText()));
								Color c = colorPanel.showDialog();

								if (c != null) {
									//Mise en forme de Color en string "#xxxxxx"
									String hex = "#" + Integer.toHexString(c.getRGB()).substring(2);

									//Test la présence de la proposition dans les valeurs déjà existantes
									if(Core.config.getList(gameType + ".acceptedInputValues").contains(hex) && !data.getText().equals(hex)) {
										//Erreur : déjà présent
										JOptionPane.showMessageDialog(null, Core.lang.get("config.setAcceptedValueExiste"), Core.lang.get("text.error"), JOptionPane.ERROR_MESSAGE, GameGFX.ERROR.getIcon());
									}else {
										//Substitution de l'ancienne valeur par la nouvelle
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
					//Si ce n'est pas une couleur
					else {
						data.addMouseListener(new MouseAdapter() {
							//Création de l'écouteur de selection de valeur
							@Override
							public void mouseClicked(MouseEvent e) {
								JLabel data = (JLabel) e.getSource();
								String originalValue = data.getText();
								String value = (String) JOptionPane.showInputDialog(null, Core.lang.get("config.setAcceptedValue"), "", JOptionPane.QUESTION_MESSAGE, GameGFX.QUESTION.getIcon(), null, "");

								if (value != null) {
									try { 
										//Test la présence de la proposition dans les valeurs déjà existantes
										if(Core.config.getList(gameType + ".acceptedInputValues").contains(value) && !data.getText().equals(value)) {
											JOptionPane.showMessageDialog(null, Core.lang.get("config.setAcceptedValueExiste"), Core.lang.get("text.error"), JOptionPane.ERROR_MESSAGE, GameGFX.ERROR.getIcon());
										}else {
											List<Integer> acceptedInput = new ArrayList<Integer>();
											int oInt = 0;
											//Création d'une nouvelle liste de Integer qui remplacera l'ancienne
											for(Object oList : Core.config.getList(gameType + ".acceptedInputValues")) {
												oInt = Integer.parseInt((String) oList);
												if(!oList.equals(originalValue))
													acceptedInput.add(oInt);
											}

											if(Integer.parseInt(value) >= 0)
												acceptedInput.add(Integer.parseInt(value));
											else
												throw new Exception();

											//Trie des Integer par ordre croissant, indispensable pour le mode MoreOrLess
											Collections.sort(acceptedInput);

											List<String> newList = new ArrayList<String>(acceptedInput.size());
											for (Integer myInt : acceptedInput)
												newList.add(String.valueOf(myInt));

											//Mise à jour des valeurs
											Core.config.set(gameType + ".acceptedInputValues", newList);
											Core.config.updateConfigFile();

											initGamePan();

										}			

									} catch(Exception err) { 
										JOptionPane.showMessageDialog(null, Core.lang.get("config.setAcceptedValueNotValid"), Core.lang.get("text.error"), JOptionPane.ERROR_MESSAGE, GameGFX.ERROR.getIcon());
									}
								}


							}
						});

					}

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
	 * Panneau Langue.
	 */
	private void initSystemPan() {
		systemPan.setBackground(Color.WHITE);
		systemPan.setPreferredSize(new Dimension(400, 300));

		// Mode de vue par defaut
		JPanel panView = new JPanel();
		panView.setBackground(Color.WHITE);
		panView.setLayout(new BoxLayout(panView, BoxLayout.Y_AXIS));
		panView.setPreferredSize(new Dimension(350, 80));
		panView.setBorder(BorderFactory.createTitledBorder(Core.lang.get("config.setViewMode")));

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
		panDebug.setBackground(Color.WHITE);
		panDebug.setPreferredSize(new Dimension(350, 80));
		panDebug.setLayout(new BoxLayout(panDebug, BoxLayout.Y_AXIS));
		panDebug.setBorder(BorderFactory.createTitledBorder(Core.lang.get("config.debugModeTitle")));

		// CheckBox
		JCheckBox debugModeBox = new JCheckBox(Core.lang.get("config.setDebugMode"));
		debugModeBox.setSelected(Core.DEBUG());
		debugModeBox.setBackground(Color.WHITE);
		panDebug.add(debugModeBox);

		// Ajout du listener
		debugModeBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.DEBUG(((AbstractButton) e.getSource()).isSelected());
				Core.config.updateConfigFile();
			}
		});

		// Definition de la langue
		JPanel panLang = new JPanel();
		panLang.setBackground(Color.WHITE);
		panLang.setPreferredSize(new Dimension(350, 80));
		panLang.setLayout(new BoxLayout(panLang, BoxLayout.Y_AXIS));
		panLang.setBorder(BorderFactory.createTitledBorder(Core.lang.get("config.setLangTitle")));

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

/**
 * Class PickupPanel.
 */
class PickupPanel extends JDialog {

	//Couleur à renvoyer
	private Color color;

	//Envoie des données autorisé
	private boolean sendData = false;

	private List<JLabel> colorsList = new ArrayList<JLabel>();

	private JPanel content = new JPanel();

	/**
	 * Constructeur
	 *
	 * @param parent 
	 * 			Le parent
	 * @param title
	 * 			Le titre de la fenetre 
	 * @param c
	 * 			La couleur d'origine
	 */
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
		JButton okBouton = new JButton(Core.lang.get("text.ok"));

		//Listener si le bouton OK est cliqué
		okBouton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendData = true;
				setVisible(false);
				dispose();
			}

		});

		//Bouton Annuler
		JButton cancelBouton = new JButton(Core.lang.get("text.cancel"));
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

	/**
	 * Initialisation du contenu
	 * Création du panel avec les couleurs à afficher
	 */
	private void initContent() {
		this.content.setLayout(new GridLayout(6, 6));

		float pas = 1.f / 30.f;

		for (float h = 1.f; h > 0.f; h = h - pas) {
			colorsList.add(initLabel(Color.getHSBColor(h, 1, 1)));
		}
		for (float b = 1.f; b >= 0.f; b = b - (1.f / 5.f)) {
			colorsList.add(initLabel(Color.getHSBColor(0, 0, b)));
		}

		for (JLabel l : colorsList)
			this.content.add(l);

	}

	/**
	 * Création de labels contenant les couleurs
	 * @param c
	 * 			La couleur de font du label
	 * @return
	 * 			JLabel
	 */
	private JLabel initLabel(Color c) {
		JLabel colorLabel = new JLabel();
		// colorLabel.setSize(new Dimension(20,20));
		colorLabel.setOpaque(true);
		colorLabel.setBackground(c);
		if (color.equals(c))
			colorLabel.setBorder(LineBorder.createGrayLineBorder());

		colorLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel colorLabel = (JLabel) e.getSource();
				color = colorLabel.getBackground();
				colorLabel.setBorder(LineBorder.createGrayLineBorder());
				for (JLabel b : colorsList) {
					if (color != b.getBackground())
						b.setBorder(null);
				}
			}
		});
		return colorLabel;
	}

	/**
	 * Affichage de la boite de dialogue
	 *
	 * @return La couleur séléctionnée
	 */
	public Color showDialog() {
		this.sendData = false;
		this.setVisible(true);
		return (this.sendData) ? color : null;
	}

}