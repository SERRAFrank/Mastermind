package  org.mastermind.view.graphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

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
	private JMenuItem exitItem = null;

	private JMenu aboutUsMenu = null;
	private JMenuItem aboutUsItem = null;

	private JPanel container ;

	private Dimension size;


	public GraphicInterface(Controller c) {
		super(c);
	}

	@Override
	protected void initView() {
		this.size = new Dimension(600, 400);

		this.setTitle(core.lang.get("appTitle"));
		this.setSize(this.size);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.container = new JPanel();

		this.container.setPreferredSize( this.size );
		this.container.setBackground(Color.white);

		this.setContentPane(this.container);
		initMenu();
		this.setVisible(true);


	}


	private void initMenu() {
		this.menuBar = new JMenuBar();

		this.fileMenu = new JMenu(core.lang.get("fileMenu"));
		this.fileMenu.setMnemonic(core.lang.getChar("fileMenuMnemonic"));


		this.newGameItem = new JMenuItem(core.lang.get("newGameItem"));
		this.newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		this.newGameItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				initGame();
			}	    	
		});

		this.scoreItem = new JMenuItem(core.lang.get("scoreItem"));
		this.scoreItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		this.scoreItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				scoresView();
			}	    	
		});

		this.rulesItem = new JMenuItem(core.lang.get("rulesItem"));
		this.rulesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
		this.rulesItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				rulesView();
			}	    	
		});

		this.exitItem = new JMenuItem(core.lang.get("exitItem"));
		this.exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK));
		this.exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});

		this.aboutUsMenu = new JMenu(core.lang.get("aboutUsMenu"));
		this.aboutUsMenu.setMnemonic(core.lang.getChar("aboutUsMenuMnemonic"));
		this.aboutUsItem = new JMenuItem(core.lang.get("aboutUsItem"));
		this.aboutUsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				aboutUsView();
			}	    	
		});

		this.fileMenu.add(this.newGameItem);
		this.fileMenu.add(this.scoreItem);
		this.fileMenu.add(this.rulesItem);
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
		String menu = "selectGameMode";

		List<String[]> optionsList = menuList(menu + ".Options");

		String[] options = new String[optionsList.size()];
		String[] keys = new String[optionsList.size()];
		String gmKey;

		for(int i = 0; i <  optionsList.size(); i++ ) {
			keys[i] = optionsList.get(i)[0];
			options[i] = optionsList.get(i)[1];
		}

		JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
		String gm = (String)jop.showInputDialog(null, 
				core.lang.get(menu + ".Title"),
				core.lang.get(menu + ".Title"),
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);

		if(gm != null) {
			for(int i = 0; i < options.length; i++) {
				if(options[i].equals(gm))
					controller.setGameMode( keys[i]);
			}
		}
	}



	@Override
	protected void newGame() {

		AbstractPanel gameViewPanel = new GamePanel(size, controller);
		controller.addObserver(gameViewPanel);
		
		this.container.removeAll();
		this.container.add(gameViewPanel.getPanel(), BorderLayout.CENTER);
		this.container.validate();

 
	}

	@Override
	protected void newRound() {
		controller.newGame();
	}

	@Override
	protected void setPlayer() {
		JOptionPane jop = new JOptionPane(), jop2 = new JOptionPane();
		String nom = jop.showInputDialog(null, core.lang.get("setPlayerName"), "", JOptionPane.QUESTION_MESSAGE);
		if(nom != null) {
			this.controller.setPlayerName(nom);
			this.playerName = score.getPlayerName();
			jop.showMessageDialog(null, core.lang.get("hello") + " " +this.playerName, core.lang.get("hello"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Override
	protected void rulesView() {
		this.container.removeAll();
		this.container.add(new RulesPanel(size).getPanel(), BorderLayout.CENTER);
		this.container.validate();
	}

	@Override
	protected void scoresView() {
		this.container.removeAll();
		this.container.add(new ScoresPanel(size, score.getScoresList()).getPanel(), BorderLayout.CENTER);
		this.container.validate();
	}

	@Override
	protected void aboutUsView() {
		this.container.removeAll();
		this.container.add(new RulesPanel(size).getPanel(), BorderLayout.CENTER);
		this.container.validate();
	}


	private String[] genOptionsList(String options) {
		String[] optionsList = {};
		int i = 0;

		while( core.lang.keyExist(options + "." + i) ) {
			optionsList[i] = core.lang.get(options + "." + i + ".desc", true);
			i++;				
		}
		return optionsList;

	}

	private String optionToKey(String options, String value) {

		String key = "";
		int i = 0;

		while( core.lang.keyExist(options + "." + i) ) {
			if(core.lang.get(options + "." + i + ".desc", true).equals(value))
				key = core.lang.get(options + "." + i + ".key", true);

		}


		return key;		
	}

	@Override
	public void updateInput(String s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOutput(String s, List<?> o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateOutput(String s, String o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateEndGame(String t, boolean winner) {
		String str = Core.lang.get(t) + "\n";
		str += Core.lang.get("getScores") + "\n";

		int win = score.getScores()[0];
		int loose = score.getScores()[1];

		double p = 0.;
		try{
			p =   (win / ((double)win + (double)loose)) * 100;
		}catch(Exception e) {}

		p = Math.round(p * Math.pow(10,2)) / Math.pow(10,2);

		str += "  " + win + " " +  Core.lang.get("victory") + " / " +  loose + " " + Core.lang.get("defeat") +  " ( " + p + "% )" + "\n\n";

		if(winner) {
			str += Core.lang.get(controller.getGameMode() + ".newRound") + "\n";
		} else {
			str += Core.lang.get(controller.getGameMode() + ".exaequo") + "\n";
		}

		int option = JOptionPane.showConfirmDialog(null, 
				str,
				"", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE);


		
		if(option == JOptionPane.OK_OPTION){
			startNewGame();
		}else {
			helloWorld();
		}

	}




}
