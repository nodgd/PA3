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
public final static short PRINT_COMP=275;
public final static short READ_INTEGER=276;
public final static short READ_LINE=277;
public final static short LITERAL=278;
public final static short IDENTIFIER=279;
public final static short AND=280;
public final static short OR=281;
public final static short STATIC=282;
public final static short INSTANCEOF=283;
public final static short LESS_EQUAL=284;
public final static short GREATER_EQUAL=285;
public final static short EQUAL=286;
public final static short NOT_EQUAL=287;
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
    0,    0,   98,    0,    0,    0,    0,    0,   90,    0,
    0,   85,    0,    0,    0,    0,    0,    0,   25,    0,
    0,    0,   28,   37,   26,    0,   30,   31,   32,    0,
    0,    0,    0,   38,    0,    0,    0,    0,   59,   60,
    0,    0,    0,    0,   57,   58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   41,    0,    0,
    0,    0,    0,    0,    0,    0,   29,   33,   34,   35,
   36,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   80,   81,    0,    0,
    0,    0,   39,    0,    0,    0,   74,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   86,    0,    0,  104,
  105,    0,    0,   82,   83,   40,   42,    0,   49,    0,
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
final static short yysindex[] = {                      -229,
 -232,    0, -229,    0, -215,    0, -224,  -54,    0,    0,
  -98,    0,    0,    0,    0, -205,    0,  240,    0,    0,
   28,  -85,    0,    0,  -83,    0,   55,    8,   65,  240,
    0,  240,    0,  -73,   67,   68,   70,    0,   -9,  240,
   -9,    0,    0,    0,    0,    6,    0,    0,   73,   76,
   77,  105,    0,  -49,   79,   82,   83,   84,    0,   86,
   87,    0,   92,   93,  105,  105,  105,   64,    0,  105,
  105,  105,    0,    0,    0,   78,    0,    0,    0,   80,
   89,   95,   97,    0,  877,   74,    0, -143,    0,    0,
  105,  105,  105,  877,    0,    0,  102,   52,  105,  105,
  103,  110,  105,  105,  105,  105, -210,    0,  475,  -26,
  -26, -133,  501,  877,  877,  877,    0,    0,    0,    0,
    0,  105,  105,  105,  105,  105,  105,  105,  105,  105,
  105,  105,  105,  105,    0,  105,  105,  112,  512,   98,
  534,  117,   85,  877,  -20,   23,    0,    0,  562,  573,
  615,  677,    0,  105,   43,  129,    0,  935,  909,   15,
   15,  -32,  -32,   56,   56,  -26,  -26,  -26,   15,   15,
  790,  877,  105,   43,  105,   43,    0,  824,  105,    0,
    0, -100,   62,    0,    0,    0,    0,  105,    0,  145,
  143,    0,  704,  -66,    0,  877,  166, -255,    0,    0,
  105,   43,    0,    0, -255,  164,  -57,  122,  207,    0,
    0,  105,  193,    0,    0,   43,  764,  105,    0,    0,
  845,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  271,    0,  162,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  228,    0,    0,  247,
    0,  247,    0,    0,    0,  249,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -42,    0,    0,    0,    0,
    0,  -10,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   12,   12,   12,   12,    0,   12,
   12,   12,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  888,  449,    0,    0,    0,
   12,  -42,   12,  233,    0,    0,    0,    0,   12,   12,
    0,    0,   12,   12,   12,   12,    0,    0,    0,  156,
  361,    0,    0,   22,  124,  161,    0,    0,    0,    0,
    0,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,    0,   12,   12,  130,    0,    0,
    0,    0,   12,   45,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   12,  -42,    0,    0,  524,  -25,  835,
  901, 1020, 1024,  960,  969,  387,  414,  423,  980,  996,
    0,  -19,   -3,  -42,   12,  -42,    0,    0,   12,    0,
    0,    0,    0,    0,    0,    0,    0,   12,    0,    0,
  252,    0,    0,  -33,    0,   50,    0,   17,    0,    0,
    7,  -42,    0,    0,   17,    0,    0,    0,    0,    0,
    0,   12,    0,    0,    0,  -42,    0,   12,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,  292,  297,   13,   18,    0,    0,    0,  281,    0,
   34,    0, -120,  -91,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  168, 1219,  -21,  510,    0,    0,    0,
 -161,    0,  118,    0,    0,  -96,    0,
};
final static int YYTABLESIZE=1437;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        101,
  140,  101,  101,  146,  132,   28,  101,   28,   47,  130,
  128,  101,  129,  135,  131,   73,   45,   28,   73,  135,
  180,   43,   59,  179,   86,  101,   19,  134,   22,  133,
  101,    1,   73,   73,  187,   25,  206,   93,   67,   43,
   72,   71,   33,  206,   33,   68,    5,   45,  103,    7,
   66,  132,   44,  192,    9,  194,  130,  128,  136,  129,
  135,  131,   77,  181,  136,   77,  179,   73,   10,   70,
   86,   98,   43,   24,   45,   67,  191,   72,   71,   77,
   77,  210,   68,  153,  154,   95,   26,   66,   95,  101,
   94,  101,  132,   94,   30,  219,   67,  130,   72,   71,
   31,  135,  131,   68,   32,  136,   70,   39,   66,  209,
   41,   40,   91,   42,   77,   92,   93,   67,   99,   72,
   71,  100,  101,  102,   68,  103,  104,   70,   42,   66,
   69,  105,  106,   86,  137,  138,  117,   67,  118,   72,
   71,  142,  143,  147,   68,  156,  136,  119,   70,   66,
  148,  173,   86,  120,   86,  121,  175,  177,   12,   13,
   14,   15,   16,   17,   78,   42,   48,   78,   70,  188,
   48,   48,   48,   48,   48,   48,   48,   31,  197,   86,
   86,   78,   78,   18,  198,  200,  179,   48,   48,   48,
   48,   48,   75,   27,   86,   29,   75,   75,   75,   75,
   75,   79,   75,  202,   79,   38,  204,   12,   13,   14,
   15,   16,   17,   75,   75,   75,   78,   75,   79,   79,
   48,  212,   48,  101,  101,  101,  101,  101,  101,   97,
  101,  213,  101,  101,  101,  101,   47,  101,  101,  101,
  101,  101,  101,  101,  101,  101,  215,  216,   75,  101,
  218,  124,  125,   79,  101,   73,  101,  101,  101,  101,
  101,  101,   12,   13,   14,   15,   16,   17,   47,   47,
    1,   48,   49,   50,   51,   47,   52,   53,   54,   55,
   56,   57,   58,   59,   15,   47,    5,   20,   60,   19,
   47,  102,   92,   61,    6,   62,   63,   64,   65,   12,
   13,   14,   15,   16,   17,   54,   47,   20,   48,   49,
   50,   51,   37,   52,   53,   54,   55,   56,   57,   58,
   59,  186,  211,    0,  112,   60,    0,   47,    0,   48,
   61,    0,   62,   63,   64,   65,   54,    0,    0,   57,
   58,   59,    0,    0,    0,    0,   60,    0,   47,    0,
   48,   61,    0,   62,   63,   64,    0,   54,    0,    0,
   57,   58,   59,    0,    0,    0,    0,   60,   47,    0,
   48,    0,   61,    0,   62,   63,   64,   54,    0,    0,
   57,   58,   59,    0,    0,    0,    0,   60,    0,    0,
    0,    0,   61,    0,   62,   63,   64,   76,    0,    0,
    0,   76,   76,   76,   76,   76,    0,   76,    0,   48,
   48,    0,    0,   48,   48,   48,   48,    0,   76,   76,
   76,    0,   76,   63,    0,    0,    0,   63,   63,   63,
   63,   63,    0,   63,    0,   75,   75,    0,    0,   75,
   75,   75,   75,    0,   63,   63,   63,    0,   63,    0,
   64,    0,    0,   76,   64,   64,   64,   64,   64,   65,
   64,    0,    0,   65,   65,   65,   65,   65,    0,   65,
    0,   64,   64,   64,    0,   64,    0,    0,    0,   63,
   65,   65,   65,    0,   65,   58,    0,    0,    0,   44,
   58,   58,    0,   58,   58,   58,   12,   13,   14,   15,
   16,   17,    0,    0,    0,    0,   64,   44,   58,    0,
   58,  132,    0,    0,    0,   65,  130,  128,    0,  129,
  135,  131,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  155,    0,  134,    0,  133,  132,    0,   58,
    0,  157,  130,  128,    0,  129,  135,  131,  132,    0,
    0,    0,  174,  130,  128,   87,  129,  135,  131,    0,
  134,    0,  133,    0,   72,  136,    0,   72,    0,    0,
  132,  134,    0,  133,  176,  130,  128,    0,  129,  135,
  131,   72,   72,    0,    0,    0,    0,    0,    0,    0,
    0,  136,    0,  134,    0,  133,    0,    0,  132,    0,
    0,   87,  136,  130,  128,  182,  129,  135,  131,  132,
    0,    0,    0,  183,  130,  128,   72,  129,  135,  131,
    0,  134,    0,  133,  136,    0,    0,    0,    0,    0,
    0,    0,  134,    0,  133,    0,    0,    0,    0,    0,
   76,   76,    0,    0,   76,   76,   76,   76,    0,    0,
    0,  132,  136,    0,    0,  184,  130,  128,    0,  129,
  135,  131,    0,  136,   87,    0,   63,   63,    0,    0,
   63,   63,   63,   63,  134,    0,  133,    0,    0,    0,
    0,    0,    0,   87,    0,   87,    0,    0,    0,    0,
    0,    0,    0,   64,   64,    0,    0,   64,   64,   64,
   64,    0,   65,   65,    0,  136,   65,   65,   65,   65,
   87,   87,    0,  132,    0,    0,    0,  185,  130,  128,
    0,  129,  135,  131,    0,   87,    0,    0,   58,   58,
    0,    0,   58,   58,   58,   58,  134,    0,  133,    0,
  132,    0,    0,    0,    0,  130,  128,    0,  129,  135,
  131,    0,    0,    0,  122,  123,    0,    0,  124,  125,
  126,  127,  201,  134,    0,  133,    0,  136,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  122,  123,    0,    0,  124,  125,  126,  127,    0,    0,
    0,  122,  123,    0,  136,  124,  125,  126,  127,    0,
  132,    0,    0,   72,   72,  130,  128,    0,  129,  135,
  131,    0,    0,  122,  123,    0,    0,  124,  125,  126,
  127,    0,  220,  134,    0,  133,  132,    0,    0,    0,
    0,  130,  128,    0,  129,  135,  131,    0,    0,    0,
    0,  122,  123,    0,    0,  124,  125,  126,  127,  134,
    0,  133,  122,  123,  136,    0,  124,  125,  126,  127,
  132,    0,    0,    0,    0,  130,  128,    0,  129,  135,
  131,    0,    0,    0,    0,   70,    0,    0,   70,    0,
  136,  132,  189,  134,    0,  133,  130,  128,    0,  129,
  135,  131,   70,   70,  122,  123,    0,    0,  124,  125,
  126,  127,    0,  222,  134,    0,  133,    0,    0,    0,
    0,    0,    0,  132,  136,    0,  195,    0,  130,  128,
    0,  129,  135,  131,   57,    0,    0,   70,    0,   57,
   57,    0,   57,   57,   57,  136,  134,    0,  133,    0,
    0,   71,    0,    0,   71,  132,    0,   57,    0,   57,
  130,  128,    0,  129,  135,  131,  122,  123,   71,   71,
  124,  125,  126,  127,    0,    0,    0,  136,  134,    0,
  133,  132,    0,    0,    0,    0,  130,  128,   57,  129,
  135,  131,    0,  122,  123,    0,    0,  124,  125,  126,
  127,    0,    0,   71,  134,    0,  133,    0,    0,  136,
   61,    0,   61,   61,   61,    0,    0,    0,    0,   62,
    0,   62,   62,   62,    0,    0,    0,   61,   61,   61,
   69,   61,    0,   69,    0,  136,   62,   62,   62,    0,
   62,    0,    0,    0,    0,    0,   68,   69,   69,   68,
    0,    0,    0,  122,  123,    0,    0,  124,  125,  126,
  127,    0,   61,   68,   68,    0,    0,    0,    0,    0,
   66,   62,    0,   66,   67,    0,    0,   67,    0,  122,
  123,    0,   69,  124,  125,  126,  127,   66,   66,    0,
    0,   67,   67,    0,    0,    0,    0,    0,   68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  122,  123,    0,    0,  124,  125,  126,
  127,    0,   66,    0,   70,   70,   67,    0,    0,    0,
   70,   70,    0,    0,  122,  123,    0,    0,  124,  125,
  126,  127,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  122,  123,    0,    0,
  124,  125,  126,  127,    0,    0,    0,   57,   57,    0,
    0,   57,   57,   57,   57,    0,    0,    0,    0,    0,
   71,   71,    0,    0,    0,    0,   71,   71,  122,    0,
    0,    0,  124,  125,  126,  127,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  124,  125,
  126,  127,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   61,
   61,    0,    0,   61,   61,   61,   61,    0,   62,   62,
    0,    0,   62,   62,   62,   62,    0,    0,    0,   69,
   69,    0,    0,    0,    0,   69,   69,    0,    0,    0,
   94,    0,    0,    0,    0,   68,   68,    0,    0,    0,
    0,   68,   68,  109,  110,  111,  113,    0,  114,  115,
  116,    0,    0,    0,    0,    0,    0,    0,    0,   66,
   66,    0,    0,   67,   67,    0,    0,    0,    0,  139,
    0,  141,    0,    0,    0,    0,    0,  144,  144,    0,
    0,  149,  150,  151,  152,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  158,  159,  160,  161,  162,  163,  164,  165,  166,  167,
  168,  169,  170,    0,  171,  172,    0,    0,    0,    0,
    0,  178,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  109,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  144,    0,  193,    0,    0,    0,  196,    0,    0,
    0,    0,    0,    0,    0,    0,  199,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  217,    0,    0,    0,    0,    0,  221,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
   92,   35,   36,  100,   37,   91,   40,   91,  264,   42,
   43,   45,   45,   46,   47,   41,   59,   91,   44,   46,
   41,   41,  278,   44,   46,   59,  125,   60,   11,   62,
   64,  261,   58,   59,  155,   18,  198,   41,   33,   59,
   35,   36,   30,  205,   32,   40,  279,   41,   59,  265,
   45,   37,   40,  174,  279,  176,   42,   43,   91,   45,
   46,   47,   41,   41,   91,   44,   44,   93,  123,   64,
   92,   54,   39,  279,   41,   33,  173,   35,   36,   58,
   59,  202,   40,  294,  295,   41,   59,   45,   44,  123,
   41,  125,   37,   44,   40,  216,   33,   42,   35,   36,
   93,   46,   47,   40,   40,   91,   64,   41,   45,  201,
   41,   44,   40,  123,   93,   40,   40,   33,   40,   35,
   36,   40,   40,   40,   40,   40,   40,   64,  123,   45,
  125,   40,   40,  155,   61,  279,   59,   33,   59,   35,
   36,   40,   91,   41,   40,  279,   91,   59,   64,   45,
   41,   40,  174,   59,  176,   59,   59,   41,  257,  258,
  259,  260,  261,  262,   41,  123,   37,   44,   64,   41,
   41,   42,   43,   44,   45,   46,   47,   93,  279,  201,
  202,   58,   59,  282,  123,   41,   44,   58,   59,   60,
   61,   62,   37,  279,  216,  279,   41,   42,   43,   44,
   45,   41,   47,  270,   44,  279,   41,  257,  258,  259,
  260,  261,  262,   58,   59,   60,   93,   62,   58,   59,
   91,   58,   93,  257,  258,  259,  260,  261,  262,  279,
  264,  289,  266,  267,  268,  269,  279,  271,  272,  273,
  274,  275,  276,  277,  278,  279,  125,   41,   93,  283,
   58,  284,  285,   93,  288,  281,  290,  291,  292,  293,
  294,  295,  257,  258,  259,  260,  261,  262,  279,  264,
    0,  266,  267,  268,  269,  279,  271,  272,  273,  274,
  275,  276,  277,  278,  123,  279,   59,   41,  283,   41,
  279,   59,   41,  288,    3,  290,  291,  292,  293,  257,
  258,  259,  260,  261,  262,  289,  264,   11,  266,  267,
  268,  269,   32,  271,  272,  273,  274,  275,  276,  277,
  278,  154,  205,   -1,  261,  283,   -1,  264,   -1,  266,
  288,   -1,  290,  291,  292,  293,  273,   -1,   -1,  276,
  277,  278,   -1,   -1,   -1,   -1,  283,   -1,  264,   -1,
  266,  288,   -1,  290,  291,  292,   -1,  273,   -1,   -1,
  276,  277,  278,   -1,   -1,   -1,   -1,  283,  264,   -1,
  266,   -1,  288,   -1,  290,  291,  292,  273,   -1,   -1,
  276,  277,  278,   -1,   -1,   -1,   -1,  283,   -1,   -1,
   -1,   -1,  288,   -1,  290,  291,  292,   37,   -1,   -1,
   -1,   41,   42,   43,   44,   45,   -1,   47,   -1,  280,
  281,   -1,   -1,  284,  285,  286,  287,   -1,   58,   59,
   60,   -1,   62,   37,   -1,   -1,   -1,   41,   42,   43,
   44,   45,   -1,   47,   -1,  280,  281,   -1,   -1,  284,
  285,  286,  287,   -1,   58,   59,   60,   -1,   62,   -1,
   37,   -1,   -1,   93,   41,   42,   43,   44,   45,   37,
   47,   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,
   -1,   58,   59,   60,   -1,   62,   -1,   -1,   -1,   93,
   58,   59,   60,   -1,   62,   37,   -1,   -1,   -1,   41,
   42,   43,   -1,   45,   46,   47,  257,  258,  259,  260,
  261,  262,   -1,   -1,   -1,   -1,   93,   59,   60,   -1,
   62,   37,   -1,   -1,   -1,   93,   42,   43,   -1,   45,
   46,   47,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   58,   -1,   60,   -1,   62,   37,   -1,   91,
   -1,   41,   42,   43,   -1,   45,   46,   47,   37,   -1,
   -1,   -1,   41,   42,   43,   46,   45,   46,   47,   -1,
   60,   -1,   62,   -1,   41,   91,   -1,   44,   -1,   -1,
   37,   60,   -1,   62,   41,   42,   43,   -1,   45,   46,
   47,   58,   59,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   60,   -1,   62,   -1,   -1,   37,   -1,
   -1,   92,   91,   42,   43,   44,   45,   46,   47,   37,
   -1,   -1,   -1,   41,   42,   43,   93,   45,   46,   47,
   -1,   60,   -1,   62,   91,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,
  280,  281,   -1,   -1,  284,  285,  286,  287,   -1,   -1,
   -1,   37,   91,   -1,   -1,   41,   42,   43,   -1,   45,
   46,   47,   -1,   91,  155,   -1,  280,  281,   -1,   -1,
  284,  285,  286,  287,   60,   -1,   62,   -1,   -1,   -1,
   -1,   -1,   -1,  174,   -1,  176,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  280,  281,   -1,   -1,  284,  285,  286,
  287,   -1,  280,  281,   -1,   91,  284,  285,  286,  287,
  201,  202,   -1,   37,   -1,   -1,   -1,   41,   42,   43,
   -1,   45,   46,   47,   -1,  216,   -1,   -1,  280,  281,
   -1,   -1,  284,  285,  286,  287,   60,   -1,   62,   -1,
   37,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,
   47,   -1,   -1,   -1,  280,  281,   -1,   -1,  284,  285,
  286,  287,   59,   60,   -1,   62,   -1,   91,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  280,  281,   -1,   -1,  284,  285,  286,  287,   -1,   -1,
   -1,  280,  281,   -1,   91,  284,  285,  286,  287,   -1,
   37,   -1,   -1,  280,  281,   42,   43,   -1,   45,   46,
   47,   -1,   -1,  280,  281,   -1,   -1,  284,  285,  286,
  287,   -1,   59,   60,   -1,   62,   37,   -1,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,
   -1,  280,  281,   -1,   -1,  284,  285,  286,  287,   60,
   -1,   62,  280,  281,   91,   -1,  284,  285,  286,  287,
   37,   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   46,
   47,   -1,   -1,   -1,   -1,   41,   -1,   -1,   44,   -1,
   91,   37,   93,   60,   -1,   62,   42,   43,   -1,   45,
   46,   47,   58,   59,  280,  281,   -1,   -1,  284,  285,
  286,  287,   -1,   59,   60,   -1,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   37,   91,   -1,   93,   -1,   42,   43,
   -1,   45,   46,   47,   37,   -1,   -1,   93,   -1,   42,
   43,   -1,   45,   46,   47,   91,   60,   -1,   62,   -1,
   -1,   41,   -1,   -1,   44,   37,   -1,   60,   -1,   62,
   42,   43,   -1,   45,   46,   47,  280,  281,   58,   59,
  284,  285,  286,  287,   -1,   -1,   -1,   91,   60,   -1,
   62,   37,   -1,   -1,   -1,   -1,   42,   43,   91,   45,
   46,   47,   -1,  280,  281,   -1,   -1,  284,  285,  286,
  287,   -1,   -1,   93,   60,   -1,   62,   -1,   -1,   91,
   41,   -1,   43,   44,   45,   -1,   -1,   -1,   -1,   41,
   -1,   43,   44,   45,   -1,   -1,   -1,   58,   59,   60,
   41,   62,   -1,   44,   -1,   91,   58,   59,   60,   -1,
   62,   -1,   -1,   -1,   -1,   -1,   41,   58,   59,   44,
   -1,   -1,   -1,  280,  281,   -1,   -1,  284,  285,  286,
  287,   -1,   93,   58,   59,   -1,   -1,   -1,   -1,   -1,
   41,   93,   -1,   44,   41,   -1,   -1,   44,   -1,  280,
  281,   -1,   93,  284,  285,  286,  287,   58,   59,   -1,
   -1,   58,   59,   -1,   -1,   -1,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  280,  281,   -1,   -1,  284,  285,  286,
  287,   -1,   93,   -1,  280,  281,   93,   -1,   -1,   -1,
  286,  287,   -1,   -1,  280,  281,   -1,   -1,  284,  285,
  286,  287,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  280,  281,   -1,   -1,
  284,  285,  286,  287,   -1,   -1,   -1,  280,  281,   -1,
   -1,  284,  285,  286,  287,   -1,   -1,   -1,   -1,   -1,
  280,  281,   -1,   -1,   -1,   -1,  286,  287,  280,   -1,
   -1,   -1,  284,  285,  286,  287,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  284,  285,
  286,  287,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  280,
  281,   -1,   -1,  284,  285,  286,  287,   -1,  280,  281,
   -1,   -1,  284,  285,  286,  287,   -1,   -1,   -1,  280,
  281,   -1,   -1,   -1,   -1,  286,  287,   -1,   -1,   -1,
   52,   -1,   -1,   -1,   -1,  280,  281,   -1,   -1,   -1,
   -1,  286,  287,   65,   66,   67,   68,   -1,   70,   71,
   72,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  280,
  281,   -1,   -1,  280,  281,   -1,   -1,   -1,   -1,   91,
   -1,   93,   -1,   -1,   -1,   -1,   -1,   99,  100,   -1,
   -1,  103,  104,  105,  106,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  122,  123,  124,  125,  126,  127,  128,  129,  130,  131,
  132,  133,  134,   -1,  136,  137,   -1,   -1,   -1,   -1,
   -1,  143,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  154,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  173,   -1,  175,   -1,   -1,   -1,  179,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  188,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  212,   -1,   -1,   -1,   -1,   -1,  218,
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
"ELSE","RETURN","BREAK","NEW","PRINT","PRINT_COMP","READ_INTEGER","READ_LINE",
"LITERAL","IDENTIFIER","AND","OR","STATIC","INSTANCEOF","LESS_EQUAL",
"GREATER_EQUAL","EQUAL","NOT_EQUAL","CASE","DEFAULT","SUPER","DCOPY","SCOPY",
"DO","OD","III","UMINUS","EMPTY",
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

