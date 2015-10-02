/**
 * 
 */
package structures;

/**
 * @author hongning
 * Suggested structure for constructing N-gram language model and vector space representation
 */
public class Token {

	int m_id; // the numerical ID you assigned to this token/N-gram
	public int getID() {
		return m_id;
	}

	public void setID(int id) {
		this.m_id = id;
	}

	String m_token; // the actual text content of this token/N-gram
	public String getToken() {
		return m_token;
	}

	public void setToken(String token) {
		this.m_token = token;
	}

	double m_value; // frequency or probability of this token/N-gram
	public double getValue() {
		return m_value;
	}

	public void setValue(double value) {

	}
	double df; // frequency or probability of this token/N-gram
	public double getDF() {
		return df;
	}

	public void setDF(double value) {
		this.df =value;
		this.df =value;
	}

	double m_idf; // frequency or probability of this token/N-gram
	public double getIDF() {
		return m_idf;
	}

	public void setIDF(double idf) {
		this.m_idf = idf;
	}

	//default constructor
	public Token(String token) {
		m_token = token;
		m_id = -1;
		m_value = 0;		
	}

	//default constructor
	public Token(int id, String token) {
		m_token = token;
		m_id = id;
		m_value = 0;		
	}

}
