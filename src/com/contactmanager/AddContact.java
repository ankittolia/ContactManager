package com.contactmanager;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Add Contact Activity that performs the addition of contacts and updation of the same, depending upon the program mode.
 * Purpose : Class Assignment
 */

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AddContact extends ActionBarActivity implements View.OnClickListener {
	String fname = "", lname = "", phone = "", email = "";
	EditText etFname = null, etLname = null, etPhone = null, etEmail = null;
	Button btnAdd;
	boolean updateMode = false;
	ProgressBar prog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Initialize View Objects
		setContentView(R.layout.addcontact);
		
		this.setTitle(getResources().getString(R.string.addContactTitle));
		
		// Check if the Contact is to be updated, then updateMode is true
		if (getIntent().getExtras() != null) {
			updateMode = true;
			this.setTitle(getResources().getString(R.string.updContactTitle));
			fname = getIntent().getExtras().get("fname").toString();
			if (getIntent().getExtras().get("lname") != null)
				lname = getIntent().getExtras().get("lname").toString();
			if (getIntent().getExtras().get("phone") != null)
				phone = getIntent().getExtras().get("phone").toString();
			if (getIntent().getExtras().get("email") != null)
				email = getIntent().getExtras().get("email").toString();
		}
		etFname = (EditText) findViewById(R.id.fname);
		etLname = (EditText) findViewById(R.id.lname);
		etPhone = (EditText) findViewById(R.id.phone);
		etEmail = (EditText) findViewById(R.id.email);
		prog = (ProgressBar) findViewById(R.id.progressBar);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		
		// If Update Mode then set the text of edit text to already present contact data
		if (updateMode) {
			etFname.setText(fname);
			if (lname != null)
				etLname.setText(lname);
			if (phone != null)
				etPhone.setText(phone);
			if (email != null)
				etEmail.setText(email);

			btnAdd.setText(getResources().getString(R.string.updateContact));
		}
		btnAdd.setOnClickListener(this);
	}

	protected void onStop() {
		// Finish the current and parent activity and display the list on stop of activity
		setResult(0);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// Finish the current and parent activity and display the list on stop of activity
		setResult(0);
		super.onDestroy();
	}

	// Called when the add/update button is clicked
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnAdd:
			// Check if first name is entered
			if (etFname.getText().toString().trim().isEmpty()) {
				Toast.makeText(v.getContext(),
						getResources().getString(R.string.fnTxt),
						Toast.LENGTH_LONG).show();
				etFname.requestFocus();
				InputMethodManager imm = (InputMethodManager) AddContact.this //hide the soft keyboard
						.getSystemService(AddContact.this.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etFname.getWindowToken(), 0);

			} else
				new WriteTask().execute(); // Perform the operation to add/update contact in a separate process
			break;
		default:
			break;
		}
	}

	// A separate process that invokes the write/update operation to file
	class WriteTask extends AsyncTask<Void, Void, Integer> {
		int chkWrite;
		//before process execution
		protected void onPreExecute() {
			if (prog != null)
				prog.setVisibility(View.VISIBLE);// display the progress bar
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				if (updateMode)
					chkWrite = FileIO.writeToFile(updateMode, fname, lname,
							phone, email, etFname.getText().toString().trim(),
							etLname.getText().toString().trim(), etPhone
									.getText().toString().trim(), etEmail
									.getText().toString().trim()); // invoke the update operation to file 
				else
					chkWrite = FileIO.writeToFile(updateMode, fname, lname,
							phone, email, etFname.getText().toString().trim(),
							etLname.getText().toString().trim(), etPhone
									.getText().toString().trim(), etEmail
									.getText().toString().trim()); //invoke the new write to file
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return chkWrite;
		}

		@Override
		protected void onPostExecute(Integer params) {
			if (prog != null && prog.isShown())
				prog.setVisibility(View.GONE); //hide the progress bar after the processing is over
			if (chkWrite == 1 && !updateMode) {
				Toast.makeText(AddContact.this,
						getResources().getString(R.string.addSuccess),
						Toast.LENGTH_LONG).show();
				AddContact.this.finish();
			} else if (chkWrite == 1 && updateMode) {
				Toast.makeText(AddContact.this,
						getResources().getString(R.string.updSuccess),
						Toast.LENGTH_LONG).show();
				AddContact.this.finish();
			} else
				Toast.makeText(AddContact.this,
						getResources().getString(R.string.addErr),
						Toast.LENGTH_LONG).show();
		}

	}

}
