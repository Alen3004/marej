package se.marej.atm.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/session/transaction/*/receipt")
public class Transaction extends HttpServlet
{

	private static final long serialVersionUID = 8540070081406579185L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		
		Util.extractInt(requestBody, keyName)
	}

}
