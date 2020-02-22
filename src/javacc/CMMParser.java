/* Generated By:JavaCC: Do not edit this line. CMMParser.java */
package javacc;
import java.util.*;

public class CMMParser implements CMMParserConstants {

    public static void main(String[] args) throws ParseException{
        CMMParser parser = new CMMParser(System.in);
        parser.Start();
    }

//--------------------�?始进行语法分�?--------------------
  static final public void Start() throws ParseException {
    Program();
    jj_consume_token(28);
  }

  static final public void Program() throws ParseException {
    StmtBlock();
  }

  static final public void StmtBlock() throws ParseException {
    jj_consume_token(29);
    label_1:
    while (true) {
      Statement();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IF:
      case WHILE:
      case READ:
      case WRITE:
      case INT:
      case REAL:
      case CHAR:
      case BREAK:
      case CONTINUE:
      case PRINT:
      case SCAN:
      case FOR:
      case IDENTIFIER:
      case 29:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 30:
      jj_consume_token(30);
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    jj_consume_token(31);
  }

  static final public void Statement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IF:
      IfStmt();
      break;
    case WHILE:
      WhileStmt();
      break;
    case IDENTIFIER:
      AssignStmt();
      break;
    case READ:
      ReadStmt();
      break;
    case WRITE:
      WriteStmt();
      break;
    case 29:
      StmtBlock();
      break;
    case INT:
    case REAL:
    case CHAR:
      DeclareStmt();
      break;
    case CONTINUE:
      ContinueStmt();
      break;
    case BREAK:
      BreakStmt();
      break;
    case FOR:
      ForStmt();
      break;
    case PRINT:
      PrintStmt();
      break;
    case SCAN:
      ScanStmt();
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void ScanStmt() throws ParseException {
    jj_consume_token(SCAN);
    jj_consume_token(32);
    Variable();
    jj_consume_token(33);
    jj_consume_token(30);
  }

  static final public void PrintStmt() throws ParseException {
    jj_consume_token(PRINT);
    jj_consume_token(32);
    EXP();
    jj_consume_token(33);
    jj_consume_token(30);
  }

  static final public void ForStmt() throws ParseException {
    jj_consume_token(FOR);
    jj_consume_token(32);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
    case REAL:
    case CHAR:
    case IDENTIFIER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
        AssignStmt();
        break;
      case INT:
      case REAL:
      case CHAR:
        DeclareStmt();
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 30:
      jj_consume_token(30);
      break;
    default:
      jj_la1[5] = jj_gen;
      ;
    }
    EXP();
    jj_consume_token(30);
    AssignStmt();
    jj_consume_token(33);
    StmtBlock();
  }

  static final public void ContinueStmt() throws ParseException {
    jj_consume_token(CONTINUE);
    jj_consume_token(30);
  }

  static final public void BreakStmt() throws ParseException {
    jj_consume_token(BREAK);
    jj_consume_token(30);
  }

  static final public void WhileStmt() throws ParseException {
    jj_consume_token(WHILE);
    jj_consume_token(32);
    EXP();
    jj_consume_token(33);
    StmtBlock();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 30:
      jj_consume_token(30);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
  }

  static final public void ReadStmt() throws ParseException {
    jj_consume_token(READ);
    Variable();
    jj_consume_token(30);
  }

  static final public void WriteStmt() throws ParseException {
    jj_consume_token(WRITE);
    EXP();
    jj_consume_token(30);
  }

