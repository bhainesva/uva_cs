/**
 *
 */
package analyzer;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.porterStemmer;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import structures.LanguageModel;
import structures.Post;
import structures.Token;

/**
 * @author hongning
 * Sample codes for demonstrating OpenNLP package usage
 * NOTE: the code here is only for demonstration purpose,
 * please revise it accordingly to maximize your implementation's efficiency!
 */
public class NewAnalyzer {
    //N-gram to be created
    int m_N;

    //proportion of positive reviews
    double p_y0;
    double p_y1;

    //Counts of positive reviews
    double numPos;
    double size;

    //a list of stopwords
    HashSet<String> m_stopwords;

    //you can store the loaded reviews in this arraylist for further processing
    ArrayList<Post> m_reviews;

    //you might need something like this to store the counting statistics for validating Zipf's and computing IDF
    HashMap<String, Token> m_stats;

    //we have also provided a sample implementation of language model in src.structures.LanguageModel
    Tokenizer m_tokenizer;

    //this structure is for language modeling
    LanguageModel m_posLangModel;
    LanguageModel m_negLangModel;

    //this structure holds all of the vocab words
    ArrayList<Token> m_vocabulary;

    //this structure holds the words that get chosen as features
    ArrayList<String> m_features;
    ArrayList<Token> m_featureTokens;

    //this structure holds randomly generated projection vectors
    ArrayList<ArrayList<Double>> m_projections;

    //this structure holds randomly generated projection vectors
    HashMap<ArrayList<Integer>, HashSet<Post>> m_buckets;

    //this structure holds tokens with indexes
    HashMap<String, Integer> index;

    //this structure holds the seeds for KNN
    ArrayList<Post> m_seeds;

    HashMap<String, Double> docFreqs = new HashMap<>();
    HashMap<String, Double> posDocFreqs = new HashMap<>();

    HashMap<Integer, HashSet<Post>> m_folds;

    public NewAnalyzer(String tokenModel, int N) throws InvalidFormatException, FileNotFoundException, IOException {
        m_N = N;
        p_y0 = 0.0;
        p_y1 = 0.0;
        numPos = 0.0;
        m_reviews = new ArrayList<Post>();
        m_tokenizer = new TokenizerME(new TokenizerModel(new FileInputStream(tokenModel)));
        m_vocabulary = new ArrayList<>();
        m_stopwords = new HashSet<>();
        m_features = new ArrayList<>();
        m_featureTokens = new ArrayList<>();
        m_posLangModel = new LanguageModel();
        m_negLangModel = new LanguageModel();
        m_projections = new ArrayList<>();
        m_buckets = new HashMap<>();
        index = new HashMap<>();
        m_seeds = new ArrayList<>();
        m_folds = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            m_folds.put(i, new HashSet<>());
        }
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

    public void LoadProjections(String filename) throws FileNotFoundException {
        String delimiter = ",";
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] projAsString = line.split(delimiter);
            ArrayList<Double> projection = new ArrayList<>();
            for (String str : projAsString) {
                projection.add(Double.parseDouble(str));
            }
            m_projections.add(projection);
        }
    }

    public void analyzeDocument(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                String[] tokens = Tokenize(review.getContent());

                /**
                 * HINT: perform necessary text processing here based on the tokenization results
                 * e.g., tokens -> normalization -> stemming -> N-gram -> stopword removal -> to vector
                 * The Post class has defined a "HashMap<String, Token> m_vector" field to hold the vector representation
                 * For efficiency purpose, you can accumulate a term's DF here as well
                 */
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty() && !m_stopwords.contains(parsedToken)) {
                        tokenList.add(parsedToken);
                    }
                }

                String[] tokenListArray = tokenList.toArray(new String[tokenList.size()]);
                review.setTokens(tokenListArray);

                // Accumulate raw TF for each token
                for (String str : tokenList) {
                    if (review.tokenToTF.containsKey(str)) {
                        review.tokenToTF.put(str, review.tokenToTF.get(str) + 1);
                    }
                    else {
                        review.tokenToTF.put(str, 1.0);
                    }
                }

                // Sub-linear normalize TF
                for (String key : review.tokenToTF.keySet()) {
                    review.tokenToTF.put(key, 1.0 + Math.log(review.tokenToTF.get(key)));
                }

                m_reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void analyzeCorpusDocument(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                String[] tokens = Tokenize(review.getContent());
                TreeSet<String> docGrams = new TreeSet<>();

                /**
                 * HINT: perform necessary text processing here based on the tokenization results
                 * e.g., tokens -> normalization -> stemming -> N-gram -> stopword removal -> to vector
                 * The Post class has defined a "HashMap<String, Token> m_vector" field to hold the vector representation
                 * For efficiency purpose, you can accumulate a term's DF here as well
                 */
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty() && !m_stopwords.contains(parsedToken) && m_features.contains(parsedToken)) {
                        tokenList.add(parsedToken);
                        docGrams.add(token);
                    }
                }

                if (docGrams.size() <= 5) {
                    continue;
                }

                String[] tokenListArray = tokenList.toArray(new String[tokenList.size()]);
                review.setTokens(tokenListArray);

