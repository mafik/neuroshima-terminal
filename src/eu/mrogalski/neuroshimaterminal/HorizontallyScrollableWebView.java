package eu.mrogalski.neuroshimaterminal;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class HorizontallyScrollableWebView extends WebView {
	public HorizontallyScrollableWebView(final Context context) {
		super(context);
	}

	public HorizontallyScrollableWebView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean canScrollHor(final int direction) {
		final int offset = computeHorizontalScrollOffset();
		final int range = computeHorizontalScrollRange()
				- computeHorizontalScrollExtent();
		if (range == 0)
			return false;
		if (direction < 0) {
			return offset > 0;
		} else {
			return offset < range - 1;
		}
	}
}
