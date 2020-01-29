package extractwordembeddings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

public class ExtractWordEmbeddings extends AbstractLanguageAnalyser  implements
ProcessingResource, Serializable {
	
	// the document to process
    Document document;
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document d) {
        document=d;
    }
    // the input annotation set
    String sentenceAnnSet;
    public String getSentenceAnnSet() {
        return sentenceAnnSet;
    }
    public void setSentenceAnnSet(String as) {
        sentenceAnnSet=as;
    }
    
    HashSet<String> stopwords;
    
    public HashSet<String> getStopwords() {
		return stopwords;
	}
	public void setStopwords(HashSet<String> stopwords) {
		this.stopwords = stopwords;
	}
	PrintWriter pACL;
    public PrintWriter getPACL() {
        return pACL;
    }
    public void setPACL(PrintWriter sa) {
    	pACL=sa;
    }
    
    PrintWriter pGoogle;
    public PrintWriter getPGoogle() {
        return pGoogle;
    }
    public void setPGoogle(PrintWriter sa) {
    	pGoogle=sa;
    }
    
    
    
    // sentence 
    
    String sentAnn;
    public String getSentAnn() {
        return sentAnn;
    }
    public void setSentAnn(String sa) {
        sentAnn=sa;
    }
    
    // vector
    String vecAnn;
    public String getVecAnn() {
        return vecAnn;
        
    }
    
    String pathRes;
    public String getPathRes() {
    	return pathRes;
    }
    
    public void setPathRes(String va) {
    	pathRes=va;
    }
    
    String pathSum;
    public String getPathSum() {
    	return pathSum;
    }
    
    public void setPathSum(String va) {
    	pathSum=va;
    }
    
    
    public void setVecAnn(String va) {
        
        vecAnn=va;
    }
    
    // "section" annotation name
    
    String secAnn;
    public String getSecAnn() {
        return secAnn;
    }
    public void setSecAnn(String sa) {
        secAnn=sa;
    }
    
    public String inSecCentroid;
    public String getInSecCentroid() {
        return inSecCentroid;
    }
    public void setInSecCentroid(String feat) {
        inSecCentroid=feat;
    }
    
    public Resource init() {
        return this;
    }
    
    
    
    String docID;
    public String getDocID() {
        return docID;
    }
    public void setDocID(String sa) {
    	docID=sa;
    }
    
    
    String padding300;
    public String getPadding300() {
        return padding300;
    }
    public void setPadding300(String sa) {
    	padding300=sa;
    }
    
    int maxtokens;
    public int getmaxtokens() {
        return maxtokens;
    }
    public void setmaxtokens(int sa) {
    	maxtokens=sa;
    }
    
    
    int sentenceSize;
    public int getSentenceSize() {
        return sentenceSize;
    }
    public void setSentenceSize(int sa) {
    	sentenceSize=sa;
    }
    
    
    private void extractTokenInfo(String instanceID, List<Annotation> speTokenSet, AnnotationSet aclAnn, AnnotationSet googleAnn) throws InvalidOffsetException, IOException {
    	
    	int numTokensACL = 0;
    	int numTokensGoogle = 0;
    	
    	String sep = ",";
    	
    	String rowGoogle = "";
    	String rowACL = "";
    	for (Annotation token: speTokenSet) {
    		
    		if (!token.getFeatures().get("kind").toString().equals("word"))
    			continue;
    		
    		double token_tf_idf = Double.parseDouble(token.getFeatures().get("token_tf_idf").toString());
    		token_tf_idf = 1.0;
    		String tokenText = token.getFeatures().get("string").toString();
    		
    		List<Annotation> aclTokens = aclAnn.get(token.getStartNode().getOffset(), token.getEndNode().getOffset()).inDocumentOrder();
    		List<Annotation> googleTokens = googleAnn.get(token.getStartNode().getOffset(), token.getEndNode().getOffset()).inDocumentOrder();
    		
    		if (stopwords.contains(tokenText.toLowerCase()))
    			continue;
    		
    		// ONLY ACL
    		if (numTokensACL < sentenceSize) {
	    		if (aclTokens.size()==1) {
	    			Annotation aclToken = aclTokens.get(0);
	    			
	    			
	    			
	    			String fv = aclToken.getFeatures().get(tokenText.toLowerCase()).toString().replaceAll(" ", sep);
	    			/////
	    			String[] fv_split = fv.split(sep);
	    			String new_fv= "";
	    			for (String fv_i:fv_split) {
	    				double fv_d = Double.parseDouble(fv_i)*token_tf_idf;
	    				new_fv = new_fv + sep + fv_d;
	    			}
	    			fv = new_fv.substring(1);
	    			/////
	    			rowACL += sep + fv;
	    			numTokensACL++;
	    		}
    		}
    		
    		// ONLY GOOGLE
    		if (numTokensGoogle < sentenceSize) {
	    		if (googleTokens.size()==1) {
	    			Annotation googleToken = googleTokens.get(0);
	    			String fv = googleToken.getFeatures().get(tokenText.toLowerCase()).toString().replaceAll(" ", sep);
	    			/////
	    			String[] fv_split = fv.split(sep);
	    			String new_fv= "";
	    			for (String fv_i:fv_split) {
	    				double fv_d = Double.parseDouble(fv_i)*token_tf_idf;
	    				new_fv = new_fv + sep + fv_d;
	    			}
	    			fv = new_fv.substring(1);
	    			/////
	    			rowGoogle += sep + fv;
	    			numTokensGoogle++;
	    		}
    		}
    	}
    	
    	//PADDING
    	
    	while (numTokensACL < sentenceSize) {
    		//System.out.println(numTokens + "\t" + "PADDING" + "\t" + padding300);
    		//pACL.write(padding300 + "\n");
    		rowACL += sep + padding300;
    		numTokensACL++;
    	}
    	
    	while (numTokensGoogle < sentenceSize) {
    		//System.out.println(numTokens + "\t" + "PADDING" + "\t" + padding300);
    		//pGoogle.write(padding300 + "\n");
    		rowGoogle += sep + padding300;
    		numTokensGoogle++;
    	}
    	
    	pACL.write(instanceID + rowACL + "\n");
    	pGoogle.write(instanceID + rowGoogle + "\n");
    	//System.out.println("numTokens: " + numTokens);
    	//System.out.println("numTokensACL: " + numTokensACL);
    	//System.out.println("numTokensGoogle: " + numTokensGoogle);
    	
    	
    	
    }
    


