//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short STRING=260;
public final static short CLASS=261;
public final static short COMPLEX=262;
public final static short PRINY_COMP=263;
public final static short NULL=264;
public final static short EXTENDS=265;
public final static short THIS=266;
public final static short WHILE=267;
public final static short FOR=268;
public final static short IF=269;
public final static short ELSE=270;
public final static short RETURN=271;
public final static short BREAK=272;
public final static short NEW=273;
public final static short PRINT=274;
public final static short READ_INTEGER=275;
public final static short READ_LINE=276;
public final static short LITERAL=277;
public final static short IDENTIFIER=278;
public final static short AND=279;
public final static short OR=280;
public final static short STATIC=281;
public final static short INSTANCEOF=282;
public final static short LESS_EQUAL=283;
public final static short GREATER_EQUAL=284;
public final static short EQUAL=285;
public final static short NOT_EQUAL=286;
public final static short PRINT_COMP=287;
public final static short CASE=288;
public final static short DEFAULT=289;
public final static short SUPER=290;
public final static short DCOPY=291;
public final static short SCOPY=292;
public final static short DO=293;
public final static short OD=294;
public final static short III=295;
public final static short UMINUS=296;
public final static short EMPTY=297;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    4,    5,    5,    5,    5,    5,
    5,    5,    2,    6,    6,    7,    7,    7,    9,    9,
   10,   10,    8,    8,   11,   12,   12,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   22,   23,
   23,   24,   14,   14,   14,   28,   28,   26,   26,   27,
   30,   32,   33,   33,   34,   35,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   31,
   31,   29,   29,   36,   36,   16,   17,   21,   15,   37,
   37,   18,   18,   19,   20,
};
final static short yylen[] = {                            2,
    1,    2,    1,    2,    2,    1,    1,    1,    1,    1,
    2,    3,    6,    2,    0,    2,    2,    0,    1,    0,
    3,    1,    7,    6,    3,    2,    0,    1,    2,    1,
    1,    1,    2,    2,    2,    2,    1,    1,    3,    3,
    1,    3,    3,    1,    0,    2,    0,    2,    4,    5,
    4,    4,    2,    0,    2,    7,    1,    1,    1,    1,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    2,    2,    2,    2,    2,    3,
    3,    4,    4,    1,    1,    4,    5,    6,    5,    1,
    1,    1,    0,    3,    1,    5,    9,    1,    6,    2,
    0,    2,    1,    4,    4,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    3,    0,    2,    0,    0,   14,   18,
    0,    7,    8,    6,   10,    0,    9,    0,   13,   16,
    0,    0,   17,   11,    0,    4,    0,    0,    0,    0,
   12,    0,   22,    0,    0,    0,    0,    5,    0,    0,
    0,   27,   24,   21,   23,    0,   91,   84,    0,    0,
    0,    0,   98,    0,    0,    0,    0,   90,    0,    0,
    0,   85,    0,    0,    0,    0,    0,    0,   25,    0,
    0,    0,   28,   37,   26,    0,   30,   31,   32,    0,
    0,    0,    0,   38,    0,    0,    0,    0,   59,   60,
    0,    0,    0,    0,   57,   58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,    0,    0,
    0,    0,    0,    0,    0,    0,   29,   33,   34,   35,
   36,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   80,   81,    0,    0,    0,
    0,    0,   39,    0,    0,    0,   74,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   86,    0,    0,  104,
    0,  105,    0,   82,   83,   40,   42,    0,   49,    0,
    0,   96,    0,    0,   87,    0,    0,    0,   89,   50,
    0,    0,   99,   88,    0,    0,    0,    0,    0,  100,
   53,    0,    0,   55,   56,    0,    0,    0,   97,   51,
    0,   52,
};
final static short yydgoto[] = {                          2,
    3,    4,   73,   21,   34,    8,   11,   23,   35,   36,
   74,   46,   75,   76,   77,   78,   79,   80,   81,   82,
   83,   84,  107,  108,   85,   95,   96,   88,  190,  205,
   89,  214,  207,  208,   90,  145,  203,
};
final static short yysindex[] = {                      -252,
 -251,    0, -252,    0, -244,    0, -245,  -66,    0,    0,
  -51,    0,    0,    0,    0, -220,    0,   86,    0,    0,
    3,  -85,    0,    0,  -83,    0,   41,  -30,   42,   86,
    0,   86,    0,  -73,   46,   47,   52,    0,  -28,   86,
  -28,    0,    0,    0,    0,    6,    0,    0,   63,   66,
   68,  117,    0, -135,   75,   77,   78,    0,   88,   95,
   96,    0,   98,   99,  117,  117,  117,   76,    0,  117,
  117,  117,    0,    0,    0,   82,    0,    0,    0,   85,
   87,   90,   92,    0,  889,   84,    0, -123,    0,    0,
  117,  117,  117,  889,    0,    0,  107,   65,  117,  118,
  124,  117,  117,  117,  117,  117, -257,    0,  478,  -23,
  -23, -120,  508,  889,  889,  889,    0,    0,    0,    0,
    0,  117,  117,  117,  117,  117,  117,  117,  117,  117,
  117,  117,  117,  117,    0,  117,  117,  128,  519,  108,
  530,  130,   97,  889,   23,    0,    0,  541,   36,  571,
  612,  709,    0,  117,   43,  134,    0,  127,  921,    7,
    7,  -32,  -32,   19,   19,  -23,  -23,  -23,    7,    7,
  786,  889,  117,   43,  117,   43,    0,  816,  117,    0,
 -102,    0,   57,    0,    0,    0,    0,  117,    0,  138,
  139,    0,  827,  -88,    0,  889,  143, -217,    0,    0,
  117,   43,    0,    0, -217,  133, -104,   69,  145,    0,
    0,  117,  146,    0,    0,   43,  857,  117,    0,    0,
  868,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  188,    0,   80,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  160,    0,    0,  180,
    0,  180,    0,    0,    0,  181,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -58,    0,    0,    0,    0,
    0,  -55,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -46,  -46,  -46,  -46,    0,  -46,
  -46,  -46,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  900,  457,    0,    0,    0,
  -46,  -58,  -46,  178,    0,    0,    0,    0,  -46,    0,
    0,  -46,  -46,  -46,  -46,  -46,    0,    0,    0,  359,
  385,    0,    0,   55,   61,  119,    0,    0,    0,    0,
    0,  -46,  -46,  -46,  -46,  -46,  -46,  -46,  -46,  -46,
  -46,  -46,  -46,  -46,    0,  -46,  -46,  155,    0,    0,
    0,    0,  -46,   56,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -46,  -58,    0,    0,  961,  -24,  737,
  778,  966,  974,  911,  941,  412,  421,  448,  808,  881,
    0,  -19,  -25,  -58,  -46,  -58,    0,    0,  -46,    0,
    0,    0,    0,    0,    0,    0,    0,  -46,    0,    0,
  206,    0,    0,  -33,    0,   60,    0,  -39,    0,    0,
   -9,  -58,    0,    0,  -39,    0,    0,    0,    0,    0,
    0,  -46,    0,    0,    0,  -58,    0,  -46,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,  268,  265,   54,   18,    0,    0,    0,  252,    0,
   34,    0, -131,  -67,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  131, 1164,  450,  929,    0,    0,    0,
 -150,    0,   81,    0,    0,  -84,    0,
};
final static int YYTABLESIZE=1382;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        101,
   45,  101,  101,  103,  132,   28,  101,   28,    1,  130,
  128,  101,  129,  135,  131,   93,   73,   28,  149,   73,
    7,   43,  135,  187,  140,  101,    5,  134,   22,  133,
  101,   45,    9,   73,   73,   25,  153,  154,   67,   43,
   72,   71,  192,  132,  194,   68,   47,  206,  130,  128,
   66,  129,  135,  131,  206,  132,   10,   24,  136,   58,
  130,   26,   31,  180,  135,  131,  179,  136,   73,   70,
  210,   98,   43,   19,   45,   67,  182,   72,   71,  179,
   30,   32,   68,   33,  219,   33,   39,   66,  191,  101,
   40,  101,   41,   44,   42,   77,   95,  136,   77,   95,
   94,   78,   91,   94,   78,   92,   70,   93,   67,  136,
   72,   71,   77,   77,   99,   68,  100,  101,   78,   78,
   66,   12,   13,   14,   15,   16,   17,  102,   42,   67,
   69,   72,   71,  209,  103,  104,   68,  105,  106,   70,
  117,   66,   97,  118,  137,  119,  142,   77,  120,   67,
  121,   72,   71,   78,  138,  143,   68,  156,  146,   79,
   70,   66,   79,  132,  147,   42,  175,  173,  130,  128,
  177,  129,  135,  131,  188,  197,   79,   79,  200,  198,
   70,  202,  179,  204,  213,  216,  134,    1,  133,   31,
  212,   48,   27,  215,   29,   48,   48,   48,   48,   48,
   48,   48,   15,  218,   38,   12,   13,   14,   15,   16,
   17,   79,   48,   48,   48,   48,   48,  136,    5,   47,
   20,   19,   47,  101,  101,  101,  101,  101,  101,   18,
  101,   47,  101,  101,  101,  101,  102,  101,  101,  101,
  101,  101,  101,  101,  101,   48,   92,   48,  101,   54,
  124,  125,   47,  101,  101,   73,  101,  101,  101,  101,
  101,  101,   12,   13,   14,   15,   16,   17,   47,   47,
    6,   48,   49,   50,   51,   20,   52,   53,   54,   55,
   56,   57,   58,   37,  186,  211,    0,   59,    0,    0,
    0,    0,   60,   61,    0,   62,   63,   64,   65,   12,
   13,   14,   15,   16,   17,    0,   47,    0,   48,   49,
   50,   51,    0,   52,   53,   54,   55,   56,   57,   58,
    0,    0,    0,    0,   59,    0,    0,    0,    0,   60,
   61,    0,   62,   63,   64,   65,  112,    0,    0,   47,
    0,   48,   12,   13,   14,   15,   16,   17,   54,    0,
   56,   57,   58,    0,    0,    0,    0,   59,    0,    0,
   47,    0,   48,   61,    0,   62,   63,   64,    0,   54,
    0,   56,   57,   58,    0,    0,    0,    0,   59,    0,
   47,    0,   48,    0,   61,    0,   62,   63,   64,   54,
    0,   56,   57,   58,    0,   75,    0,    0,   59,   75,
   75,   75,   75,   75,   61,   75,   62,   63,   64,  124,
  125,  126,  127,    0,    0,    0,   75,   75,   75,    0,
   75,   76,    0,    0,    0,   76,   76,   76,   76,   76,
    0,   76,    0,   48,   48,    0,    0,   48,   48,   48,
   48,    0,   76,   76,   76,    0,   76,    0,   63,    0,
    0,   75,   63,   63,   63,   63,   63,   64,   63,    0,
    0,   64,   64,   64,   64,   64,    0,   64,    0,   63,
   63,   63,    0,   63,    0,    0,    0,   76,   64,   64,
   64,    0,   64,    0,   65,    0,    0,    0,   65,   65,
   65,   65,   65,   58,   65,   86,    0,   44,   58,   58,
    0,   58,   58,   58,   63,   65,   65,   65,    0,   65,
    0,    0,    0,   64,  132,   44,   58,    0,   58,  130,
  128,    0,  129,  135,  131,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  155,    0,  134,    0,  133,
   65,   86,    0,    0,  132,    0,    0,   58,  157,  130,
  128,    0,  129,  135,  131,  132,    0,    0,    0,  174,
  130,  128,    0,  129,  135,  131,  132,  134,  136,  133,
  176,  130,  128,    0,  129,  135,  131,  132,  134,    0,
  133,    0,  130,  128,  181,  129,  135,  131,    0,  134,
    0,  133,    0,    0,    0,    0,    0,    0,  136,    0,
  134,    0,  133,    0,   86,    0,    0,  132,    0,  136,
    0,  183,  130,  128,    0,  129,  135,  131,    0,    0,
  136,    0,    0,   86,    0,   86,    0,    0,    0,    0,
  134,  136,  133,    0,    0,    0,    0,   75,   75,    0,
    0,   75,   75,   75,   75,    0,    0,    0,  132,    0,
   86,   86,  184,  130,  128,    0,  129,  135,  131,    0,
    0,  136,    0,   76,   76,   86,    0,   76,   76,   76,
   76,  134,    0,  133,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   63,   63,    0,    0,   63,   63,   63,   63,    0,   64,
   64,    0,  136,   64,   64,   64,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   65,   65,    0,    0,
   65,   65,   65,   65,    0,   58,   58,    0,    0,   58,
   58,   58,   58,    0,    0,  132,    0,    0,    0,  185,
  130,  128,    0,  129,  135,  131,  122,  123,    0,    0,
  124,  125,  126,  127,    0,    0,    0,    0,  134,    0,
  133,    0,    0,    0,    0,    0,    0,   70,    0,    0,
   70,    0,    0,    0,    0,    0,  122,  123,    0,    0,
  124,  125,  126,  127,   70,   70,    0,  122,  123,  136,
    0,  124,  125,  126,  127,    0,    0,    0,  122,  123,
    0,    0,  124,  125,  126,  127,    0,    0,   71,  122,
  123,   71,  132,  124,  125,  126,  127,  130,  128,   70,
  129,  135,  131,    0,    0,   71,   71,    0,    0,    0,
    0,    0,    0,    0,    0,  134,    0,  133,   69,  122,
  123,   69,  132,  124,  125,  126,  127,  130,  128,    0,
  129,  135,  131,  132,    0,   69,   69,    0,  130,  128,
   71,  129,  135,  131,    0,  134,  136,  133,  189,    0,
    0,    0,    0,    0,    0,  201,  134,    0,  133,    0,
  122,  123,    0,  132,  124,  125,  126,  127,  130,  128,
   69,  129,  135,  131,  132,    0,  136,    0,  195,  130,
  128,    0,  129,  135,  131,  220,  134,  136,  133,    0,
    0,   68,    0,    0,   68,  132,  222,  134,    0,  133,
  130,  128,    0,  129,  135,  131,   57,    0,   68,   68,
    0,   57,   57,    0,   57,   57,   57,  136,  134,    0,
  133,   61,    0,   61,   61,   61,    0,  132,  136,   57,
    0,   57,  130,  128,    0,  129,  135,  131,   61,   61,
   61,    0,   61,   68,   87,    0,    0,    0,    0,  136,
  134,   62,  133,   62,   62,   62,    0,  122,  123,    0,
   57,  124,  125,  126,  127,    0,    0,    0,   62,   62,
   62,   72,   62,   61,   72,    0,   66,    0,    0,   66,
    0,  136,    0,    0,   67,   70,   70,   67,   72,   72,
   87,   70,   70,   66,   66,    0,    0,    0,    0,    0,
    0,   67,   67,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   72,    0,    0,   71,   71,   66,    0,
    0,    0,   71,   71,  122,  123,   67,    0,  124,  125,
  126,  127,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   87,    0,    0,   69,   69,    0,    0,
    0,    0,   69,   69,  122,  123,    0,    0,  124,  125,
  126,  127,   87,    0,   87,  122,  123,    0,    0,  124,
  125,  126,  127,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   87,
   87,    0,    0,    0,    0,  122,  123,    0,    0,  124,
  125,  126,  127,    0,   87,    0,  122,  123,    0,    0,
  124,  125,  126,  127,    0,    0,    0,    0,    0,   68,
   68,    0,    0,    0,    0,   68,   68,  122,  123,    0,
    0,  124,  125,  126,  127,    0,    0,    0,   57,   57,
    0,    0,   57,   57,   57,   57,    0,    0,    0,   61,
   61,    0,    0,   61,   61,   61,   61,    0,    0,  122,
    0,    0,    0,  124,  125,  126,  127,    0,    0,    0,
    0,    0,    0,    0,    0,   94,    0,    0,    0,   62,
   62,    0,    0,   62,   62,   62,   62,    0,  109,  110,
  111,  113,    0,  114,  115,  116,    0,    0,    0,   72,
   72,    0,    0,    0,   66,   66,    0,    0,    0,    0,
    0,    0,   67,   67,  139,    0,  141,    0,    0,    0,
    0,    0,  144,    0,    0,  148,  144,  150,  151,  152,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  158,  159,  160,  161,  162,
  163,  164,  165,  166,  167,  168,  169,  170,    0,  171,
  172,    0,    0,    0,    0,    0,  178,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  109,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  144,    0,  193,    0,
    0,    0,  196,    0,    0,    0,    0,    0,    0,    0,
    0,  199,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  217,    0,    0,    0,    0,
    0,  221,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   59,   35,   36,   59,   37,   91,   40,   91,  261,   42,
   43,   45,   45,   46,   47,   41,   41,   91,  103,   44,
  265,   41,   46,  155,   92,   59,  278,   60,   11,   62,
   64,   41,  278,   58,   59,   18,  294,  295,   33,   59,
   35,   36,  174,   37,  176,   40,  264,  198,   42,   43,
   45,   45,   46,   47,  205,   37,  123,  278,   91,  277,
   42,   59,   93,   41,   46,   47,   44,   91,   93,   64,
  202,   54,   39,  125,   41,   33,   41,   35,   36,   44,
   40,   40,   40,   30,  216,   32,   41,   45,  173,  123,
   44,  125,   41,   40,  123,   41,   41,   91,   44,   44,
   41,   41,   40,   44,   44,   40,   64,   40,   33,   91,
   35,   36,   58,   59,   40,   40,   40,   40,   58,   59,
   45,  257,  258,  259,  260,  261,  262,   40,  123,   33,
  125,   35,   36,  201,   40,   40,   40,   40,   40,   64,
   59,   45,  278,   59,   61,   59,   40,   93,   59,   33,
   59,   35,   36,   93,  278,   91,   40,  278,   41,   41,
   64,   45,   44,   37,   41,  123,   59,   40,   42,   43,
   41,   45,   46,   47,   41,  278,   58,   59,   41,  123,
   64,  270,   44,   41,  289,   41,   60,    0,   62,   93,
   58,   37,  278,  125,  278,   41,   42,   43,   44,   45,
   46,   47,  123,   58,  278,  257,  258,  259,  260,  261,
  262,   93,   58,   59,   60,   61,   62,   91,   59,  278,
   41,   41,  278,  257,  258,  259,  260,  261,  262,  281,
  264,  278,  266,  267,  268,  269,   59,  271,  272,  273,
  274,  275,  276,  277,  278,   91,   41,   93,  282,  289,
  283,  284,  278,  287,  288,  280,  290,  291,  292,  293,
  294,  295,  257,  258,  259,  260,  261,  262,  278,  264,
    3,  266,  267,  268,  269,   11,  271,  272,  273,  274,
  275,  276,  277,   32,  154,  205,   -1,  282,   -1,   -1,
   -1,   -1,  287,  288,   -1,  290,  291,  292,  293,  257,
  258,  259,  260,  261,  262,   -1,  264,   -1,  266,  267,
  268,  269,   -1,  271,  272,  273,  274,  275,  276,  277,
   -1,   -1,   -1,   -1,  282,   -1,   -1,   -1,   -1,  287,
  288,   -1,  290,  291,  292,  293,  261,   -1,   -1,  264,
   -1,  266,  257,  258,  259,  260,  261,  262,  273,   -1,
  275,  276,  277,   -1,   -1,   -1,   -1,  282,   -1,   -1,
  264,   -1,  266,  288,   -1,  290,  291,  292,   -1,  273,
   -1,  275,  276,  277,   -1,   -1,   -1,   -1,  282,   -1,
  264,   -1,  266,   -1,  288,   -1,  290,  291,  292,  273,
   -1,  275,  276,  277,   -1,   37,   -1,   -1,  282,   41,
   42,   43,   44,   45,  288,   47,  290,  291,  292,  283,
  284,  285,  286,   -1,   -1,   -1,   58,   59,   60,   -1,
   62,   37,   -1,   -1,   -1,   41,   42,   43,   44,   45,
   -1,   47,   -1,  279,  280,   -1,   -1,  283,  284,  285,
  286,   -1,   58,   59,   60,   -1,   62,   -1,   37,   -1,
   -1,   93,   41,   42,   43,   44,   45,   37,   47,   -1,
   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,   58,
   59,   60,   -1,   62,   -1,   -1,   -1,   93,   58,   59,
   60,   -1,   62,   -1,   37,   -1,   -1,   -1,   41,   42,
   43,   44,   45,   37,   47,   46,   -1,   41,   42,   43,
   -1,   45,   46,   47,   93,   58,   59,   60,   -1,   62,
   -1,   -1,   -1,   93,   37,   59,   60,   -1,   62,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   58,   -1,   60,   -1,   62,
   93,   92,   -1,   -1,   37,   -1,   -1,   91,   41,   42,
   43,   -1,   45,   46,   47,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,   37,   60,   91,   62,
   41,   42,   43,   -1,   45,   46,   47,   37,   60,   -1,
   62,   -1,   42,   43,   44,   45,   46,   47,   -1,   60,
   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,
   60,   -1,   62,   -1,  155,   -1,   -1,   37,   -1,   91,
   -1,   41,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   91,   -1,   -1,  174,   -1,  176,   -1,   -1,   -1,   -1,
   60,   91,   62,   -1,   -1,   -1,   -1,  279,  280,   -1,
   -1,  283,  284,  285,  286,   -1,   -1,   -1,   37,   -1,
  201,  202,   41,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   91,   -1,  279,  280,  216,   -1,  283,  284,  285,
  286,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  279,  280,   -1,   -1,  283,  284,  285,  286,   -1,  279,
  280,   -1,   91,  283,  284,  285,  286,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  279,  280,   -1,   -1,
  283,  284,  285,  286,   -1,  279,  280,   -1,   -1,  283,
  284,  285,  286,   -1,   -1,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,  279,  280,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,   -1,   -1,   60,   -1,
   62,   -1,   -1,   -1,   -1,   -1,   -1,   41,   -1,   -1,
   44,   -1,   -1,   -1,   -1,   -1,  279,  280,   -1,   -1,
  283,  284,  285,  286,   58,   59,   -1,  279,  280,   91,
   -1,  283,  284,  285,  286,   -1,   -1,   -1,  279,  280,
   -1,   -1,  283,  284,  285,  286,   -1,   -1,   41,  279,
  280,   44,   37,  283,  284,  285,  286,   42,   43,   93,
   45,   46,   47,   -1,   -1,   58,   59,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   60,   -1,   62,   41,  279,
  280,   44,   37,  283,  284,  285,  286,   42,   43,   -1,
   45,   46,   47,   37,   -1,   58,   59,   -1,   42,   43,
   93,   45,   46,   47,   -1,   60,   91,   62,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   59,   60,   -1,   62,   -1,
  279,  280,   -1,   37,  283,  284,  285,  286,   42,   43,
   93,   45,   46,   47,   37,   -1,   91,   -1,   93,   42,
   43,   -1,   45,   46,   47,   59,   60,   91,   62,   -1,
   -1,   41,   -1,   -1,   44,   37,   59,   60,   -1,   62,
   42,   43,   -1,   45,   46,   47,   37,   -1,   58,   59,
   -1,   42,   43,   -1,   45,   46,   47,   91,   60,   -1,
   62,   41,   -1,   43,   44,   45,   -1,   37,   91,   60,
   -1,   62,   42,   43,   -1,   45,   46,   47,   58,   59,
   60,   -1,   62,   93,   46,   -1,   -1,   -1,   -1,   91,
   60,   41,   62,   43,   44,   45,   -1,  279,  280,   -1,
   91,  283,  284,  285,  286,   -1,   -1,   -1,   58,   59,
   60,   41,   62,   93,   44,   -1,   41,   -1,   -1,   44,
   -1,   91,   -1,   -1,   41,  279,  280,   44,   58,   59,
   92,  285,  286,   58,   59,   -1,   -1,   -1,   -1,   -1,
   -1,   58,   59,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   93,   -1,   -1,  279,  280,   93,   -1,
   -1,   -1,  285,  286,  279,  280,   93,   -1,  283,  284,
  285,  286,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  155,   -1,   -1,  279,  280,   -1,   -1,
   -1,   -1,  285,  286,  279,  280,   -1,   -1,  283,  284,
  285,  286,  174,   -1,  176,  279,  280,   -1,   -1,  283,
  284,  285,  286,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  201,
  202,   -1,   -1,   -1,   -1,  279,  280,   -1,   -1,  283,
  284,  285,  286,   -1,  216,   -1,  279,  280,   -1,   -1,
  283,  284,  285,  286,   -1,   -1,   -1,   -1,   -1,  279,
  280,   -1,   -1,   -1,   -1,  285,  286,  279,  280,   -1,
   -1,  283,  284,  285,  286,   -1,   -1,   -1,  279,  280,
   -1,   -1,  283,  284,  285,  286,   -1,   -1,   -1,  279,
  280,   -1,   -1,  283,  284,  285,  286,   -1,   -1,  279,
   -1,   -1,   -1,  283,  284,  285,  286,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   52,   -1,   -1,   -1,  279,
  280,   -1,   -1,  283,  284,  285,  286,   -1,   65,   66,
   67,   68,   -1,   70,   71,   72,   -1,   -1,   -1,  279,
  280,   -1,   -1,   -1,  279,  280,   -1,   -1,   -1,   -1,
   -1,   -1,  279,  280,   91,   -1,   93,   -1,   -1,   -1,
   -1,   -1,   99,   -1,   -1,  102,  103,  104,  105,  106,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  122,  123,  124,  125,  126,
  127,  128,  129,  130,  131,  132,  133,  134,   -1,  136,
  137,   -1,   -1,   -1,   -1,   -1,  143,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  154,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  173,   -1,  175,   -1,
   -1,   -1,  179,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  188,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  212,   -1,   -1,   -1,   -1,
   -1,  218,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=297;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,"'#'","'$'","'%'",null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,"'@'",null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","STRING",
