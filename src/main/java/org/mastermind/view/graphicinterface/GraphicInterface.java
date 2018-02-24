package org.mastermind.view.graphicinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.view.AbstractInterface;
import org.mastermind.view.graphicinterface.panel.AbstractPanel;
import org.mastermind.view.graphicinterface.panel.ConfigPanel;
import org.mastermind.view.graphicinterface.panel.GamePanel;
import org.mastermind.view.graphicinterface.panel.HelloWorldPanel;
import org.mastermind.view.graphicinterface.panel.RulesPanel;
import org.mastermind.view.graphicinterface.panel.ScoresPanel;

public class GraphicInterface extends AbstractInterface {

	private JMenuBar menuBar = null;

	private JMenu menuFile = null;
	private JMenuItem menuNewGameItem = null;
	private JMenuItem menuScoreItem = null;
	private JMenuItem menuRulesItem = null;
	private JMenuItem menuConfigItem = null;
	private JMenuItem menuExitItem = null;

	private JMenu menuAboutUsMenu = null;
	private JMenuItem menuAboutUsItem = null;

	private JPanel container;

	private Dimension size;

	private AbstractPanel currentView;

	public GraphicInterface(Controller c) {
		super(c);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

	}

	@Override
	protected void initView() {
		this.size = new Dimension(650, 600);

		this.setTitle(Core.lang.get("appTitle"));
		this.setSize(this.size);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.container = new JPanel();

		this.container.setPreferredSize(this.size);
		this.container.setBackground(Color.white);

		this.setContentPane(this.container);
		initMenu();
		this.setVisible(true);

	}

	/**
	 * Menu principal
	 */
	private void initMenu() {
		this.menuBar = new JMenuBar();

		this.menuFile = new JMenu(Core.lang.get("graphic.menuFile"));
		this.menuFile.setMnemonic(Core.lang.getChar("graphic.menuFileMnemonic"));

		this.menuNewGameItem = new JMenuItem(Core.lang.get("graphic.menuNewGameItem"));
		this.menuNewGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		this.menuNewGameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				initGame();
			}
		});

		this.menuScoreItem = new JMenuItem(Core.lang.get("graphic.menuScoreItem"));
		this.menuScoreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		this.menuScoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scoresView();
			}
		});

		this.menuRulesItem = new JMenuItem(Core.lang.get("graphic.menuRulesItem"));
		this.menuRulesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		this.menuRulesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rulesView();
			}
		});

		this.menuConfigItem = new JMenuItem(Core.lang.get("graphic.menuConfigItem"));
		this.menuConfigItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		this.menuConfigItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				configView();
			}
		});

		this.menuExitItem = new JMenuItem(Core.lang.get("graphic.menuExitItem"));
		this.menuExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		this.menuExitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.menuAboutUsMenu = new JMenu(Core.lang.get("graphic.menuAboutUsMenu"));
		this.menuAboutUsMenu.setMnemonic(Core.lang.getChar("graphic.menuAboutUsMenuMnemonic"));
		this.menuAboutUsItem = new JMenuItem(Core.lang.get("graphic.menuAboutUsItem"));
		this.menuAboutUsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aboutUsView();
			}
		});

		this.menuFile.add(this.menuNewGameItem);
		this.menuFile.add(this.menuScoreItem);
		this.menuFile.add(this.menuRulesItem);
		this.menuFile.addSeparator();
		this.menuFile.add(this.menuConfigItem);
		this.menuFile.addSeparator();
		this.menuFile.add(this.menuExitItem);
		this.menuAboutUsMenu.add(this.menuAboutUsItem);

		this.menuBar.add(this.menuFile);
		this.menuBar.add(this.menuAboutUsMenu);

		this.setJMenuBar(menuBar);

	}

	@Override
	protected void helloWorld() {
		this.container.removeAll();
		this.container.add(new HelloWorldPanel(this.size).getPanel());
		this.container.validate();
	}

	@Override
	protected void initInterface() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setGameMode() {
		NewGameSettingDialog dial = new NewGameSettingDialog(null, Core.lang.get("hello") + " " + this.playerName, true, controller);
	}

	@Override
	protected void newGame() {
		currentView = new GamePanel(size, controller);
		controller.addObserver(currentView);

		updateView();
	}

	@Override
	protected void newRound() {
		controller.newGame();
	}

	@Override
	protected void setPlayer() {
		String nom = (String) JOptionPane.showInputDialog(null, Core.lang.get("setPlayerName"), "", JOptionPane.QUESTION_MESSAGE, GameGFX.QUESTION.getIcon(), null, "");
		if (nom != null) {
			this.controller.setPlayerName(nom);
			this.playerName = Core.score.getPlayerName();
		}
	}

	@Override
	protected void rulesView() {
		currentView = new RulesPanel(size);
		updateView();
	}

	@Override
	protected void scoresView() {
		currentView = new ScoresPanel(size);
		updateView();

	}

	private void configView() {
		currentView = new ConfigPanel(size);
		updateView();

	}

	private void updateView() {
		this.container.removeAll();
		this.container.add(this.currentView.getPanel());
		this.container.validate();

	}

	@Override
	protected void aboutUsView() {
		ImageIcon logo = new ImageIcon(Core.config.get("dir.img") + "logo.png");

		String aboutUsText = "";
		for (String s : Core.lang.getArray("text.aboutUs"))
			aboutUsText += s + "\n";

		JOptionPane.showMessageDialog(null, aboutUsText, Core.lang.get("graphic.menuAboutUsItem"), JOptionPane.INFORMATION_MESSAGE,logo);

	}

	@Override
	public void updateInitGame(String s, List<Object> l, List<Object> r, boolean u, boolean ml) {
	}

	@Override
	public void updateInput(String s) {
	}

	@Override
	public void updateOutputPropos(List<Object> o) {
	}

	@Override
	public void updateOutputCompar(List<Object> o) {
	}

	@Override
	public void updateRound(int o) {
	}

	@Override
	public void updateEndGame(String t, boolean winner) {
		String str = Core.lang.get(t) + "\n";
		str += Core.lang.get("getScores") + "\n";

		int win = Core.score.getScores()[0];
		int loose = Core.score.getScores()[1];

		double p = 0.;
		try {
			p = (win / ((double) win + (double) loose)) * 100;
		} catch (Exception e) {
		}

		p = Math.round(p * Math.pow(10, 2)) / Math.pow(10, 2);

		str += "  " + win + " " + Core.lang.get("text.victory") + " / " + loose + " " + Core.lang.get("text.defeat") + " ( " + p
				+ "% )" + "\n\n";

		if (winner) {
			str += Core.lang.get("newRound." + controller.getGameMode() ) + "\n";
		} else {
			str += Core.lang.get("exaequo") + "\n";
		}

		int option = JOptionPane.showConfirmDialog(null, str, "", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, GameGFX.QUESTION.getIcon());

		if (option == JOptionPane.OK_OPTION) {
			startNewGame();
		} else {
			helloWorld();
		}

	}

}
