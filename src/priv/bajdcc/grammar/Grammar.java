package priv.bajdcc.grammar;

import priv.bajdcc.grammar.error.LostHandler;
import priv.bajdcc.grammar.semantic.infer.SemanticAction;
import priv.bajdcc.lexer.error.RegexException;
import priv.bajdcc.lexer.token.KeywordType;
import priv.bajdcc.lexer.token.OperatorType;
import priv.bajdcc.lexer.token.TokenType;
import priv.bajdcc.semantic.Semantic;
import priv.bajdcc.syntax.error.SyntaxException;

/**
 * <strong>语法分析器</strong>
 * <p>
 * 基于文法与语义动作
 * </p>
 *
 * @author bajdcc
 */
public class Grammar extends Semantic {

	/**
	 * 语义动作
	 */
	private SemanticAction action = new SemanticAction();

	public Grammar(String context) throws RegexException, SyntaxException {
		super(context);
		initialize();
	}

	/**
	 * 初始化
	 * 
	 * @throws SyntaxException
	 */
	private void initialize() throws SyntaxException {
		declareTerminal();
		declareNonTerminal();
		declareErrorHandler();
		infer();
	}

	/**
	 * 声明终结符
	 */
	private void declareTerminal() {
		addTerminal("IDENTIFIER", TokenType.ID, null);
		addTerminal("STRING_LITERAL", TokenType.STRING, null);
		addTerminal("CHARACTER", TokenType.CHARACTER, null);
		addTerminal("INTEGER", TokenType.INTEGER, null);
		addTerminal("REAL", TokenType.REAL, null);
		for (KeywordType keywordType : KeywordType.values()) {
			addTerminal(keywordType.name(), TokenType.KEYWORD, keywordType);
		}
		addTerminal("ELLIPSIS", TokenType.OPERATOR, OperatorType.ELLIPSIS);
		addTerminal("PTR_OP", TokenType.OPERATOR, OperatorType.POINTER);
		addTerminal("INC_OP", TokenType.OPERATOR, OperatorType.PLUS_PLUS);
		addTerminal("DEC_OP", TokenType.OPERATOR, OperatorType.MINUS_MINUS);
		addTerminal("LEFT_OP", TokenType.OPERATOR, OperatorType.LEFT_SHIFT);
		addTerminal("RIGHT_OP", TokenType.OPERATOR, OperatorType.RIGHT_SHIFT);
		addTerminal("LE_OP", TokenType.OPERATOR,
				OperatorType.LESS_THAN_OR_EQUAL);
		addTerminal("GE_OP", TokenType.OPERATOR,
				OperatorType.GREATER_THAN_OR_EQUAL);
		addTerminal("EQ_OP", TokenType.OPERATOR, OperatorType.EQUAL);
		addTerminal("NE_OP", TokenType.OPERATOR, OperatorType.NOT_EQUAL);
		addTerminal("AND_OP", TokenType.OPERATOR, OperatorType.LOGICAL_AND);
		addTerminal("OR_OP", TokenType.OPERATOR, OperatorType.LOGICAL_OR);
		addTerminal("MUL_ASSIGN", TokenType.OPERATOR, OperatorType.TIMES_ASSIGN);
		addTerminal("DIV_ASSIGN", TokenType.OPERATOR, OperatorType.DIV_ASSIGN);
		addTerminal("MOD_ASSIGN", TokenType.OPERATOR, OperatorType.MOD_ASSIGN);
		addTerminal("ADD_ASSIGN", TokenType.OPERATOR, OperatorType.PLUS_ASSIGN);
		addTerminal("SUB_ASSIGN", TokenType.OPERATOR, OperatorType.MINUS_ASSIGN);
		addTerminal("LEFT_ASSIGN", TokenType.OPERATOR,
				OperatorType.LEFT_SHIFT_ASSIGN);
		addTerminal("RIGHT_ASSIGN", TokenType.OPERATOR,
				OperatorType.RIGHT_SHIFT_ASSIGN);
		addTerminal("AND_ASSIGN", TokenType.OPERATOR, OperatorType.AND_ASSIGN);
		addTerminal("XOR_ASSIGN", TokenType.OPERATOR, OperatorType.XOR_ASSIGN);
		addTerminal("OR_ASSIGN", TokenType.OPERATOR, OperatorType.OR_ASSIGN);
		addTerminal("ADD", TokenType.OPERATOR, OperatorType.PLUS);
		addTerminal("SUB", TokenType.OPERATOR, OperatorType.MINUS);
		addTerminal("MUL", TokenType.OPERATOR, OperatorType.TIMES);
		addTerminal("DIV", TokenType.OPERATOR, OperatorType.DIVIDE);
		addTerminal("MOD", TokenType.OPERATOR, OperatorType.MOD);
		addTerminal("AND", TokenType.OPERATOR, OperatorType.BIT_AND);
		addTerminal("OR", TokenType.OPERATOR, OperatorType.BIT_OR);
		addTerminal("XOR", TokenType.OPERATOR, OperatorType.BIT_XOR);
		addTerminal("NOT", TokenType.OPERATOR, OperatorType.BIT_NOT);
		addTerminal("NOT_OP", TokenType.OPERATOR, OperatorType.LOGICAL_NOT);
		addTerminal("LT", TokenType.OPERATOR, OperatorType.LESS_THAN);
		addTerminal("GT", TokenType.OPERATOR, OperatorType.GREATER_THAN);
		addTerminal("QUERY", TokenType.OPERATOR, OperatorType.QUERY);
		addTerminal("COMMA", TokenType.OPERATOR, OperatorType.COMMA);
		addTerminal("SEMI", TokenType.OPERATOR, OperatorType.SEMI);
		addTerminal("DOT", TokenType.OPERATOR, OperatorType.DOT);
		addTerminal("ASSIGN", TokenType.OPERATOR, OperatorType.ASSIGN);
		addTerminal("COLON", TokenType.OPERATOR, OperatorType.COLON);
		addTerminal("LPA", TokenType.OPERATOR, OperatorType.LPARAN);
		addTerminal("RPA", TokenType.OPERATOR, OperatorType.RPARAN);
		addTerminal("LSQ", TokenType.OPERATOR, OperatorType.LSQUARE);
		addTerminal("RSQ", TokenType.OPERATOR, OperatorType.RSQUARE);
		addTerminal("LBR", TokenType.OPERATOR, OperatorType.LBRACE);
		addTerminal("RBR", TokenType.OPERATOR, OperatorType.RBRACE);
	}

