package se.marej.atm.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.exception.ATMException;
import se.marej.atm.model.ATMReceipt;
import se.marej.atm.service.ATMSession;

public class ATMServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<code><ul>"
				+ "<li>To create an account: /createcard?account=nnnn&bank=xxxx&pin=nnnn</li>"
				+ "	<li>To get a session: /?acc=nnnn&pin=nnnn&do=verifypin</li>"
				+ "	<li>To check balance: /?acc=nnnn&pin=nnnn&do=showbalance</li>"
				+ "	<li>To withdraw money: /?acc=nnnn&pin=nnnn&do=withdraw&amount=nnnn</li>"
				+ "	<li>To show receipt: /?acc=nnnn&pin=nnnn&do=showreceipt&transactionid=nnnn</li>"
				+ "</ul></code>"
				+ "<hr />"
				+ "<br />");

		try
		{
			int accountId = Util.extractSingleNumericParameterValue(request, "acc");
			int accountPin = Util.extractSingleNumericParameterValue(request, "pin");

			ATMSession session = SessionManager.getInstance().getSession(accountId, accountPin);

			String doThis = request.getParameter("do");

			switch (doThis)
			{
			case "verifypin":
				writer.println("<h1>Successfully verifyed pin<br/>"
						+ "You now have a new session.</h1>");
				break;
			case "showbalance":
				writer.println("<h1>Show Balance</h1>");
				long balance = session.checkBalance();
				writer.println("<h2>balance: " + balance + "</h2>");
				break;
			case "withdraw":
				writer.println("<h1>Withdraw</h1>");

				int amount = Util.extractSingleNumericParameterValue(request, "amount");
				writer.println("<h2>transaction id: " + session.withdrawAmount(amount));
				break;
			case "showreceipt":
				writer.println("<h1>Showreceipt</h1>");

				int transactionId = Util.extractSingleNumericParameterValue(request, "transactionid");
				ATMReceipt receipt = session.requestReceipt(transactionId);
				writer.println("------------ RECEIPT ------------<br/>"
						+ " " + receipt.getDate() + "</br>"
						+ " TransactionID: " + transactionId + "</br>"
						+ " Amount: " + receipt.getAmount() + "</br>"
						+ "--------------------------------------");
				break;
			default:
				response.sendError(418, "Invalid value for do parameter");
				break;
			}
		}
		catch (BadRequestException | ATMException e)
		{
			response.sendError(400, e.getMessage());
		}
	}
}
