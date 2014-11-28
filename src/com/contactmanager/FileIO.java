package com.contactmanager;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Program of Phonebook for an Android device that allows user to manage contacts using add, Edit, delete and view.
 * Purpose : Class Assignment
 * FileIO Class uses 'IO' package to perform technical operation like Delete, Edit, View, Add
 */



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.os.Environment;

public class FileIO {
	private static String contactFileName = "ContactRecords.txt";
	static File contactFile;

	/* 
	 * readFromFile Function will read each record from file and return every record to display in View
	 * @return StringBuilder : It will read all the lines delimited by \n
	 * @created by Abhishek Bansal
	 */
	
	public static StringBuilder readFromFile() throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			String line;
			contactFile = new File(Environment.getExternalStorageDirectory(),
					"ContactFolder/" + contactFileName);
			if (!contactFile.exists())
				return null;
			reader = new BufferedReader(new FileReader(new File(
					Environment.getExternalStorageDirectory(), "ContactFolder/"
							+ contactFileName)));
			while ((line = reader.readLine()) != null) {
				builder.append(line + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return builder;
	}
	
	/* 
	 * writeToFile Function will write the new record to file or update and return 1 for Success and return 0 for Failure
	 * @return int : 1 for Success and 0 for Failure	 
	 * @created by Ankit Tolia
	 */

	public static int writeToFile(boolean updateMode, String fnameOld,
			String lnameOld, String phoneOld, String emailOld, String fnameNew,
			String lnameNew, String phoneNew, String emailNew)
			throws IOException {
		try {
			File newFolder = new File(
					Environment.getExternalStorageDirectory(), "ContactFolder");
			FileWriter contactWriter = null;
			BufferedWriter bw = null;

			if (updateMode) {
				String bldSplitLine[] = null;

				StringBuilder bldRead = readFromFile(); // Read records from
														// file
				StringBuilder bldUpdatedFile = new StringBuilder();
				if (bldRead != null)
					bldSplitLine = bldRead.toString().split(
							System.lineSeparator());
				else
					return 0;

				for (int i = 1; i < bldSplitLine.length; i++) {
					String bldSplitTab[] = bldSplitLine[i].split("\t");
					String filefn = "", fileln = "", filephone = "", filemail = "";

					for (int k = 0; k < bldSplitTab.length; k++) {
						if (k == 0)
							filefn = bldSplitTab[0];
						else if (k == 1)
							fileln = bldSplitTab[1];
						else if (k == 2)
							filephone = bldSplitTab[2];
						else
							filemail = bldSplitTab[3];
					}

					/* Load the updated record into a string builder */
					if (fnameOld.equalsIgnoreCase(filefn)
							&& lnameOld.equalsIgnoreCase(fileln)
							&& phoneOld.equalsIgnoreCase(filephone)
							&& emailOld.equalsIgnoreCase(filemail))
						bldUpdatedFile.append(fnameNew + "\t" + lnameNew + "\t"
								+ phoneNew + "\t" + emailNew
								+ System.lineSeparator());
					else
						/*
						 * if the record doesn't match with the file, load it
						 * into builder.
						 */
						bldUpdatedFile.append(filefn + "\t" + fileln + "\t"
								+ filephone + "\t" + filemail
								+ System.lineSeparator());
				}

				contactFile = new File(newFolder, contactFileName);
				contactWriter = new FileWriter(contactFile, false);
				bw = new BufferedWriter(contactWriter);

				bw.write("First Name" + "\t" + "Last Name" + "\t" + "Phone"
						+ "\t" + "Email" + System.lineSeparator());
				bw.write(bldUpdatedFile.toString());
				bw.flush();
				bw.close();

				return 1;
			}

			boolean bExists = true;
			if (!newFolder.exists())
				bExists = newFolder.mkdir();
			if (!bExists)
				return 0;
			contactFile = new File(newFolder, contactFileName);

			if (!contactFile.exists()) {
				contactFile.createNewFile();
				if (!contactFile.canWrite())
					return 0;
				contactWriter = new FileWriter(contactFile, true);
				bw = new BufferedWriter(contactWriter);
				bw.write("First Name" + "\t" + "Last Name" + "\t" + "Phone"
						+ "\t" + "Email" + System.lineSeparator());
			}
			if (contactWriter == null)
				contactWriter = new FileWriter(contactFile, true);
			if (bw == null)
				bw = new BufferedWriter(contactWriter);

			bw.write(fnameNew + "\t" + lnameNew + "\t" + phoneNew + "\t"
					+ emailNew + System.lineSeparator());

			bw.flush();
			bw.close();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/* 
	 * delAllContact Function will delete all the contacts
	 * @return int : 1 for Success and 0 for Failure
	 * @created by Abhishek Bansal
	 */

	
	public static int delAllContact() throws IOException {
		File newFolder = new File(Environment.getExternalStorageDirectory(),
				"ContactFolder");
		contactFile = new File(newFolder, contactFileName);
		if (contactFile.exists())
			contactFile.delete();
		else
			return 0;
		return 1;
	}


	/* 
	 * removeFromFile Function will delete the selected contact
	 * @param String FnameOld,lnameOld,phoneOld,emailOld details of the contact which is to be deleted
	 * @return int : 1 for Success and 0 for Failure
	 * @created by Ankit Tolia
	 */

	
	public static int removeFromFile(String fnameOld, String lnameOld,
			String phoneOld, String emailOld) throws IOException {
		File newFolder = new File(Environment.getExternalStorageDirectory(),
				"ContactFolder");
		contactFile = new File(newFolder, contactFileName);

		StringBuilder bldRead = readFromFile();
		/*
		 * Create a temporary file that will contain the records not to be
		 * deleted
		 */
		File tempFile = new File(newFolder, "ContactTempFile.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String bldSplitLine[] = bldRead.toString()
				.split(System.lineSeparator());
		for (int i = 0; i < bldSplitLine.length; i++) {
			String bldSplitTab[] = bldSplitLine[i].split("\t");
			/*
			 * If the record to be deleted matches, don't write it into
			 * temporary file
			 */
			String filefn = "", fileln = "", filephone = "", filemail = "";

			for (int k = 0; k < bldSplitTab.length; k++) {
				if (k == 0)
					filefn = bldSplitTab[0];
				else if (k == 1)
					fileln = bldSplitTab[1];
				else if (k == 2)
					filephone = bldSplitTab[2];
				else
					filemail = bldSplitTab[3];
			}

			if (fnameOld.equalsIgnoreCase(filefn)
					&& lnameOld.equalsIgnoreCase(fileln)
					&& phoneOld.equalsIgnoreCase(filephone)
					&& emailOld.equalsIgnoreCase(filemail))
				continue;
			writer.write(bldSplitLine[i] + System.lineSeparator());
		}
		writer.close();
		contactFile.delete(); // delete the original file
		/* rename the temporary file to original file name */
		boolean success = tempFile.renameTo(contactFile);
		if (success) {
			return 1;
		} else
			return 0;
	}

}
