package client.ui.style;

public abstract class Style {
	public class InvalidElementTypeException extends Exception {}

	// TODO: find a way to static this, or something similar
	public abstract void apply(Object... elements) throws InvalidElementTypeException;
}
