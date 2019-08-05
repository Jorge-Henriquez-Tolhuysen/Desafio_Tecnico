package cl.tolhuysen.henriquez.Jorge;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SvltLogin
 */
@WebServlet("/Login")
public class SvltLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvltLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = (request.getParameter(DesafioStrings.strUserName)!=null)?request.getParameter(DesafioStrings.strUserName):"";
	    String password = (request.getParameter(DesafioStrings.strPassword)!=null)?request.getParameter(DesafioStrings.strPassword):"";
	    DesafioDB.User usr = null;
	    if (!email.contentEquals("") && !password.contentEquals("")) {
	    	usr = DesafioDB.getUserData(email);
	    	if (usr!=null && usr.password.contentEquals(password)) {
	    		if (usr.isBorrower) {
	    			HttpSession session = request.getSession(true);
	    			session.setAttribute(DesafioStrings.strUserId,  usr.id);
	    			session.setAttribute(DesafioStrings.strUserFName,  usr.fName);
	    			session.setAttribute(DesafioStrings.strUserLName,  usr.lName);
	    			request.getServletContext().getRequestDispatcher("/Borrower").forward(request,response);
	    		}
	    		if (usr.isLender) {
	    			HttpSession session = request.getSession(true);
	    			session.setAttribute(DesafioStrings.strUserId,  usr.id);
	    			session.setAttribute(DesafioStrings.strUserFName,  usr.fName);
	    			session.setAttribute(DesafioStrings.strUserLName,  usr.lName);
	    			request.getServletContext().getRequestDispatcher("/Lender").forward(request,response);
	    		}
	    		return;
	    	}
	    }
	    response.setContentType("text/html");
		response.setHeader("Cache-Control","no-cache"); 
		response.setHeader("Pragma","no-cache"); 
		response.setDateHeader ("Expires", -1);
		response.getWriter()
		.append("<!DOCTYPE html>")
		.append("<html>")
		.append("<head>")
		.append("<meta charset='ISO-8859-1'>")
		.append("<title>Login Page</title>")
		.append("<script type='text/javascript'>")
		.append("if (typeof (history.pushState) != 'undefined') {")
		.append("    var obj = { Title: 'Login Page', Url: 'Login' };")
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
		.append("</style>")
		.append("</head>")
		.append("	<body>")
		.append("	<div id='Login' style='display: inline-block; width: 99%;'>")
		//.append(String.format("IDX=%s<br/>",(usr!=null)?usr.fName + " " + usr.lName :"" ))
		//.append(String.format("REGISTERTYPE=%s<br/>",registerType ))		
		.append("		LOGIN<br/>")
		.append(String.format("<form class='lblLogin' id='frmLogin' action='%s' method='POST'>",request.getContextPath() + request.getServletPath()))
		.append(String.format("<label for='idlEMail'>Email:</label><input type='email' name='%s' id='idlEMail' value='%s'><br/>", DesafioStrings.strUserName, email))
		.append(String.format("<label for='idlPassword'>Password:</label><input type='password' name='%s' id='idlPassword' value='%s'><br/>", DesafioStrings.strPassword, password))
		.append("			<a href='./Register'>Click here to register</a>")
		.append("			<div style='float: right;'>")
		.append("				<input style='width: 100px;' type='submit' value='Login'><div style='display: inline-block;width:50px;'/>")
		.append("			</div>")
		.append("		</form><br/>")
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
