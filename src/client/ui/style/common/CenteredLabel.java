package client.ui.style.common;

import javax.swing.JLabel;

import client.ui.style.Style;

public class CenteredLabel extends Style {
	CenteredLabel() {}

	public void apply(Object... elements) throws InvalidElementTypeException {
		for (Object element : elements) {
			if (!(element instanceof JLabel)) {
				throw new InvalidElementTypeException();
			}

			JLabel label = (JLabel) element;
			
			label.setHorizontalAlignment(JLabel.CENTER);
		}
	}
}
