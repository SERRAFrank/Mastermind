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

public class GraphicInterface extends AbstractInterface {

	private JMenuBar menuBar = null;

	private JMenu fileMenu = null;
	private JMenuItem newGameItem = null;
	private JMenuItem scoreItem = null;
	private JMenuItem rulesItem = null;
	private JMenuItem configItem = null;
	private JMenuItem exitItem = null;

	private JMenu aboutUsMenu = null;
	private JMenuItem aboutUsItem = null;

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
		this.size = new Dimension(600, 600);

		this.setTitle(Core.lang.get("appTitle"));
		this.setSize(this.size);
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

		this.fileMenu = new JMenu(Core.lang.get("fileMenu"));
		this.fileMenu.setMnemonic(Core.lang.getChar("fileMenuMnemonic"));

		this.newGameItem = new JMenuItem(Core.lang.get("newGameItem"));
		this.newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		this.newGameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				initGame();
			}
		});

		this.scoreItem = new JMenuItem(Core.lang.get("scoreItem"));
		this.scoreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		this.scoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scoresView();
			}
		});

		this.rulesItem = new JMenuItem(Core.lang.get("rulesItem"));
		this.rulesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		this.rulesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rulesView();
			}
		});

		this.configItem = new JMenuItem(Core.lang.get("configItem"));
		this.configItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		this.configItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				configView();
			}
		});

		this.exitItem = new JMenuItem(Core.lang.get("exitItem"));
		this.exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		this.exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		this.aboutUsMenu = new JMenu(Core.lang.get("aboutUsMenu"));
		this.aboutUsMenu.setMnemonic(Core.lang.getChar("aboutUsMenuMnemonic"));
		this.aboutUsItem = new JMenuItem(Core.lang.get("aboutUsItem"));
		this.aboutUsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				aboutUsView();
			}
		});

		this.fileMenu.add(this.newGameItem);
		this.fileMenu.add(this.scoreItem);
		this.fileMenu.add(this.rulesItem);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.configItem);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.exitItem);
		this.aboutUsMenu.add(this.aboutUsItem);

		this.menuBar.add(this.fileMenu);
		this.menuBar.add(this.aboutUsMenu);

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
		GameModeDialog dial = new GameModeDialog(null, "Coucou les ZérOs", true, controller);
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
		String nom = JOptionPane.showInputDialog(null, Core.lang.get("setPlayerName"), "",
				JOptionPane.QUESTION_MESSAGE);
		if (nom != null) {
			this.controller.setPlayerName(nom);
			this.playerName = Core.score.getPlayerName();
			JOptionPane.showMessageDialog(null, Core.lang.get("hello") + " " + this.playerName, Core.lang.get("hello"),
					JOptionPane.INFORMATION_MESSAGE);
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
		for (int i = 0; Core.lang.keyExist("aboutUs" + "." + i); i++) {
			aboutUsText += Core.lang.get("aboutUs" + "." + i, true) + "\n";
		}

		JOptionPane.showMessageDialog(null, aboutUsText, Core.lang.get("aboutUsItem"), JOptionPane.INFORMATION_MESSAGE,
				logo);

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

		str += "  " + win + " " + Core.lang.get("victory") + " / " + loose + " " + Core.lang.get("defeat") + " ( " + p
				+ "% )" + "\n\n";

		if (winner) {
			str += Core.lang.get(controller.getGameMode() + ".newRound") + "\n";
		} else {
			str += Core.lang.get(controller.getGameMode() + ".exaequo") + "\n";
		}

		int option = JOptionPane.showConfirmDialog(null, str, "", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			startNewGame();
		} else {
			helloWorld();
		}

	}

}
