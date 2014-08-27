package com.octopod.nixium.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

        private static int minimum(int a, int b, int c) {
                return Math.min(Math.min(a, b), c);
        }
        
        public static String[] get_ambiguous(char character) {
        	
        	switch(character) {
        		case 'A':
        		case 'a':
        			return new String[]{"A", "a", "4"};
        		case 'i':
        		case 'I':
        			return new String[]{"I", "i", "1", "!"};
        		case 'S':
        		case 's':
        			return new String[]{"S", "s", "5", "$"};
        		case 'T':
        		case 't':
        			return new String[]{"T", "t", "7"};
        		default:
        			return new String[]{String.valueOf(character)};
        	}
        	
        }
        
        public static String implode(String[] strings) {return implode(strings, "");}
        public static String implode(String[] strings, String glue) {
        	
        	String output = "";
        	
        	if(strings.length > 0){
        		StringBuilder sb = new StringBuilder();
        		sb.append(strings[0]);
        		for(int i = 1; i < strings.length; i++){
        			sb.append(glue);
        			sb.append(strings);
        		}
        		output = sb.toString();
        	}
        	
        	return output;
        	
        }
        
        public static String[] reg_match(String string, String regex) {

        	List<String> matches = new ArrayList<String>();
        	
    		Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(string);

    		while(matcher.find()){matches.add(matcher.group());}
    		
    		return matches.toArray(new String[matches.size()]);
        	
        }
        
        public static String reg_match_first(String string, String regex) {

    		Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(string);

    		if(matcher.find()){return matcher.group();
    		}else{return null;}
        	
        }
        
        public static int reg_count(String string, String regex) {

        	int matches = 0;
        	
    		Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(string);

    		while(matcher.find()){matches++;}
    		
    		return matches;
        	
        }
        
        public static int levenshteinDistance(CharSequence str1, CharSequence str2) {
                int[][] distance = new int[str1.length() + 1][str2.length() + 1];
 
                for (int i = 0; i <= str1.length(); i++)
                        distance[i][0] = i;
                for (int j = 1; j <= str2.length(); j++)
                        distance[0][j] = j;
 
                for (int i = 1; i <= str1.length(); i++)
                        for (int j = 1; j <= str2.length(); j++)
                                distance[i][j] = minimum(
                                                distance[i - 1][j] + 1,
                                                distance[i][j - 1] + 1,
                                                distance[i - 1][j - 1]
                                                                + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0
                                                                                : 1));
 
                return distance[str1.length()][str2.length()];    
        }
        
        static public String capitalizeFully(String text){
	        
	        String[] split = text.split(" ");
	        for(int i = 0; i < split.length; ++i) {
			    char[] chars = split[i].toCharArray();
			    chars[0] = Character.toUpperCase(chars[0]);
			    split[i] = new String(chars);
	        }
	        return implode(split, " ");
            
        }
        
        //splits up a string into arguments except if between quotations
        static public String[] parse_args(String s){

        	List<String> list = new ArrayList<String>();
        	Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(s);
        	while (m.find()) list.add(m.group(1));
        	return list.toArray(new String[list.size()]);
        	
        }
        
}