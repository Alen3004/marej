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
		try
		{
			int atmSessionId;
			try
			{
				atmSessionId = Integer.parseInt(req.getHeader("atmSessionId"));
			}
			catch (NumberFormatException e)
			{
				throw new WebException("Invalid atmSessionId: " + req.getHeader("atmSessionId") + ". Must be an integer.");
			}

			ATMSession session = SessionServlet.getSessionWithId(atmSessionId);

			final int transactionId = extractTransactionId(req.getPathInfo());

			final ATMReceipt receipt = session.requestReceipt(transactionId);

			resp.getWriter().println("Receipt:");
			resp.getWriter().println("");
			resp.getWriter().println(receipt.getDate());
			resp.getWriter().println("TransactionID: " + receipt.getTransactionId());
			resp.getWriter().println("Amount: " + receipt.getAmount());
		}
		catch (WebException e)
		{
			resp.sendError(400, e.getMessage());
		}
	}

	public int extractTransactionId(String pathInfo) throws WebException
	{
		String[] pathSegments = pathInfo.split("/");
		try
		{
			int transactionId = Integer.parseInt(pathSegments[1]);
			return transactionId;
		}
		catch (NumberFormatException e)
		{
			throw new WebException("Invalid transaction id: " + pathSegments[1] + ". Must be an integer.");
		}
	}
}