//#line 525 "Parser.y"
    
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
//#line 718 "Parser.java"
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
//#line 56 "Parser.y"
{
						tree = new Tree.TopLevel(val_peek(0).clist, val_peek(0).loc);
					}
break;
case 2:
//#line 62 "Parser.y"
{
						yyval.clist.add(val_peek(0).cdef);
					}
break;
case 3:
//#line 66 "Parser.y"
{
                		yyval.clist = new ArrayList<Tree.ClassDef>();
                		yyval.clist.add(val_peek(0).cdef);
                	}
break;
case 5:
//#line 76 "Parser.y"
{
						yyval.vdef = new Tree.VarDef(val_peek(0).ident, val_peek(1).type, val_peek(0).loc);
					}
break;
case 6:
//#line 82 "Parser.y"
{
						yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
					}
break;
case 7:
//#line 86 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc);
                	}
break;
case 8:
//#line 90 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc);
                	}
break;
case 9:
//#line 94 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.COMPLEX, val_peek(0).loc);
                	}
break;
case 10:
//#line 98 "Parser.y"
{
                		yyval.type = new Tree.TypeIdent(Tree.STRING, val_peek(0).loc);
                	}
break;
case 11:
//#line 102 "Parser.y"
{
                		yyval.type = new Tree.TypeClass(val_peek(0).ident, val_peek(1).loc);
                	}
