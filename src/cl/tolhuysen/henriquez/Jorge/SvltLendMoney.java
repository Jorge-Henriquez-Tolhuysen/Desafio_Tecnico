package cl.tolhuysen.henriquez.Jorge;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SvltLendMoney
 */
@WebServlet("/LendMoney")
public class SvltLendMoney extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvltLendMoney() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		HttpSession session = request.getSession(false);
		if (session==null) {
			request.getServletContext().getRequestDispatcher("/Login").forward(request,response);
			return;
		}
		int    lenderId       = (int)session.getAttribute(DesafioStrings.strUserId);
		String strBorrowerId  = (request.getParameter(DesafioStrings.strUserToLendMoney)!=null)?request.getParameter(DesafioStrings.strUserToLendMoney):"";
		String strMoneyToLend = (request.getParameter(DesafioStrings.strMoneyToLend)!=null)?request.getParameter(DesafioStrings.strMoneyToLend):"";
		DesafioDB.registerLentMoney(lenderId, Integer.parseInt(strBorrowerId), Integer.parseInt(strMoneyToLend));
		request.getServletContext().getRequestDispatcher("/Lender").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
