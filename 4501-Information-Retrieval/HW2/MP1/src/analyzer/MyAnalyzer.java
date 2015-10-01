/**
 *
 */
package analyzer;

import java.io.*;
import java.text.Normalizer;
import java.util.*;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.porterStemmer;

import structures.Post;
import structures.Token;

import static java.lang.System.exit;

public class MyAnalyzer {
    //Tools
    Tokenizer tokenizer;
    SnowballStemmer stemmer;

    //a list of stopwords
    HashSet<String> m_stopwords;

    //Set of all (normalized/stemmed) tokens encountered
    ArrayList<Token> vocabulary;

    //you can store the loaded reviews in this arraylist for further processing
    ArrayList<Post> m_reviews;

    //you might need something like this to store the counting statistics for validating Zipf's and computing IDF
    //HashMap<String, Token> m_stats;

    //we have also provided sample implementation of language model in src.structures.LanguageModel

    public MyAnalyzer() throws IOException {
        m_reviews = new ArrayList<Post>();
        m_stopwords = new HashSet<>();
        vocabulary = new ArrayList<>();
        tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream("./data/Model/en-token.bin")));
        stemmer = new englishStemmer();
    }

    //sample code for loading a list of stopwords from file
    //you can manually modify the stopword file to include your newly selected words
    public void LoadStopwords(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {
                //it is very important that you perform the same processing operation to the loaded stopwords
                //otherwise it won't be matched in the text content
                line = SnowballStemming(Normalization(line));
                if (!line.isEmpty())
                    m_stopwords.add(line);
            }
            reader.close();
            System.out.format("Loading %d stopwords from %s\n", m_stopwords.size(), filename);
        } catch(IOException e){
            System.err.format("[Error]Failed to open file %s!!", filename);
        }
    }

    public void analyzeDocument(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                /**
                 * HINT: perform necessary text processing here, e.g., tokenization, stemming and normalization
                 */
                String content = review.getContent();
                String[] tokens = tokenizer.tokenize(content);
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty()) {
                        tokenList.add(parsedToken);
                        //m_tokens.add(new Token(parsedToken));
                    }
                }
                review.setTokens(tokenList);

                m_reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //sample code for loading a json file
    public JSONObject LoadJson(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            StringBuffer buffer = new StringBuffer(1024);
            String line;

            while((line=reader.readLine())!=null) {
                buffer.append(line);
            }
            reader.close();

            return new JSONObject(buffer.toString());
        } catch (IOException e) {
            System.err.format("[Error]Failed to open file %s!", filename);
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            System.err.format("[Error]Failed to parse json file %s!", filename);
            e.printStackTrace();
            return null;
        }
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix)){
                analyzeDocument(LoadJson(f.getAbsolutePath()));
            }
            else if (f.isDirectory())
                LoadDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);
    }

    //sample code for demonstrating how to use Snowball stemmer
    public String SnowballStemming(String token) {
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    //sample code for demonstrating how to use Porter stemmer
    public String PorterStemmingDemo(String token) {
        porterStemmer stemmer = new porterStemmer();
        stemmer.setCurrent(token);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return token;
    }

    //sample code for demonstrating how to perform text normalization
    public String Normalization(String token) {
        // remove all non-word characters
        // please change this to removing all English punctuation
        token = token.replaceAll("\\W+", "");
        token = token.replaceAll("\\p{Punct}+", "");
        // convert to lower case
        token = token.toLowerCase();

        // add a line to recognize integers and doubles via regular expression
        // and convert the recognized integers and doubles to a special symbol "NUM"
        token = token.replaceAll("\\d+", "NUM");
        //token = token.replaceAll("[-+]?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)([eE][-+]?[0-9]+)?", "NUM");

        return token;
    }

    public Map<String, Integer> TotalTermFreq() {
        Map<String, Integer> freqs = new HashMap<>();
        for (Post doc : m_reviews) {
            for (String token : doc.getTokens()) {
                if (freqs.containsKey(token)) {
                    freqs.put(token, freqs.get(token) + 1);
                } else {
                    freqs.put(token, 1);
                }

            }
        }

        return freqs;
    }


    public HashMap<String, Integer> DocumentFrequency() {
        //Stores DF for one word tokens
        HashMap<String, Integer> freqs = new HashMap<>();
        for (Post doc : m_reviews) {
            //Set<String> tokens = new HashSet<>(doc.getTokens());
            HashMap<String, Integer> docFreqs = new HashMap<>();
            ArrayList<String> tokens = doc.getTokens();
            for (String token : tokens) {
                if (docFreqs.containsKey(token)) {
                    docFreqs.put(token, docFreqs.get(token) + 1);
                }
                else {
                    docFreqs.put(token, 1);
                }
            }

            //Update DF score for one word tokens
            for (String str : docFreqs.keySet()) {
                doc.tokenToTF.put(str, docFreqs.get(str));
                if (freqs.containsKey(str)) {
                    freqs.put(str, freqs.get(str) + 1);
                }
                else {
                    freqs.put(str, 1);
                }

            }
        }

        return freqs;
    }

    public ArrayList<String> tokensByFreq(Map<String, Integer> freqs) {
        ArrayList<String> sorted = new ArrayList<>();

        for (String s : freqs.keySet()) {
            sorted.add(s);
        }

        Collections.sort(sorted, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return -freqs.get(s).compareTo(freqs.get(t1));
            }
        });

        return sorted;

    }

    //Compares tokens by value
    //Used to sort vocabulary
    //Where value is DF
    //Sorts from highest to lowest
    static class TokenComp implements Comparator<Token>{
        @Override
        public int compare(Token e1, Token e2) {
            if (e1.getValue() < e2.getValue()) {
                return 1;
            }
            else if (e1.getValue() > e2.getValue()) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }

    public HashMap<String, Integer> CreateVocabulary() {
        //Freq stores DF for each token
        HashMap<String, Integer> freq = new HashMap<>();
        for (Post p : m_reviews) {
            HashMap<String, Integer> doc2Grams = new HashMap<>();
            HashMap<String, Integer> doc1Grams = new HashMap<>();
            //1-Gram
            for (int i = 0; i <= p.getTokens().size() - 1; i++) {
                String str = p.getTokens().get(i);
                if (doc1Grams.containsKey(str)) {
                    doc1Grams.put(str, doc1Grams.get(str) + 1);
                }
                else {
                    doc1Grams.put(str, 1);
                }
            }

            for (int i = 0; i <= p.getTokens().size() - 2; i++) {
                String str = p.getTokens().get(i) + " " + p.getTokens().get(i + 1);
                if (doc2Grams.containsKey(str)) {
                   doc2Grams.put(str, doc2Grams.get(str) + 1);
                }
                else {
                    doc2Grams.put(str, 1);
                }
            }
            for (String tk : doc2Grams.keySet()) {
                p.tokenToTF.put(tk, doc2Grams.get(tk));
                if (freq.containsKey(tk)) {
                    freq.put(tk, freq.get(tk) + 1);
                }
                else {
                    freq.put(tk, 1);
                }
            }
        }

        for (String str : freq.keySet()) {
            Token tk = new Token(str);
            //Value of tokens in vocabulary is DF
            tk.setValue(freq.get(str));
            tk.setIDF(1 + Math.log((double) m_reviews.size() / tk.getValue()));
            vocabulary.add(tk);
        }

        return freq;
    }

    public static void main(String[] args) throws IOException, JSONException {
        //Initialize analyzer, load data
        MyAnalyzer analyzer = new MyAnalyzer();
        analyzer.LoadStopwords("./data/english.stop");
        analyzer.LoadDirectory("./data/yelp", "json");

        //Part 1:
        //Get total term frequencies for all tokens
        //Map<String, Integer> totalTermFreqs = analyzer.TotalTermFreq();
        //Sort tokens by decreasing TTF
        //ArrayList<String> ttfSorted = analyzer.tokensByFreq(totalTermFreqs);
        //System.out.println("TTF Calculated.");

        //File ttfFile = new File("ttfOut.csv");
        //PrintWriter ttfWriter = new PrintWriter(ttfFile);
        //System.out.println(ttfSorted.size());
        //for (String s : ttfSorted) {
            //ttfWriter.println(s + "," + totalTermFreqs.get(s));
        //}
        //ttfWriter.close();

       // Map<String, Integer> docFreqs = analyzer.DocumentFrequency();
       // ArrayList<String> dfSorted = analyzer.tokensByFreq(docFreqs);
       // File dfFile = new File("dfOut.csv");
       // PrintWriter dfWriter = new PrintWriter(dfFile);
       // System.out.println(dfSorted.size());
       // for (String s : dfSorted) {
       //     dfWriter.println(s + "," + docFreqs.get(s));
       // }
       // dfWriter.close();

        //Part 2:
        //Generate bigrams and calculate document frequency for all Ngrams
        HashMap<String, Integer> vocabToDF = analyzer.CreateVocabulary();
        System.out.println(analyzer.vocabulary.size());
        //Find top 100 words by DF
        Collections.sort(analyzer.vocabulary, new TokenComp());

        //Top 100 stop words in restaurant
        //This is top 100 Ngrams by DF
        List<Token> top100 = new ArrayList<>();
        for (int i = 0; i< 100; i++) {
            top100.add(analyzer.vocabulary.get(i));
        }

        //Subtract original stopwords to find restaurant specific ones
        top100.removeAll(analyzer.m_stopwords);
        File restSpecificFile = new File("restSpecificStopwords.txt");
        PrintWriter restSpecificWriter = new PrintWriter(restSpecificFile);
        for (Token i: top100) {
            restSpecificWriter.println(i.getToken() + "," + i.getValue());

            if (!analyzer.m_stopwords.contains(i.getToken())) {
               analyzer.m_stopwords.add(i.getToken());
            }
        }
        restSpecificWriter.close();

       //Filter words out of vocabulary that have DF less than 50
       // while(analyzer.vocabulary.get(analyzer.vocabulary.size()-1).getValue() < 50) {
       //     analyzer.vocabulary.remove(analyzer.vocabulary.size()-1);
       // }

       ////Size of controlled vocabulary
       // File vocabSizeFile = new File("vocabSize.txt");
       // PrintWriter vocabSizeWriter = new PrintWriter(vocabSizeFile);
       // vocabSizeWriter.println(analyzer.vocabulary.size());
       // vocabSizeWriter.close();

       ////Top and bottom 50 words by DF and their corresponding IDFs
       // ArrayList<Token> top50IDF = new ArrayList<>();
       // ArrayList<Token> bottom50IDF = new ArrayList<>();
       // for(int i =0; i< 50 && i < analyzer.vocabulary.size(); i++) {
       //     Token toptkn = new Token(analyzer.vocabulary.get(i).getToken());
       //     toptkn.setIDF(analyzer.vocabulary.get(i).getIDF());
       //     toptkn.setValue(analyzer.vocabulary.get(i).getValue());
       //     Token bottkn = new Token(analyzer.vocabulary.get(analyzer.vocabulary.size()-(i+1)).getToken());
       //     toptkn.setIDF(analyzer.vocabulary.get(analyzer.vocabulary.size()-(i+1)).getIDF());
       //     toptkn.setValue(analyzer.vocabulary.get(analyzer.vocabulary.size()-(i+1)).getValue());
       //     top50IDF.add(toptkn);
       //     bottom50IDF.add(bottkn);
       // }

       // //Write Top 50 to file
       // File top50File = new File("top50Controlled.csv");
       // PrintWriter top50Writer = new PrintWriter(top50File);
       // top50Writer.println("Token" + "," + "IDF" + "," + "DF");
       // for (Token t : top50IDF) {
       //     top50Writer.println(t.getToken() + "," + t.getIDF() + "," + t.getValue());
       // }
       // top50Writer.close();

       // //Write bottom 50 to file
       // File bot50File = new File("bot50Controlled.csv");
       // PrintWriter bot50Writer = new PrintWriter(bot50File);
       // top50Writer.println("Token" + "," + "IDF" + "," + "DF");
        //for (Token t : top50IDF) {
            //bot50Writer.println(t.getToken() + "," + t.getIDF() + "," + t.getValue());
        //}
        //bot50Writer.close();

        //PART 3
        //Create index for looking up tokens
        HashMap<String, Integer> index = new HashMap<>();
        for (int i = 0; i < analyzer.vocabulary.size(); i++) {
            index.put(analyzer.vocabulary.get(i).getToken(), i);
            analyzer.vocabulary.get(i).setID(i);
        }

        //Compute vector representation of every document.
        for (Post p : analyzer.m_reviews) {
            //Use the keyset not getTokens because that's only the actual token/1grams
            for (String str : p.tokenToTF.keySet()) {
                int id = index.get(str);
                double tf = p.tokenToTF.get(str);
                double subLin_tf = 1 + Math.log(tf);

                double idf = analyzer.vocabulary.get(id).getIDF();
                double tf_idf = subLin_tf * idf;
                p.idToTF_IDF.put(id, tf_idf);
            }
        }

        //Load special queries, convert to vector space representation and find most similar
        //documents by cosine measure.
        ArrayList<Post> queries = new ArrayList<>();
        JSONObject query = analyzer.LoadJson("./data/samples/query.json");
        JSONArray jarray = query.getJSONArray("Reviews");
        for(int i=0; i<jarray.length(); i++) {
            Post review = new Post(jarray.getJSONObject(i));
            String content = review.getContent();
            String[] tokens = analyzer.tokenizer.tokenize(content);
            ArrayList<String> tokenList = new ArrayList<>();
            for (String token : tokens) {
                String parsedToken = analyzer.SnowballStemming(analyzer.Normalization(token));
                if (!parsedToken.isEmpty()) {
                    tokenList.add(parsedToken);
                }
            }
            review.setTokens(tokenList);
            queries.add(review);
        }
        for (Post p : queries) {
            HashMap<String, Integer> tf = new HashMap<>();
            for (int i = 0; i < p.getTokens().size()-1; i++) {
                String str1 = p.getTokens().get(i);
                String str2 = p.getTokens().get(i) + " " + p.getTokens().get(i+1);
                if (tf.containsKey(str1)) {
                    tf.put(str1, tf.get(str1) + 1);
                }
                else {
                    tf.put(str1, 1);
                }
                if (tf.containsKey(str2)) {
                    tf.put(str2, tf.get(str2) + 1);
                }
                else {
                    tf.put(str2, 1);
                }
            }
            String str1 = p.getTokens().get(p.getTokens().size()-1);
            if (tf.containsKey(str1)) {
                tf.put(str1, tf.get(str1) + 1);
            }
            else {
                tf.put(str1, 1);
            }
            for (String str : tf.keySet()) {
                Integer tfInt = tf.get(str);
                p.tokenToTF.put(str, tfInt);
                Integer id = index.get(str);
                if (id != null) {
                    p.idToTF_IDF.put(id, (1 + Math.log(tfInt)) * analyzer.vocabulary.get(id).getIDF());
                }
            }
        }

        //FIND MOST SIMILAR
        PriorityQueue<Post> similarTo0 = new PriorityQueue<>(new Comparator<Post>() {
            @Override
            public int compare(Post o, Post t1) {
                double osim = queries.get(0).similiarity(o);
                double t1sim = queries.get(0).similiarity(t1);
                if (osim > t1sim) {
                    return 1;
                }
                else if (osim == t1sim) {
                    return 0;
                }
                return -1;
            }
        });
       // PriorityQueue<Post> similarTo1 = new PriorityQueue<>(new Comparator<Post>() {
       //     @Override
       //     public int compare(Post o, Post t1) {
       //         double osim = queries.get(1).similiarity(o);
       //         double t1sim = queries.get(1).similiarity(t1);
       //         if (osim > t1sim) {
       //             return 1;
       //         }
       //         else if (osim == t1sim) {
       //             return 0;
       //         }
       //         return -1;
       //     }
       // });
       // PriorityQueue<Post> similarTo2 = new PriorityQueue<>(new Comparator<Post>() {
       //     @Override
       //     public int compare(Post o, Post t1) {
       //         double osim = queries.get(2).similiarity(o);
       //         double t1sim = queries.get(2).similiarity(t1);
       //         if (osim > t1sim) {
       //             return 1;
       //         }
       //         else if (osim == t1sim) {
       //             return 0;
       //         }
       //         return -1;
       //     }
       // });
       // PriorityQueue<Post> similarTo3 = new PriorityQueue<>(new Comparator<Post>() {
       //     @Override
       //     public int compare(Post o, Post t1) {
       //         double osim = queries.get(3).similiarity(o);
       //         double t1sim = queries.get(3).similiarity(t1);
       //         if (osim > t1sim) {
       //             return 1;
       //         }
       //         else if (osim == t1sim) {
       //             return 0;
       //         }
       //         return -1;
       //     }
       // });
       // PriorityQueue<Post> similarTo4 = new PriorityQueue<>(new Comparator<Post>() {
       //     @Override
       //     public int compare(Post o, Post t1) {
       //         double osim = queries.get(4).similiarity(o);
       //         double t1sim = queries.get(4).similiarity(t1);
       //         if (osim > t1sim) {
       //             return 1;
       //         }
       //         else if (osim == t1sim) {
       //             return 0;
       //         }
       //         return -1;
       //     }
       // });

        ArrayList<PriorityQueue<Post>> queues = new ArrayList<>();
        queues.add(similarTo0);
       // queues.add(similarTo1);
       // queues.add(similarTo2);
       // queues.add(similarTo3);
       // queues.add(similarTo4);

        for (Post p : analyzer.m_reviews) {
            for (PriorityQueue q : queues) {
                q.add(p);
                if (q.size() > 3) {
                    q.poll();
                }
            }
        }

        for (int i = 0; i < 1; i ++) {
            PriorityQueue<Post> q = queues.get(i);
            File qFile = new File("similarTo" + i + ".txt");
            PrintWriter qWriter = new PrintWriter(qFile);
            qWriter.println("COMPARING TO : " + queries.get(i).getContent());
            qWriter.println();
            for (Post s : q) {
                qWriter.println(s.getContent());
                qWriter.println();
            }
            qWriter.close();
        }

    }
}

