package db;

import java.util.List;

import entity.AdItem;

public interface DBConnection {
	/**
	 * Close the connection.
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception;

	/**
	 * return all ads
	 * 
	 * @return List of all the ads
	 */
	public List<AdItem> searchAdItems();

	/**
	 * Given an advertiser_id, return budget of this advertiser
	 * 
	 * @param advertiser_id
	 * @return budget of the advertiser
	 */
	public double getBudget(int advertiser_id);

	/**
	 * Update advertiser's budget
	 * 
	 * @param advertiser_id
	 * @param budget
	 */
	public double updateBudget(int advertiser_id, double budget);

	/**
	 * Given an ad_id, return bid of this ad
	 * 
	 * @param ad_id
	 * @return bid of the ad
	 */
	public double getBid(int ad_id);

	/**
	 * Update ad bid
	 * 
	 * @param ad_id
	 * @param bid
	 */
	public double updateBid(int ad_id, double bid);

	/**
	 * create an advertiser
	 * 
	 * @param advertiser_name
	 * @param budget
	 * @return
	 */
	public int createAdvertiser(String advertiser_name, double budget);

	/**
	 * create an ad
	 * 
	 * @param bid
	 * @param image_url
	 * @param advertiser_id
	 * @param ad_score
	 * @return
	 */
	public long createAd(double bid, String image_url, int advertiser_id, double ad_score);
}