  static final public void IfStmt() throws ParseException {
    jj_consume_token(IF);
    jj_consume_token(32);
    EXP();
    jj_consume_token(33);
    StmtBlock();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ELSEIF:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_2;
      }
      jj_consume_token(ELSEIF);
      jj_consume_token(32);
      EXP();
      jj_consume_token(33);
      StmtBlock();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ELSE:
      jj_consume_token(ELSE);
      jj_consume_token(32);
      EXP();
      jj_consume_token(33);
      StmtBlock();
      break;
    default:
      jj_la1[8] = jj_gen;
      ;
    }
  }

  static final public void DeclareStmt() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      jj_consume_token(INT);
      break;
    case REAL:
      jj_consume_token(REAL);
      break;
    case CHAR:
      jj_consume_token(CHAR);
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    jj_consume_token(IDENTIFIER);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 34:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_3;
      }
      jj_consume_token(34);
      EXP();
      jj_consume_token(35);
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 36:
      jj_consume_token(36);
      ValueStmt();
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 37:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_4;
      }
      jj_consume_token(37);
      jj_consume_token(IDENTIFIER);
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 34:
          ;
          break;
        default:
          jj_la1[13] = jj_gen;
          break label_5;
        }
        jj_consume_token(34);
        EXP();
        jj_consume_token(35);
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 36:
        jj_consume_token(36);
        ValueStmt();
        break;
      default:
        jj_la1[14] = jj_gen;
        ;
      }
    }
  }

  static final public void AssignStmt() throws ParseException {
    Variable();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 36:
      jj_consume_token(36);
      ValueStmt();
      break;
    case 38:
      jj_consume_token(38);
      jj_consume_token(38);
      break;
    case 39:
      jj_consume_token(39);
      jj_consume_token(39);
      break;
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 30:
      jj_consume_token(30);
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
  }

  static final public void ValueStmt() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      Variable();
      break;
    case INTEGER_LITERAL:
    case REAL_LITERAL:
    case CHAR_LITERAL:
    case 32:
    case 42:
      EXP();
      break;
    case 29:
      jj_consume_token(29);
      EXP();
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 37:
          ;
          break;
        default:
          jj_la1[17] = jj_gen;
          break label_6;
        }
        jj_consume_token(37);
        EXP();
      }
      jj_consume_token(31);
      break;
    case CHARS:
      jj_consume_token(CHARS);
      break;
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Variable() throws ParseException {
    jj_consume_token(IDENTIFIER);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 34:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_7;
      }
      jj_consume_token(34);
      EXP();
      jj_consume_token(35);
    }
  }

  static final public void EXP() throws ParseException {
    OrEXP();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 40:
      jj_consume_token(40);
      EXP();
      break;
    default:
      jj_la1[20] = jj_gen;
      ;
    }
  }

  static final public void OrEXP() throws ParseException {
    AndEXP();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 41:
      jj_consume_token(41);
      OrEXP();
      break;
    default:
      jj_la1[21] = jj_gen;
      ;
    }
  }

  static final public void AndEXP() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 42:
      jj_consume_token(42);
      break;
    default:
      jj_la1[22] = jj_gen;
      ;
    }
    NotEXP();
  }

  static final public void NotEXP() throws ParseException {
    AddEXP();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
        ;
        break;
      default:
        jj_la1[23] = jj_gen;
        break label_8;
      }
      LogOp();
      AddEXP();
    }
  }

  static final public void AddEXP() throws ParseException {
    Term();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 38:
      case 39:
        ;
        break;
      default:
        jj_la1[24] = jj_gen;
        break label_9;
      }
      AddOp();
      Term();
    }
  }

  static final public void Term() throws ParseException {
    Factor();
    label_10:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 49:
      case 50:
        ;
        break;
      default:
        jj_la1[25] = jj_gen;
        break label_10;
      }
      MulOp();
      Factor();
    }
  }

  static final public void Factor() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 32:
      jj_consume_token(32);
      EXP();
      jj_consume_token(33);
      break;
    case IDENTIFIER:
      Variable();
      break;
    case INTEGER_LITERAL:
      jj_consume_token(INTEGER_LITERAL);
      break;
    case REAL_LITERAL:
      jj_consume_token(REAL_LITERAL);
      break;
    case CHAR_LITERAL:
      jj_consume_token(CHAR_LITERAL);
      break;
    default:
      jj_la1[26] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void LogOp() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 43:
      jj_consume_token(43);
      break;
    case 44:
      jj_consume_token(44);
      break;
    case 45:
      jj_consume_token(45);
      break;
    case 46:
      jj_consume_token(46);
      break;
    case 47:
      jj_consume_token(47);
      break;
    case 48:
      jj_consume_token(48);
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void AddOp() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 38:
      jj_consume_token(38);
      break;
    case 39:
      jj_consume_token(39);
      break;
    default:
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void MulOp() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 49:
      jj_consume_token(49);
      break;
    case 50:
      jj_consume_token(50);
      break;
    default:
      jj_la1[29] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public CMMParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[30];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x220ffe80,0x40000000,0x220ffe80,0x2007000,0x2007000,0x40000000,0x40000000,0x100000,0x100,0x7000,0x0,0x0,0x0,0x0,0x0,0x0,0x40000000,0x0,0x23e00000,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2e00000,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x4,0x10,0x20,0x4,0x10,0xd0,0x0,0x20,0x401,0x4,0x100,0x200,0x400,0x1f800,0xc0,0x60000,0x1,0x1f800,0xc0,0x60000,};
   }

  /** Constructor with InputStream. */
  public CMMParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public CMMParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new CMMParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public CMMParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new CMMParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public CMMParser(CMMParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CMMParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 30; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[51];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 30; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 51; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}