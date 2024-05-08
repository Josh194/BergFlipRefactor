package client.ui.style.common;

import java.awt.Font;

import javax.swing.JComponent;

import client.ui.style.Style;

public class TitleFont extends Style {
	TitleFont() {}

	public void apply(Object... elements) throws InvalidElementTypeException {
		for (Object element : elements) {
			if (!(element instanceof JComponent)) {
				throw new InvalidElementTypeException();
			}

			JComponent component = (JComponent) element;
			
			component.setFont(FONT);
		}
	}

	private static final Font FONT = new Font("Arial", Font.PLAIN, 40);
}