"CLASS","COMPLEX","PRINY_COMP","NULL","EXTENDS","THIS","WHILE","FOR","IF",
"ELSE","RETURN","BREAK","NEW","PRINT","READ_INTEGER","READ_LINE","LITERAL",
"IDENTIFIER","AND","OR","STATIC","INSTANCEOF","LESS_EQUAL","GREATER_EQUAL",
"EQUAL","NOT_EQUAL","PRINT_COMP","CASE","DEFAULT","SUPER","DCOPY","SCOPY","DO",
"OD","III","UMINUS","EMPTY",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ClassList",
"ClassList : ClassList ClassDef",
"ClassList : ClassDef",
"VariableDef : Variable ';'",
"Variable : Type IDENTIFIER",
"Type : INT",
"Type : VOID",
"Type : BOOL",
"Type : COMPLEX",
"Type : STRING",
"Type : CLASS IDENTIFIER",
"Type : Type '[' ']'",
"ClassDef : CLASS IDENTIFIER ExtendsClause '{' FieldList '}'",
"ExtendsClause : EXTENDS IDENTIFIER",
"ExtendsClause :",
"FieldList : FieldList VariableDef",
"FieldList : FieldList FunctionDef",
"FieldList :",
"Formals : VariableList",
"Formals :",
"VariableList : VariableList ',' Variable",
"VariableList : Variable",
"FunctionDef : STATIC Type IDENTIFIER '(' Formals ')' StmtBlock",
"FunctionDef : Type IDENTIFIER '(' Formals ')' StmtBlock",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : VariableDef",
"Stmt : SimpleStmt ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : PrintCompStmt ';'",
"Stmt : BreakStmt ';'",
"Stmt : StmtBlock",
"Stmt : DoStmt",
"DoStmt : DO DoBranchList OD",
"DoBranchList : DoBranchList III DoBranch",
"DoBranchList : DoBranch",
"DoBranch : Expr ':' Stmt",
"SimpleStmt : LValue '=' Expr",
"SimpleStmt : Call",
"SimpleStmt :",
"Receiver : Expr '.'",
"Receiver :",
"LValue : Receiver IDENTIFIER",
"LValue : Expr '[' Expr ']'",
"Call : Receiver IDENTIFIER '(' Actuals ')'",
"OneCaseExpr : Constant ':' Expr ';'",
"DefCaseExpr : DEFAULT ':' Expr ';'",
"ConstCaseList : OneCaseExpr ConstCaseList",
"ConstCaseList :",
"AllCaseList : ConstCaseList DefCaseExpr",
"CondExpr : CASE '(' Expr ')' '{' AllCaseList '}'",
"Expr : LValue",
"Expr : Call",
"Expr : Constant",
"Expr : CondExpr",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr '<' Expr",
"Expr : Expr '>' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '(' Expr ')'",
"Expr : '-' Expr",
"Expr : '!' Expr",
"Expr : '@' Expr",
"Expr : '$' Expr",
"Expr : '#' Expr",
"Expr : READ_INTEGER '(' ')'",
"Expr : READ_LINE '(' ')'",
"Expr : DCOPY '(' Expr ')'",
"Expr : SCOPY '(' Expr ')'",
"Expr : THIS",
"Expr : SUPER",
"Expr : NEW IDENTIFIER '(' ')'",
"Expr : NEW Type '[' Expr ']'",
"Expr : INSTANCEOF '(' Expr ',' IDENTIFIER ')'",
"Expr : '(' CLASS IDENTIFIER ')' Expr",
"Constant : LITERAL",
"Constant : NULL",
"Actuals : ExprList",
"Actuals :",
"ExprList : ExprList ',' Expr",
"ExprList : Expr",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"ForStmt : FOR '(' SimpleStmt ';' Expr ';' SimpleStmt ')' Stmt",
"BreakStmt : BREAK",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"PrintStmt : PRINT '(' ExprList ')'",
"PrintCompStmt : PRINT_COMP '(' ExprList ')'",
};

