package cl.tolhuysen.henriquez.Jorge;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.tolhuysen.henriquez.Jorge.DesafioDB.UserInNeed;
import cl.tolhuysen.henriquez.Jorge.DesafioDB.UserInNeedAndLent;

/**
 * Servlet implementation class SvltLender
 */
@WebServlet("/Lender")
public class SvltLender extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvltLender() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		if (session==null) {
			request.getServletContext().getRequestDispatcher("/Login").forward(request,response);
			return;
		}
		//int id = (request.getAttribute(DesafioStrings.strUserId)!=null)?(int)request.getAttribute(DesafioStrings.strUserId):-1;
		//String fName = (request.getAttribute(DesafioStrings.strUserFName)!=null)?(String)request.getAttribute(DesafioStrings.strUserFName):"";
		//String lName = (request.getAttribute(DesafioStrings.strUserLName)!=null)?(String)request.getAttribute(DesafioStrings.strUserLName):"";
		int id = (int)session.getAttribute(DesafioStrings.strUserId);
		String fName = (String)session.getAttribute(DesafioStrings.strUserFName);
		String lName = (String)session.getAttribute(DesafioStrings.strUserLName);
		int accountBalance = DesafioDB.getAcountBalance(id);
		response.setContentType("text/html");
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		response.getWriter()
		.append("<!DOCTYPE html>")
		.append("<html>")
		.append("<head>")
		.append("<meta charset='ISO-8859-1'>")
		.append("<title>Lender's Page</title>")
		.append("<script type='text/javascript'>")
		.append("if (typeof (history.pushState) != 'undefined') {")
		.append(String.format("var obj = { Title: 'Lender\\'s Page', Url: 'Borrower/%d' };",id ))
		.append("    history.pushState(obj, obj.Title, obj.Url);")
		.append("} else {")
		.append("    alert('Browser does not support HTML5.');")
		.append("}")
		.append("</script>")
		.append("<style>")
		.append("table, th, td {")
		.append("   border: 1px solid black;")
		.append("	border-collapse: collapse;")
		.append("}")
		.append("table tr:nth-child(even) {")
		.append("  background-color: #eee;")
		.append("}")
		.append("table tr:nth-child(odd) {")
		.append(" background-color: #fff;")
		.append("}")
		.append("table th {")
		.append("  background-color: black;")
		.append("  color: white;")
		.append("}")
		.append("</style>")
		.append("</head>")
		.append("	<body>")
		.append(String.format("<div style='display: inline-block; float: right;'><a href='http://%s:%d%s/Logout'>Logout</a></div>", request.getServerName(),request.getServerPort(),request.getContextPath()))
		.append("	<div id='Login' style='display: inline-block; width: 99%;'>")
		.append(String.format("Name: %s<br/>", fName + " " + lName))
		.append(String.format("Account Balance: %d<br/><br/>", accountBalance))
		.append("List of people who are in need of help:<br/><br/>");
		ArrayList<UserInNeed> users = DesafioDB.getUsersInNeed();
		response.getWriter().append("<table style='width:100%'><tr><th>Name</th><th>Money Need For</th><th>Descrption</th><th>Amount Needed</th><th>Amount Raised</th><th>Action</th></tr>");
		if (users.size()>0) {
			for(UserInNeed u: users) {
				response.getWriter().append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%d</td><td>", u.fName + " " + u.lName, u.needMoneyFor, u.description, u.amountNeeded, u.amountRaised));
				if (accountBalance>0) {
					response.getWriter()
					.append(String.format("<form id='frmLendToId%d' action='http://%s:%d%s/LendMoney' method='POST'>", u.id, request.getServerName(),request.getServerPort(),request.getContextPath()))
					.append(String.format("  <input type='number' name='%s' id='idlMoney' min='1' max='%d' required>", DesafioStrings.strMoneyToLend, (accountBalance>u.amountNeeded)?u.amountNeeded:accountBalance))
					.append(String.format("  <input type='hidden' name='%s' value='%d'>", DesafioStrings.strUserToLendMoney, u.id))
					.append("<input style='width: 100px;' type='submit' value='Lend'>")
					.append("</form><br/>");						
				}
				else {
					response.getWriter().append("&nbsp;");	
				}
				response.getWriter().append("</td></tr>");
			}
		}
		response.getWriter().append("</table><br/>")
		.append("List of people you lent money to:<br/><br/>");
		ArrayList<UserInNeedAndLent> lents = DesafioDB.getLentsByLender(id);
		response.getWriter().append("<table style='width:100%'><tr><th>Name</th><th>Money Need for</th><th>Description</th><th>Amount Needed</th><th>Amount Raised</th><th>Amount Lent</th></tr>");
		if (lents.size()>0) {
			for(UserInNeedAndLent l: lents) {
				response.getWriter().append(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%d</td><td>%d</td><td>%d</td></tr>", l.fName + " " + l.lName, l.needMoneyFor, l.description, l.amountNeeded, l.amountRaised, l.amountLent));	
			}
		}
		response.getWriter().append("</table>")
		.append("	</div>")
		.append("	</body>")
		.append("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
