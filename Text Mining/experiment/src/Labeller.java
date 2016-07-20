import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.BuildBinarizedDataset;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentTraining;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;


public class Labeller {

    public String m_filename = "";
    public Properties props;
    public StanfordCoreNLP pipeline;

    public Labeller() {
    }

    static void label()
            throws IOException {
        // Open the file
        FileInputStream fstream = new FileInputStream("labellingInput.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        String label;
        String strLine;
        PrintWriter labelWriter = new PrintWriter("labellingOutput.txt", "UTF-8");

        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            System.out.println(strLine);
            label = reader.nextLine();
            if (label.equals("p")) {
                labelWriter.println("Positive\t" + strLine);
            } else if (label.equals("n")) {
                labelWriter.println("Negative\t" + strLine);
            } else if (label.equals("x")) {
                br.close();
                labelWriter.close();
                return;
            } else {
                labelWriter.println("Neutral\t" + strLine);
            }

        }

        labelWriter.close();
//Close the input stream
        br.close();
    }

    static void score()
            throws IOException {
        // Open the file
        FileInputStream fstream = new FileInputStream("labellingOutput.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        FileInputStream fstream2 = new FileInputStream("labelCompare.txt");
        BufferedReader br2 = new BufferedReader(new InputStreamReader(fstream2));
        String strLine1;
        String strLine2;

        //Read File Line By Line
        Double count = 0.0;
        Double correct = 0.0;
        Double truePos = 0.0;
        Double totalPos = 0.0;
        Double trueNeg = 0.0;
        Double totalNeg = 0.0;
        Double tp = 0.0;
        Double fp = 0.0;
        Double tn = 0.0;
        Double fn = 0.0;

        while ((strLine1 = br.readLine()) != null) {
            strLine2 = br2.readLine();
            // Print the content on the console
            String arr[] = strLine1.split("\t", 2);
            String arr2[] = strLine2.split("\t", 2);
            String label1 = arr[0];
            String label2 = arr2[0];
//            if (label1.equals("Neutral") || label2.equals("Neutral")) {
//                continue;
//            }
            if (label1.equals(label2)) {
                count +=1;
                correct +=1;
                if (label1.equals("Positive")) {
                    truePos += 1;
                    totalPos += 1;
                    tp += 1;
                }
                if (label1.equals("Negative")) {
                    trueNeg += 1;
                    totalNeg += 1;
                    tn += 1;
                }
            }
            else if ((label1.equals("Positive") && label2.equals("Negative")) || (label1.equals("Negative") && label2.equals("Positive"))) {
                count += 1;
                if (label1.equals("Positive")) {
                    totalPos += 1;
                    fn += 1;
                }
                if (label1.equals("Negative")) {
                    totalNeg += 1;
                    fp += 1;
                }
            }
            else {
                count++;
                continue;
            }
        }

        System.out.println("Total labelled: " + count);
        System.out.println("Total correct: " + correct);
        System.out.println("Percentage correct: " + correct/count);
        System.out.println("Total pos: " + totalPos);
        System.out.println("Pos correct: " + truePos);
        System.out.println("Percentage pos correct: " + truePos/totalPos);
        System.out.println("Total neg: " + totalNeg);
        System.out.println("Neg correct: " + trueNeg);
        System.out.println("Percentage neg correct: " + trueNeg/totalNeg);
        System.out.println("Precision: " + tp / (tp + fp));
        Double p = tp / (tp + fp);
        Double r = tp / (tp + fn);
        System.out.println("Recall: " + tp / (tp + fn));
        System.out.println("F1: " + 2 * (p * r)/(p + r));

//Close the input stream
        br.close();
        br2.close();
    }

    public static void main(String[] args) throws IOException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution

        // read some text in the text variable
        Labeller t = new Labeller();
        t.score();
//        String text = readFile("../../../tm-project/fmoc-minutes/apr24-2012", StandardCharsets.UTF_8); // Add your text here!

    }
}

