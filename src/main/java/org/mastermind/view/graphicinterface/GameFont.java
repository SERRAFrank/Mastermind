package org.mastermind.view.graphicinterface;

import java.awt.Font;

public enum GameFont {
	/** Definition des font par defaut */
	COMICS30(new Font("Comics Sans MS", Font.BOLD, 30)),
	COMICS40(new Font("Comics Sans MS", Font.BOLD, 40)),
	COMICS20(new Font("Comics Sans MS", Font.BOLD, 20)),
	ARIAL(new Font("Arial", Font.BOLD, 14)),
	DIALOG(new Font("Dialog", Font.BOLD + Font.ITALIC, 15));
	
	
    private Font f;

   GameFont(Font f) {
        this.f = f;
    }

    public Font getFont() {
        return this.f;
    }
}
