package org.mst.acm.soda;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Soda activity
 * @author nathan
 * 
 * Implements most of the functionality of the soda order machine.
 */
public class Soda extends Activity implements iSodaClick
{
	//Constants for communicating with Login
	public static final String TOKEN = "token";
	public static final int LOGIN = 1;

	//List of sodas to vend
	private GridView sodaGrid;
	private ArrayList<SodaCan> sodas = new ArrayList<SodaCan>();
	private SodaCanAdapter sodaAdapter;
	
	private Button logoutButton;

	//Asynctasks. Variables created to let us cancel stuff in onDestroy()
	private GetSodasTask getSodas;
	private BuySodaTask buySoda;
	private LogoutTask logoutTask;
	
	private static final boolean STUB = true; //For testing purpoes only...removes check for token validity.
	private String token;
	private Intent loginIntent = new Intent(this, Login.class);
	
	private enum taskResult {SUCCESS, FAIL, AUTH_FAIL};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.main);
		
		//Need to log in
		startActivityForResult(loginIntent, LOGIN);
		
		//Wire up the listadapter to the soda gridview
		sodaGrid = (GridView) findViewById(R.id.gridViewSodas);
		sodaAdapter = new SodaCanAdapter(this, R.layout.soda_can, sodas, this);
		sodaGrid.setAdapter(sodaAdapter);
		
		//logout button listener
		logoutButton = (Button) findViewById(R.id.ButtonLogout);
		logoutButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v)
			{
				logoutTask = (LogoutTask) new LogoutTask().execute();
			}
			
		});

	}

	/**
	 * onResume() is called every time the activity comes to the front.
	 * Here we refresh the soda list, mostly for the fun of it.
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		getSodas = (GetSodasTask) new GetSodasTask().execute();
	}

	/**
	 * onDestroy() is called before the activity is destroyed.
	 * Here we cancel AsyncTasks to prevent null pointer references.
	 */
	@Override
	public void onDestroy()
	{
		if (getSodas != null)
		{
			getSodas.cancel(true);
		}

		if (buySoda != null)
		{
			buySoda.cancel(true);
		}
		
		if(logoutTask != null)
		{
			logoutTask.cancel(true);
		}
		
		super.onDestroy();
	}

	/**
	 * Called when Login returns.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode){
			case LOGIN: //Sanity check.
				if (resultCode == Activity.RESULT_OK) {
					token = data.getStringExtra(TOKEN);
				}
				break;
		}
	}
	
	/**
	 * Called from the onClickListeners set in SodaCanAdapter. 
	 * Lets us move all the business logic here instead of inside an adapter.
	 */
	public void sodaClick(Context ctx, int position){
		buySoda = (BuySodaTask) new BuySodaTask(ctx)
		.execute(sodas.get(position));
	}
	
	/**
	 * AsyncTask to get sodas
	 * @author nathan
	 * Gets sodas via REST request and repopulates the list adapter.
	 */
	private class GetSodasTask extends AsyncTask<Void, Void, Void>
	{
		//Created to prevent race conditions caused by modifying the data of the 
		//GridView on a different thread.
		private ArrayList<SodaCan> temp = new ArrayList<SodaCan>();

		@Override
		protected void onPreExecute()
		{
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... arg0)
		{
			temp.add(new SodaCan("Coke", 5));
			temp.add(new SodaCan("DP", 2));
			temp.add(new SodaCan("Crush", 8));
			temp.add(new SodaCan("Mountain Holler Radical Citrus Thirst Blaster",
					9001));
			return null;
		}

		@Override
		protected void onPostExecute(Void v)
		{
			//keeps sodas pointed at the same spot in memory so the adapter is happy.
			sodas.clear();
			sodas.addAll(temp);
			sodaAdapter.notifyDataSetInvalidated();
			
			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * AsyncTask to buy sodas
	 * @author nathan
	 * Uses a rest request to vend a particular soda.
	 */
	private class BuySodaTask extends AsyncTask<SodaCan, Void, taskResult>
	{

		private Context m_ctx;
		private ProgressDialog pd;

		public BuySodaTask(Context ctx)
		{
			m_ctx = ctx;
		}

		@Override
		protected void onPreExecute()
		{
			pd = CreateProgressDialog(m_ctx, R.string.buySodaMessage);
		}

		@Override
		protected taskResult doInBackground(SodaCan... params)
		{
			//if not logged in, we bail out.
			if(token == null && !STUB)
			{
				return taskResult.AUTH_FAIL;
			}
			
			SodaCan can = params[0]; //sanity check
			
			
			Log.d("Soda", can.name()+can.depth());
			return taskResult.SUCCESS;
		}

		@Override
		protected void onPostExecute(taskResult result)
		{
			pd.dismiss();

			switch (result)
			{
				case AUTH_FAIL:
					Toast.makeText(m_ctx, 5, Toast.LENGTH_SHORT);
					break;
				case FAIL:
					CreateErrorDialog(m_ctx, 5);
					break;
			}
		}
	}

	/**
	 * AsyncTask to log user out
	 * @author nathan
	 * TODO: implement!
	 */
	private class LogoutTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0)
		{
			// TODO logout rest request
			return null;
		}
		
	}
	
	/**
	 * Class to create a spinning progress dialog
	 * @param context 
	 * @param message
	 * @return
	 */
	private ProgressDialog CreateProgressDialog(Context context, int message)
	{
		ProgressDialog pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage(getString(message));
		pd.show();
		return pd;
	}

	/**
	 * Class to create an error dialog
	 * @param context
	 * @param message
	 * @return
	 */
	private AlertDialog CreateErrorDialog(Context context, int message)
	{
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setMessage(getString(message));
		ad.show();
		return ad;
	}
}
