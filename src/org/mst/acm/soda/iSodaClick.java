package org.mst.acm.soda;

import android.content.Context;

/**
 * Interface to pass clicks back to the activity from a list adapter
 * @author nathan
 *
 */
public interface iSodaClick
{
	public void sodaClick(Context ctx, int position);
}
