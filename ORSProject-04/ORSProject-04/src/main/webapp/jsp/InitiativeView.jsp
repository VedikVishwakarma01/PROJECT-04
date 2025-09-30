<%@page import="in.co.rays.controller.InitiativeCtl"%>
<%@page import="in.co.rays.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@page import="in.co.rays.bean.InitiativeBean"%>
<%@page import="java.util.List"%>
<%@page import="in.co.rays.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Add Initiative</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<form action="<%=ORSView.INITIATIVE_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.bean.InitiativeBean"
			scope="request"></jsp:useBean>

		<%
			List<InitiativeBean> initiativeList = (List<InitiativeBean>) request.getAttribute("initiativeList");
		%>

		<div align="center">

			<h1 align="center" style="margin-bottom: -15; color: navy">
				<%
					if (bean != null && bean.getId() > 0) {
				%>Update<%
					} else {
				%>Add<%
					}
				%>
				Initiative
			</h1>
			<div style="height: 15px; margin-bottom: 12px">
				<H3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</H3>

				<H3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</H3>
			</div>
			
			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">
				
				<table>
				<tr>
					<th align="left">Initiative Name<span style="color: red">*</span></th>
					<td><input type="text" name="initiativeName"
						placeholder="Enter initiative name"
						value="<%=DataUtility.getStringData(bean.getInitiativeName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("initiativeName", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Type<span style="color: red">*</span></th>
					<td>
						<%
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("Technology", "Technology");
							map.put("Sustainability", "Sustainability");
							map.put("Innovation", "Innovation");
							map.put("Business", "Business");
							map.put("IT", "IT");


							String htmlList = HTMLUtility.getList("type", bean.getType(), map);
						%> <%=htmlList%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("type", request)%></font></td>
				</tr>
				
				<tr>
					<th align="left">Start Date<span style="color: red">*</span></th>
					<td><input type="text" id="udate" name="startDate" placeholder="Select Start date"
						value="<%=DataUtility.getDateString(bean.getStartDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("startDate", request)%></font></td>
				</tr>
				<tr>
					<th align="left">Version Number<span style="color: red">*</span></th>
					<td><input type="text" name="version"
						placeholder="Enter Version"
						value="<%=DataUtility.getStringData(bean.getInitiativeName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("version", request)%></font></td>
				</tr>
				<tr>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th></th>
					<%
						if (bean != null && bean.getId() > 0) {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=InitiativeCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=InitiativeCtl.OP_CANCEL%>">
						<%
							} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=InitiativeCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=InitiativeCtl.OP_RESET%>">
						<%
							}
						%>
				</tr>
			</table>
		</div>
	</form>
	<%@ include file="Footer.jsp" %>
				
</body>
</html>