	/**
	 * 声明非终结符
	 */
	private void declareNonTerminal() {
		String[] nonTerminals = new String[] { "program", "declaration_list",
				"external_declaration", "function_definition", "declaration",
				"type_specifier", "declarator", "compound_statement",
				"pointer direct_declarator", "direct_declarator", "pointer",
				"constant_expression", "parameter_list", "identifier_list",
				"conditional_expression", "parameter_declaration",
				"declaration_specifiers", "statement_list", "init_declarator",
				"init_declarator_list", "initializer", "assignment_expression",
				"initializer_list", "statement", "expression_statement",
				"selection_statement", "iteration_statement", "jump_statement",
				"expression", "unary_expression", "assignment_operator",
				"logical_or_expression", "logical_and_expression",
				"inclusive_or_expression", "exclusive_or_expression",
				"and_expression", "equality_expression",
				"relational_expression", "shift_expression",
				"multiplicative_expression", "cast_expression",
				"postfix_expression", "unary_operator", "type_name",
				"primary_expression", "argument_expression_list",
				"struct_or_union_specifier", "struct_or_union",
				"struct_declarator", "struct_declaration",
				"struct_declaration_list", "struct_declarator_list",
				"specifier_qualifier_list", "type_qualifier", "enum_specifier",
				"enumerator_list", "enumerator", "type_qualifier_list",
				"parameter_type_list", "abstract_declarator",
				"direct_abstract_declarator", "labeled_statement",
				"additive_expression", "storage_class_specifier",
				"for_statement", "while_statement", "if_statement",
				"switch_statement" };
		for (String string : nonTerminals) {
			addNonTerminal(string);
		}
	}

