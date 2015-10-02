/**
 * 
 */
package structures;

import json.JSONException;
import json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hongning
 * @version 0.1
 * @category data structure
 * data structure for a Yelp review document
 * You can create some necessary data structure here to store the processed text content, e.g., bag-of-word representation
 */
public class Post {
	//unique review ID from Yelp
	String m_ID;
	//Stores TF for each token in the doc.
    public HashMap<String, Double> tokenToTF;

    public HashMap<Integer, Double> idToTF_IDF = new HashMap<>();

    public void setID(String ID) {
		m_ID = ID;
	}
	
	public String getID() {
		return m_ID;
	}

	//author's displayed name
	String m_author;
	
	public String getAuthor() {
		return m_author;
	}

	public void setAuthor(String author) {
		this.m_author = author;
	}
	
	//author's location
	String m_location;
	public String getLocation() {
		return m_location;
	}

	public void setLocation(String location) {
		this.m_location = location;
	}

	//review text content
	String m_content;
	public String getContent() {
		return m_content;
	}

	public void setContent(String content) {
		if (!content.isEmpty())
			this.m_content = content;
	}
	
	public boolean isEmpty() {
		return m_content==null || m_content.isEmpty();
	}

	//timestamp of the post
	String m_date;
	public String getDate() {
		return m_date;
	}

	public void setDate(String date) {
		this.m_date = date;
	}
	
	//overall rating to the business in this review
	double m_rating;	

	public double getRating() {
		return m_rating;
	}

	public void setRating(double rating) {
		this.m_rating = rating;
	}


	public Post(String ID) {
		tokenToTF = new HashMap<>();
		m_ID = ID;
	}

//	HashMap<String, Token> m_vector; // suggested sparse structure for storing the vector space representation with N-grams for this document

	//Single tokens
	ArrayList<String> tokens;
    public void setTokens(ArrayList<String> tkns) {
        tokens = tkns;
    }
    public ArrayList<String> getTokens() {
        return tokens;
    }

	public double similiarity(Post p) {
        double magA = 0;
        double magB = 0;
        for (double i : idToTF_IDF.values()) {
            magA += Math.pow(i, 2);
        }
        for (double i : p.idToTF_IDF.values()) {
            magB += Math.pow(i, 2);
        }
        magA = Math.sqrt(magA);
        magB = Math.sqrt(magB);
        double denom = magA * magB;
        double numer = 0;
        HashSet<Integer> intersection = new HashSet<Integer>(idToTF_IDF.keySet()); // use the copy constructor
        intersection.retainAll(p.idToTF_IDF.keySet());
        for (Integer i : intersection) {
            numer += idToTF_IDF.get(i) * p.idToTF_IDF.get(i);
        }
		return numer/denom;//compute the cosine similarity between this post and input p based on their vector space representation
	}
	
	public Post(JSONObject json) {
		try {
            tokenToTF = new HashMap<>();
			m_ID = json.getString("ReviewID");
			setAuthor(json.getString("Author"));
			
			setDate(json.getString("Date"));			
			setContent(json.getString("Content"));
			setRating(json.getDouble("Overall"));
			setLocation(json.getString("Author_Location"));			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSON() throws JSONException {
		JSONObject json = new JSONObject();
		
		json.put("ReviewID", m_ID);//must contain
		json.put("Author", m_author);//must contain
		json.put("Date", m_date);//must contain
		json.put("Content", m_content);//must contain
		json.put("Overall", m_rating);//must contain
		json.put("Author_Location", m_location);//must contain
		
		return json;
	}

}
