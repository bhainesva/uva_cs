Problem Description: providing coin change of a given amount in the fewest number of coins
Inputs: Dollar amount to calculate change for. There will be 2 digits of accuracy and the dollar value will not exceed 1 billion dollars. There is no dollar sign in the input.
Output: List of coins that creates desired value in the fewest number of coins possible.
Assumptions: Coins are the standard quarter, dime, nickel, and penny. All inputs are non-negative. Dollar component of input is ignored, change only calculated for cents.
Strategy: Greedy algorithm that uses largest possible coins.
Description: Sort the coins in decreasing order of value. For each coin in the list, while the value of the coin is less than the amount of change desired, subtract the value of the coin from the change needed and output the coin. 