//                // Accumulate raw TF for each token
//                for (String str : tokenList) {
//                    if (review.tokenToTF.containsKey(str)) {
//                        review.tokenToTF.put(str, review.tokenToTF.get(str) + 1);
//                    }
//                    else {
//                        review.tokenToTF.put(str, 1.0);
//                    }
//                }
//
//                // Sub-linear normalize TF
//                for (String key : review.tokenToTF.keySet()) {
//                    review.tokenToTF.put(key, 1.0 + Math.log(review.tokenToTF.get(key)));
//                }

                m_reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CreateVocabulary() {
        //Freq stores DF for each token
        for (Post p : m_reviews) {
            boolean isPos = false;
            TreeSet<String> docGrams = new TreeSet<>();

            //Check whether the review is positive (y = 1) or negative (y = 0)
            if (p.getRating() >= 4) {
                numPos++;
                isPos = true;
            }

            // Create set of words contained in this review
            for (int i = 0; i < p.getTokens().length; i++) {
                String token = p.getTokens()[i];
                if (!docGrams.contains(token)) {
                    docGrams.add(token);
                }
            }

            // For each word, add 1 to DF count
            // If the review is positive add one to posDF count
            for (String str : docGrams) {
                if (docFreqs.containsKey(str)) {
                    docFreqs.put(str, docFreqs.get(str) + 1.0);
                } else {
                    docFreqs.put(str, 1.0);
                }

                if (isPos) {
                    if (posDocFreqs.containsKey(str)) {
                        posDocFreqs.put(str, posDocFreqs.get(str) + 1.0);
                    } else {
                        posDocFreqs.put(str, 1.0);
                    }
                }
                else {
                    if (!posDocFreqs.containsKey(str)) {
                        posDocFreqs.put(str, 0.0);
                    }
                }
            }

            //Set p_y1
            p_y1 = numPos / (double)m_reviews.size();
            p_y0 = 1 - p_y1;
        }

    }

    public void LMAnalyze(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                /**
                 * HINT: perform necessary text processing here, e.g., tokenization, stemming and normalization
                 */
                String content = review.getContent();
                String[] tokens = Tokenize(content);
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty() && !m_stopwords.contains(parsedToken) && m_features.contains(parsedToken)) {
                        tokenList.add(parsedToken);
                    }
                }

                if (tokenList.size() <= 5) {
                    continue;
                }

                //get term frequencies
                if (review.getRating() >= 4) {
                    numPos++;
                    for (int j=0; j < tokenList.size(); j++) {
                        if (m_posLangModel.m_model.containsKey(tokenList.get(j))) {
                            m_posLangModel.m_model.get(tokenList.get(j)).incTF();
                            m_posLangModel.total_unigram_count += 1;
                        }
                        else {
                            Token tk = new Token(tokenList.get(j));
                            tk.setTF(1.0);
                            m_posLangModel.m_model.put(tokenList.get(j), tk);
                            m_posLangModel.unique_unigram_count += 1;
                            m_posLangModel.total_unigram_count += 1;
                        }
                    }
                }
                else {
                    for (int j=0; j < tokenList.size(); j++) {
                        if (m_negLangModel.m_model.containsKey(tokenList.get(j))) {
                            m_negLangModel.m_model.get(tokenList.get(j)).incTF();
                            m_negLangModel.total_unigram_count += 1;
                        }
                        else {
                            Token tk = new Token(tokenList.get(j));
                            tk.setTF(1.0);
                            m_negLangModel.m_model.put(tokenList.get(j), tk);
                            m_negLangModel.unique_unigram_count += 1;
                            m_negLangModel.total_unigram_count += 1;
                        }
                    }

                }
                String[] tokenListArray = tokenList.toArray(new String[tokenList.size()]);
                review.setTokens(tokenListArray);
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
            if (f.isFile() && f.getName().endsWith(suffix))
                analyzeDocument(LoadJson(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadCorpusDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix))
                analyzeCorpusDocument(LoadJson(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadCorpusDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadLanguageModelDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix))
                LMAnalyze(LoadJson(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadLanguageModelDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadKNNInputDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix))
                KNNFile(LoadJson(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadKNNInputDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);
    }


    public void KNNAnalyze(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                String[] tokens = Tokenize(review.getContent());

                // Create set of words contained in this review
                TreeSet<String> docGrams = new TreeSet<>();

                /**
                 * HINT: perform necessary text processing here based on the tokenization results
                 * e.g., tokens -> normalization -> stemming -> N-gram -> stopword removal -> to vector
                 * The Post class has defined a "HashMap<String, Token> m_vector" field to hold the vector representation
                 * For efficiency purpose, you can accumulate a term's DF here as well
                 */
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty() && !m_stopwords.contains(parsedToken) && m_features.contains(parsedToken)) {
                        tokenList.add(parsedToken);
                        docGrams.add(parsedToken);
                    }
                }

                if (docGrams.size() <= 5) {
                    continue;
                }

                String[] tokenListArray = tokenList.toArray(new String[tokenList.size()]);
                review.setTokens(tokenListArray);

                // Accumulate raw TF for each token
                for (String str : tokenList) {
                    if (review.tokenToTF.containsKey(str)) {
                        review.tokenToTF.put(str, review.tokenToTF.get(str) + 1);
                    }
                    else {
                        review.tokenToTF.put(str, 1.0);
                    }
                }

                // Sub-linear normalize TF
                for (String key : review.tokenToTF.keySet()) {
                    review.tokenToTF.put(key, 1.0 + Math.log(review.tokenToTF.get(key)));
                }

                // For each word, add 1 to DF count
                for (String str : docGrams) {
                    if (docFreqs.containsKey(str)) {
                        docFreqs.put(str, docFreqs.get(str) + 1.0);
                    } else {
                        docFreqs.put(str, 1.0);
                    }
                }

                m_reviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void FoldsAnalyze(JSONObject json) {
//        Random random = new Random();
        try {
            int count = 0;
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));

                String[] tokens = Tokenize(review.getContent());

                // Create set of words contained in this review
                TreeSet<String> docGrams = new TreeSet<>();

                /**
                 * HINT: perform necessary text processing here based on the tokenization results
                 * e.g., tokens -> normalization -> stemming -> N-gram -> stopword removal -> to vector
                 * The Post class has defined a "HashMap<String, Token> m_vector" field to hold the vector representation
                 * For efficiency purpose, you can accumulate a term's DF here as well
                 */
                ArrayList<String> tokenList = new ArrayList<>();
                for (String token : tokens) {
                    String parsedToken = SnowballStemming(Normalization(token));
                    if (!parsedToken.isEmpty() && !m_stopwords.contains(parsedToken) && m_features.contains(parsedToken)) {
                        tokenList.add(parsedToken);
                        docGrams.add(parsedToken);
                    }
                }

                if (docGrams.size() <= 5) {
                    continue;
                }

                String[] tokenListArray = tokenList.toArray(new String[tokenList.size()]);
                review.setTokens(tokenListArray);

                // Accumulate raw TF for each token
                for (String str : tokenList) {
                    if (review.tokenToTF.containsKey(str)) {
                        review.tokenToTF.put(str, review.tokenToTF.get(str) + 1);
                    }
                    else {
                        review.tokenToTF.put(str, 1.0);
                    }
                }

                // Sub-linear normalize TF
                for (String key : review.tokenToTF.keySet()) {
                    review.tokenToTF.put(key, 1.0 + Math.log(review.tokenToTF.get(key)));
                }

