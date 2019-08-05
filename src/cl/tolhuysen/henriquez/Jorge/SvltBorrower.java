package cl.tolhuysen.henriquez.Jorge;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.tolhuysen.henriquez.Jorge.DesafioDB.Lent;

/**
 * Servlet implementation class SvltBorrower
 */
@WebServlet("/Borrower")
public class SvltBorrower extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvltBorrower() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		response.setContentType("text/html");
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		response.getWriter()
		.append("<!DOCTYPE html>")
		.append("<html>")
		.append("<head>")
		.append("<meta charset='ISO-8859-1'>")
		.append("<title>Borrower's Page</title>")
		.append("<script type='text/javascript'>")
		.append("if (typeof (history.pushState) != 'undefined') {")
		.append(String.format("var obj = { Title: 'Borrower\\'s Page', Url: 'Borrower/%d' };",id ))
		.append("    history.pushState(obj, obj.Title, obj.Url);")
		.append("} else {")
		.append("    alert('Browser does not support HTML5.');")
		.append("}")
		.append("</script>")
		.append("<style>")
		.append(".lblLogin label {")
		.append("	width:19%;")
		.append("	display: inline-block;")
		.append("}")
		.append(".lblLogin input {")
		.append("	width:79%;")
		.append("}")
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
		.append(String.format("Amount Needed: %d<br/>", DesafioDB.getAmountNeeded(id)))
		.append(String.format("Amount Raised: %d<br/><br/>", DesafioDB.getAmountRaised(id)))
		.append("List of people who lent you money<br/>");
		ArrayList<Lent> lents = DesafioDB.getLentsToBorrower(id);
		response.getWriter().append("<table style='width:100%'><tr><th>Name</th><th>Email</th><th>Amount Lent</th></tr>");
		if (lents.size()>0) {
			for(Lent l: lents) {
				response.getWriter().append(String.format("<tr><td>%s</td><td>%s</td><td>%d</td></tr>", l.fName + " " + l.lName, l.eMail, l.amountLent));	
			}
		}
		response.getWriter().append("</table>")
		//.append(String.format("<form class='lblLogin' id='frmLogin' action='%s' method='POST'>",request.getContextPath() + request.getServletPath()))
		//.append(String.format("<label for='idlEMail'>Email:</label><input type='email' name='%s' id='idlEMail' value='%s'><br/>", strUserName, email))
		//.append(String.format("<label for='idlPassword'>Password:</label><input type='password' name='%s' id='idlPassword' value='%s'><br/>", strPassword, password))
		//.append("		</form><br/>")
		//response.getWriter()
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
