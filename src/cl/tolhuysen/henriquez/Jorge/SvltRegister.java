package cl.tolhuysen.henriquez.Jorge;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SvltRegister
 */
@WebServlet("/Register")
public class SvltRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvltRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String registerType         = (request.getParameter(DesafioStrings.strRegisterType)!=null)?request.getParameter(DesafioStrings.strRegisterType):"";
		String lenderFirsName       = (request.getParameter(DesafioStrings.strLenderFirsName)!=null)?request.getParameter(DesafioStrings.strLenderFirsName):"";
		String lenderLastName       = (request.getParameter(DesafioStrings.strLenderLastName)!=null)?request.getParameter(DesafioStrings.strLenderLastName):"";
		String lenderEMail          = (request.getParameter(DesafioStrings.strLenderEMail)!=null)?request.getParameter(DesafioStrings.strLenderEMail):"";
		String lenderPassword       = (request.getParameter(DesafioStrings.strLenderPassword)!=null)?request.getParameter(DesafioStrings.strLenderPassword):"";
		String lenderMoney          = (request.getParameter(DesafioStrings.strLenderMoney)!=null)?request.getParameter(DesafioStrings.strLenderMoney):"";
		
		String borrowerFirsName     = (request.getParameter(DesafioStrings.strBorrowerFirsName)!=null)?request.getParameter(DesafioStrings.strBorrowerFirsName):"";
		String borrowerLastName     = (request.getParameter(DesafioStrings.strBorrowerLastName)!=null)?request.getParameter(DesafioStrings.strBorrowerLastName):"";
		String borrowerEmail        = (request.getParameter(DesafioStrings.strBorrowerEmail)!=null)?request.getParameter(DesafioStrings.strBorrowerEmail):"";
		String borrowerPassword     = (request.getParameter(DesafioStrings.strBorrowerPassword)!=null)?request.getParameter(DesafioStrings.strBorrowerPassword):"";
		String borrowerNeedMoneyFor = (request.getParameter(DesafioStrings.strBorrowerNeedMoneyFor)!=null)?request.getParameter(DesafioStrings.strBorrowerNeedMoneyFor):"";
		String borrowerDescription  = (request.getParameter(DesafioStrings.strBorrowerDescription)!=null)?request.getParameter(DesafioStrings.strBorrowerDescription):"";
		String borrowerMoney        = (request.getParameter(DesafioStrings.strBorrowerMoney)!=null)?request.getParameter(DesafioStrings.strBorrowerMoney):"";
		
		int userId = 0;
		
		
	    if      (registerType.contentEquals(DesafioStrings.strRegisterLender)) {
	    	if (!lenderFirsName.contentEquals("") &&
	    		!lenderLastName.contentEquals("") &&
	    		!lenderEMail.contentEquals("")    &&
	    		!lenderPassword.contentEquals("") &&
	    		!lenderMoney.contentEquals("")) {
	    		try {
	    			int Money = Integer.parseInt(lenderMoney);
	    			userId = DesafioDB.createUser(lenderFirsName, lenderLastName, lenderEMail, lenderPassword, true, false);
	    		    if (userId>-1) {
	    		    	DesafioDB.saveLendData(userId, Money);
		    			HttpSession session = request.getSession(true);
		    			session.setAttribute(DesafioStrings.strUserId,  userId);
		    			session.setAttribute(DesafioStrings.strUserFName,  lenderFirsName);
		    			session.setAttribute(DesafioStrings.strUserLName,  lenderLastName);
		    			request.getServletContext().getRequestDispatcher("/Lender").forward(request,response);
		    			return;
	    		    }
	    			
	    		}
	    		finally {
	    			
	    		}
	    		
	    	}
	    	
	    }
	    else if (registerType.contentEquals(DesafioStrings.strRegisterBorrower)) {
	    	if (!borrowerFirsName.contentEquals("")     &&
		    	!borrowerLastName.contentEquals("")     &&
		    	!borrowerEmail.contentEquals("")        &&
		    	!borrowerPassword.contentEquals("")     &&
		    	!borrowerNeedMoneyFor.contentEquals("") &&
		    	!borrowerDescription.contentEquals("")  &&
		    	!borrowerMoney.contentEquals("")) {
		    		try {
		    			int Money = Integer.parseInt(borrowerMoney);
		    			userId = DesafioDB.createUser(borrowerFirsName, borrowerLastName, borrowerEmail, borrowerPassword, false, true);
		    		    if (userId>-1) {
		    		    	DesafioDB.saveBorrowData(userId, borrowerNeedMoneyFor, borrowerDescription, Money);
			    			HttpSession session = request.getSession(true);
			    			session.setAttribute(DesafioStrings.strUserId,  userId);
			    			session.setAttribute(DesafioStrings.strUserFName,  borrowerFirsName);
			    			session.setAttribute(DesafioStrings.strUserLName,  borrowerLastName);
			    			request.getServletContext().getRequestDispatcher("/Borrower").forward(request,response);
			    			return;
		    		    }
		    			
		    		}
		    		finally {
		    			
		    		}
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
		.append("<title>Registration Page</title>")
		.append("<style>")
		.append(".lblLender label {")
		.append("  width:19%;")
		.append("  display: inline-block;")
		.append("}")
		.append(".lblLender input {")
		.append("  width:79%;")
		.append("}")
		.append(".lblBorrower label {")
		.append("  width:29%;")
		.append("  display: inline-block;")
		.append("}")
		.append(".lblBorrower input,textarea {")
		.append("  width:69%;")
		.append("}")
		.append("</style>")
		.append("</head>")
		.append("	<body>")
		.append("	<div id='Lender' style='display: inline-block; width: 45%;'>")
		//.append(String.format("IDX=%d<br/>",userId ))
		//.append(String.format("REGISTERTYPE=%s<br/>",registerType ))
		.append("		LENDER<br/>")
		.append(String.format("<form class='lblLender' id='frmLender' action='%s' method='POST'>",request.getContextPath() + request.getServletPath()))
		.append(String.format("<label for='idlFName'>First Name:</label><input type='text' name='%s' id='idlFName' value='' required><br/>", DesafioStrings.strLenderFirsName))
		.append(String.format("<label for='idlLName'>Last Name:</label><input type='text' name='%s' id='idlLName' value='' required><br/>", DesafioStrings.strLenderLastName))
		.append(String.format("<label for='idlEMail'>Email:</label><input type='email' name='%s' id='idlEMail' value='' required><br/>", DesafioStrings.strLenderEMail))
		.append(String.format("<label for='idlPassword'>Password:</label><input type='password' name='%s' id='idlPassword' value='' required><br/>", DesafioStrings.strLenderPassword))
		.append(String.format("<label for='idlMoney'>Money:</label><input type='number' name='%s' id='idlMoney' min='100' max='5000' required><br/>", DesafioStrings.strLenderMoney))
		.append(String.format("<input type='hidden' name='%s' value='%s'>", DesafioStrings.strRegisterType, DesafioStrings.strRegisterLender))
		.append("			<input style='width: 100px; float: right;' type='submit' value='Register'>")
		.append("		</form>")
		.append(            "<a href='./Login'>Already a member?</a>")
		.append("	</div>")
		.append("	<div id='Borrower' style='display: inline-block; width:45%; float: right;'>")
		.append("		BORROWER<br/>")
		.append(String.format("<form class='lblBorrower' id='frmBorrower' action='%s' method='POST'>",request.getContextPath() + request.getServletPath()))
		.append(String.format("<label for='idbFName'>First Name:</label><input type='text' name='%s' id='idfName' value='' required><br/>", DesafioStrings.strBorrowerFirsName))
		.append(String.format("<label for='idbLName'>Last Name:</label><input type='text' name='%s' id='idlName' value='' required><br/>", DesafioStrings.strBorrowerLastName))
		.append(String.format("<label for='idbEmail'>Email:</label><input type='email' name='%s' id='ideMail' value='' required><br/>", DesafioStrings.strBorrowerEmail))
		.append(String.format("<label for='idbPassword'>Password:</label><input type='password' name='%s' id='idlPassword' value='' required><br/>", DesafioStrings.strBorrowerPassword))
		.append(String.format("<label for='idbMoneyFor'>Need Money for:</label><input type='text' name='%s' id='idbMoneyFor' value='' required><br/>", DesafioStrings.strBorrowerNeedMoneyFor))
		.append(String.format("<label for='idbDescription'>Description:</label><textarea name='%s' id='idbDescription' required></textarea><br/>", DesafioStrings.strBorrowerDescription))
		.append(String.format("<label for='idbMoney'>Amount Needed:</label><input type='number' name='%s' id='idbMoney' min='1' max='5000' required><br/>", DesafioStrings.strBorrowerMoney))
		.append("<br/>")
		.append(            "<a href='./Login'>Already a member?</a>")
		.append(String.format("<input type='hidden' name='%s' value='%s'>", DesafioStrings.strRegisterType, DesafioStrings.strRegisterBorrower))
		.append("			<input style='width: 100px; float: right;' type='submit' value='Register'>")
		.append("		</form>")
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
