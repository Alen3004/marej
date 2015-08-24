package se.marej.atm.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.marej.atm.exception.WebException;
import se.marej.atm.service.ATMSession;

@WebServlet("/session/operation")
public class OperationServlet extends HttpServlet
{

	private static final long serialVersionUID = 6035654188545903334L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		Map<String, String> requestBody = Util.getRequestBody(req);

		// operation:withdraw,atmSessionId:8907654,amount:500

		final int sessionId;
		ATMSession session;
		try
		{
			sessionId = Util.extractInt(requestBody, "atmSessionId");

			session = SessionServlet.getSessionWithId(sessionId);
		}
		catch (WebException e)
		{
			resp.sendError(400, e.getMessage());
			return;
		}

		if (requestBody.containsKey("operation"))
		{
			switch (requestBody.get("operation"))
			{
			case "check-balance":
				resp.getWriter().println(session.checkBalance());
				break;
			case "withdraw":
				resp.getWriter().println(session.withdrawAmount(Integer.parseInt(requestBody.get("amount"))));
				break;
			default:
				resp.sendError(400, "Unknown operation: " + requestBody.get("operation"));
				break;
			}
		}
		else
		{
			resp.sendError(400, "Expected an operation");
		}
	}
}
