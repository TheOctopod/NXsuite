
package com.octopod.nixium.utils;

import java.io.StringReader;
import java.net.URL;
import java.util.Scanner;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NXML {

    private Document xml;
    
    public NXML(String website){

        try{
            URL url = new URL(website);
            String text = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.xml = db.parse(new InputSource(new StringReader(text)));
        }catch(Exception e){}

    }
    
    //Returns array of tag contents matching 'tag'
    public String[] getTags(String tag){

        NodeList nodelist = this.xml.getElementsByTagName(tag);
        int size = nodelist.getLength();
        
        String[] array = new String[size];
        for(int i = 0; i < size; i++){
            Node node = nodelist.item(i);
            array[i] = node.getTextContent();
        }
        
        return(array);
        
    }
    
    //Returns array of tag contents matching 'tag', but only if the parent matches 'filter'
    public String[] getTags(String tag, String filter){

        NodeList nodelist = this.xml.getElementsByTagName(tag);
        String[] array = new String[0];
        
        for(int i = 0; i < nodelist.getLength(); i++){
            Node node = nodelist.item(i);
            if(node.getParentNode().getNodeName().equals(filter)){
                array = NArrays.push(array, node.getTextContent());
            }
        }
        
        return array;

    }
 
}