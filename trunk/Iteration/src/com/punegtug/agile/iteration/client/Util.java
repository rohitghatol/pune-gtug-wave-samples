/**
 * 
 */
package com.punegtug.agile.iteration.client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Owner
 *
 */
public class Util {

	public static List<String> getMemberList(String members) {
		List<String> membersList = new ArrayList<String>(); 
		int startIndex = 0;
		int lastIndex = -1;
		do {
			lastIndex = members.indexOf(",", startIndex);
			if (lastIndex > 0) {
				String member = members.substring(startIndex, lastIndex);
				membersList.add(member.trim());
				startIndex=lastIndex+1;
			}
			else if(startIndex>0 && lastIndex==-1){
				String member = members.substring(startIndex);
				membersList.add(member.trim());
			}
			

		} while (lastIndex > 0);

		if (membersList.size() == 0) {
			membersList.add(members.trim());
		}
		return membersList;
	}

}