break;
case 12:
//#line 106 "Parser.y"
{
                		yyval.type = new Tree.TypeArray(val_peek(2).type, val_peek(2).loc);
                	}
break;
case 13:
//#line 112 "Parser.y"
{
						yyval.cdef = new Tree.ClassDef(val_peek(4).ident, val_peek(3).ident, val_peek(1).flist, val_peek(5).loc);
					}
break;
case 14:
//#line 118 "Parser.y"
{
						yyval.ident = val_peek(0).ident;
					}
break;
case 15:
//#line 122 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 16:
//#line 128 "Parser.y"
{
						yyval.flist.add(val_peek(0).vdef);
					}
break;
case 17:
//#line 132 "Parser.y"
{
						yyval.flist.add(val_peek(0).fdef);
					}
break;
case 18:
//#line 136 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.flist = new ArrayList<Tree>();
                	}
break;
case 20:
//#line 144 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.vlist = new ArrayList<Tree.VarDef>(); 
                	}
break;
case 21:
//#line 151 "Parser.y"
{
						yyval.vlist.add(val_peek(0).vdef);
					}
break;
case 22:
//#line 155 "Parser.y"
{
                		yyval.vlist = new ArrayList<Tree.VarDef>();
						yyval.vlist.add(val_peek(0).vdef);
                	}
