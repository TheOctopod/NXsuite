
package com.octopod.nixium.utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XML {

    private Document xml;
    
    public XML(String website){

        try{

            URL url = new URL(website);
            
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");
            String text = scanner.useDelimiter("\\A").next();
            scanner.close();
            
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.xml = db.parse(new InputSource(new StringReader(text))); 

        }catch(IOException | ParserConfigurationException | SAXException e){}

    }
    
    public Document toDocument(){return xml;}
    
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
        List<String> array = new ArrayList<String>();
        
        for(int i = 0; i < nodelist.getLength(); i++){
            Node node = nodelist.item(i);
            if(node.getParentNode().getNodeName().equals(filter)){
                array.add(node.getTextContent());
            }
        }
        
        return array.toArray(new String[array.size()]);

    }
 
}