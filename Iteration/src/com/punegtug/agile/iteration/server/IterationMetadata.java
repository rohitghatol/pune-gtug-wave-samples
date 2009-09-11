package com.punegtug.agile.iteration.server;

import com.google.wave.api.RobotMessageBundle;
import com.google.wave.api.Wavelet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IterationMetadata {

  // Delimeter and separator constants.
  private static final String LINE_SEP_REGEX = "\\n";
  private static final String FIELD_SEP_REGEX = "\\:";

  private static final String METADATA_DOC = "iteration-metadata";
  private static final String METADATA_FIELD_SEP = "|";
  private static final String METADATA_FIELD_SEP_REGEX = "\\|";

  private static final String NEWIDS_DOC = "_new_ids_";
  private static final String NEWIDS_ITERATION_BLIP_PREFIX = "iteration-blip-";
  
  private static final String ITERATION_DOC = "iteration_doc";

  
  private String iterationName = "";
  
  
  private String startDate = "";

  
  private String duration = "";

  private String recipients = "";
  
  private String owner="";
  
  private String spreadsheetURL="";
  private String userName = "";
  private String password = "";
  
  /**
   * A map of recipients to PollWavelets.
   */
  private Map<String, String> iterationWaveletBlipIds = new HashMap<String, String>();
  
  /**
   * A map of recipients to their votes. 
   */
  private Map<String, String> iterationProgress = new HashMap<String, String>();
  


  /**
   * The serialized form of the data document to compare against to determine
   * changes. 
   */
  private String dataDocument = "";
  
  /**
   * The current robot context.
   */
  private RobotMessageBundle context;
  
  /**
   * Whether any of the poll metadata has changed (and should be saved).
   */
  private boolean hasChanged = true;
  

  /**
   * Constructs the previous state of the Admin or Poll wavelet from metadata
   * stored in a data document. 
   * 
   * @param context the context the robot is currently called with.
   */
  public IterationMetadata(RobotMessageBundle context) {
    this.context = context;
    loadMetadata();
    processNewIds();

  }

  /**
   * Deserializes the previous state of the poll metadata from the data stored
   * in the data document.
   */
  private void loadMetadata() {
    this.dataDocument = context.getWavelet().getDataDocument(METADATA_DOC);
    if (dataDocument != null) {
      try {
        String[] items = dataDocument.split(METADATA_FIELD_SEP_REGEX);

        this.iterationName = items[0];
        this.startDate = items[1];
        this.duration = items[2];
        this.recipients = items[3];

      } catch(IndexOutOfBoundsException iobx) {
        // Receiving an exception means that the data document has not yet
        // been fully populated.
      }
      hasChanged = false;
    }
  }
  
  /**
   * Saves the poll metadata as a data document in the robot context's wavelet.
   */
  public void saveMetadata() {
    saveMetadata(context.getWavelet());
  }
  
  /**
   * Serializes the state of the poll metadata to the given wavelet. If the
   * new data document happens to be unchanged (ie. a person voted with the
   * same vote twice), the data document is not saved.
   * 
   * @param wavelet the wavelet in which to persist the data document.
   */
  public void saveMetadata(Wavelet wavelet) {
    
    StringBuilder newDataDocument = new StringBuilder();
    
    newDataDocument.append(iterationName);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(startDate);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(duration);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(recipients);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(owner);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(spreadsheetURL);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(userName);
    newDataDocument.append(METADATA_FIELD_SEP);
    newDataDocument.append(password);
    newDataDocument.append(METADATA_FIELD_SEP);
    
    if (!newDataDocument.toString().equals(dataDocument)) {
      wavelet.setDataDocument(METADATA_DOC, newDataDocument.toString());
    }
  }

  /**
   * Checks for the presence of the _new_ids_ data document and if present,
   * processes and stores the newly created poll wavelet ids.
   */
  private void processNewIds() {
    String newIds = context.getWavelet().getDataDocument(NEWIDS_DOC);
    if (newIds != null && !newIds.isEmpty()) {
      try {
        for (String line : newIds.split(LINE_SEP_REGEX)) {
          if (line.startsWith(NEWIDS_ITERATION_BLIP_PREFIX)) {
            String[] pollBlip = line.split(FIELD_SEP_REGEX);
            String participant = pollBlip[0].substring(NEWIDS_ITERATION_BLIP_PREFIX.length());
            String participantPollBlipId = pollBlip[1];
            iterationWaveletBlipIds.put(participant, participantPollBlipId);
            hasChanged = true;
          }
        }
      } catch(IndexOutOfBoundsException iobx) {
        // Receiving an exception means that the data document has not yet
        // been populated.
      }
      context.getWavelet().setDataDocument(NEWIDS_DOC, "");
    }
  }


  /**
   * Returns whether the metadata has changed. This can be caused by the
   * addition of new polls or votes, or the updating of metadata by changes in
   * the AdminWavelet.
   * 
   * @return true if the metadata has changed, false otherwise.
   */
  public boolean hasChanged() {
    return hasChanged;
  }
  public String getIterationName() {
    return iterationName;
  }

  public String getStartDate() {
    return startDate;
  }
  public String getDuration() {
	    return duration;
	  }


  public String getRecipients() {
    return recipients;
  }

  public String getOwner(){
	  return owner;
	  
  }
  
  public String getSpreadSheetURL(){
	  return spreadsheetURL;
  }
  
  public String getUserName(){
	  return userName;
  }
  public String getPassword(){
	  return password;
  }
  /**
   * Converts the comma separated list of recipients into a list.
   * 
   * @return a list of recipients.
   */
  public List<String> getRecipientsAsList() {
    List<String> recipientList = new ArrayList<String>();
    if (!recipients.isEmpty()) {
      for (String recipient : recipients.split(",")) {
        recipientList.add(recipient.trim());
      }
    }
    return recipientList;
  }


  
  public void setIterationName(String iterationName) {
    this.iterationName = iterationName;
    hasChanged = true;
  }
  
  
  public void setStartDate(String startDate) {
    this.startDate = startDate;
    hasChanged = true;
  }
  
  public void setDuration(String duration) {
	    this.duration= duration;
	    hasChanged = true;
	  }
  
  /**
   * Sets the list of recipients.
   * 
   * @param recipients the new list of recipients.
   */
  public void setRecipients(String recipients) {
    this.recipients = recipients;
    hasChanged = true;
  }

  public void setOwner(String owner){
	  this.owner=owner;
	  hasChanged=true;
  }

  public void setSpreadSheetURL(String spreadsheetURL){
	  this.spreadsheetURL=spreadsheetURL;
  }
  public void setUserName(String userName){
	  this.userName=userName;
  }
  
  public void setPassword(String password){
	  this.password=password;
  }

  
}