//                Integer fold = random.nextInt(10);
                Integer fold = count % 10;
                m_folds.get(fold).add(review);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadFolds(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix))
                FoldsAnalyze(LoadJson(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadFolds(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);

    }

    public void TrainModels(Integer testSet) {
        System.out.println("Training Model - Excluding Set " + testSet);
        //Clear data from previous trainings
        docFreqs = new HashMap<>();
        posDocFreqs = new HashMap<>();
        m_posLangModel = new LanguageModel();
        m_negLangModel = new LanguageModel();
        numPos = 0.0;
        m_vocabulary = new ArrayList<>();
        index = new HashMap<>();
        m_buckets = new HashMap<>();
        size = 0.0;
        CreateIndex();


        for (int i = 0; i < 10; i++) {
            if (i == testSet) {
                continue;
            }

            size += m_folds.get(i).size();

            for (Post review : m_folds.get(i)) {
                boolean isPos = false;
                String[] tokens = review.getTokens();
                Set<String> docGrams = new HashSet<>(Arrays.asList(tokens));

                if (getClass(review) == 1) {
                    numPos++;
                    isPos = true;
                    for (int j=0; j < tokens.length; j++) {
                        if (m_posLangModel.m_model.containsKey(tokens[j])) {
                            m_posLangModel.m_model.get(tokens[j]).incTF();
                            m_posLangModel.total_unigram_count += 1;
                        }
                        else {
                            Token tk = new Token(tokens[j]);
                            tk.setTF(1.0);
                            m_posLangModel.m_model.put(tokens[j], tk);
                            m_posLangModel.unique_unigram_count += 1;
                            m_posLangModel.total_unigram_count += 1;
                        }
                    }
                }

                else {
                    for (int j=0; j < tokens.length; j++) {
                        if (m_negLangModel.m_model.containsKey(tokens[j])) {
                            m_negLangModel.m_model.get(tokens[j]).incTF();
                            m_negLangModel.total_unigram_count += 1;
                        }
                        else {
                            Token tk = new Token(tokens[j]);
                            tk.setTF(1.0);
                            m_negLangModel.m_model.put(tokens[j], tk);
                            m_negLangModel.unique_unigram_count += 1;
                            m_negLangModel.total_unigram_count += 1;
                        }
                    }

                }

                // For each word, add 1 to DF count
                for (String str : docGrams) {
                    if (docFreqs.containsKey(str)) {
                        docFreqs.put(str, docFreqs.get(str) + 1.0);
                    } else {
                        docFreqs.put(str, 1.0);
                    }

                    if (isPos) {
                        if (posDocFreqs.containsKey(str)) {
                            posDocFreqs.put(str, posDocFreqs.get(str) + 1.0);
                        } else {
                            posDocFreqs.put(str, 1.0);
                        }
                    }
                    else {
                        if (!posDocFreqs.containsKey(str)) {
                            posDocFreqs.put(str, 0.0);
                        }
                    }
                }

            }
        }

        // Compute and store DF, IDF, posDF, IG, and Chi for each token
        for (String str: m_features) {
            Token tkn = new Token(str);
            tkn.setDF(docFreqs.getOrDefault(str, 0.0));
            if (docFreqs.containsKey(str)) {
                tkn.setIDF(1.0 + Math.log(size / docFreqs.get(str)));
                tkn.setPosDF(posDocFreqs.get(str));
            }
            else {
                tkn.setIDF(0.0);
                tkn.setPosDF(0.0);
            }
            m_vocabulary.add(tkn);
        }

        //Create index for looking up tokens by string
//        for (int j = 0; j < m_vocabulary.size(); j++) {
//            index.put(m_vocabulary.get(j).getToken(), j);
//            m_vocabulary.get(j).setID(j);
//        }

        //Compute vector representation of every document.
        for (int i = 0; i < 10; i++) {
            if (i == testSet) {
                continue;
            }
            for (Post p : m_folds.get(i)) {
                for (String str : p.tokenToTF.keySet()) {
                    Integer id = index.get(str);

                    double tf = p.tokenToTF.get(str);

                    double idf = m_vocabulary.get(id).getIDF();
                    double tf_idf = tf * idf;
                    p.idToTF_IDF.put(id, tf_idf);
                }

                ArrayList<Integer> hashVector = HashReview(p);

                if (m_buckets.containsKey(hashVector)) {
                    m_buckets.get(hashVector).add(p);
                }
                else {
                    HashSet<Post> key = new HashSet();
                    key.add(p);
                    m_buckets.put(hashVector, key);
                }
            }
        }
    }

    public void EvaluateModels(Integer testSet) throws FileNotFoundException, UnsupportedEncodingException {
        m_seeds = new ArrayList<>();
        PrintWriter evalWriter = new PrintWriter("evalResultsTestSet" + testSet + "timingl11.txt", "UTF-8");
        evalWriter.println("Actual Class,Bayes Predicted Class,KNN Predicted Class");
        evalWriter.println();
        for (Post query : m_folds.get(testSet)) {
            EncodePost(query);
            m_seeds.add(query);

        }
        for (Post p : m_seeds) {
            int classVote = 0;
            ArrayList<Integer> seedBucket = HashReview(p);

            PriorityQueue<Post> neighbours = new PriorityQueue<>(new Comparator<Post>() {
                                                                                    @Override
                                                                                    public int compare(Post o, Post t1) {
                                                                                    double osim = o.similarity(p);
                                                                                    double t1sim = t1.similarity(p);
                                                                                    if (osim < t1sim) {
                                                                                    return -1;
                                                                                    }
                                                                                    else if (osim == t1sim) {
                                                                                    return 0;
                                                                                    }
                                                                                    return 1;
                                                                                    }
                                                                                    });

            for (Post candidate : m_buckets.get(seedBucket)) {
                neighbours.add(candidate);
                if (neighbours.size() > 5) {
                    neighbours.poll();
                }
            }
            while(!neighbours.isEmpty()) {
                Post res = neighbours.poll();
                classVote += getClass(res);
            }
            int knnPredClass = classVote >= 4 ? 1 : 0;
            int bayesPredClass = EvalClassifyDoc(p);

            evalWriter.println(getClass(p) + "," + bayesPredClass + "," + knnPredClass);
        }
        evalWriter.close();
    }

    // sample code for demonstrating how to recursively load files in a directory
    public void LoadKNNDirectory(String folder, String suffix) {
        File dir = new File(folder);
        int size = m_reviews.size();
        for (File f : dir.listFiles()) {
            if (f.isFile() && f.getName().endsWith(suffix))
                KNNAnalyze(LoadJson(f.getAbsolutePath()));
            else if (f.isDirectory())
                LoadKNNDirectory(f.getAbsolutePath(), suffix);
        }
        size = m_reviews.size() - size;
        System.out.println("Loading " + size + " review documents from " + folder);

    }

    public void KNNVectorise() {
        //Create index for looking up tokens by string
        for (int i = 0; i < m_vocabulary.size(); i++) {
            index.put(m_vocabulary.get(i).getToken(), i);
            m_vocabulary.get(i).setID(i);
        }

        //Compute vector representation of every document.
        for (Post p : m_reviews) {
            for (String str : p.tokenToTF.keySet()) {
                Integer id = index.get(str);

                double tf = p.tokenToTF.get(str);

                double idf = m_vocabulary.get(id).getIDF();
                double tf_idf = tf * idf;
                p.idToTF_IDF.put(id, tf_idf);
            }
        }
    }

    public void KNNFile(JSONObject json) {
        try {
            JSONArray jarray = json.getJSONArray("Reviews");
            for(int i=0; i<jarray.length(); i++) {
                Post review = new Post(jarray.getJSONObject(i));
                EncodePost(review);
                m_seeds.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void KNN() throws FileNotFoundException, UnsupportedEncodingException {
        int count = 0;
        for (Post p : m_seeds) {
            ArrayList<Integer> seedBucket = HashReview(p);

            PriorityQueue<Post> neighbours = new PriorityQueue<>(new Comparator<Post>() {
                @Override
                public int compare(Post o, Post t1) {
                    double osim = o.similarity(p);
                    double t1sim = t1.similarity(p);
                    if (osim < t1sim) {
                        return -1;
                    }
                    else if (osim == t1sim) {
                        return 0;
                    }
                    return 1;
                }
            });

            for (Post candidate : m_buckets.get(seedBucket)) {
                neighbours.add(candidate);
                if (neighbours.size() > 5) {
                    neighbours.poll();
                }
            }

            PrintWriter knnWriter = new PrintWriter("knnResults" + count + "timingl11.txt", "UTF-8");
            knnWriter.println("Input Query: " + p.getContent());
            knnWriter.println("===========================");
            knnWriter.println();
            while(!neighbours.isEmpty()) {
                Post res = neighbours.poll();
                knnWriter.println("Score: " + res.similarity(p));
                knnWriter.println("Result: " + res.getContent());
                knnWriter.println();
            }
            knnWriter.close();
            count++;
        }
    }

    public void BruteKNN() throws FileNotFoundException, UnsupportedEncodingException {
        int count = 0;
        for (Post p : m_seeds) {
            PriorityQueue<Post> neighbours = new PriorityQueue<>(new Comparator<Post>() {
                @Override
                public int compare(Post o, Post t1) {
                    double osim = o.similarity(p);
                    double t1sim = t1.similarity(p);
                    if (osim < t1sim) {
                        return -1;
                    }
                    else if (osim == t1sim) {
                        return 0;
                    }
                    return 1;
                }
            });

            for (Post candidate : m_reviews) {
                neighbours.add(candidate);
                if (neighbours.size() > 5) {
                    neighbours.poll();
                }
            }

            PrintWriter knnWriter = new PrintWriter("BruteKNNResults" + count + ".txt", "UTF-8");
            knnWriter.println("Input Query: " + p.getContent());
            knnWriter.println("===========================");
            knnWriter.println();
            while(!neighbours.isEmpty()) {
                Post res = neighbours.poll();
                knnWriter.println("Score: " + res.similarity(p));
                knnWriter.println("Result: " + res.getContent());
                knnWriter.println();
            }
            knnWriter.close();
            count++;
        }
    }

    //sample code for demonstrating how to use Snowball stemmer
    public String SnowballStemming(String token) {
        SnowballStemmer stemmer = new englishStemmer();
        stemmer.setCurrent(token);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return token;
    }

    //sample code for demonstrating how to use Porter stemmer
    public String PorterStemming(String token) {
        porterStemmer stemmer = new porterStemmer();
        stemmer.setCurrent(token);
        if (stemmer.stem())
            return stemmer.getCurrent();
        else
            return token;
    }

    //sample code for demonstrating how to perform text normalization
    //you should implement your own normalization procedure here
    public String Normalization(String token) {
        // remove all English punctuation
        token = token.replaceAll("\\p{Punct}+", "");
        // convert to lower case
        token = token.toLowerCase();

        //Note that this doesn't attempt to recognize doubles
        //This is because all periods were removed in the previous step.
        //At this point in the processing a double looks the same as an int.
        token = token.replaceAll("\\d+", "NUM");

        return token;
    }

    String[] Tokenize(String text) {
        return m_tokenizer.tokenize(text);
    }

    public double InformationGain(Token tkn) {
        double term1 = 0.0;
        double term2 = 0.0;
        double term3 = 0.0;

        //Calc probabilities
        double y1_given_t = tkn.getPosDF()/tkn.getDF();
        double y0_given_t = 1 - y1_given_t;
        double y1_given_no_t = (numPos - tkn.getPosDF())/(m_reviews.size() - tkn.getDF());
        double y0_given_no_t = 1 - y1_given_no_t;
        double p_t = tkn.getDF()/m_reviews.size();
        double p_no_t = 1 - p_t;

        //Calculate term 1
        term1 += p_y0 * Math.log(p_y0);
        term1 += p_y1 * Math.log(p_y1);

        //Calculate term 2
        if (y0_given_t != 0) {
            term2 += y0_given_t * Math.log(y0_given_t);
        }
        if (y1_given_t != 0) {
            term2 += y1_given_t * Math.log(y1_given_t);
        }

        term2 *= p_t;

        //Calculate term3
        if (y0_given_no_t != 0) {
            term3 += y0_given_no_t * Math.log(y0_given_no_t);
        }
        if (y1_given_t != 0) {
            term3 += y1_given_no_t * Math.log(y1_given_no_t);
        }

        term3 *= p_no_t;

        return -term1 + term2 + term3;
    }

    public double ChiSquareScore(Token tkn) {
        double A = tkn.getPosDF();
        double B = numPos - A;
        double C = tkn.getDF() - A;
        double D = (m_reviews.size() - numPos) - C;

        double numerator = (A + B + C + D) * Math.pow((A*D - B*C),2);
        double denominator = (A + C) * (B + D) * (A + B) * (C + D);

        return numerator / denominator;
    }

    public void FilterVocab() {
        //Filter by DF
        m_vocabulary.removeIf(s -> s.getDF() < 50);

        //Filter by Chi^2
        m_vocabulary.removeIf(s -> s.getChi() < 3.841);
    }

    public ArrayList<Token> tokensByIG() {
        ArrayList<Token> sorted = new ArrayList<>();

        for (Token t : m_vocabulary){
            sorted.add(t);
        }

        Collections.sort(sorted, new Comparator<Token>() {
            @Override
            public int compare(Token s, Token t1) {
                return Double.compare(s.getIG(), t1.getIG());
            }
        }.reversed());

        if (sorted.size() > 5000) {
            sorted.subList(5000, sorted.size()).clear();
        }
        return sorted;
    }

    public ArrayList<Token> tokensByChi() {
        ArrayList<Token> sorted = new ArrayList<>();

        for (Token t : m_vocabulary){
            sorted.add(t);
        }

        Collections.sort(sorted, new Comparator<Token>() {
            @Override
            public int compare(Token s, Token t1) {
                return Double.compare(s.getChi(), t1.getChi()); }
        }.reversed());

        if (sorted.size() > 5000) {
            sorted.subList(5000, sorted.size()).clear();
        }
        return sorted;
    }

    public ArrayList<String> tokensByLMRatio() {
        ArrayList<String> sorted = new ArrayList<>();

        for (String t : m_features){
            sorted.add(t);
        }

        Collections.sort(sorted, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return Double.compare(Math.log(m_posLangModel.additiveSmoothedUnigramProb(s)) - Math.log(m_negLangModel.additiveSmoothedUnigramProb(s)), Math.log(m_posLangModel.additiveSmoothedUnigramProb(t1)) - Math.log(m_negLangModel.additiveSmoothedUnigramProb(t1)));
            }
        });

        return sorted;
    }

    public ArrayList<Post> reviewsByFx() {
        ArrayList<Post> sorted = new ArrayList<>();

        for (Post p : m_reviews){
            sorted.add(p);
        }

        Collections.sort(sorted, new Comparator<Post>() {
            @Override
            public int compare(Post s, Post t1) {
                return -Double.compare(f(s), f(t1));
            }
        });

        return sorted;
    }

    public Integer getClass(Post p) {
        return p.getRating() >= 4 ? 1 : 0;
    }
    public double f(Post X) {
        String[] tokens = X.getTokens();

        Double t1 = Math.log((numPos/(double)(m_reviews.size() - numPos)));
        Double t2 = 0.0;
        for (String tk : tokens) {
            t2 += Math.log(m_posLangModel.additiveSmoothedUnigramProb(tk)) - Math.log(m_negLangModel.additiveSmoothedUnigramProb(tk));
        }

        Double fx = t1 + t2;
        return fx;
    }

    public double f2(Post X) {
        String[] tokens = X.getTokens();

        Double t1 = Math.log((numPos/(double)(size - numPos)));
        Double t2 = 0.0;
        for (String tk : tokens) {
            t2 += Math.log(m_posLangModel.additiveSmoothedUnigramProb(tk)) - Math.log(m_negLangModel.additiveSmoothedUnigramProb(tk));
        }

        Double fx = t1 + t2;
        return fx;
    }

    public int ClassifyDoc(Post X) {
        Double fx = f(X);
        return fx >= 0 ? 1 : 0;
    }

    public int EvalClassifyDoc(Post X) {
        Double fx = f2(X);
        return fx >= 0 ? 1 : 0;
    }

    public ArrayList<Integer> HashReview(Post p) {
        ArrayList<Integer> hashVector = new ArrayList<>();
        for (ArrayList<Double> projection : m_projections) {
            Double dotProd = p.innerProduct(projection);
            Integer bit = dotProd >= 0 ? 1 : 0;
            hashVector.add(bit);
        }

        return hashVector;
    }

    public void HashReviews() {
        for (Post p : m_reviews) {
            ArrayList<Integer> hashVector = HashReview(p);

            if (m_buckets.containsKey(hashVector)) {
                m_buckets.get(hashVector).add(p);
            }
            else {
                HashSet<Post> key = new HashSet();
                key.add(p);
                m_buckets.put(hashVector, key);
            }
        }
    }

    public void EncodePost(Post p) {
        String[] tokens = Tokenize(p.getContent());

        ArrayList<String> tokenList = new ArrayList<>();
        for (String token : tokens) {
            String parsedToken = SnowballStemming(Normalization(token));
            if (!parsedToken.isEmpty() && !m_stopwords.contains(parsedToken) && m_features.contains(parsedToken)) {
                tokenList.add(parsedToken);
            }
        }

        String[] tokenListArray = tokenList.toArray(new String[tokenList.size()]);
        p.setTokens(tokenListArray);

        // Accumulate raw TF for each token
        for (String str : tokenList) {
            if (p.tokenToTF.containsKey(str)) {
                p.tokenToTF.put(str, p.tokenToTF.get(str) + 1);
            }
            else {
                p.tokenToTF.put(str, 1.0);
            }
        }

        // Sub-linear normalize TF
        for (String key : p.tokenToTF.keySet()) {
            p.tokenToTF.put(key, 1.0 + Math.log(p.tokenToTF.get(key)));
        }

        //Compute vector representation
        for (String str : p.tokenToTF.keySet()) {
            Integer id = index.get(str);

            double tf = p.tokenToTF.get(str);

            double idf = m_vocabulary.get(id).getIDF();
            double tf_idf = tf * idf;
            p.idToTF_IDF.put(id, tf_idf);
        }
    }

    public void CreateIndex() {
        for (int i = 0; i < m_features.size(); i++) {
            index.put(m_features.get(i), i);
        }
    }
    public void LoadFeatures(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {
                //it is very important that you perform the same processing operation to the loaded stopwords
                //otherwise it won't be matched in the text content
                line = SnowballStemming(Normalization(line));
                if (!line.isEmpty()) {
                    m_features.add(line);
                    Token tkn = new Token(line);
                    m_featureTokens.add(tkn);
                }
            }
            reader.close();
            System.out.format("Loading %d features from %s\n", m_features.size(), filename);
        } catch(IOException e){
            System.err.format("[Error]Failed to open file %s!!", filename);
        }

    }
    public static void main(String[] args) throws InvalidFormatException, FileNotFoundException, IOException {
        //Part One
        //Initialize Analyzer
        NewAnalyzer analyzer = new NewAnalyzer("./data/Model/en-token.bin", 2);

        //Load Stopwords
        analyzer.LoadStopwords("./data/Model/english.stop");

//        //Load Reviews
//        analyzer.LoadDirectory("./data/yelp", "json");
//
//        //Create Vocabulary
//        analyzer.CreateVocabulary();
//        // Compute and store DF, IDF, posDF, IG, and Chi for each token
//        for (String str: analyzer.docFreqs.keySet()) {
//            Token tkn = new Token(str);
//            tkn.setDF(analyzer.docFreqs.get(str));
//            tkn.setIDF(1.0 + Math.log(((double)analyzer.m_reviews.size()) / analyzer.docFreqs.get(str)));
//            tkn.setPosDF(analyzer.posDocFreqs.get(str));
//            tkn.setIG(analyzer.InformationGain(tkn));
//            tkn.setChi(analyzer.ChiSquareScore(tkn));
//            analyzer.m_vocabulary.add(tkn);
//        }
//
//        //Filter out tokens that don't meet DF and Chi^2 requirements
//        analyzer.FilterVocab();
//
//        //Get top features from each measure
//        List<Token> chiFeatures = analyzer.tokensByChi();
//        List<Token> IGFeatures = analyzer.tokensByIG();
//
//        File chiFile = new File("chiFeatures.txt");
//        PrintWriter chiWriter = new PrintWriter(chiFile);
//        for (int i = 0; i < 20; i++) {
//            chiWriter.println(chiFeatures.get(i).getToken() + "," + chiFeatures.get(i).getChi());
//        }
//        chiWriter.close();
//
//        File IGFile = new File("IGFeatures.txt");
//        PrintWriter IGWriter = new PrintWriter(IGFile);
//        for (int i = 0; i < 20; i++) {
//            IGWriter.println(IGFeatures.get(i).getToken() + "," + IGFeatures.get(i).getIG());
//        }
//        IGWriter.close();
//
//        //Merge feature lists and write to file
//        Set<Token> featureSet = new HashSet<Token>(IGFeatures);
//        featureSet.addAll(chiFeatures);
//
//        File featureFile = new File("features.txt");
//        PrintWriter featureWriter = new PrintWriter(featureFile);
//        for (Token tk : featureSet) {
//            featureWriter.println(tk.getToken());
//        }
//        featureWriter.close();

//        Create feature vectors and stuff
//        analyzer.LoadFeatures("./features.txt");
//        analyzer.LoadCorpusDirectory("./data/yelp", "json");
        //Size of feature corpus
//        System.out.println("There are " + analyzer.m_reviews.size() + " documents in the corpus.");

        //PART TWO
//        analyzer.LoadFeatures("./features.txt");
//        analyzer.LoadLanguageModelDirectory("./data/yelp", "json");
//        ArrayList<String> sortedFeatures = analyzer.tokensByLMRatio();
//
//        File LMFile = new File("LMSortedFeatures.txt");
//        PrintWriter LMWriter = new PrintWriter(LMFile);
//        LMWriter.println("Lowest Ratios ----------------");
//        for (int i = 0; i < 20; i++) {
//            LMWriter.println(sortedFeatures.get(i) + "," + (Math.log(analyzer.m_posLangModel.additiveSmoothedUnigramProb(sortedFeatures.get(i))) - Math.log(analyzer.m_negLangModel.additiveSmoothedUnigramProb(sortedFeatures.get(i)))));
//        }
//
//        LMWriter.println("Highest Ratios ----------------");
//        for (int i = 1; i < 21; i++) {
//            LMWriter.println(sortedFeatures.get(sortedFeatures.size() - i) + "," + (Math.log(analyzer.m_posLangModel.additiveSmoothedUnigramProb(sortedFeatures.get(sortedFeatures.size() - i))) - Math.log(analyzer.m_negLangModel.additiveSmoothedUnigramProb(sortedFeatures.get(sortedFeatures.size() - i)))));
//        }
//        LMWriter.close();
//
        //Classify all documents, sort by fx
//        ArrayList<Post> fxReviews = analyzer.reviewsByFx();

        //Write data for plotting precision/recall curve
//        File fxFile = new File("fxRanks" + analyzer.m_posLangModel.m_delta + ".csv");
//        PrintWriter fxWriter = new PrintWriter(fxFile);
//        fxWriter.println("f(x), ybar, y");
//        for (Post p : fxReviews) {
//            double f = analyzer.f(p);
//            int ybar = f >= 0 ? 1 : 0;
//            int y = p.getRating() >= 4 ? 1 : 0;
//            fxWriter.println(f + "," + ybar + "," + y);
//        }
//        fxWriter.close();
//
//        //PART THREE - KNN
//        //Load Reviews
//        analyzer.LoadFeatures("./features.txt");
//        analyzer.LoadProjections("./data/Model/projectionVectors.txt");
//        analyzer.LoadKNNDirectory("./data/yelp", "json");
//        // Compute and store DF, IDF for each token
//        for (String str: analyzer.docFreqs.keySet()) {
//            Token tkn = new Token(str);
//            tkn.setDF(analyzer.docFreqs.get(str));
//            tkn.setIDF(1.0 + Math.log(((double)analyzer.m_reviews.size()) / analyzer.docFreqs.get(str)));
//            analyzer.m_vocabulary.add(tkn);
//        }
//        analyzer.KNNVectorise();
//
//        analyzer.LoadKNNInputDirectory("./data/samples/queries", "json");
//        analyzer.HashReviews();
//        analyzer.BruteKNN();
//        long startTime = System.nanoTime();
//        analyzer.KNN();
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime);
//        System.out.println("Projection Running Time: " + duration);



        //PART FOUR - PERFORMANCE EVALUATION
        long startTime = System.nanoTime();
        analyzer.LoadFeatures("./features.txt");
        analyzer.LoadProjections("./data/Model/projectionVectorsl11.txt");
        analyzer.LoadFolds("./data/yelp", "json");
        for (int fuck = 0; fuck < 10; fuck++) {
            analyzer.TrainModels(fuck);
            analyzer.EvaluateModels(fuck);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Running Time: " + duration);
    }
}

