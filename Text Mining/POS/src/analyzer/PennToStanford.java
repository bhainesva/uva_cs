package analyzer;

import edu.stanford.nlp.trees.EnglishGrammaticalStructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * The main method of this class performs the conversion of Penn treebank trees to Stanford simple dependencies in
 * CoNLL format that was used in the COLING 2010 paper "Evaluating Dependency Parsers on Unbounded Dependencies", by
 * Joakim Nivre, Laura Rimell, Ryan McDonald and Carlos Gómez-Rodríguez.
 *
 * The conversion is done in three steps:
 * 1. We use the Stanford parser to obtain for us the Stanford simple dependencies from Penn Treebank .mrg files. We obtain
 * a description of these dependencies in Stanford format, and we lose the part-of-speech tags.
 * 2. We convert the Stanford format files to the CoNLL format.
 * 3. We go back to the Penn Treebank .mrg files to recover the part-of-speech tags that were lost in step 1, and add them
 * to the CoNLL files that we obtained in step 2.
 * Note that this program will write (to the paths specified in the constants below) not only the final output of step 3,
 * but also the intermediate conversions obtained in steps 1 and 2. If you are not interested in these intermediate results,
 * you may prefer to modify the program so that it deletes them automatically.
 *
 * To use this converter, you will first need to have the following:
 * - An input directory containing a set of Penn Treebank tree files in .mrg format. These files can reside in various subdirectories
 * of the given directory, as they typically come in Penn Treebank distributions. The structure of the input directory will be
 * replicated in the output.
 * - The Stanford Parser class files. The version that we used to obtain the results in the paper was that of 2009-12-03.
 * If you use a later version, please take into account that changes to the Stanford parser could make this program not work as
 * intended.
 *
 * And then do the following:
 * 1. Set the values of the String constants pennTreesDir, stanfordDir, conllIntermDir, pennTagsDir and conllOutputDir below.
 * pennTreesDir and pennTagsDir should both point to the path to the input Penn treebank trees (pennTreesDir is used by step 1
 * above while pennTagsDir is used by step 3 to add the part-of-speech tags, so they should be set to the same path in most
 * cases). Set conllOutputDir to the directory where you want the final output in CoNLL format to be written. Set the strings
 * stanfordDir and conllIntermDir to the directories where you would like the intermediate outputs of steps 1 and 2 to be written.
 * Note that these two paths must point to some valid location with write access for the conversion to work, even if you are
 * not interested in the intermediate results.
 * 2. Compile this class and run it with the bin/ directory of the Stanford Parser in the classpath, the way to do this will
 * depend on your IDE (e.g. in Eclipse, create a separate project for the Stanford Parser and then use "Add Class Folder..." in
 * the Properties dialog of this class's project. Then run the class with Ctrl+F11).
 * 3. If nothing went wrong, the simple Stanford dependencies in CoNLL format will be written to the directory that you specified
 * in the constant conllOutputDir. The output files will be organised in subdirectories replicating the structure of the input
 * directory.
 *
 * @author Carlos Gómez-Rodríguez
 */
public class PennToStanford
{

    /**
     * Input directory containing Penn treebank trees in .mrg format. Subdirectories will
     * be read recursively.
     */
    private static final String pennTreesDir =
            //"C:\\Treebanks\\Penn\\treebank-3\\parsed\\mrg\\wsj";
            "C:\\Treebanks\\QuestionBank";

    /**
     * Output directory for simple Stanford dependencies in Stanford format.
     */
    private static final String stanfordDir =
            //"C:\\Treebanks\\PennOut2\\trees";
            "C:\\Treebanks\\QuestionBankStanSimple";

    /**
     * Output directory for simple Stanford dependencies in CoNLL format without part-of-speech tags.
     */
    private static final String conllIntermDir =
            //"C:\\Treebanks\\PennOutConll2\\trees";
            "C:\\Treebanks\\QuestionBankInterm";

    /**
     * Input directory with Penn treebank trees where part-of-speech tags will be read.
     * This should normally be the same as pennTreesDir.
     */
    private static final String pennTagsDir =
            //"C:\\Treebanks\\Penn\\treebank-3\\parsed\\mrg\\wsj";
            "C:\\Treebanks\\QuestionBank";

    /**
     * Output directory for simple Stanford dependencies in CoNLL format including the part-of-speech tags.
     */
    private static final String conllOutputDir =
            //"C:\\Treebanks\\PennOutConll2\\fulltrees";
            "C:\\Treebanks\\QuestionBankConll";


    /**CoNLL format columns*/
    private static final int COL_ID = 0;
    private static final int COL_FORM = 1;
    private static final int COL_LEMMA = 2;
    private static final int COL_TAG1 = 3;
    private static final int COL_TAG2 = 4;
    private static final int COL_FS = 5;
    private static final int COL_HEAD = 6;
    private static final int COL_DEPREL = 7;
    private static final int COL_PHEAD = 8;
    private static final int COL_PDEPREL = 9;

    public static <T> void setPosition ( List<T> l , int index , T content )
    {
        while ( l.size() < index+1 )
            l.add(null);
        l.set(index,content);
    }


    public static List<String[]> readTagsForNextSentenceFromTree ( BufferedReader brTags ) throws IOException
    {
        List<String[]> result = new ArrayList<String[]>();
        //synchronized(result){try{result.wait(50);}catch(Throwable t){t.printStackTrace();}}
        Stack<String> tokens = new Stack<String>();
        String line;
        String separators = " ()\n\r\t";
        while ( (line=brTags.readLine()) != null )
        {
            boolean endOfTree = false;
            StringTokenizer st = new StringTokenizer(line,separators,true);
            while ( st.hasMoreTokens() )
            {
                String tok = st.nextToken();
                if ( tok.trim().length() > 0 )
                    tokens.push(tok);
                if ( tokens.peek().equals(")") )
                {
                    List<String> backwards = new ArrayList<String>();
                    tokens.pop();
                    while ( !tokens.peek().equals("(") )
                    {
                        String symbol = tokens.pop();
                        backwards.add(symbol);
                    }
                    if ( backwards.size() == 2 )
                    {
                        //System.out.println("Backwards: " + backwards);
                        if ( !backwards.get(1).equals("-NONE-") )
                        {
                            String[] wordtag = new String[2];
                            wordtag[0] = backwards.get(0);
                            wordtag[1] = backwards.get(1);
                            result.add(wordtag);
                            //result.add(backwards.get(0)+"/"+backwards.get(1));
                        }
                    }
                    if ( backwards.size() > 2 )
                    {
                        System.err.println("Warning: found " + backwards);

                    }
                    tokens.pop(); //pop "("
                    if ( tokens.isEmpty() )
                    {
                        endOfTree = true;
                        break;
                    }
                }
            }
            if ( endOfTree ) break;
        }
        return result;
    }

    /**
     * Reads next sentence in penn POS tag file and returns list of strings of the form [ "word1/tag1" , "word2/tag2" , ... ]
     * @param brTags
     * @return
     * @throws IOException
     */
    public static List<String> readTagsForNextSentence ( BufferedReader brTags ) throws IOException
    {

        List<String> result = new ArrayList<String>();
        String tagLine;
        do
        {
            tagLine = brTags.readLine();
        }
        while ( tagLine.replace('=',' ').trim().length() == 0 );

        do
        {
            StringTokenizer st = new StringTokenizer(tagLine," \t[]");

            boolean done = false;
            while ( st.hasMoreTokens() )
            {
                String pair = st.nextToken();
                result.add(pair);
                System.out.println("Added: " + pair);
                if ( pair.equals("./.") ) { done = true; break; }
                if ( pair.endsWith("/:" ) ) { done = true ; break; }
            }
            if ( done ) break;

            do
            {
                tagLine = brTags.readLine();
                //System.out.println("Line: " + tagLine + ":trimmed:" + tagLine.trim());
            }
            while ( tagLine != null && tagLine.trim().length() == 0 );
        }
        while ( tagLine != null && tagLine.replace('=',' ').trim().length() != 0 );

        System.out.println("STOP");

        return result;

    }

    /**
     * Adds part-of-speech tags from Penn treebank trees to extracted CoNLL files.
     * @param inConllPath Path to CoNLL files.
     * @param inTagsPath Path to Penn treebank .mrg trees to obtain the part-of-speech tags.
     * @param outPath Output tags to write the resulting CoNLL files to.
     */
    public static void addTagsToConll ( File inConllPath , File inTagsPath , File outPath ) throws IOException
    {
        if ( inConllPath.isFile() )
        {
            FileInputStream fis1 = new FileInputStream(inConllPath);
            FileInputStream fis2 = new FileInputStream(inTagsPath);
            FileOutputStream fos = new FileOutputStream(outPath);
            PrintStream ps = new PrintStream(fos);
            addTagsToConll(fis1,fis2,ps);
        }
        else if ( !inConllPath.isFile() )
        {
            String[] contentsOfPath = inConllPath.list();
            for ( int i = 0 ; i < contentsOfPath.length ; i++ )
            {
                File newInPath = new File(inConllPath,contentsOfPath[i]);
                File newOutPath = new File ( outPath.getAbsolutePath() + File.separatorChar + contentsOfPath[i] );
                if ( !newOutPath.exists() && newInPath.isDirectory() )
                {
                    newOutPath.mkdirs();
                }
                String contentsOfPathWithoutExt = contentsOfPath[i];
                if ( contentsOfPathWithoutExt.contains(".") )
                    contentsOfPathWithoutExt = contentsOfPathWithoutExt.substring(0,contentsOfPathWithoutExt.lastIndexOf("."));
                String tagPathName = contentsOfPathWithoutExt;
                //if ( newInPath.isFile() ) tagPathName = tagPathName + ".pos";
                if ( newInPath.isFile() ) tagPathName = tagPathName + ".mrg";
                File newTagInPath = new File(inTagsPath,tagPathName);
                addTagsToConll(newInPath,newTagInPath,newOutPath);
            }
        }
    }


    public static void addTagsToConll ( FileInputStream conllIn , FileInputStream tagsIn , PrintStream out ) throws IOException
    {

        BufferedReader brConll = new BufferedReader ( new InputStreamReader ( conllIn ) );
        BufferedReader brTags = new BufferedReader ( new InputStreamReader ( tagsIn ) );

        String conllLine;

        List<String[]> rows = new ArrayList<String[]>();

        boolean lastWasEmpty = false;
        while ( ( conllLine = brConll.readLine() ) != null )
        {

            if ( conllLine.trim().length() == 0 )
            {

                //process the already read sentence

                if ( rows.size() == 0 && lastWasEmpty )
                {
                    //"empty" Stanford trees take 2 rows
                    lastWasEmpty = false;
                    System.out.println("CONTINUING");
                    continue;
                }

                if ( rows.size() == 0 ) lastWasEmpty = true;
                else lastWasEmpty = false;

                //System.out.println("Rows:" + Arrays.deepToString(rows.toArray()));
                //List<String> wordTagPairs = readTagsForNextSentence ( brTags );
                List<String[]> wordTagPairs = readTagsForNextSentenceFromTree ( brTags );
                for ( int i = 0 ; i < wordTagPairs.size() ; i++ )
                {
                    String[] wordTagPair = wordTagPairs.get(i);
                    if ( rows.size() <= i ) rows.add( new String[] {"_" , "_" , "_" , "_" , "_" , "_" , "_" , "_" , "_" , "_" });
                    //StringTokenizer st = new StringTokenizer(wordTagPair,"/");
                    //String newWord = st.nextToken();
                    //String newTag = st.nextToken();
                    String newWord = wordTagPair[0];
                    String newTag = wordTagPair[1];
                    String oldWord = rows.get(i)[COL_FORM];
                    if ( !"_".equals(oldWord) && !newWord.equals(oldWord) )
                        if ( !"UNKN_FORM".equals(oldWord) )
                            System.err.println("Warning, word mismatch: " + newWord + " found at .tag file, " + oldWord + " at CoNLL file.");
                    rows.get(i)[COL_FORM] = newWord;
                    rows.get(i)[COL_LEMMA] = newWord;
                    rows.get(i)[COL_TAG1] = newTag;
                    rows.get(i)[COL_TAG2] = newTag;
                    //defaults for new punctuation added
                    if ( rows.get(i)[COL_ID].equals("_") ) rows.get(i)[COL_ID] = ""+(i+1);
                    if ( rows.get(i)[COL_HEAD].equals("_") ) rows.get(i)[COL_HEAD] = ""+i;
                    if ( rows.get(i)[COL_PHEAD].equals("_") ) rows.get(i)[COL_PHEAD] = ""+i;
                    if ( rows.get(i)[COL_DEPREL].equals("_") ) rows.get(i)[COL_DEPREL] = "PUNCT";
                    if ( rows.get(i)[COL_PDEPREL].equals("_") ) rows.get(i)[COL_PDEPREL] = "PUNCT";
                }

                for ( int i = 0 ; i < rows.size() ; i++ )
                {
                    for ( int j = 0 ; j < rows.get(i).length ; j++ )
                    {
                        out.print(rows.get(i)[j]);
                        if ( j == rows.get(i).length-1 ) out.println();
                        else out.print("\t");
                    }
                }

                out.println();

                rows.clear();

            }
            else
            {
                String[] rowContents = new String[10];
                StringTokenizer st = new StringTokenizer(conllLine," ");
                int i = 0;
                while ( st.hasMoreTokens() )
                {
                    rowContents[i++] = st.nextToken();
                }
                rows.add(rowContents);
            }


        }

    }

    /**
     * Convert a file from the Stanford to the CoNLL dependency format.
     */
    public static void stanfordToConll ( FileInputStream in , PrintStream out ) throws IOException
    {

        BufferedReader br = new BufferedReader ( new InputStreamReader ( in ) );

        String line;

        List<String> forms = new ArrayList<String>();
        List<Integer> heads = new ArrayList<Integer>();
        List<String> depRels = new ArrayList<String>();

        while ( (line=br.readLine()) != null )
        {

            if ( line.trim().length() == 0 )
            {

                //process the already read sentence

                for ( int i = 0 ; i < forms.size() ; i++ )
                {
                    boolean isPunctuation = false;
                    if ( forms.get(i) == null )
                    {
                        isPunctuation = true;
                        forms.set(i,"UNKN_FORM");
                    }
                    if ( heads.size() < i+1 || heads.get(i) == null )
                    {
                        if ( isPunctuation ) //punctuation
                            setPosition(heads,i,i);
                        else //the genuine root
                            setPosition(heads,i,0);
                    }
                    if ( depRels.size() < i+1 || depRels.get(i) == null )
                    {
                        if ( isPunctuation ) //punctuation
                            setPosition(depRels,i,"PUNCT");
                        else
                            setPosition(depRels,i,"ROOT");
                    }

                    StringBuffer myLine = new StringBuffer();
                    myLine.append(String.valueOf(i+1));
                    myLine.append(" ");
                    myLine.append(forms.get(i));
                    myLine.append(" ");
                    myLine.append(forms.get(i));
                    myLine.append(" ");
                    myLine.append("_");
                    myLine.append(" ");
                    myLine.append("_");
                    myLine.append(" ");
                    myLine.append("_");
                    myLine.append(" ");
                    myLine.append(heads.get(i));
                    myLine.append(" ");
                    myLine.append(depRels.get(i));
                    myLine.append(" ");
                    myLine.append(heads.get(i));
                    myLine.append(" ");
                    myLine.append(depRels.get(i));
                    myLine.append(" ");
                    out.println(myLine);
                }

                out.println("");

                forms.clear();
                heads.clear();
                depRels.clear();
            }
            else
            {
                StringTokenizer st = new StringTokenizer(line,"()");
                String depRel = st.nextToken();
                String content = st.nextToken();
                StringTokenizer st2 = new StringTokenizer(content, " ");
                String arg1 = st2.nextToken();
                String arg2 = st2.nextToken();
                if ( arg1.endsWith(",") ) arg1 = arg1.substring(0,arg1.length()-1);
                String word1 = arg1.substring(0,arg1.lastIndexOf("-"));
                int position1 = Integer.parseInt(arg1.substring(arg1.lastIndexOf("-")+1,arg1.length()));
                String word2 = arg2.substring(0,arg2.lastIndexOf("-"));
                int position2 = Integer.parseInt(arg2.substring(arg2.lastIndexOf("-")+1,arg2.length()));
                setPosition(forms,position1-1,word1);
                setPosition(forms,position2-1,word2);
                setPosition(heads,position2-1,position1);
                setPosition(depRels,position2-1,depRel);
            }


        }


    }

    public static void stanfordToConll ( File inPath , File outPath ) throws IOException
    {
        if ( inPath.isFile() && inPath.getName().endsWith(".mrg") )
        {
            FileInputStream fis = new FileInputStream(inPath);
            FileOutputStream fos = new FileOutputStream(outPath);
            PrintStream ps = new PrintStream(fos);
            stanfordToConll(fis,ps);
        }
        else if ( !inPath.isFile() )
        {
            String[] contentsOfPath = inPath.list();
            for ( int i = 0 ; i < contentsOfPath.length ; i++ )
            {
                File newInPath = new File(inPath,contentsOfPath[i]);
                File newOutPath = new File ( outPath.getAbsolutePath() + File.separatorChar + contentsOfPath[i] );
                if ( !newOutPath.exists() && newInPath.isDirectory() )
                {
                    newOutPath.mkdirs();
                }
                stanfordToConll(newInPath,newOutPath);
            }
        }
    }


    /**
     * Converts the Penn Treebank files (.mrg) in the specified input path
     * to Stanford basic typed dependency files.
     * @param inPath
     * @param outPath
     * @throws FileNotFoundException
     */
    public static void pennToStanford ( File inPath , File outPath ) throws FileNotFoundException
    {
        if ( inPath.isFile() && inPath.getName().endsWith(".mrg") )
        {
            String arg1 = "-treeFile";
            String arg2 = inPath.getAbsolutePath();
            String arg3 = "-basic";
            PrintStream oldOut = System.out;
            System.setOut( new PrintStream(new FileOutputStream(outPath)) );
            EnglishGrammaticalStructure.main(new String[]{arg1,arg2,arg3});
            System.setOut(oldOut);
        }
        else if ( !inPath.isFile() )
        {
            String[] contentsOfPath = inPath.list();
            for ( int i = 0 ; i < contentsOfPath.length ; i++ )
            {
                File newInPath = new File(inPath,contentsOfPath[i]);
                File newOutPath = new File ( outPath.getAbsolutePath() + File.separatorChar + contentsOfPath[i] );
                if ( !newOutPath.exists() && newInPath.isDirectory() )
                {
                    newOutPath.mkdirs();
                }
                pennToStanford(newInPath,newOutPath);
            }
        }


    }

    public static void main ( String[] args ) throws Throwable
    {

        File inDir = new File(pennTreesDir);
        File stanDir = new File(stanfordDir);
        File conDir = new File(conllIntermDir);
        File conOutDir = new File(conllOutputDir);
        File tagDir = new File(pennTagsDir);

        System.out.println("Converting Penn Treebank -> Stanford dependencies");

        pennToStanford(inDir,stanDir);

        System.out.println("Converting Stanford dependencies -> CoNLL dependencies");

        stanfordToConll(stanDir,conDir);

        System.out.println("Adding POS tags to CoNLL dependencies from Penn Treebank");

        addTagsToConll ( conDir , tagDir , conOutDir );

    }



}

