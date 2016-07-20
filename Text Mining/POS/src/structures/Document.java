/**
 *
 */
package structures;

import java.util.HashMap;
import java.util.List;

import json.JSONException;
import json.JSONObject;

/**
 * @author hongning
 * @version 0.1
 * @category data structure
 * data structure for a Yelp review document
 * You can create some necessary data structure here to store the processed text content, e.g., bag-of-word representation
 */
public class Document {
    //unique review ID from Yelp
    String m_ID;
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

    List<String> m_tokens; // we will store the tokens
    public List<String> getTokens() {
        return m_tokens;
    }

    public void setTokens(List<String> tokens) {
        m_tokens = tokens;
    }

    public String getToken(int i) {
        return m_tokens.get(i);
    }

    List<Tag> m_tags; // we will store the tokens
    public List<Tag> getTags() {
        return m_tags;
    }

    public void setTags(List<Tag> tags) {
        m_tags = tags;
    }

    public Tag getTag(int i) {
        return m_tags.get(i);
    }

    public int size() {
        return m_tokens.size();
    }
    public Document(List<String> tokens, List<Tag> tags) {
        m_tokens = tokens;
        m_tags = tags;
    }
}

