package lab1p1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletController
 */
/**
 * @author Adela
 *
 */
/**
 * @author Adela
 * 
 */
@WebServlet(name = "ServletController", urlPatterns = { "/ServletController" })
public class ServletController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BufferedReader br = null;
	private Map<String, String> hmBack;
	private Map<String, String> hmForward;
	private Map<String, String> hmHome;
	private String index = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletController() {
		super();
	}

	@Override
	public void init() {
		String key = null;
		String value = null;
		String initialPage = null;
		String redirectToPage = null;
		String action = null;
		String anteriorPage = null;
		String filename = "/WEB-INF/navigationRules.txt";
		ServletContext context = getServletContext();
		InputStream is = context.getResourceAsStream(filename);
		hmBack = new HashMap<String, String>();
		hmForward = new HashMap<String, String>();
		hmHome = new HashMap<String, String>();
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			try {
				index = reader.readLine();
				initialPage = index;
				String line = "";
				while ((line = reader.readLine()) != null) {
					anteriorPage = initialPage;
					initialPage = line.split(", ")[0];
					action = line.split(", ")[1];
					redirectToPage = line.split(", ")[2];
					if (!anteriorPage.equals(initialPage)
							&& (action.equals("back"))) {
						key = initialPage;
						value = redirectToPage;
						hmBack.put(key, value);
					}
					if (action.equals("forward")) {
						key = initialPage;
						value = redirectToPage;
						hmForward.put(key, value);
					}
					if (action.equals("home")) {
						key = initialPage;
						value = redirectToPage;
						hmHome.put(key, value);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String urlParam = "";
		String urlForward = "";
		String urlBack = "";
		String urlHome = "";
		RequestDispatcher dispatcher = null;
		ServletContext context = getServletContext();
		PrintWriter out = new PrintWriter(response.getWriter());

		urlParam = request.getParameter("url");
		if (urlParam == null) {
			urlParam = index;
		}

		if (hmBack.containsKey(urlParam)) {
			urlBack = hmBack.get(urlParam);
			out.println("<form name=\"back\" action=\"ServletController\" method=\"GET\">"
					+ "<div align=\"left\"><input type=\"hidden\" name=\"url\" value="
					+ urlBack
					+ ">"
					+ "<input type=\"submit\" value=\"Back\"></div></form><br>");
		}
		if (hmForward.containsKey(urlParam)) {
			urlForward = hmForward.get(urlParam);
			out.println("<form name=\"forward\" action=\"ServletController\" method=\"GET\">"
					+ "<div align=\"left\"><input type=\"hidden\" name=\"url\" value="
					+ urlForward
					+ ">"
					+ "<input type=\"submit\" value=\"Forward\"></div></form><br>");
		}
		if (hmHome.containsKey(urlParam)) {
			urlHome = hmHome.get(urlParam);
			out.println("<form name=\"home\" action=\"ServletController\" method=\"GET\">"
					+ "<div align=\"left\"><input type=\"hidden\" name=\"url\" value="
					+ urlHome
					+ ">"
					+ "<input type=\"submit\" value=\"Home\"></div></form>");
		}
		dispatcher = context.getRequestDispatcher("/WEB-INF/" + urlParam);
		dispatcher.include(request, response);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		try {
			br.close();
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
