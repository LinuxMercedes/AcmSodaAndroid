package org.mst.acm.soda;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * ArrayAdapter to wire SodaCans to the GridView
 * @author nathan
 *
 */
public class SodaCanAdapter extends ArrayAdapter<SodaCan>
{
	private LayoutInflater li;
	private int layoutId;
	private iSodaClick m_cb; //callback for OnClickListener

	public SodaCanAdapter(Context context, int textViewResourceId,
			List<SodaCan> objects, iSodaClick cb)
	{
		super(context, textViewResourceId, objects);
		layoutId = textViewResourceId;
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_cb = cb;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		convertView = (convertView == null) ? li.inflate(layoutId, null)
				: convertView;

		//Sets the text for the depth and name
		((TextView) convertView.findViewById(R.id.TextViewName)).setText(getItem(
				position).name());
		((TextView) convertView.findViewById(R.id.TextViewDepth)).setText(String
				.valueOf(getItem(position).depth()));
		
		//Sets the button enabled/disabled and the onclicklistener
		Button b;
		(b = (Button) convertView.findViewById(R.id.ButtonBuy)).setEnabled((getItem(
				position).depth() > 0) ? true : false);
		
		b.setOnClickListener(new OnClickListener(){

			public void onClick(View v)
			{
				m_cb.sodaClick(v.getContext(), position);
			}
			
		});
		
		
		return convertView;
	}
}
