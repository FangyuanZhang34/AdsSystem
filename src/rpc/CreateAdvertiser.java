package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class CreateAdvertiser
 */
@WebServlet("/CreateAdvertiser")
public class CreateAdvertiser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateAdvertiser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * Convert request from front-end into JSONObject. Write new row into DB and get
	 * advertiser_id. Format advertiser_id as JsonObject. Write advertiser_id into
	 * response.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// establish new connection with DB
		DBConnection conn = DBConnectionFactory.getConnection("mysql");

		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String advertiser_name = input.getString("name");
			double budget = input.getDouble("budget");
			int advertiser_id = conn.createAdvertiser(advertiser_name, budget); // create a row of ad into DB
			RpcHelper.writeJsonObject(response, new JSONObject().put("advertiser_id", advertiser_id)); // write ad_id to
																										// response
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { // try to close DB connection
				System.out.println("closing DB connection...");
				conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Cannot close connection at CreateAdvertiser-doPost.");
				e.printStackTrace();
			}
		}

	}

}
