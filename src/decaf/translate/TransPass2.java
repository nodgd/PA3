package decaf.translate;

import java.util.Stack;

import decaf.tree.Tree;
import decaf.backend.OffsetCounter;
import decaf.machdesc.Intrinsic;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.tac.Label;
import decaf.tac.Temp;
import decaf.type.ArrayType;
import decaf.type.BaseType;
import decaf.type.ClassType;
import decaf.type.Type;

public class TransPass2 extends Tree.Visitor {

	private Translater tr;

	private Temp currentThis;

	private Stack<Label> loopExits;

	public TransPass2(Translater tr) {
		this.tr = tr;
		loopExits = new Stack<Label>();
	}

	@Override
	public void visitClassDef(Tree.ClassDef classDef) {
		for (Tree f : classDef.fields) {
			f.accept(this);
		}
	}

	@Override
	public void visitMethodDef(Tree.MethodDef funcDefn) {
		if (!funcDefn.statik) {
			currentThis = ((Variable) funcDefn.symbol.getAssociatedScope()
					.lookup("this")).getTemp();
		}
		tr.beginFunc(funcDefn.symbol);
		funcDefn.body.accept(this);
		tr.endFunc();
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		for (Tree.ClassDef cd : program.classes) {
			cd.accept(this);
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		if (varDef.symbol.isLocalVar()) {
			Temp t = Temp.createTempI4();
			t.sym = varDef.symbol;
			varDef.symbol.setTemp(t);
			if (varDef.type.type.equal(BaseType.COMPLEX)) {
				Temp tj = Temp.createTempI4();
				tj.sym = varDef.symbol;
				varDef.symbol.setTemj(tj);
			}
		}
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.left.accept(this);
		expr.right.accept(this);
		switch (expr.tag) {
		case Tree.PLUS:
			if (expr.type.equal(BaseType.INT)) {
				//integer result
				expr.val = tr.genAdd(expr.left.val, expr.right.val);
				expr.vaj = null;
				break;
			} else {
				//complex result
				//re + re
				if (expr.left.val == null) {
					if (expr.right.val == null) {
						expr.val = tr.genLoadImm4(0);
					} else {
						expr.val = Temp.createTempI4();
						tr.genAssign(expr.val, expr.right.val);
					}
				} else {
					if (expr.right.val == null) {
						expr.val = Temp.createTempI4();
						tr.genAssign(expr.val, expr.left.val);
					} else {
						expr.val = tr.genAdd(expr.left.val, expr.right.val);						
					}
				}
				//im + im
				if (expr.left.vaj == null) {
					if (expr.right.vaj == null) {
						expr.vaj = tr.genLoadImm4(0);
					} else {
						expr.vaj = Temp.createTempI4();
						tr.genAssign(expr.vaj, expr.right.vaj);
					}
				} else {
					if (expr.right.vaj == null) {
						expr.vaj = Temp.createTempI4();
						tr.genAssign(expr.vaj, expr.left.vaj);
					} else {
						expr.vaj = tr.genAdd(expr.left.vaj, expr.right.vaj);						
					}
				}
				break;
			}
		case Tree.MINUS:
			expr.val = tr.genSub(expr.left.val, expr.right.val);
			break;
		case Tree.MUL:
			if (expr.type.equal(BaseType.INT)) {
				//integer result
				expr.val = tr.genMul(expr.left.val, expr.right.val);
				break;
			} else {
				//complex result
				//mrr = left.re * right.re
				Temp mrr = null;
				if (expr.left.val == null || expr.right.val == null) {
					mrr = tr.genLoadImm4(0);
				} else {
					mrr = tr.genMul(expr.left.val, expr.right.val);
				}
				//mrj = left.re * right.im
				Temp mrj = null;
				if (expr.left.val == null || expr.right.vaj == null) {
					mrj = tr.genLoadImm4(0);
				} else {
					mrj = tr.genMul(expr.left.val, expr.right.vaj);
				}
				//mjr = left.im * right.re
				Temp mjr = null;
				if (expr.left.vaj == null || expr.right.val == null) {
					mjr = tr.genLoadImm4(0);
				} else {
					mjr = tr.genMul(expr.left.vaj, expr.right.val);
				}
				//mjj = left.im * right.im
				Temp mjj = null;
				if (expr.left.vaj == null || expr.right.vaj == null) {
					mjj = tr.genLoadImm4(0);
				} else {
					mjj = tr.genMul(expr.left.vaj, expr.right.vaj);
				}
				//re = mrr - mjj
				expr.val = tr.genSub(mrr, mjj);
				//im = mrj + mjr
				expr.vaj = tr.genAdd(mrj, mjr);
				break;
			}
		case Tree.DIV:
			expr.val = tr.genDiv(expr.left.val, expr.right.val);
			break;
		case Tree.MOD:
			expr.val = tr.genMod(expr.left.val, expr.right.val);
			break;
		case Tree.AND:
			expr.val = tr.genLAnd(expr.left.val, expr.right.val);
			break;
		case Tree.OR:
			expr.val = tr.genLOr(expr.left.val, expr.right.val);
			break;
		case Tree.LT:
			expr.val = tr.genLes(expr.left.val, expr.right.val);
			break;
		case Tree.LE:
			expr.val = tr.genLeq(expr.left.val, expr.right.val);
			break;
		case Tree.GT:
			expr.val = tr.genGtr(expr.left.val, expr.right.val);
			break;
		case Tree.GE:
			expr.val = tr.genGeq(expr.left.val, expr.right.val);
			break;
		case Tree.EQ:
		case Tree.NE:
			genEquNeq(expr);
			break;
		}
	}

	private void genEquNeq(Tree.Binary expr) {
		if (expr.left.type.equal(BaseType.STRING)
				|| expr.right.type.equal(BaseType.STRING)) {
			tr.genParm(expr.left.val);
			tr.genParm(expr.right.val);
			expr.val = tr.genDirectCall(Intrinsic.STRING_EQUAL.label,
					BaseType.BOOL);
			if(expr.tag == Tree.NE){
				expr.val = tr.genLNot(expr.val);
			}
		} else {
			if(expr.tag == Tree.EQ)
				expr.val = tr.genEqu(expr.left.val, expr.right.val);
			else
				expr.val = tr.genNeq(expr.left.val, expr.right.val);
		}
	}

	@Override
	public void visitAssign(Tree.Assign assign) {
		assign.left.accept(this);
		assign.expr.accept(this);
		switch (assign.left.lvKind) {
		case ARRAY_ELEMENT:
			if (assign.left.type.equal(BaseType.COMPLEX)) {
				//complex array
				Tree.Indexed arrayRef = (Tree.Indexed) assign.left;
				Temp esz = tr.genLoadImm4(OffsetCounter.DOUBLE_SIZE);
				Temp t = tr.genMul(arrayRef.index.val, esz);
				Temp base = tr.genAdd(arrayRef.array.val, t);
				if (assign.expr.val != null) {
					tr.genStore(assign.expr.val, base, 0);
				} else {
					Temp zero = tr.genLoadImm4(0);
					tr.genStore(zero, base, 0);
				}
				if (assign.expr.vaj != null) {
					tr.genStore(assign.expr.vaj, base, 4);
				} else {
					Temp zero = tr.genLoadImm4(0);
					tr.genStore(zero, base, 4);
				}
			} else {
				//other type array
				Tree.Indexed arrayRef = (Tree.Indexed) assign.left;
				Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
				Temp t = tr.genMul(arrayRef.index.val, esz);
				Temp base = tr.genAdd(arrayRef.array.val, t);
				tr.genStore(assign.expr.val, base, 0);
			}
			break;
		case MEMBER_VAR:
			Tree.Ident varRef = (Tree.Ident) assign.left;
			tr.genStore(assign.expr.val, varRef.owner.val, varRef.symbol.getOffset());
			if (assign.left.type.equal(BaseType.COMPLEX)) {
				tr.genStore(assign.expr.vaj, varRef.owner.val, varRef.symbol.getOffset() + 4);
			}
			break;
		case PARAM_VAR:
		case LOCAL_VAR:
			if (assign.left.type.equal(BaseType.COMPLEX)) {
				//complex
				tr.genAssign(((Tree.Ident) assign.left).symbol.getTemp(), assign.expr.val);
				tr.genAssign(((Tree.Ident) assign.left).symbol.getTemj(), assign.expr.vaj);
			} else {
				//other type
				tr.genAssign(((Tree.Ident) assign.left).symbol.getTemp(), assign.expr.val);
			}
			break;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.val = tr.genLoadImm4(((Integer)literal.value).intValue());
			literal.vaj = null;
			break;
		case Tree.BOOL:
			literal.val = tr.genLoadImm4((Boolean)(literal.value) ? 1 : 0);
			break;
		case Tree.IMG:
			literal.val = tr.genLoadImm4(0);
			literal.vaj = tr.genLoadImm4(((Integer)literal.value).intValue());
			break;
		default:
			literal.val = tr.genLoadStrConst((String)literal.value);
		}
	}

	@Override
	public void visitExec(Tree.Exec exec) {
		exec.expr.accept(this);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		switch (expr.tag){
		case Tree.NEG:
			expr.val = tr.genNeg(expr.expr.val);
			break;
		case Tree.NOT:
			expr.val = tr.genLNot(expr.expr.val);
			break;
		case Tree.RE:
			if (expr.expr.val == null) {
				expr.val = tr.genLoadImm4(0);
			} else {
				expr.val = Temp.createTempI4();
				tr.genAssign(expr.val, expr.expr.val);
			}
			break;
		case Tree.IM:
			if (expr.expr.vaj == null) {
				expr.val = tr.genLoadImm4(0);
			} else {
				expr.val = Temp.createTempI4();
				tr.genAssign(expr.val, expr.expr.vaj);
			}
			break;
		case Tree.COMPCAST:
			if (expr.expr.val == null) {
				expr.val = tr.genLoadImm4(0);
			} else {
				expr.val = Temp.createTempI4();
				tr.genAssign(expr.val, expr.expr.val);
			}
			if (expr.expr.vaj == null) {
				expr.vaj = tr.genLoadImm4(0);
			} else {
				expr.vaj = Temp.createTempI4();
				tr.genAssign(expr.vaj, expr.expr.vaj);
			}
			break;
		default:
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.val = tr.genLoadImm4(0);
	}

	@Override
	public void visitBlock(Tree.Block block) {
		for (Tree s : block.block) {
			s.accept(this);
		}
	}

	@Override
	public void visitThisExpr(Tree.ThisExpr thisExpr) {
		thisExpr.val = currentThis;
	}

	@Override
	public void visitSuperExpr(Tree.SuperExpr superExpr) {
		superExpr.val = currentThis;
	}

	@Override
	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
		readIntExpr.val = tr.genIntrinsicCall(Intrinsic.READ_INT);
	}

	@Override
	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
		readStringExpr.val = tr.genIntrinsicCall(Intrinsic.READ_LINE);
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
			tr.genReturn(returnStmt.expr.val);
		} else {
			tr.genReturn(null);
		}

	}

	@Override
	public void visitPrint(Tree.Print printStmt) {
		for (Tree.Expr r : printStmt.exprs) {
			r.accept(this);
			tr.genParm(r.val);
			if (r.type.equal(BaseType.BOOL)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_BOOL);
			} else if (r.type.equal(BaseType.INT)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			} else if (r.type.equal(BaseType.STRING)) {
				tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			}
		}
	}

	@Override
	public void visitPrintComp(Tree.PrintComp printCompStmt) {
		for (Tree.Expr r : printCompStmt.exprs) {
			r.accept(this);
			tr.genParm(r.val);
			tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			Temp opAdd = tr.genLoadStrConst("+");
			tr.genParm(opAdd);
			tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			tr.genParm(r.vaj);
			tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			Temp opIm = tr.genLoadStrConst("j");
			tr.genParm(opIm);
			tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
		}
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.array.accept(this);
		indexed.index.accept(this);
		if (indexed.array.type.isArrayType() && 
			((ArrayType) indexed.array.type).getElementType().equal(BaseType.COMPLEX)) {
			//complex array
			Temp compIndex = tr.genAdd(indexed.index.val, indexed.index.val);
			tr.genCheckArrayIndex(indexed.array.val, compIndex);
			
			Temp esz = tr.genLoadImm4(OffsetCounter.DOUBLE_SIZE);
			Temp t = tr.genMul(indexed.index.val, esz);
			Temp base = tr.genAdd(indexed.array.val, t);
			indexed.val = tr.genLoad(base, 0);		
			indexed.vaj = tr.genLoad(base, 4);
		} else {
			//other type array
			tr.genCheckArrayIndex(indexed.array.val, indexed.index.val);	
			
			Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
			Temp t = tr.genMul(indexed.index.val, esz);
			Temp base = tr.genAdd(indexed.array.val, t);
			indexed.val = tr.genLoad(base, 0);		
		}
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		if(ident.lvKind == Tree.LValue.Kind.MEMBER_VAR){
			ident.owner.accept(this);
		}
		
		switch (ident.lvKind) {
		case MEMBER_VAR:
			ident.val = tr.genLoad(ident.owner.val, ident.symbol.getOffset());
			if (ident.type.equal(BaseType.COMPLEX)) {
				ident.vaj = tr.genLoad(ident.owner.val, ident.symbol.getOffset() + 4);
			} else {
				ident.vaj = null;
			}
			break;
		default:
			ident.val = ident.symbol.getTemp();
			if (ident.type.equal(BaseType.COMPLEX)) {
				ident.vaj = ident.symbol.getTemj();
			} else {
				ident.vaj = null;
			}
			break;
		}
	}
	
	@Override
	public void visitBreak(Tree.Break breakStmt) {
		tr.genBranch(loopExits.peek());
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		if (callExpr.isArrayLength) {
			callExpr.receiver.accept(this);
			callExpr.val = tr.genLoad(callExpr.receiver.val,
					-OffsetCounter.WORD_SIZE);
		} else {
			if (callExpr.receiver != null) {
				callExpr.receiver.accept(this);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				expr.accept(this);
			}
			if (callExpr.receiver != null) {
				tr.genParm(callExpr.receiver.val);
			}
			for (Tree.Expr expr : callExpr.actuals) {
				tr.genParm(expr.val);
				if (expr.type.equal(BaseType.COMPLEX)) {
					tr.genParm(expr.vaj);
				}
			}
			if (callExpr.receiver == null) {
				callExpr.val = tr.genDirectCall(
						callExpr.symbol.getFuncty().label, callExpr.symbol
								.getReturnType());
			} else {
				/*
				 * 这个地方，项目中原本给出的代码是有错误的，例如下面这段decaf代码
				 * class A {
				 *     void func1() {
				 *         Print("A.func1\n");
				 *     }
				 *     void func2() {
				 *         Print("A.func2\n");
				 *         func1();
				 *     }
				 * }
				 * class B {
				 *     void func1() {
				 *         Print("B.func1\n");
				 *     }
				 * }
				 * class Main {
				 *     static void main() {
				 *         class B b;
				 *         b = new B();
				 *         b.func2();
				 *     }
				 * }
				 * 在调用b.func2时，实际会调用A类的func2，在它里面又调用func1，原则上应该调用A类的func1
				 * 测试用例中q3-super-test4中的class E和class F出现了这种情况
				 * 但在原本给出的代码中，运行func2时仍然会调用B类的func1，就错啦！
				 */
				Temp originalVTable = tr.genLoad(callExpr.receiver.val, 0);
				Temp receiverVTable = originalVTable;
				ClassType receiverType = (ClassType) callExpr.receiver.type;
				if (callExpr.receiver.tag == Tree.SUPEREXPR) {
					receiverVTable = tr.genLoad(receiverVTable, 0);
					receiverType = receiverType.getParentType();
				}
				while (true) {
					Symbol symbol = receiverType.getClassScope().lookup(callExpr.method);
					if (symbol != null) {
						break;
					}
					receiverVTable = tr.genLoad(receiverVTable, 0);
					receiverType = receiverType.getParentType();
				}
				tr.genStore(receiverVTable, callExpr.receiver.val, 0);
				Temp func = tr.genLoad(receiverVTable, callExpr.symbol.getOffset());
				callExpr.val = tr.genIndirectCall(func, callExpr.symbol.getReturnType());
				tr.genStore(originalVTable, callExpr.receiver.val, 0);
			}
		}

	}
	
	@Override
	public void visitCondExpr(Tree.CondExpr condExpr) {
		condExpr.switchExpr.accept(this);
		condExpr.val = Temp.createTempI4();
		Label condEnd = Label.createLabel();
		for (Tree.Expr expr: condExpr.caseList) {
			Tree.CaseExpr caseExpr = (Tree.CaseExpr) expr;
			if (caseExpr.constant != null) {
				caseExpr.constant.accept(this);
				Label caseEnd = Label.createLabel();
				Temp cmpResult = tr.genEqu(condExpr.switchExpr.val, caseExpr.constant.val);
				tr.genBeqz(cmpResult, caseEnd);
				caseExpr.expression.accept(this);
				tr.genAssign(condExpr.val, caseExpr.expression.val);
				tr.genBranch(condEnd);
				tr.genMark(caseEnd);
			} else {
				caseExpr.expression.accept(this);
				tr.genAssign(condExpr.val, caseExpr.expression.val);
			}
		}
		tr.genMark(condEnd);
	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		Label cond = Label.createLabel();
		Label loop = Label.createLabel();
		tr.genBranch(cond);
		tr.genMark(loop);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		tr.genMark(cond);
		forLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(forLoop.condition.val, exit);
		loopExits.push(exit);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}
	
	@Override
	public void visitDoStmt(Tree.DoStmt doStmt) {
		Label doStmtBegin = Label.createLabel();
		Label doStmtEnd = Label.createLabel();
		tr.genMark(doStmtBegin);
		loopExits.push(doStmtEnd);
		for (Tree.Expr expr: doStmt.branchList) {
			Tree.DoBranch doBranch = (Tree.DoBranch) expr;
			Label nextDoBranch = Label.createLabel();
			doBranch.les.accept(this);
			tr.genBeqz(doBranch.les.val, nextDoBranch);
			doBranch.assign.accept(this);
			tr.genBranch(doStmtBegin);
			tr.genMark(nextDoBranch);
		}
		loopExits.pop();
		tr.genMark(doStmtEnd);
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		ifStmt.condition.accept(this);
		if (ifStmt.falseBranch != null) {
			Label falseLabel = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, falseLabel);
			ifStmt.trueBranch.accept(this);
			Label exit = Label.createLabel();
			tr.genBranch(exit);
			tr.genMark(falseLabel);
			ifStmt.falseBranch.accept(this);
			tr.genMark(exit);
		} else if (ifStmt.trueBranch != null) {
			Label exit = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, exit);
			if (ifStmt.trueBranch != null) {
				ifStmt.trueBranch.accept(this);
			}
			tr.genMark(exit);
		}
	}

	@Override
	public void visitNewArray(Tree.NewArray newArray) {
		newArray.length.accept(this);
		if (newArray.elementType.type.equal(BaseType.COMPLEX)) {
			//complex
			Temp compLen = tr.genAdd(newArray.length.val, newArray.length.val);
			newArray.val = tr.genNewArray(compLen);
		} else {
			//other type
			newArray.val = tr.genNewArray(newArray.length.val);
		}
	}

	@Override
	public void visitNewClass(Tree.NewClass newClass) {
		newClass.val = tr.genDirectCall(newClass.symbol.getNewFuncLabel(),
				BaseType.INT);
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		Label loop = Label.createLabel();
		tr.genMark(loop);
		whileLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(whileLoop.condition.val, exit);
		loopExits.push(exit);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitTypeTest(Tree.TypeTest typeTest) {
		typeTest.instance.accept(this);
		typeTest.val = tr.genInstanceof(typeTest.instance.val,
				typeTest.symbol);
	}

	@Override
	public void visitTypeCast(Tree.TypeCast typeCast) {
		typeCast.expr.accept(this);
		if (!typeCast.expr.type.compatible(typeCast.symbol.getType())) {
			tr.genClassCast(typeCast.expr.val, typeCast.symbol);
		}
		typeCast.val = typeCast.expr.val;
	}
}
