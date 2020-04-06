/*
Copyright 2020 Mohammad A. Rahin                                                                                                          

Licensed under the Apache License, Version 2.0 (the "License");                                                                           
you may not use this file except in compliance with the License.                                                                          
You may obtain a copy of the License at                                                                                                   
    http://www.apache.org/licenses/LICENSE-2.0                                                                                            
Unless required by applicable law or agreed to in writing, software                                                                       
distributed under the License is distributed on an "AS IS" BASIS,                                                                         
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.                                                                  
See the License for the specific language governing permissions and                                                                       
limitations under the License.       
*/

package net.dollmar.web.apiworkbench.pages;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.dollmar.web.apiworkbench.dao.ApiDefDao;
import net.dollmar.web.apiworkbench.entity.ApiDef;
import net.dollmar.web.apiworkbench.utils.Utils;

public class APIDefsViewPage {


	public String render(Map<String, String[]> qm) {
	  ApiDefDao apiDao = new ApiDefDao();

		return buildHtmlTableForApiDefs("List of registered API definitions", apiDao.getAllApiDefs());
	}


	@SuppressWarnings("unchecked")
	private String buildHtmlTableForApiDefs(final String title, final Collection<ApiDef> apps) {

		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<h3>Number of registered API definitions = %d</h3>", apps.size()));

		sb.append("<input type='text' id='apiName' name='search_input' onkeyup='searchApiDef()' placeholder='Search for API defnitions ...'>");

		sb.append("<table id ='apisTable' class='sortable' border='1'>");
		sb.append("<thead><tr><th>Id</th>");
		sb.append("<th>API Name</th>");
		sb.append("<th>API Description</th>");
		sb.append("<th>Version</th>");
		sb.append("<th> </th>");
		sb.append("<th> </th>");
		sb.append("<th> </th>");
		sb.append("</tr></thead>");

		if (apps != null) {
			// sort the collection for better presentation
			List<ApiDef> apiList = Utils.sort(apps);
			sb.append("<tbody>");
			for (ApiDef api : apiList) {
				String aid = "" + api.getId();
				sb.append("<tr><td>").append(aid).append("</td>");
				sb.append("<td>").append(api.getApiName()).append("</td>");
        sb.append("<td>").append(api.getApiDesc()).append("</td>");
				sb.append("<td>").append(api.getVersion()).append("</td>");
				sb.append("<td>").append(String.format("<a href='/swagger-editor/?url=/APIWorkbench/apis/%s' target='ifrm'>Edit", api.getFileName())).append("</td>");
        sb.append("<td>").append(String.format("<a href='/swagger-ui/?url=/APIWorkbench/apis/%s' target='ifrm'>Test", api.getFileName())).append("</td>");
        sb.append("<td>").append("Delete").append("</td>");
				sb.append("</tr>");
				
				
			}
			sb.append("</tbody>");
		}

		sb.append("</table>");
		sb.append("<br>");

		return Utils.buildStyledHtmlPage(title, sb.toString());
	}


//	private String createLinkForPopup(String rowId) {
//		String url = String.format("%s?appId=%s", Main.EDIT_PATH, rowId);
//
//		StringBuilder sb = new StringBuilder();
//		sb.append(String.format("<a href=\"%s\" ", url)).append("target=\"popup\" ");
//		sb.append(
//				String.format("onclick=\"window.open('%s', 'popup', 'width=1000, height=600'); return false;\">", url));
//		sb.append(rowId).append("</a>");
//		return sb.toString();
//	}

}
