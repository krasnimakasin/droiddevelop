package kn.outside; 

import java.util.regex.*; 
import java.util.*; 
/** * Syntax highlighter for Java language */ 

public class Higlight 
{ 
// class setup 
static final Pattern patternCommon = Pattern.compile("(?m)^(.*?)(//|/\\*|\")(.*)$"); 
static final Pattern patternCommentEnd = Pattern.compile("(?m)^(.*?\\*/)"); 
static final Pattern patternStringEnd = Pattern.compile("^((?:[^\"\\\\]|(?:\\\\\\\\)*|[^\\\\]*\\\\\"|\\\\[^\\\\])*\")"); 
static final Pattern patternKeywords = Pattern.compile("\\b(package|import|" + 
          "extends|implements|throws|abstract|interface|enum|" + 
          "class|public|protected|private|static|final|return|synchronized|volatile|" + 
          "void|int|long|float|double|byte|char|boolean|new|return|true|false|null|" + 
          "break|while|for|if|else|switch|case|default|throw|try|catch|finally)\\b"); 
static final String commentStart = "<font color=\"#50c050\">"; 
static final String commentEnd = "</font>"; 
static final String stringStart = "<font color=\"#5050c0\">";
static final String stringEnd = "</font>"; 
static final String keywordReplace = "<font color=\"#800050\">$1</font>"; 
/** * parse plain string and mark keywords */ 
static String parseKeywords(String s) 
{ 
return patternKeywords.matcher(s).replaceAll(keywordReplace); } 
/** * parse string and replace all comments and string variables */ 
public static String parse(String s) 
{ 
// just a result 
StringBuilder res = new StringBuilder(); 
// will read input string by line 
Scanner sc = new Scanner(s); 
while (sc.hasNextLine()) 
{ 
String sl = sc.nextLine(); 
// search for comment or a string start in the line 
Matcher matcherCommon = patternCommon.matcher(sl);
while (matcherCommon.find()) 
{ 
// append to result all before found block 
res.append(parseKeywords(matcherCommon.group(1))); 
if (matcherCommon.group(2).equals("//")) 
{ 
// single-line comment 
res .append(commentStart) .append(matcherCommon.group(2)) .append(matcherCommon.group(3)) .append(commentEnd); 
sl = sl.substring(matcherCommon.end()); 
} else 
if (matcherCommon.group(2).equals("/*") || matcherCommon.group(2).equals("\"")) 
{ 
// multi-line comment or a string -should be ended 
Pattern patternEnd; 
String blockEnd; 
// setup variables 
if (matcherCommon.group(2).equals("/*")) 
{ 
res.append(commentStart); 
patternEnd = patternCommentEnd; 
blockEnd = commentEnd; 
} else 
{ 
res.append(stringStart); 
patternEnd = patternStringEnd; 
blockEnd = stringEnd; 
}
res.append(matcherCommon.group(2)); 
sl = matcherCommon.group(3); 
// search for the end of block 
Matcher endMatcher; 
while (!(endMatcher = patternEnd.matcher(sl)).find()) 
{ 
res .append(sl) .append("\n"); 
if (sc.hasNextLine()) 
{ 
sl = sc.nextLine(); 
} else
{ 
sl = ""; break; 
} 
} 
if (sl.length() > 0) 
{ 
res.append(endMatcher.group(1)); 
sl = sl.substring(endMatcher.end()); 
} 
res.append(blockEnd); 
// continue to primary search after end of block 
matcherCommon.reset(sl); 
} 
} 
// no blocks in a current line, but still more characters there
if (sl.length() > 0) 
{ 
res.append(parseKeywords(sl)); 
} 
res.append("\n"); 
} 
return res.toString(); 
} 
}