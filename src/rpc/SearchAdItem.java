package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.AdItem;

/**
 * 
 * Servlet implementation class SearchAdItem
 */
@WebServlet("/SearchAdItem")
public class SearchAdItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchAdItem() {
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
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
//			JSONArray array = new JSONArray();
			JSONObject ad = new JSONObject();

			AdItem adWithHighestRank = null;
			AdItem adWithSecondHighestRank = null;

			List<AdItem> items = conn.searchAdItems();

			if (items.size() < 2) {
				return;
			}

			// Initialize: get first two ads
			AdItem ad0 = items.get(0);
			AdItem ad1 = items.get(1);

			double adRank0 = ad0.getAd_score() * ad0.getBid();
			double adRank1 = ad1.getAd_score() * ad1.getBid();

			if (adRank0 > adRank1) {
				adWithHighestRank = ad0;
				adWithSecondHighestRank = ad1;
			} else {
				adWithHighestRank = ad1;
				adWithSecondHighestRank = ad0;
			}

			ad = adWithHighestRank.toJSONObject();
			// Iteratively find two ads with highest scores
			for (int i = 2; i < items.size(); i++) {
				AdItem item = items.get(i);
				double adRankScore = item.getAd_score() * item.getBid();
				if (adRankScore > adWithHighestRank.getAd_score() * adWithHighestRank.getBid()) { // higher than highest
					adWithSecondHighestRank = adWithHighestRank;
					adWithHighestRank = item;
					ad = item.toJSONObject();
				} else if (adRankScore > adWithHighestRank.getAd_score() * adWithHighestRank.getBid()) { // higher than
																											// second //
																											// highest
					adWithSecondHighestRank = item;
				}
			}

			// calculate bid of highest ad
			double secondRankScore = adWithSecondHighestRank.getBid() * adWithSecondHighestRank.getAd_score();
			double cost = secondRankScore / adWithHighestRank.getAd_score() + 0.01;
			System.out.println("cost is: " + cost);

			// update advertiser's budget
			int advertiser_id = adWithHighestRank.getAdvertiser_id(); // get advertiser_id from highest ranked ad
			double curBudget = conn.getBudget(advertiser_id); // get budget of advertiser
			conn.updateBudget(advertiser_id, curBudget - cost); // update budget
			curBudget = conn.getBudget(advertiser_id); // get the updated budget
			System.out.println("Budget is: " + curBudget);
//			array.put(ad);
			RpcHelper.writeJsonObject(response, ad); // respond with all the information of the selected ad
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { // try to close DB connection
				System.out.println("closing DB connection...");
				conn.close();
			} catch (Exception e) {
				System.out.println("Cannot close connection at SearchAdItems-doPost.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
