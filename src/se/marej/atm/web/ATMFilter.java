package se.marej.atm.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ATMFilter implements Filter
{
	@Override
	public void init(FilterConfig fConfig) throws ServletException
	{
	}

	@Override
	public void destroy()
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final HttpServletRequest httpRequest = (HttpServletRequest) request;

		SessionManager sessionManager = SessionManager.getInstance();

		try
		{
			int accountId = Util.extractSingleNumericParameterValue(httpRequest, "acc");
			int accountPin = Util.extractSingleNumericParameterValue(httpRequest, "pin");

			String doThis = request.getParameter("do");

			if (doThis.equals("verifypin"))
			{
				sessionManager.createSession(accountId, accountPin);
				chain.doFilter(request, response);
				return;
			}

			if (sessionManager.loginHasSession(accountId, accountPin))
			{
				// pass the request along the filter chain
				chain.doFilter(request, response);
				return;
			}
		}
		catch (BadRequestException e)
		{
			httpResponse.sendError(400, e.getMessage());
			return;
		}
		httpResponse.sendError(422, "No session for specified login credentials");
	}
}
