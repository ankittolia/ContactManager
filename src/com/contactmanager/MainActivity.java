package com.contactmanager;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Main Activity for the phone book program that displays the list of contacts ad also allows the user to add a new contact and delete all contacts.
 * Purpose : Class Assignment
 * Created By: Ankit Tolia
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.contactmanager.adapter.CustomListViewAdapter;
import com.contactmanager.bean.ContactListBean;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	ListView contactListView; //list that displays the contacts
	List contactList = null;
	TextView errTxt = null, errTxtSec = null; //error text
	ProgressBar prog;
	CustomListViewAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// Initialize the view
		setContentView(R.layout.activity_main);
		contactListView = (ListView) findViewById(R.id.contactList);
		errTxt = (TextView) findViewById(R.id.errTxt);
		errTxtSec = (TextView) findViewById(R.id.errTxtSec);
		prog = (ProgressBar) findViewById(R.id.progressBar);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		invalidateOptionsMenu(); //refresh the action menu bar
		contactList = new ArrayList<ContactListBean>();
		int chkFile = populateLisView();

		if (chkFile == 0) {
			contactListView.setVisibility(View.GONE);
			errTxt.setVisibility(View.VISIBLE);
			errTxtSec.setVisibility(View.VISIBLE);
		} else {
			if (adapter != null) {
				adapter.clear();
			}
			// an adaptor that is registered to listview so that items in list are displayed and updated accordingly
			adapter = new CustomListViewAdapter(this, R.layout.list_item,
					contactList);
			contactListView.setAdapter(adapter);

			contactListView.setVisibility(View.VISIBLE);
			errTxt.setVisibility(View.GONE);
			errTxtSec.setVisibility(View.GONE);

			// on click of a list item we show the contact data
			contactListView
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							ContactListBean bean = (ContactListBean) contactList
									.get(position);
							Intent intent = new Intent(MainActivity.this,
									ContactOpen.class);
							intent.putExtra("fname", bean.getFirstName());
							if (bean.getLastName() != null)
								intent.putExtra("lname", bean.getLastName());
							if (bean.getPhone() != null)
								intent.putExtra("phone", bean.getPhone());
							if (bean.getEmail() != null)
								intent.putExtra("email", bean.getEmail());
							startActivity(intent);
						}
					});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// This function is called each time the menu is updated, so that it gets refreshed
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		try {
			// If the file doesn't exist we don't show the Delete All option in action bar menu.
			if (FileIO.readFromFile() == null)
				menu.findItem(R.id.delAllContact).setVisible(false);
			else {
				if (FileIO.readFromFile().toString()
						.split(System.lineSeparator()).length <= 1)
					menu.findItem(R.id.delAllContact).setVisible(false);
				else
					menu.findItem(R.id.delAllContact).setVisible(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.addContact:
			this.startActivity(new Intent(this, AddContact.class)); //call the AddContact activity
			break;

		case R.id.delAllContact:
			int chkdelAll;
			try {
				// Confirm the user regarding deleting all the contacts with an alert dialog
				new AlertDialog.Builder(this)
						.setMessage(
								"Are you sure you want to delete all the contacts?")
						.setCancelable(true)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										new DeleteAllTask().execute();
									}
								}).setNegativeButton("No", null).show();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	//Function that reads the contacts from file and populates the listview accordingly.
	// Input: File that stores contacts. Output: An arraylist of contacts to be displayed in listview
	protected int populateLisView() {
		StringBuilder bldRead = null;
		try {
			bldRead = FileIO.readFromFile(); // reads records from file
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return 0;
		}
		if (bldRead == null)
			return 0;
		String bldSplitLine[] = bldRead.toString()
				.split(System.lineSeparator());
		if (bldSplitLine.length <= 1)
			return 0;
		for (int i = 1; i < bldSplitLine.length; i++) {
			String bldSplitTab[] = bldSplitLine[i].split("\t");

			ContactListBean contactBean = new ContactListBean();
			for (int j = 0; j < bldSplitTab.length; j++) {
				if (j == 0)
					contactBean.setFirstName(bldSplitTab[0]);
				else if (j == 1) {
					if (bldSplitTab[1] != null)
						contactBean.setLastName(bldSplitTab[1]);
				} else if (j == 2) {
					if (bldSplitTab[2] != null)
						contactBean.setPhone(bldSplitTab[2]);
				} else {
					if (bldSplitTab[3] != null)
						contactBean.setEmail(bldSplitTab[3]);
				}
			}
			contactList.add(contactBean); //add contact records to arraylist
		}
		return 1;

	}

	// A separate process that performs the deletion of all contacts
	class DeleteAllTask extends AsyncTask<Void, Void, Integer> {
		int removeChk;
		// Called before the processing
		protected void onPreExecute() {
			if (prog != null)
				prog.setVisibility(View.VISIBLE); //show the progress bar
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				removeChk = FileIO.delAllContact(); // invokes the delete all contact operation
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
				prog.setVisibility(View.GONE); //hide the progress bar
			if (removeChk == 1) { //if successfully deleted all the contacts
				Toast.makeText(MainActivity.this,
						getResources().getString(R.string.delAllSuccess),
						Toast.LENGTH_LONG).show();
				if (adapter != null) {
					adapter.clear();
					errTxt.setVisibility(View.VISIBLE);
					errTxtSec.setVisibility(View.VISIBLE);
					invalidateOptionsMenu();
				}

			} else
				Toast.makeText(MainActivity.this,
						getResources().getString(R.string.delFailed),
						Toast.LENGTH_LONG).show();
		}

	}
}
