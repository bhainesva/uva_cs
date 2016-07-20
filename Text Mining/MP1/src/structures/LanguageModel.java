/**
 * 
 */
package structures;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author hongning
 * Suggested structure for constructing N-gram language model
 */
public class LanguageModel {

	int m_N; // N-gram
	int m_V; // the vocabulary size
	public HashMap<String, Token> m_model; // sparse structure for storing the maximum likelihood estimation of LM with the seen N-grams
    public HashMap<String, ArrayList<String>> followers;
	public LanguageModel m_reference; // pointer to the reference language model for smoothing purpose
    public double unique_unigram_count;
    public double unique_bigram_count;
    public double total_unigram_count;
    public double total_bigram_count;

	double m_lambda; // parameter for linear interpolation smoothing
	double m_delta; // parameter for absolute discount smoothing
    double add_delta;

	public LanguageModel(int N) {
        add_delta = 0.1;
        m_delta = 0.1;
        m_lambda = 0.9;
        total_unigram_count = 0.0;
        total_bigram_count = 0.0;
		m_N = N;
		m_model = new HashMap<String, Token>();
        followers = new HashMap<String, ArrayList<String>>();
        unique_unigram_count = 0.0;
        unique_bigram_count = 0.0;
	}
	
    public double calcMLProb(String want, String given) {
        if (m_model.containsKey(given)) {
            double numerator = m_model.containsKey(given + " " + want) ? m_model.get(given + " " + want).getValue() : 0.0;
            double denomenator = m_model.get(given).getValue();
            return numerator / denomenator;
        }
        else {
            return 1/unique_unigram_count;
        }
    }

    public double linearInterpolation(String want, String given) {
        double term1 = m_lambda * calcMLProb(want, given);
        double term2 =  (1.0 - m_lambda) * additiveSmoothedUnigramProb(want);

        return term1 + term2;
    }

    public double absoluteDiscounting(String want, String given) {
        //Unseen given
        if (!m_model.containsKey(given)) {
            return additiveSmoothedUnigramProb(want);
        }
        // number of occurences of w_i-1
        double denom = m_model.get(given).getValue();

        //occurrences of bigram
        double count = m_model.containsKey(given + " " + want) ? m_model.get(given + " " + want).getValue() : 0.0;

        // max of occurrences of bigram - delta and 0
        double numert1 = Math.max(0.0, count - m_delta);

        //number of unique followers of w_i-1
        double S = followers.containsKey(given) ? followers.get(given).size() : 0.0;

        double lambda = S != 0.0 ? (S * m_delta)/denom : 1;

        return (numert1/denom) + lambda * additiveSmoothedUnigramProb(want);
    }

    public double additiveSmoothedUnigramProb(String token) {
        double count = m_model.containsKey(token) ? m_model.get(token).getValue() : 0.0;
        double numerator = count + add_delta;
        double denomenator = total_unigram_count + add_delta * unique_unigram_count;

        return numerator/denomenator;
    }

	//We have provided you a simple implementation based on unigram language model, please extend it to bigram (i.e., controlled by m_N)
//	public String sampling() {
//		double prob = Math.random(); // prepare to perform uniform sampling
//		for(String token:unigrams) {
//			prob -= additiveSmoothedUnigramProb(token);
//			if (prob<=0)
//				return token;
//		}
//		return null; //How to deal with this special case?
//	}
	
	//We have provided you a simple implementation based on unigram language model, please extend it to bigram (i.e., controlled by m_N)
//	public double logLikelihood(Post review) {
//		double likelihood = 0;
//		for(String token:review.getTokens()) {
//			likelihood += Math.log(calcLinearSmoothedProb(token));
//		}
//		return likelihood;
//	}
}
