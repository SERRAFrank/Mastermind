package org.mastermind.view.graphicinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class GameComponent {

	private List<Widget> widgetList = new ArrayList<Widget>();
	private int lenght = 0;

	private String border = "";
	private String returnValue;
	private List<Object> possibleValue = new ArrayList<Object>();
	private Set<Object> usedValue = new HashSet<Object>();
	private boolean uniqueValue = true;

	public GameComponent(int l) {
		this.lenght = l;

		for (int i = 0; i < lenght; i++) {
			Widget w = new Widget();
			widgetList.add(w);
		}

	}

	public String getValue(int i) {
		return widgetList.get(i).getText();
	}

	public JPanel toPanel() {
		JPanel panel = new JPanel();
		for (Widget w : widgetList)
			panel.add(w);

		return panel;

	}

	private void addSelector() {
		for (final Widget widget : widgetList) {

			widget.addMouseListener(new MouseListener() {
				@Override
				public void mousePressed(MouseEvent me) {
				}

				@Override
				public void mouseReleased(MouseEvent me) {
				}

				@Override
				public void mouseEntered(MouseEvent me) {
				}

				@Override
				public void mouseExited(MouseEvent me) {
				}

				@Override
				public void mouseClicked(MouseEvent me) {
					if (widget.isEnabled()) {
						int index;
						Object oldValue = widget.getValue();
						boolean oldValueErased = widget.isErased();
						Object newValue = null;
						if (possibleValue.contains(oldValue)) {
							index = possibleValue.indexOf(oldValue);
						} else {
							index = 0;
						}

						if (me.getButton() == MouseEvent.BUTTON2) {
							widget.erase();
						} else {
							do {
								if (me.getButton() == MouseEvent.BUTTON1) {
									index = (index < (possibleValue.size() - 1)) ? index + 1 : 0;
								} else {
									index = (index > 0) ? index - 1 : possibleValue.size() - 1;
								}
								newValue = possibleValue.get(index);

							} while ((uniqueValue && usedValue.contains(newValue) && possibleValue.size() > 1));

							widget.setValue(newValue);
						}

						if (uniqueValue) {
							if (!oldValueErased)
								usedValue.remove(oldValue);
							if (!widget.isErased())
								usedValue.add(newValue);
						}

					}
				}
			});
		}
	}

	public void setBackground(int i, Color color) {
		widgetList.get(i).setBackground(color);
	}

	public void setColor(int i, Color color) {
		widgetList.get(i).setBorder(BorderFactory.createLineBorder(color));
	}

	public void setText(int i, String value) {
		widgetList.get(i).setText(value);

	}

	public void setPossibleValue(List<Object> v, boolean u) {
		possibleValue = v;
		uniqueValue = u;
		addSelector();
	}

	public void setValues(List<Object> list) {
		if (widgetList.size() == list.size()) {
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				if (uniqueValue)
					usedValue.add(obj);
				widgetList.get(i).setValue(obj);
			}
		}
	}

	public void setReturnValue(String r) {
		this.returnValue = r;
	}

	public void setEnabled(boolean b) {
		for (Widget w : widgetList)
			w.setEnabled(b);
	}

	public void setBorder(String b) {
		this.border = b;
	}

	public JLabel get(int i) {
		return widgetList.get(i);

	}

	public List<Object> toList() {
		List<Object> value = new ArrayList<Object>();
		for (Widget widget : widgetList) {
			value.add(widget.getValue());
		}
		return value;
	}
}

class Widget extends JLabel {
	private Font font = GameFont.COMICS30.getFont();
	private Dimension dim = new Dimension(50, 50);
	private String eraseText = "X";
	private Color eraseColor = new Color(254, 254, 254);
	private JLabel label = new JLabel();
	private Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	private String typeValue = "String";
	private boolean erased = true;

	public Widget() {
		super();
		setFont(font);
		setPreferredSize(dim);
		setHorizontalAlignment(SwingConstants.CENTER);
		setBackground(Color.WHITE);
		setOpaque(true);
		setBorder(border);
		setBackground(eraseColor);
		setText(eraseText);
		setIcon(null);
	}

	public Object getValue() {
		Object value;
		switch (typeValue) {
		default:
		case "Integer":
		case "String":
			value = getText();
			break;
		case "Color":
			value = getBackground();
			break;
		case "Icon":
			value = getIcon();
			break;
		}
		return value;
	}

	public void setValue(Object obj) {
		erase();
		setText("");
		if (obj instanceof String) {
			setText(obj.toString());
			typeValue = "String";
		} else if (obj instanceof Integer) {
			setText(String.valueOf(obj));
			typeValue = "Integer";
		} else if (obj instanceof Color) {
			setBackground((Color) obj);
			typeValue = "Color";
		} else if (obj instanceof ImageIcon) {
			setIcon((Icon) obj);
			typeValue = "Icon";
		}

		erased = false;
	}

	public void erase() {
		setBackground(eraseColor);
		setText(eraseText);
		setIcon(null);
		erased = true;
	}

	public boolean isErased() {
		return erased;
	}
}