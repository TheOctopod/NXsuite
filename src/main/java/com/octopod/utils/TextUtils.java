
package com.octopod.utils;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;


public class TextUtils {
    
    static public int width(char character){
        switch(character){
            case '*': return 5;
            case '>': return 5;
            case '<': return 5;
            case ',': return 2;
            case '!': return 2;
            case '{': return 5;
            case '}': return 5;
            case ')': return 5;
            case '(': return 5;
            case '\u00a7': return 0;
            case '[': return 4;
            case ']': return 4;
            case ':': return 2;
            case '\'': return 3;
            case '|': return 2;
            case '.': return 2;
            case '\u2019': return 2; //filler character
            case '`': return 2; //old filler character; width change since 1.7
            case ' ': return 4;
            case 'f': return 5;
            case 'k': return 5;
            case 'I': return 4;
            case 't': return 4;
            case 'l': return 3;
            case 'i': return 2;
            default: return 6;
        }
    }

    final static int wordWrapThreshold = 15;
    
    static public String block(String text, int toWidth, int alignment){return block(text, toWidth, alignment, true, " ");}
    static public String block(String text, int toWidth, int alignment, boolean precise){return block(text, toWidth, alignment, precise, " ");}
    static public String block(String text, int toWidth, int alignment, boolean precise, String emptyFiller){
        
        text = cut(text, toWidth, false) + ChatColor.RESET;

        //The total width (in pixels) needed to fill
        final int totalFillerWidth = toWidth - width(text);
        int[] extra;
        String[] fill;

        switch(alignment) {
        	case 2: //Center alignment; cuts the total width to fill in half
                extra = new int[]{(int)Math.floor(totalFillerWidth/2), (int)Math.ceil(totalFillerWidth/2)};
                fill = new String[]{"",""};
                break;
            default:
                extra = new int[]{totalFillerWidth};
                fill = new String[]{""};
        }

        for(int i = 0; i < extra.length; i++)
            fill[i] += filler(extra[i], true, emptyFiller);
        
        switch(alignment){
            case 0:
                text = text + fill[0];
                break;
            case 1:
                text = fill[0] + text;
                break;
            case 2:
                text = fill[0] + text + fill[1];
                break;
            default:
                break;
        }
        
        return text + ChatColor.RESET;
        
    }

    final static String FILLER_COLOR = ChatColor.DARK_GRAY + "";
    final static String FILLER_2PX = "\u2019";
    
    static public String filler(int width, boolean precise, String emptyFiller) {
    	
    	final int emptyFillerWidth = width(emptyFiller);
        StringBuilder filler = new StringBuilder();
        
        while(width > emptyFillerWidth + 1){
            filler.append((String)emptyFiller);
            width -= emptyFillerWidth;
        }
        
        if(precise) {
            switch(width){
            case 6:
                if(emptyFillerWidth == 6) {filler.append((String)emptyFiller); break;}
            case 5:
                if(emptyFillerWidth == 5) {filler.append((String)emptyFiller); break;}
                filler.append(ChatColor.BOLD + " ");
                break;
            case 4:
                if(emptyFillerWidth == 4) {filler.append((String)emptyFiller); break;}
                filler.append(" ");
                break;
            case 3:
                if(emptyFillerWidth == 3) {filler.append((String)emptyFiller); break;}
                filler.append(FILLER_COLOR + ChatColor.BOLD + FILLER_2PX);
                break;
            case 2:
                if(emptyFillerWidth == 2) {filler.append((String)emptyFiller); break;}
                filler.append(FILLER_COLOR + FILLER_2PX);
                break;
            }       	
        }

        return filler.toString();
        
    }
    
    static public int width(String text){
        
        int width = 0;
        boolean noWidth;
        boolean bolded = false;
        char lastChar = ' ';

        for(char character:text.toCharArray()){
        	noWidth = false;
            if(lastChar == '\u00a7'){
                if(Character.toString(character).toLowerCase().equals("l")){bolded = true;}else{bolded = false;}
                noWidth = true;
            }
            lastChar = character;
        	if(character == '\u00a7'){continue;}
        	if(!noWidth){
                if(bolded){width += width(character) + 1;}
                else{width += width(character);}        		
        	}
        }

        return width;
        
    }
    
    static public String capitalizeFully(String text){
        
        String[] split = text.split(" ");
        for(int i = 0; i < split.length; ++i) {
		    char[] chars = split[i].toCharArray();
		    chars[0] = Character.toUpperCase(chars[0]);
		    split[i] = new String(chars);
        }
        return StringUtils.join(split, " ");
        
    }
    
    static public String cut(String s, int toWidth, boolean wrap){

    	String extra = s;
    	int start = 0;
    	int end = extra.length();

    	while(width(extra.substring(start, end)) > toWidth){end--;}
    	s = extra.substring(start, end);
    	extra = extra.substring(end);

    	return s;
    	
    }
    
}
