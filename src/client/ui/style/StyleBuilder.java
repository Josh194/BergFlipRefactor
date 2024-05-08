package client.ui.style;

public class StyleBuilder extends Style {
	private Style[] styles;

	public StyleBuilder(Style[] styles) {
		this.styles = styles;
	}

	public void apply(Object... elements) throws InvalidElementTypeException {
		for (Style style : styles) {
			style.apply(elements);
		}
	}
}