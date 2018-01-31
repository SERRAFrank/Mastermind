package org.mastermind.view.graphicinterface;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mastermind.core.Core;
import org.mastermind.observer.Observer;

public abstract class AbstractPanel  implements Observer{
	/** Definition des font par defaut */
	protected Font comics30 = new Font("Comics Sans MS", Font.BOLD, 30);
	protected Font comics40 = new Font("Comics Sans MS", Font.BOLD, 40);
	protected Font comics20 = new Font("Comics Sans MS", Font.BOLD, 20);
	protected Font arial = new Font("Arial", Font.BOLD, 12);
	protected Font dialog = new Font("Dialog", Font.BOLD + Font.ITALIC, 15);
	
	
	protected JPanel panel;
	protected JPanel content;

	protected Dimension dimension;

	public AbstractPanel(Dimension dim){

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
		
		
		this.dimension = dim;
		this.panel = new JPanel();	
		this.panel.setSize(this.dimension);
		this.panel.setBackground(Color.DARK_GRAY);
		this.panel.setLayout(new BorderLayout());
		
		this.content = new JPanel();
		this.content.setBackground(Color.white);
	} 

	public JPanel getPanel(){
		return this.panel;
	}

	
	/**
	 * Mise en forme du titre du panel
	 * @param msg
	 * 		Titre
	 */
	protected void setTitle(String msg) {
		JLabel titre = new JLabel(msg);
		titre.setHorizontalAlignment(JLabel.CENTER);
		titre.setFont(comics30);
		this.panel.add(titre, BorderLayout.NORTH);

	}
	/** 
	 * Initialisation du panel 
	 */
	protected abstract void initPanel();

	public void setInput(String o) {}	
	
	/** Observer Pattern */
	@Override
	public void updateInput(String s) {}

	@Override
	public void updateOutput(String s, Object o){}


	@Override
	public void updateEndGame(String e, boolean w){}
  

}