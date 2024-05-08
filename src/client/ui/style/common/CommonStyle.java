package client.ui.style.common;

import client.ui.style.Style;
import client.ui.style.StyleBuilder;

public enum CommonStyle {
	CENTERED_LABEL (new CenteredLabel()),
	DEFAULT_FONT   (new DefaultFont()),
	TITLE_FONT     (new TitleFont()),
	DEFAULT_LABEL  (new StyleBuilder(new Style[] {new client.ui.style.common.DefaultFont(), new client.ui.style.common.CenteredLabel()})),
	LARGE_LABEL    (new StyleBuilder(new Style[] {new client.ui.style.common.LargeFont(), new client.ui.style.common.CenteredLabel()})),
	TITLE_LABEL    (new StyleBuilder(new Style[] {new client.ui.style.common.TitleFont(), new client.ui.style.common.CenteredLabel()}));
	
	// would be nice to just be able to extend from StyleBuilder
	private CommonStyle(Style style) {
		this.style = style;
	}

	public final Style style;
}
