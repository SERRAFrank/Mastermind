package org.mastermind.view.graphicinterface.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.mastermind.core.Core;
import org.mastermind.observer.Observer;
import org.mastermind.view.graphicinterface.GameGFX;

public abstract class AbstractPanel implements Observer {

	protected JPanel panel;
	protected JPanel content;

	protected Dimension dimension;

	public AbstractPanel(Dimension dim) {

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		this.dimension = dim;
		this.panel = new JPanel();
		this.panel.setSize(this.dimension);
		this.panel.setBackground(Color.white);
		this.panel.setLayout(new BorderLayout());

		this.content = new JPanel();
		this.content.setBackground(Color.white);
	}

	public JPanel getPanel() {
		return this.panel;
	}

	/**
	 * Mise en forme du titre du panel
	 * 
	 * @param msg
	 *            Titre
	 */
	protected void setTitle(String msg) {
		JLabel titre = new JLabel(msg);
		titre.setHorizontalAlignment(SwingConstants.CENTER);
		titre.setFont(GameGFX.COMICS30.getFont());
		this.panel.add(titre, BorderLayout.NORTH);

	}

	/**
	 * Initialisation du panel
	 */
	protected abstract void initPanel();

	public void setInput(String o) {
	}

	/** Observer Pattern */

	@Override
	public void updateInput(String s) {
	}

	@Override
	public void updateInitGame(String s, List<Object> l, List<Object> r, boolean u, boolean ml) {
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
	public void updateEndGame(String e, boolean w) {
	}

}