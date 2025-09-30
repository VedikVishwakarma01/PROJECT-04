<%@page import="in.co.rays.controller.InitiativeCtl"%>
<%@page import="in.co.rays.controller.InitiativeListCtl"%>
<%@page import="in.co.rays.bean.InitiativeBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="in.co.rays.model.RoleModel"%>
<%@page import="in.co.rays.controller.UserListCtl"%>
 <%@page import="in.co.rays.util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.util.DataUtility"%>
<%@page import="in.co.rays.util.ServletUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<html>
<head>
<title>Initiative List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>
	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.bean.InitiativeBean"
		scope="request"></jsp:useBean>

	<div align="center">
		<h1 align="center" style="margin-bottom: -15; color: navy;">Initiative
			List</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3>
				<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
			</h3>
			<h3>
				<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>
		</div>

		<form action="<%=ORSView.INITIATIVE_LIST_CTL%>" method="post">
			<%
                int pageNo = ServletUtility.getPageNo(request);
                int pageSize = ServletUtility.getPageSize(request);
                int index = ((pageNo - 1) * pageSize) + 1;
                int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

               List initiativeList =  (List<InitiativeBean>)request.getAttribute("initiativeList");
               HashMap<String, String> typeMap= (HashMap<String, String>)request.getAttribute("typeMap");
                
                List<InitiativeBean> list = (List<InitiativeBean>) ServletUtility.getList(request);
                Iterator<InitiativeBean> it = list.iterator();

                if (list.size() != 0) {
            %>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<table style="width: 100%">
				<tr>
					<td align="center"><label><b>Initiative Name :</b></label> <input
						type="text" name="initiativeName"
						placeholder="Enter Initiative Name"
						value="<%=ServletUtility.getParameter("initiativeName", request)%>">&emsp;
						
						
						<label><b>Type :</b></label>
					    <%=HTMLUtility.getList("type", bean.getType(), typeMap) %>
					    

						<label><b>Version : </b></label> <input type="text" name="version"
						placeholder="Enter version"
						value="<%=ServletUtility.getParameter("version", request)%>">&emsp;

						<input type="submit" name="operation"
						value="<%=InitiativeListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation" value="<%=InitiativeCtl.OP_RESET%>">
					</td>
				</tr>
			</table>
			<br>

			<table border="1" style="width: 100%; border: groove;">
				<tr style="background-color: #e1e6f1e3;">
					<th width="5%"><input type="checkbox" id="selectall" /></th>
					<th width="5%">S.No</th>
					<th width="13%">Initiative Name</th>
					<th width="8%">Type</th>
					<th width="8%">Start date</th>
					<th width="8%">Version Number</th>
					<th width="5%">Edit</th>
				</tr>

				<%
                    while (it.hasNext()) {
                        bean = (InitiativeBean) it.next();
                         
                        
                %>

				<tr>
					<td style="text-align: center;"><input type="checkbox"
						class="case" name="ids" value="<%=bean.getId()%>">
					</td>
					<td style="text-align: center;"><%=index++%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getInitiativeName()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getType()%></td>
					<td style="text-align: center; text-transform: lowercase;"><%=bean.getStartDate()%></td>
					<td style="text-align: center; text-transform: capitalize;"><%=bean.getVersion()%></td>
					<td style="text-align: center;"><a
						href="<%=ORSView.INITIATIVE_CTL%>?id=<%=bean.getId()%>">Edit</a>
					</td>
				</tr>

				<%
                    }
                %>
			</table>

			<table style="width: 100%">
				<tr>
					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=InitiativeListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>
					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=InitiativeListCtl.OP_NEW%>"></td>
					<td align="center" tyle="width: 25%"><input type="submit"
						name="operation" value="<%=InitiativeListCtl.OP_DELETE%>">
					</td>
					<td style="width: 25%" align="right"><input type="submit"
						name="operation" value="<%=InitiativeListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>
				</tr>
			</table>

			<%
                } else {
            %>

			<table>
				<tr>
					<td align="right"><input type="submit" name="operation"
						value="<%=InitiativeListCtl.OP_BACK%>"></td>
				</tr>
			</table>

			<%
                }
            %>
		</form>
	</div>
	<%@ include file="Footer.jsp"%>
</body>
</html>