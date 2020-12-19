package eu.mrogalski.neuroshimaterminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import eu.mrogalski.neuroshimaterminal.R;

public class ShopFragment extends SherlockFragment {
	private Context mContext;
	private LayoutParams mWideParams;
	private LinearLayout.LayoutParams mTallParams;
	private LinearLayout mColumns;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = inflater.getContext();
		mWideParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mTallParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		mTallParams.weight = 1f;
		View root = inflater.inflate(R.layout.shop, container, false);
		mColumns = (LinearLayout) root.findViewById(R.id.columns);
		createColumns(root);
		try {
			readJSON();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return root;
	}

	private void createColumns(View root) {
		
	    Display display = getActivity().getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = getResources().getDisplayMetrics().density;
	    float dpWidth  = outMetrics.widthPixels / density;
		
        int columnCount = getColumnCount(dpWidth);
        
		while(columnCount-->0) {
			LinearLayout column = new LinearLayout(mContext);
			column.setLayoutParams(mTallParams);
			column.setOrientation(LinearLayout.VERTICAL);
			mColumns.addView(column);
		}
	}

	private int getColumnCount(float dpWidth) {
		if(dpWidth < 320) {
			return 1;
		} else if(dpWidth <= 480) {
			return 2;
		} else if(dpWidth <= 800) {
			return 3;
		} else if(dpWidth <= 960) {
			return 4;
		} else if(dpWidth <= 1280) {
			return 5;
		}
		return (int) (dpWidth / 240);
	}

	private void readJSON() throws JSONException, IOException {
		InputStream in = getResources().getAssets().open("shop.json");
		StringBuilder builder = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while((line = br.readLine()) != null) {
			builder.append(line);
		}
		JSONObject categoriesJSON = new JSONObject(builder.toString());
		ArrayList<View> shopCategories = null;
		int columnCount = mColumns.getChildCount();
		
	    Display display = getActivity().getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    int columnWidth  = outMetrics.widthPixels / columnCount;
		
		shopCategories = readShopCategories(categoriesJSON);
		for (View v : shopCategories) {
			v.measure(View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY),
					View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		}
		
		Collections.sort(shopCategories, new Comparator<View>() {

			@Override
			public int compare(View lhs, View rhs) {
				return rhs.getMeasuredHeight() - lhs.getMeasuredHeight();
			}
		});

		for (View v : shopCategories) {
			LinearLayout column = (LinearLayout) mColumns.getChildAt(0); 
			for(int i=1; i<columnCount; ++i) {
				LinearLayout nextColumn = (LinearLayout) mColumns.getChildAt(i);
				if(nextColumn.getMeasuredHeight() < column.getMeasuredHeight()) {
					column = nextColumn;
				}
			}
			
			column.addView(v, 0);
			column.measure(View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY),
					View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		}
	}

	private ArrayList<View> readShopCategories(JSONObject categoriesJSON) throws IOException, JSONException {
		ArrayList<View> shopCategories = new ArrayList<View>();
		
		@SuppressWarnings("unchecked")
		Iterator<String> it = categoriesJSON.keys();
		while (it.hasNext()) {

			String name = it.next();

			TextView title = new TextView(mContext, null,
					android.R.attr.textAppearanceLarge);
			title.setText(name);
			title.setPadding(5, 5, 5, 5);

			
			View itemList = readItemList(categoriesJSON.getJSONObject(name));
			itemList.setPadding(10, 10, 10, 10);

			LinearLayout container = new LinearLayout(mContext);

			container.addView(title);
			container.addView(itemList);
			container.setOrientation(LinearLayout.VERTICAL);

			shopCategories.add(container);

		}
		return shopCategories;
	}

	private View readItemList(JSONObject itemsJSON) throws IOException, JSONException {
		LinearLayout list = new LinearLayout(mContext);
		list.setLayoutParams(mWideParams);
		list.setOrientation(LinearLayout.VERTICAL);
		
		@SuppressWarnings("unchecked")
		Iterator<String> it = itemsJSON.keys();
		while (it.hasNext()) {

			String name = it.next();
			int value = itemsJSON.getInt(name);

			TextView nameText = new TextView(mContext);
			nameText.setText(name);

			TextView valueText = new TextView(mContext);
			valueText.setText(String.valueOf(value));
			valueText.setGravity(Gravity.RIGHT);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
					LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.RIGHT;
			params.weight = 1f;

			LinearLayout item = new LinearLayout(mContext);
			item.addView(nameText, params);
			item.addView(valueText);

			list.addView(item);

		}
		return list;
	}
}