break;
case 23:
//#line 162 "Parser.y"
{
						yyval.fdef = new MethodDef(true, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 24:
//#line 166 "Parser.y"
{
						yyval.fdef = new MethodDef(false, val_peek(4).ident, val_peek(5).type, val_peek(2).vlist, (Block) val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 25:
//#line 172 "Parser.y"
{
						yyval.stmt = new Block(val_peek(1).slist, val_peek(2).loc);
					}
break;
case 26:
//#line 178 "Parser.y"
{
						yyval.slist.add(val_peek(0).stmt);
					}
break;
case 27:
//#line 182 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.slist = new ArrayList<Tree>();
                	}
break;
case 28:
//#line 189 "Parser.y"
{
						yyval.stmt = val_peek(0).vdef;
					}
break;
case 29:
//#line 194 "Parser.y"
{
                		if (yyval.stmt == null) {
                			yyval.stmt = new Tree.Skip(val_peek(0).loc);
                		}
                	}
break;
case 39:
//#line 211 "Parser.y"
{
						yyval.stmt = new Tree.DoStmt(val_peek(1).elist, val_peek(1).loc);
					}
break;
case 40:
//#line 217 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 41:
//#line 221 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Expr> ();
                		yyval.elist.add(val_peek(0).expr); 
                	}
break;
case 42:
//#line 229 "Parser.y"
{
						yyval.expr = new Tree.DoBranch(val_peek(2).expr, val_peek(0).stmt, val_peek(2).loc);
					}
break;
case 43:
//#line 235 "Parser.y"
{
						yyval.stmt = new Tree.Assign(val_peek(2).lvalue, val_peek(0).expr, val_peek(1).loc);
					}
break;
case 44:
//#line 239 "Parser.y"
{
                		yyval.stmt = new Tree.Exec(val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 45:
//#line 243 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 47:
//#line 250 "Parser.y"
{
                		yyval = new SemValue();
                	}
break;
case 48:
//#line 256 "Parser.y"
{
						yyval.lvalue = new Tree.Ident(val_peek(1).expr, val_peek(0).ident, val_peek(0).loc);
						if (val_peek(1).loc == null) {
							yyval.loc = val_peek(0).loc;
						}
					}
break;
case 49:
//#line 263 "Parser.y"
{
                		yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 50:
//#line 269 "Parser.y"
{
						yyval.expr = new Tree.CallExpr(val_peek(4).expr, val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
						if (val_peek(4).loc == null) {
							yyval.loc = val_peek(3).loc;
						}
					}
break;
case 51:
//#line 278 "Parser.y"
{
						yyval.expr = new Tree.CaseExpr(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
					}
break;
case 52:
//#line 284 "Parser.y"
{
						yyval.expr = new Tree.CaseExpr(val_peek(1).expr, val_peek(3).loc);
					}
break;
case 53:
//#line 290 "Parser.y"
{
						yyval.elist = val_peek(0).elist;
						yyval.elist.add(0, val_peek(1).expr);
					}
break;
case 54:
//#line 295 "Parser.y"
{
						yyval = new SemValue();
                		yyval.elist = new ArrayList<Expr>();
					}
break;
case 55:
//#line 302 "Parser.y"
{
						yyval.elist = val_peek(1).elist;
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 56:
//#line 309 "Parser.y"
{
                		yyval.expr = new Tree.CondExpr(val_peek(4).expr, val_peek(1).elist, val_peek(6).loc);
                	}
break;
case 57:
//#line 315 "Parser.y"
{
						yyval.expr = val_peek(0).lvalue;
					}
break;
case 61:
//#line 322 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 62:
//#line 326 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 63:
//#line 330 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 64:
//#line 334 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 65:
//#line 338 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 66:
//#line 342 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 67:
//#line 346 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 68:
//#line 350 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 69:
//#line 354 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 70:
//#line 358 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 71:
//#line 362 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 72:
//#line 366 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 73:
//#line 370 "Parser.y"
{
                		yyval.expr = new Tree.Binary(Tree.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 74:
//#line 374 "Parser.y"
{
                		yyval = val_peek(1);
                	}
break;
case 75:
//#line 378 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NEG, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 76:
//#line 382 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.NOT, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 77:
//#line 386 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.RE, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 78:
//#line 390 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.IM, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 79:
//#line 394 "Parser.y"
{
                		yyval.expr = new Tree.Unary(Tree.COMPCAST, val_peek(0).expr, val_peek(1).loc);
                	}
break;
case 80:
//#line 398 "Parser.y"
{
                		yyval.expr = new Tree.ReadIntExpr(val_peek(2).loc);
                	}
break;
case 81:
//#line 402 "Parser.y"
{
                		yyval.expr = new Tree.ReadLineExpr(val_peek(2).loc);
                	}
break;
case 82:
//#line 406 "Parser.y"
{
                		yyval.expr = new Tree.DCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 83:
//#line 410 "Parser.y"
{
                		yyval.expr = new Tree.SCopyExpr(val_peek(1).expr, val_peek(3).loc);
                	}
break;
case 84:
//#line 414 "Parser.y"
{
                		yyval.expr = new Tree.ThisExpr(val_peek(0).loc);
                	}
break;
case 85:
//#line 418 "Parser.y"
{
                		yyval.expr = new Tree.SuperExpr(val_peek(0).loc);
                	}
break;
case 86:
//#line 422 "Parser.y"
{
                		yyval.expr = new Tree.NewClass(val_peek(2).ident, val_peek(3).loc);
                	}
break;
case 87:
//#line 426 "Parser.y"
{
                		yyval.expr = new Tree.NewArray(val_peek(3).type, val_peek(1).expr, val_peek(4).loc);
                	}
break;
case 88:
//#line 430 "Parser.y"
{
                		yyval.expr = new Tree.TypeTest(val_peek(3).expr, val_peek(1).ident, val_peek(5).loc);
                	}
break;
case 89:
//#line 434 "Parser.y"
{
                		yyval.expr = new Tree.TypeCast(val_peek(2).ident, val_peek(0).expr, val_peek(0).loc);
                	}
break;
case 90:
//#line 440 "Parser.y"
{
						yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
					}
break;
case 91:
//#line 444 "Parser.y"
{
						yyval.expr = new Null(val_peek(0).loc);
					}
break;
case 93:
//#line 451 "Parser.y"
{
                		yyval = new SemValue();
                		yyval.elist = new ArrayList<Tree.Expr>();
                	}
break;
case 94:
//#line 458 "Parser.y"
{
						yyval.elist.add(val_peek(0).expr);
					}
break;
case 95:
//#line 462 "Parser.y"
{
                		yyval.elist = new ArrayList<Tree.Expr>();
						yyval.elist.add(val_peek(0).expr);
                	}
break;
case 96:
//#line 469 "Parser.y"
{
						yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
					}
break;
case 97:
//#line 475 "Parser.y"
{
						yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
					}
break;
case 98:
//#line 481 "Parser.y"
{
						yyval.stmt = new Tree.Break(val_peek(0).loc);
					}
break;
case 99:
//#line 487 "Parser.y"
{
						yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
					}
break;
case 100:
//#line 493 "Parser.y"
{
						yyval.stmt = val_peek(0).stmt;
					}
break;
case 101:
//#line 497 "Parser.y"
{
						yyval = new SemValue();
					}
break;
case 102:
//#line 503 "Parser.y"
{
						yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
					}
break;
case 103:
//#line 507 "Parser.y"
{
                		yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                	}
break;
case 104:
//#line 513 "Parser.y"
{
						yyval.stmt = new Print(val_peek(1).elist, val_peek(3).loc);
					}
break;
case 105:
//#line 519 "Parser.y"
{
						yyval.stmt = new PrintComp(val_peek(1).elist, val_peek(3).loc);
					}
break;
//#line 1418 "Parser.java"
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