//#line 526 "Parser.y"
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}
//#line 708 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 57 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 63 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 67 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 77 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 83 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 87 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 91 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 95 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.COMPLEX, val_peek(0).loc);
                	}
break;
case 10:
//#line 99 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 11:
//#line 103 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 12:
//#line 107 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 13:
//#line 113 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 14:
//#line 119 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 15:
//#line 123 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 16:
//#line 129 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 17:
//#line 133 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 18:
//#line 137 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 145 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 21:
//#line 152 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 22:
//#line 156 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 23:
//#line 163 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 167 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 25:
//#line 173 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 179 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 183 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 190 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 195 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 39:
//#line 212 "Parser.y"
{
						yyval.stmt = new Tree.DoStmt(val_peek(1).elist, val_peek(1).loc);
					}
break;
case 40:
//#line 218 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 41:
//#line 222 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Expr> ();
                		yyval.elist.add(val_peek(0).expr); 
                	}
break;
case 42:
//#line 230 "Parser.y"
{
						yyval.expr = new Tree.DoBranch(val_peek(2).expr, val_peek(0).stmt, val_peek(2).loc);
					}
break;
case 43:
//#line 236 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 44:
//#line 240 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 45:
//#line 244 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 47:
//#line 251 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 48:
//#line 257 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 49:
//#line 264 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 50:
//#line 270 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 51:
//#line 279 "Parser.y"
{
						yyval.expr = new Tree.CaseExpr(val_peek(3).expr, val_peek(1).expr, val_peek(1).loc);
					}
