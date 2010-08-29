package org.mst.acm.soda;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Login activity for the soda app
 * 
 * @author nathan
 * 
 */
public class Login extends Activity
{

	// set here so we can cancel it
	private LoginTask loginTask;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.login);

		((Button) findViewById(R.id.ButtonLogin))
				.setOnClickListener(new OnClickListener()
				{

					public void onClick(View v)
					{
						loginTask = (LoginTask) new LoginTask().execute();
					}

				});
	}

	@Override
	protected void onDestroy()
	{
		if (loginTask != null)
		{
			loginTask.cancel(true);
		}

		super.onDestroy();
	}

	private class LoginTask extends AsyncTask<Void, Void, Boolean>
	{
		private String token;

		@Override
		protected void onPreExecute()
		{
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Boolean doInBackground(Void... params)
		{
			// TODO: Login rest call
			return true;
		}

		@Override
		protected void onPostExecute(Boolean success)
		{
			setProgressBarIndeterminateVisibility(false);

			if (success)
			{
				Intent result = new Intent();
				result.putExtra(Soda.TOKEN, token);
				setResult(Activity.RESULT_OK, result);
				finish();
			}
			else
			{
				// Display a message to the user
				Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG);
			}
		}
	}

}