public void execute() {
    Document doc=getDocument();
    
    AnnotationSet all;
    List<Annotation> sentences;
    AnnotationSet tokensAnnSet;
    
    AnnotationSet allW2V;
    AnnotationSet aclAnn;
    AnnotationSet googleAnn;
    
    try {
    	
    	if(sentenceAnnSet.equals("")) {
            all=doc.getAnnotations();
        } else {
            all=doc.getAnnotations(sentenceAnnSet);
        }
        
        sentences=all.get(sentAnn).inDocumentOrder();
        
        tokensAnnSet = all.get("Token");
        
        // Word2Vec
        allW2V = doc.getAnnotations("Word2Vec");
        aclAnn = allW2V.get("ACL");
        googleAnn = allW2V.get("GoogleNews");
        
    	for (int i=0; i<sentences.size(); i++){
    		// Token
    		Annotation sent = sentences.get(i);
    		List<Annotation> speTokenSet = tokensAnnSet.get(sent.getStartNode().getOffset(), sent.getEndNode().getOffset()).inDocumentOrder();
    	    String sentenceID = "000" + i;
    		String instanceID = docID + "-" + sentenceID.substring(sentenceID.length() - 3);
    		
    		extractTokenInfo(instanceID, speTokenSet, aclAnn, googleAnn);
    		
    	}
    	
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidOffsetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
    
}



	
	
	public static void main(String[] args) throws IOException {
	    Document doc;	   
	    
	    // TRAINING DOCS:
        //String inputFolder = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016";
        String inputFolder = args[0];
        File inDir=new File(inputFolder);
        File[] flist=inDir.listFiles();  
        
        String modeSum = "human";
        modeSum = args[2];
        boolean testMode = false;
        if (modeSum.contains("test"))
        	testMode = true;
        
        
        String path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab";
        path = args[1];
        
        File directory = new File(path);
        if (! directory.exists()){
            directory.mkdir();
        } 
        
        
        String stopwords_path = "/home/upf/software/scisumm-corpus/2017-evaluation-script/program/rouge/smart_common_words.txt";
        stopwords_path = "/data/abravo/software/smart_common_words.txt";
        stopwords_path = args[3];
        
        InputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        HashSet<String> stopwords = new HashSet<String>(); 
        String line;
        
        if (stopwords_path.length()>0) {
        
	        fis = new FileInputStream(stopwords_path);
	        isr = new InputStreamReader(fis);
	        br = new BufferedReader(isr);
	        
	        while ((line = br.readLine()) != null) {
	           
	        	stopwords.add(line.trim());
	        }
        br.close();
        }
        
        
        
        
        int mazSentSize = Integer.parseInt(args[3]);
        String [] arr = new String[300];
	    ArrayList<String> myList= new ArrayList<String>(Arrays.asList(arr));
	    Collections.fill(myList, "0.0");
	    
	    String padding300 = myList.toString().replace(", ", ",").replace("[", "").replace("]", "");
	    
	    String resultACLPath = path + File.separator + "acl_vec_sw_"+mazSentSize+".txt";
        String resultGooglePath = path + File.separator +"google_vec_sw_"+mazSentSize+".txt";
        
		try {
	        Gate.init();
	        
        	int maxtokens = 0;
        	Arrays.sort(flist);
        	
        	PrintWriter pwACL = null;
        	PrintWriter pwGoogle = null;
        	
        	
        	if (!testMode) {
	        	pwACL = new PrintWriter(new FileWriter(resultACLPath));
	        	pwGoogle = new PrintWriter(new FileWriter(resultGooglePath));
        	}
        	
        	int nfiles = 0;
        	
	        for (File f: flist) {
	        	ExtractWordEmbeddings extSent=new ExtractWordEmbeddings();
	        	
	        	//if (nfiles==10)
	        	//	break;
	        	nfiles++;
	        	
	        	String docID = f.getName().replace("-PreProcessed.xml", "").replace(".xml", "").replace("summary", "community").replace("E09_2008", "E09-2008").replace("_", ".");
	        	docID = f.getName().replace("_PreProcessed_gate.xml", "").replace(",", "-");
	        	if (testMode) {
	        		pwACL = new PrintWriter(new FileWriter(resultACLPath.replace(".txt", "_" + docID)));
		        	pwGoogle = new PrintWriter(new FileWriter(resultGooglePath.replace(".txt", "_" + docID)));
	        	}
	        	
	        	String filePath = f.getAbsolutePath();
	        	
	        	System.out.println(filePath);
		        
		        doc=Factory.newDocument(new URL("file:" + filePath)); 
		        extSent.setStopwords(stopwords);
		        extSent.setPadding300(padding300);
		        extSent.setDocID(docID);
		        extSent.setmaxtokens(maxtokens);
		        extSent.setSentenceSize(mazSentSize);
		        extSent.setPACL(pwACL);
		        extSent.setPGoogle(pwGoogle);
		        extSent.setDocument(doc);
		        extSent.setSentenceAnnSet("Analysis");
		        extSent.setSentAnn("Sentence");
		        extSent.execute();
		        maxtokens = extSent.getmaxtokens();
		        extSent.cleanup();
		        Factory.deleteResource(doc);
		        Factory.deleteResource(extSent);
		        if (testMode) {
		        	pwACL.close();
			        pwGoogle.close();
	        	}
	        }
	        if (!testMode) {
		        pwACL.close();
		        pwGoogle.close();
	        }
		        
	     } catch(GateException ge) {
	        ge.printStackTrace();
	    } catch (MalformedURLException ex) {
	        ex.printStackTrace();
	    } catch (IOException ex) {
	       ex.printStackTrace();
	    }
        	
        
    
	}
}
