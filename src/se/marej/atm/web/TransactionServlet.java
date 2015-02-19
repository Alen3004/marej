package se.marej.atm.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.exception.WebException;
import se.marej.atm.model.ATMReceipt;
import se.marej.atm.service.ATMSession;

@WebServlet("/session/transaction/*")
public class TransactionServlet extends HttpServlet
{

	private static final long serialVersionUID = 8540070081406579185L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// resp.getWriter().println(req.getHeader("atmSessionId"));

		// String[] pathSegments = req.getPathInfo().split("/");
		//
		// for(String segment : pathSegments)
		// {
		// resp.getWriter().println(segment);
		// }
		//
		// resp.getWriter().println(req.getPathInfo());

		int atmSessionId = Integer.parseInt(req.getHeader("atmSessionId"));

		ATMSession session = SessionServlet.getSessionWithId(atmSessionId);

		String pathInfo = req.getPathInfo();
		String transactionIdString = extractTransactionId(pathInfo);
		int transactionId = Integer.parseInt(transactionIdString);

		ATMReceipt receipt = session.requestReceipt(transactionId);

		resp.getWriter().println(receipt.getTransactionId());
	}

	public String extractTransactionId(String pathInfo)
	{
		String[] pathSegments = pathInfo.split("/");

		return pathSegments[1];
	}
}
