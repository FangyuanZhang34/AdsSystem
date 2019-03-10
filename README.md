# AdsSystem

This is a ad bidding system that can register advertisers, create advertisements, rank each advertisement and choose the best advertisement to deliver for a ad spot. <br>
The bidding strategy is that: <br>
1. Each advertisement has an ad quality score and maximum bid from the advertiser. We multiply these two and get the ad rank for each ad. The higher the better.<br>
2. We pick the top-ranked ad and the actural CPC(Cost Per Click) price for this winner is calculated as the second-place ad's rank divided by the winner's quality score, then plus 0.01. 