break;
case 52:
//#line 285 "Parser.y"
{
						yyval.expr = new Tree.CaseExpr(val_peek(1).expr, val_peek(3).loc);
					}
break;
case 53:
//#line 291 "Parser.y"
{
						yyval.elist = val_peek(0).elist;
						yyval.elist.add(0, val_peek(1).expr);
					}
break;
case 54:
//#line 296 "Parser.y"
{
						yyval = new SemValue();
                		yyval.elist = new ArrayList<Expr>();
					}
break;
case 55:
//#line 303 "Parser.y"
{
						yyval.elist = val_peek(1).elist;
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 56:
//#line 310 "Parser.y"
{
                		yyval.expr = new Tree.CondExpr(val_peek(4).expr, val_peek(1).elist, val_peek(-1).loc);
                	}
break;
case 57:
//#line 316 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 61:
//#line 323 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 327 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 331 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 335 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 339 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 66:
//#line 343 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 67:
//#line 347 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 68:
//#line 351 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 69:
//#line 355 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 70:
//#line 359 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 71:
//#line 363 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 72:
//#line 367 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 73:
//#line 371 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 74:
//#line 375 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 75:
//#line 379 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 76:
//#line 383 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 77:
//#line 387 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.RE, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 78:
//#line 391 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.IM, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 79:
//#line 395 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.COMPCAST, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 80:
//#line 399 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 81:
//#line 403 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 82:
//#line 407 "Parser.y"
{
                		yyval.expr = new Tree.DCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 83:
//#line 411 "Parser.y"
{
                		yyval.expr = new Tree.SCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 84:
//#line 415 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 85:
//#line 419 "Parser.y"
{
                		yyval.expr = new Tree.SuperExpr(val_peek(0).loc);
                	}
break;
case 86:
//#line 423 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 87:
//#line 427 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 88:
//#line 431 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 89:
//#line 435 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 90:
//#line 441 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 91:
//#line 445 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 93:
//#line 452 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 94:
//#line 459 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 95:
//#line 463 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 96:
//#line 470 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 97:
//#line 476 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 98:
//#line 482 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 99:
//#line 488 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 100:
//#line 494 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 101:
//#line 498 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 102:
//#line 504 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 103:
//#line 508 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 104:
//#line 514 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 105:
//#line 520 "Parser.y"
{
						yyval.stmt = new PrintComp(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1408 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
