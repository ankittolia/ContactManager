package com.contactmanager;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Activity that opens the contact in view mode and allows the user to edit and delete contact.
 * Purpose : Class Assignment
 * @created by Abhishek Bansal
 */

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ContactOpen extends ActionBarActivity {
	String fname = "", lname = "", phone = "", email = "";
	EditText etFname = null, etLname = null, etPhone = null, etEmail = null;
	TextView txtLname = null, txtEmail = null, txtPhone = null;
	ProgressBar prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Initialize View
		setContentView(R.layout.viewcontact);
		this.setTitle(getResources().getString(R.string.viewContact));
		etFname = (EditText) findViewById(R.id.fnameOpen);
		etLname = (EditText) findViewById(R.id.lnameOpen);
		etPhone = (EditText) findViewById(R.id.phoneOpen);
		etEmail = (EditText) findViewById(R.id.emailOpen);
		txtLname = (TextView) findViewById(R.id.txtLname);
		txtPhone = (TextView) findViewById(R.id.txtPhone);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		prog = (ProgressBar) findViewById(R.id.progressBar);

		if (getIntent().getExtras() != null) {
			// Check for Contact Data
			fname = getIntent().getExtras().get("fname").toString();
			if (getIntent().getExtras().get("lname") != null)
				lname = getIntent().getExtras().get("lname").toString();
			if (getIntent().getExtras().get("phone") != null)
				phone = getIntent().getExtras().get("phone").toString();
			if (getIntent().getExtras().get("email") != null)
				email = getIntent().getExtras().get("email").toString();
		}

		etFname.setText(fname);
		if (!lname.isEmpty())
			etLname.setText(lname);
		else {
			etLname.setVisibility(View.GONE);
			txtLname.setVisibility(View.GONE);
		}
		if (!phone.isEmpty())
			etPhone.setText(phone);
		else {
			etPhone.setVisibility(View.GONE);
			txtPhone.setVisibility(View.GONE);
		}
		if (!email.isEmpty())
			etEmail.setText(email);
		else {
			etEmail.setVisibility(View.GONE);
			txtEmail.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contactopen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.itemEdtContact:
			Intent intent = new Intent(ContactOpen.this, AddContact.class); // Invoke the add contact class on clicking edit contact
			intent.putExtra("fname", fname);
			if (!lname.isEmpty())
				intent.putExtra("lname", lname);
			if (!phone.isEmpty())
				intent.putExtra("phone", phone);
			if (!email.isEmpty())
				intent.putExtra("email", email);

			startActivityForResult(intent, 0);
			break;
		case R.id.itemDelContact://Confirm Deletion of Contact
			new AlertDialog.Builder(this)
					.setMessage("Are you sure you want to delete the contact?")
					.setCancelable(true)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									new DeleteTask().execute(); 
								}
							}).setNegativeButton("No", null).show();

			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	// A Separate Process that performs the deletion of contact
	class DeleteTask extends AsyncTask<Void, Void, Integer> {
		int removeChk;

		protected void onPreExecute() {
			if (prog != null)
				prog.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				removeChk = FileIO.removeFromFile(fname, lname, phone, email); // Invokes the remove from file function to remove the contact
			} catch (IOException e) {
				// TODO Auto-generated catch
				// block
				e.printStackTrace();
			}
			return removeChk;
		}

		@Override
		protected void onPostExecute(Integer params) {
			if (prog != null && prog.isShown())
				prog.setVisibility(View.GONE);
			if (removeChk == 1) {
				Toast.makeText(ContactOpen.this,
						getResources().getString(R.string.delSuccess),
						Toast.LENGTH_LONG).show();
				ContactOpen.this.finish();
			} else
				Toast.makeText(ContactOpen.this,
						getResources().getString(R.string.delFailed),
						Toast.LENGTH_LONG).show();
		}

	}
	
	// Function that finishes the current activity and calls the finish to parent activity. Input: request and result code, and data
	// Finish the parent activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0) {
			finish();
		}
	}

}