	/**
	 * 声明错误处理器
	 */
	private void declareErrorHandler() {
		addErrorHandler("lost_exp", new LostHandler("表达式"));
		addErrorHandler("lost_lpa", new LostHandler("左圆括号'('"));
		addErrorHandler("lost_rpa", new LostHandler("右圆括号')'"));
		addErrorHandler("lost_lsq", new LostHandler("左方括号'['"));
		addErrorHandler("lost_rsq", new LostHandler("右方括号']'"));
		addErrorHandler("lost_lbr", new LostHandler("左花括号'{'"));
		addErrorHandler("lost_rbr", new LostHandler("右花括号'}'"));
		addErrorHandler("lost_semi", new LostHandler("分号';'"));
	}

	/**
	 * 进行推导
	 * 
	 * @throws SyntaxException
	 */
	private void infer() throws SyntaxException {
		infer("abstract_declarator           -> direct_abstract_declarator | pointer [direct_abstract_declarator]");
		infer("additive_expression           -> [additive_expression (@ADD | @SUB)] multiplicative_expression");
		infer("and_expression                -> [and_expression @AND] equality_expression");
		infer("argument_expression_list      -> [argument_expression_list @COMMA] (assignment_expression | declaration_specifiers)");
		infer("assignment_expression         -> conditional_expression | unary_expression assignment_operator assignment_expression");
		infer("assignment_operator           -> @ASSIGN | @MUL_ASSIGN | @DIV_ASSIGN | @MOD_ASSIGN | @ADD_ASSIGN | @SUB_ASSIGN | @LEFT_ASSIGN | @RIGHT_ASSIGN | @AND_ASSIGN | @XOR_ASSIGN | @OR_ASSIGN");
		infer("cast_expression               -> unary_expression");
		infer("conditional_expression        -> logical_or_expression [@QUERY expression @COLON conditional_expression]");
		infer("compound_statement            -> @LBR [[declaration_list] [statement_list]] @RBR");
		infer("constant_expression           -> conditional_expression");
		infer("declaration                   -> declaration_specifiers [init_declarator_list] @SEMI");
		infer("declaration_list              -> [declaration_list] declaration");
		infer("declaration_specifiers        -> [type_qualifier] [storage_class_specifier] type_specifier");
		infer("declarator                    -> [pointer] direct_declarator");
		infer("direct_abstract_declarator    -> @LPA [abstract_declarator] @RPA | [direct_abstract_declarator] @LSQ [constant_expression] @RSQ | @LPA parameter_type_list @RPA | direct_abstract_declarator @LPA @RPA");
		infer("direct_declarator             -> @IDENTIFIER | @LPA declarator @RPA | direct_declarator (@LSQ [constant_expression] @RSQ | @LPA [(parameter_type_list | identifier_list)] @RPA)");
		infer("equality_expression           -> [equality_expression (@EQ_OP | @NE_OP)] relational_expression");
		infer("exclusive_or_expression       -> [exclusive_or_expression @XOR] and_expression");
		infer("expression                    -> [expression @COMMA] assignment_expression");
		infer("expression_statement          -> [expression] @SEMI");
		infer("external_declaration          -> function_definition | declaration");
		infer("for_statement                 -> @FOR @LPA expression_statement expression_statement [expression] @RPA statement");
		infer("function_definition           -> declaration_specifiers declarator [declaration_list] compound_statement");
		infer("identifier_list               -> [identifier_list @COMMA] @IDENTIFIER");
		infer("if_statement                  -> @IF @LPA expression @RPA statement [@ELSE statement]");
		infer("inclusive_or_expression       -> [inclusive_or_expression @OR] exclusive_or_expression");
		infer("init_declarator               -> declarator [@ASSIGN initializer]");
		infer("init_declarator_list          -> [init_declarator_list @COMMA] init_declarator");
		infer("initializer                   -> assignment_expression | @LBR initializer_list [@COMMA] @RBR");
		infer("initializer_list              -> [initializer_list @COMMA] initializer");
		infer("iteration_statement           -> while_statement | for_statement");
		infer("jump_statement                -> (@GOTO @IDENTIFIER | @CONTINUE | @BREAK | @RETURN [expression]) @SEMI");
		infer("labeled_statement             -> @IDENTIFIER @COLON statement | @CASE constant_expression @COLON statement | @DEFAULT @COLON statement");
		infer("logical_and_expression        -> [logical_and_expression @AND_OP] inclusive_or_expression");
		infer("logical_or_expression         -> [logical_or_expression @OR_OP] logical_and_expression");
		infer("multiplicative_expression     -> [multiplicative_expression (@MUL | @DIV | @MOD)] cast_expression");
		infer("parameter_declaration         -> declaration_specifiers [declarator | abstract_declarator]");
		infer("parameter_list                -> [parameter_list @COMMA] parameter_declaration");
		infer("parameter_type_list           -> parameter_list [@COMMA @ELLIPSIS]");
		infer("pointer                       -> @MUL [type_qualifier_list [pointer] | pointer]");
		infer(action.get("postfix_expression"),
				"postfix_expression            -> primary_expression[0] | postfix_expression (@LSQ expression @RSQ | @LPA [argument_expression_list] @RPA | @DOT @IDENTIFIER | @PTR_OP @IDENTIFIER | @INC_OP | @DEC_OP)");
		infer(action.get("primary_expression"),
				"primary_expression            -> @IDENTIFIER[0] | @INTEGER[0] | @REAL[0] | @STRING_LITERAL[0] | @CHARACTER[0] | @LPA expression[1]{lost_exp} @RPA{lost_rpa}");
		infer("program                       -> external_declaration [program]");
		infer("relational_expression         -> [relational_expression (@LT | @GT | @LE_OP | @GE_OP)] shift_expression");
		infer("selection_statement           -> if_statement | switch_statement");
		infer("shift_expression              -> [shift_expression (@LEFT_OP | @RIGHT_OP)] additive_expression");
		infer("specifier_qualifier_list      -> (type_specifier | type_qualifier) [specifier_qualifier_list]");
		infer("statement                     -> labeled_statement | compound_statement | expression_statement | selection_statement | iteration_statement | jump_statement");
		infer("statement_list                -> [statement_list] statement");
		infer("storage_class_specifier       -> @TYPEDEF | @EXTERN | @STATIC | @AUTO | @REGISTER");
		infer("switch_statement              -> @SWITCH @LPA expression @RPA statement");
		infer("type_name                     -> specifier_qualifier_list [abstract_declarator]");
		infer("type_qualifier                -> @CONST | @VOLATILE");
		infer("type_qualifier_list           -> [type_qualifier_list] type_qualifier");
		infer("type_specifier                -> @VOID[1] | ([@SIGNED[0] | @UNSIGNED[0]] (@CHAR[1] | @SHORT[1] | @INT[1] | @LONG[1] | @FLOAT[1] | @DOUBLE[1] | @BOOL[1]))");
		infer("unary_expression              -> postfix_expression | (@INC_OP | @DEC_OP | @SIZEOF) unary_expression | unary_operator cast_expression | @SIZEOF @LPA type_name @RPA");
		infer("unary_operator                -> @AND | @MUL | @ADD | @SUB | @NOT | @NOT_OP");
		infer("while_statement               -> @WHILE @LPA expression @RPA statement | @DO statement @WHILE @LPA expression @RPA @SEMI");
		initialize("program");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNGAString());
		sb.append(getNPAString());
		sb.append(getInst());
		sb.append(getError());
		sb.append(getTokenList());
		sb.append(getObject());
		return sb.toString();
	}
}
