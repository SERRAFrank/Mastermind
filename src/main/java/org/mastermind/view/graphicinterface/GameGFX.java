package org.mastermind.view.graphicinterface;

import java.awt.Font;

import javax.swing.ImageIcon;

import org.mastermind.core.Core;

public enum GameGFX {
	/** Definition des font par defaut */
	COMICS30(new Font("Comics Sans MS", Font.BOLD, 30)),
	COMICS40(new Font("Comics Sans MS", Font.BOLD, 40)),
	COMICS20(new Font("Comics Sans MS", Font.BOLD, 20)),
	ARIAL(new Font("Arial", Font.BOLD, 14)), 
	DIALOG(new Font("Dialog", Font.BOLD + Font.ITALIC, 15)),
	
	/** Definition des images par defaut des boites de dialogue */
	WARNING(new ImageIcon(Core.config.get("dir.img") + "dialog-warning-icon.png")),
	ERROR(new ImageIcon(Core.config.get("dir.img") + "dialog-error-icon.png")),
	INFO(new ImageIcon(Core.config.get("dir.img") + "dialog-info-icon.png")),
	QUESTION(new ImageIcon(Core.config.get("dir.img") + "dialog-question-icon.png"));
	
	

	private Font f;
	private ImageIcon i;

	GameGFX(Font f) {
		this.f = f;
	}
	
	public Font getFont() {
		return this.f;
	}

	
	GameGFX(ImageIcon i) {
		this.i = i;
	}
	public ImageIcon getIcon() {
		return this.i;
	}
	
	

}
