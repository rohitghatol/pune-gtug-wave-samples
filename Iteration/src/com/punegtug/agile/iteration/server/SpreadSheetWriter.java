/**
 * 
 */
package com.punegtug.agile.iteration.server;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Owner
 * 
 */
public class SpreadSheetWriter {

	private static final String KEY = "key=";
	private String key = null;
	private String userName = null;
	private String password = null;
	private String iterationNo = null;
	private SpreadsheetService service = null;
	private String workSheetId;

	public SpreadSheetWriter(String uriString, String userName,
			String password, String iterationNo) throws AuthenticationException {
		this.iterationNo=iterationNo;
		URI uri = URI.create(uriString);
		int startIndex = uri.getQuery().indexOf(KEY) + KEY.length();
		int endIndex = uri.getQuery().indexOf("&", startIndex);
		key = uri.getQuery().substring(startIndex, endIndex);
		service = new SpreadsheetService("Iteration-Wave");
		service.setUserCredentials(userName, password);

	}

	public void setTemplate(int members, int days) throws MalformedURLException, IOException, ServiceException {
		ListEntry newEntry = new ListEntry();
		String nameValuePairs = "Rosa=T1,AA=2";
		// Split first by the commas between the different fields.
		for (String nameValuePair : nameValuePairs.split(",")) {
		  // Then, split by the equal sign.
		  String[] parts = nameValuePair.split("=", 2);
		  String tag = parts[0]; // such as "name"
		  String value = parts[1]; // such as "Rosa"
		  newEntry.getCustomElements().setValueLocal(tag, value);
		}
		
		ListEntry insertedRow = service.insert(getListURL(), newEntry);
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	private String getWorkSheetGETURL() {
		return "http://spreadsheets.google.com/feeds/worksheets/" + key
				+ "/private/full";
	}
	
	private URL getListURL() throws MalformedURLException{
		return new URL("http://spreadsheets.google.com/feeds/list/"+key+"/"+iterationNo+"/private/full");
	}

	private SpreadsheetEntry getSpreadSheetEntry() throws IOException,
			ServiceException {
		URL metafeedUrl = new URL(
				"http://spreadsheets.google.com/feeds/spreadsheets/private/full");
		SpreadsheetFeed feed = service.getFeed(metafeedUrl,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		for (int i = 0; i < spreadsheets.size(); i++) {
			SpreadsheetEntry entry = spreadsheets.get(i);
			if (key.equals(entry.getKey())) {
				return entry;
			}
		}
		return null;
	}

	public void createWorkSheet() throws IOException, ServiceException {
		List<WorksheetEntry> worksheets = getSpreadSheetEntry().getWorksheets();
		boolean alreadyCreated = false;
		for (int i = 0; i < worksheets.size(); i++) {
			WorksheetEntry worksheet = worksheets.get(i);
			System.out.println(iterationNo + "  "
					+ worksheet.getTitle().getPlainText());
			if (worksheet.getTitle()!=null && iterationNo.equals(worksheet.getTitle().getPlainText())) {
				alreadyCreated = true;
				workSheetId=worksheet.getId();
			}
		}

		if (!alreadyCreated) {
			WorksheetEntry worksheet = new WorksheetEntry();
			worksheet.setTitle(new PlainTextConstruct(iterationNo));
			worksheet.setRowCount(200);
			worksheet.setColCount(100);

			URL worksheetFeedUrl = getSpreadSheetEntry().getWorksheetFeedUrl();
			service.insert(worksheetFeedUrl, worksheet);
			workSheetId=worksheet.getId();
		}
	}

	public static void main(String[] args) {
		SpreadSheetWriter writer;
		try {
			writer = new SpreadSheetWriter(
					"http://spreadsheets.google.com/ccc?key=tglpgaJYVk2gVvx3Ih5JZdQ&hl=en_GB",
					"rohitsghatol@gmail.com", "Peace4rohit", "1");

			writer.createWorkSheet();
			writer.setTemplate(1, 1);

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
