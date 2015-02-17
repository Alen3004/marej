package se.marej.atm.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.exception.ATMException;
import se.marej.atm.service.ATMSession;

public class ATMServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<h1>Welcome to ATM marej!</h1>"
				+ "<h3>Manual:</h3><br />"
				+ "<ul>"
				+ "	<li><b>To create an account:</b> /createcard?account=nnnn&bank=xxxx&pin=nnnn</li>"
				+ "	<li><b>To get a session:</b> /?acc=nnnn&pin=nnnn&do=verifypin</li>"
				+ "	<li><b>To check balance:</b> /?acc=nnnn&pin=nnnn&do=checkbalance</li>"
				+ "	<li><b>To withdraw money:</b> /?acc=nnnn&pin=nnnn&do=withdraw&amount=nnnn</li>"
				+ "</ul>");

		try
		{
			int accountId = Util.extractSingleNumericParameterValue(request, "acc");
			int accountPin = Util.extractSingleNumericParameterValue(request, "pin");

			ATMSession session = ATMSessionServlet.getSession(accountId, accountPin);

			writer.println("<p style=\"color: blue\">");

			String doThis = request.getParameter("do");

			switch (doThis)
			{
			case "verifypin":
				writer.println("<h1>Verify pin!</h1>");
				break;
			case "showbalance":
				writer.println("<h1>Show Balance!</h1>");
				long balance = session.checkBalance();
				writer.println("<h2>balance: " + balance + "</h2>");
				break;
			case "withdraw":
				writer.println("<h1>Withdraw!</h1>");

				int amount = Util.extractSingleNumericParameterValue(request, "amount");
				writer.println("<h2>transaction id: " + session.withdrawAmount(amount));
				break;
			default:
				response.sendError(418, "Invalid value for do parameter");
				break;
			}
			writer.println("</p>");
		}
		catch (BadRequestException | ATMException e)
		{
			response.sendError(400, e.getMessage());
		}
	}
}
