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

	double m_value; // frequency or count of this token/N-gram
	public double getValue() {
		return m_value;
	}

	public void setValue(double value) {
		this.m_value =value;
	}

	public void incTF() {
		this.m_tf += 1;
	}

	double m_idf; // inverse document frequency of this token
	public double getIDF() { return m_idf; }

	public void setIDF(double value) { this.m_idf = value; }

	double m_df; // document frequency of this token
	public double getDF() { return m_df; }

	public void setDF(double value) { this.m_df = value; }

	double m_posdf; // how many positive reviews does this token appear in
	public double getPosDF() { return m_posdf; }

	public void setPosDF(double value) { this.m_posdf = value; }

	double m_ig; // how many positive reviews does this token appear in
	public double getIG() { return m_ig; }

	public void setIG(double value) { this.m_ig = value; }

	double m_chi; // how many positive reviews does this token appear in
	public double getChi() { return m_chi; }

	public void setChi(double value) { this.m_chi = value; }

    double m_tf; // how many positive reviews does this token appear in
    public double getTF() { return m_tf; }

    public void setTF(double value) { this.m_tf = value; }

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
