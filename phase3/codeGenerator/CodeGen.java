package codeGenerator;
import Lexer.*;
import codeGenerator.data.*;
import parser.CodeGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Stack;

import codeGenerator.data.*;

public class CodeGen implements CodeGenerator {
    private Lexer scanner;
    private StringBuilder code = new StringBuilder();
    private StringBuilder data = new StringBuilder();
    private String[] reservedKeyWords = new String[]{"void","int","real","bool","string", "class", "for", "while", "if"
    , "else", "return","let", "in","func"
    , "new", "break", "continue", "loop", "range", "inputInt", "print"
    ,  "len", "true", "false","static","inputStr"};
    private Scope topMostScope = new Scope("TOP", "program", null, null);
    private Scope MainScope = new Scope("Main", "class", "Main", topMostScope);
    {
        topMostScope.symbolTable.put("Main", MainScope);
    }
    private Scope currentScope = MainScope;
    
    
    private final static String STRING_MAX_SIZE = "20";
   
    private final static String EXCEPTION_MESSAGE = "run time exception ...";
    private final static String EXCEPTION_LABEL = "EXP";

    private boolean inMethodInputDCL = false;
    private boolean inArrayDCL = false;
    private boolean newArrayInRight = false;
    private boolean breakSeen = false;
    private ArrayList <String> breakpoints = new ArrayList<String>();
    private ArrayType array;
    private Data globalData;
    private int literalCounter = 1;
    private int codeLabelingCounter = 1;

    private static int linesLabelNumber = 0;
    private String calleeMethodNameAddress;
    private int methodArgumentsCount = 0;
    String [] MathValue = new String [2];

    private Stack<String> semanticStack = new Stack<>();
    public CodeGen(Lexer lexer) {
        this.scanner = lexer;
        code.append(".text\n");
        code.append(".globl main\n");
        GlobalRoutines();
        data.append(".data\n");
        data.append(".align 2\n");
        data.append("toAlign: .space 404\n");
        data.append("EXCEP" + ": .asciiz " + "\"" + "run time exception ..." + "\"\n");
    }

    @Override
public void doSemantic(String sem)  {
    try {
        switch (sem) {
           
            case "pushSTRING":
                semanticStack.push(Lexer.Value);
                break;

           case "variableDCL":
                varDCL();
                break;

            case "switchArrDCL":
                inArrayDCL = !inArrayDCL;
                break;

            case "popStack":
                semanticStack.pop();
                inArrayDCL=false;
                break;
            
            case "popStackMethod":
                semanticStack.pop();
                inArrayDCL=false;
                inMethodInputDCL=false;
                break;
            
            case "BackScope":
                currentScope = currentScope.previousScope;
                break;

            case "SwitchInputDCL":
                inMethodInputDCL=false;
                break;

            case "SetReturnMethod":
                SetReturnMethod();
                break;

            case "Casting":
                casting();
                break;

            case "check&pushAddrForAssign":
                checkAndPushAddrForAssign();
                break;

            case "pushIntNum":
                ImmediateAssign("int");
                break;

            case "pushRealNum":
            ImmediateAssign("real");
                break;

            case "pushHexNum":
            ImmediateAssign("hex");
                break;

            case "pushSciNum":
                ImmediateAssign("sci");
                break;
            
            case "check&pushUsedID":
                checkAndPushUsedID();
                break;

            case "Check&PushVarID":
                checkAndPushVarId();    
                break;
            case "Assign":
                assignment();
                break;

            case "final":
                Final();
                break;

            case "StringLiteral":
                setStringData();
                break;

            case "NewArr":
                NewArray();
                break;

            case "ArrLeftAssign":
                arrayLeftAssign();
                break;

            case "Print":
                PrintOut();
                break;
            
            case "MethodDCL":
                methodDCL();
                break;

            case "jump_zero":
                Jumpzero();
                break;

            case "complete_jz":
                CompleteJZ();
                break;
                
            case "else_jump":
                ElseJmp();
                break;

            case "complete_else_jmp":
                CompleteElseJmp();
                break;

            case "LogicORR":
                MathValue[0] = "or";
                MathValue[1]= null;
                expressionFunctions(MathValue);
                break;

            case "LogicAND":
                MathValue[0] = "and";
                MathValue[1]= null;
                expressionFunctions(MathValue);
                break;

            case "BitwiseOR":
                MathValue[0] = "or";
                MathValue[1]= null;
                expressionFunctions(MathValue);
                break;

            case "BitwiseXOR":
                MathValue[0] = "xor";
                MathValue[1]= null;
                expressionFunctions(MathValue);
                break;

            case "BitwiseAND":
                MathValue[0] = "and";
                MathValue[1]= null;
                expressionFunctions(MathValue);
                break;
        
            case "Equality":
                MathValue[0] = "seq";
                MathValue[1]= "c.eq.s";
                compressionFunction(MathValue);
                break;

            case "NotEqual":
                MathValue[0] = "sne";
                MathValue[1]= "c.ne.s";
                compressionFunction(MathValue);
                break;

            case "GRThan":
                MathValue[0] = "sgt";
                MathValue[1]=  "c.gt.s";
                compressionFunction(MathValue);
                break;

            case "GREqual":
                MathValue[0] = "sgt";
                MathValue[1]=  "c.ge.s";
                compressionFunction(MathValue);
                break;

            case "LSThan":
                MathValue[0] = "slt";
                MathValue[1]=  "c.lt.s";
                compressionFunction(MathValue);
                break;

            case "LSEqual":
                MathValue[0] = "sle";
                MathValue[1]=  "c.le.s";
                compressionFunction(MathValue);
                break;

            case "ADDExpr":
                MathValue[0] = "add";
                MathValue[1]=  "add.s";
                expressionFunctions(MathValue);
                break;

            case "SubExpr":
                MathValue[0] = "sub";
                MathValue[1]=  "sub.s";
                expressionFunctions(MathValue);
                break;

            case "MulExpr":
                MathValue[0] = "mulo";
                MathValue[1]=  "mul.s";
                expressionFunctions(MathValue);
                break;

            case "DivExpr":
                MathValue[0] = "div";
                MathValue[1]=  "div.s";
                expressionFunctions(MathValue);
                break;

            case "RemExpr":
                MathValue[0] = "rem";
                MathValue[1]=  null;
                expressionFunctions(MathValue);
                break;
            
            case "MinusSignPush":
                semanticStack.push("-1");
                break;

            case "PlusSignPush":
                semanticStack.push("1");
                break; 

            case "SignApply":
                SignApply();
                break;
            
            case "ForLabel":
                ForLabelPush();
                break;

            case "ForConditionJZ":
                For_JZ_Condition();
                break;

            case "ForCompleteLabeling":
                For_Labeling_Update();
                break;  

            case "ForCompleteJZ":
                For_Complete_JZ();
                break;  
            
            case "While_Condition":
                While_Condition_Jump();
                break; 
                
            case "WhileComplete":
                For_Complete_JZ(); 
                break;    

            case "Break":
                // Break_LOOP();
                break;

            case "StatementINC1":
                StatementINC1();
                break;

            case "StatementDEC1":
                StatementDEC1();
                break;    

            case "True":
                String register = RegisterPool.getTemp();
                code.append("li ").append(register).append(", ").append("1").append("\n");
                semanticStack.push(register);
                break;

            case "False":
                register = RegisterPool.getTemp();
                code.append("li ").append(register).append(", ").append("0").append("\n");
                semanticStack.push(register);
                break;

            case "fetchArrValue":
                fetchArrVal();
                break;

            case "FindMethod":
                findMethodName();
                break;

            case "AddMethodArguments":
                addMethodArgument();
                break;

            case "MethodCall":
                MethodCall();
                break;

            case "IN_STR":
                read_string();
                break;

            case "IN_INT":
                read_int();
                break;

            case "ReturnVoidCheck":
                checkVoidReturnMethod();
                break;

            case "ReturnMethodCheck":
                checkReturnMethod();
                break;
            case "Add1Before":
                Add1BeforeUnary();
                break;
            case "Dec1Before":
                Dec1BeforeUnary();
                break;

        }
    }catch (Exception e) {
        e.printStackTrace();
    }
}
private void addVarToData(VarType variable) {
    switch (variable.type) {
        case "int":
            data.append(variable.address).append(": ").append(".word ").append("0").append("\n");
            break;
        case "bool":
            data.append(variable.address).append(": ").append(".word ").append("0").append("\n");
            break;
        case "void":
            data.append(variable.address).append(": ").append(".word ").append("0").append("\n");
            break;
        case "string":
            data.append(variable.address).append(": ").append(".word ").append("0").append("\n");
            break;
        case "real":
            data.append(variable.address).append(": ").append(".float ").append("0.0").append("\n");
            break;

        case "array":
            data.append(variable.address).append(": ").append(".space ").append("0").append("\n");
            data.append(variable.address).append("__size").append(": ").append(".word ").append("0").append("\n");
            break;
    }
}
private void varDCL() throws CoolCompileError {
    String varType;
    String varID;
    VarType variable;
    varType = semanticStack.pop();
    varID = Lexer.Value;
    System.out.print(varType);
    System.out.print(Lexer.Value.toString());
    if (Arrays.asList(reservedKeyWords).contains(varID)) {
        throw new CoolCompileError("reserved keywords must not be used");
    }
    if (currentScope.symbolTable.containsKey(varID)) {
        throw new CoolCompileError("ID has used");
    } else {
        if (inArrayDCL) {
            variable = new ArrayType(varID, "array", varType, currentScope.address + "__" + varID, inMethodInputDCL);
        } else if (varType.equals("string")) {
            variable = new StringCool(varID, varType, currentScope.address + "__" + varID, inMethodInputDCL, "\"\"");
        } else {
            variable = new VarType(varID, varType, currentScope.address + "__" + varID, inMethodInputDCL);
        }
    
        addVarToData(variable);
        currentScope.symbolTable.put(varID, variable);
        semanticStack.push(varType);
    }
    }
    private void checkAndPushAddrForAssign() throws CoolCompileError {
        String id;
        String register;
        id = Lexer.Value;
        if (currentScope.symbolTable.containsKey(id)) {
            globalData = currentScope.symbolTable.get(id);
        } else if (currentScope.previousScope.symbolTable.containsKey(id)) {
            globalData = currentScope.previousScope.symbolTable.get(id);
        } else if (currentScope.previousScope.previousScope.symbolTable.containsKey(id)) {
            return;
        } else {
            throw new CoolCompileError("id not defined");
        }
        if (globalData.type.equals("array"))
            array = (ArrayType) globalData;

        register = RegisterPool.getTemp();
        code.append("la ").append(register).append(", ").append(globalData.address).append("\n");
        semanticStack.push(register);
    }
    private void checkAndPushVarId() throws CoolCompileError {
        String varID;
        varID = Lexer.Value;
        if (Arrays.asList(reservedKeyWords).contains(varID)) {
            throw new CoolCompileError("reserved keywords must not be used");
        }
        if (currentScope.symbolTable.containsKey(varID)) {
            throw new CoolCompileError("ID has used");
        } else {
            semanticStack.push(varID);
        }
    }
    private void assignment() throws CoolCompileError {
        if (newArrayInRight) {
            String left = semanticStack.pop();
            RegisterPool.backSavedTemp(left);
            newArrayInRight = false;
            return;
        }

        String rightReg = semanticStack.pop();
        String leftReg = semanticStack.pop();


        switch (globalData.type) {
            case "int":
                if (!rightReg.contains("$t")) {
                    System.out.println(globalData.type+"hello1/n");
                    throw new CoolCompileError("not compatible in assignment");
                }
                code.append("sw ").append(rightReg).append(", ").append("(").append(leftReg).append(")").append("\n");
            System.out.println(rightReg+"right\n");
            System.out.println(leftReg+"left\n");
                RegisterPool.backTemp(rightReg);
                RegisterPool.backTemp(leftReg);
                break;
            case "bool":
                if (!rightReg.contains("$t")) {
                    System.out.println(globalData.type+"hello1/n");
                    throw new CoolCompileError("not compatible in assignment");
                }
                code.append("sw ").append(rightReg).append(", ").append("(").append(leftReg).append(")").append("\n");
               System.out.println(rightReg+"right\n");
               System.out.println(leftReg+"left\n");
                RegisterPool.backTemp(rightReg);
                RegisterPool.backTemp(leftReg);
                break;
            case "real":
                if (!rightReg.contains("$f")) {
                    throw new CoolCompileError("not compatible in assignment");
                }
                code.append("s.s ").append(rightReg).append(", ").append("(").append(leftReg).append(")").append("\n");
                RegisterPool.backFloat(rightReg);
                RegisterPool.backFloat(leftReg);
                break;
            case "string":
                code.append("sw ").append(rightReg).append(", ").append("(").append(leftReg).append(")").append("\n");
                RegisterPool.backTemp(rightReg);
                RegisterPool.backSavedTemp(leftReg);
                break;
            case "array":
                code.append("sw ").append(rightReg).append(", ").append("(").append(leftReg).append(")").append("\n");
                RegisterPool.backTemp(rightReg);
                RegisterPool.backSavedTemp(leftReg);
                break;
            default:
                throw new CoolCompileError("error in assignment");
        }
    }
    private void assignVarToReg(Data variable) throws CoolCompileError {
        String register;
        switch (variable.type) {
            case "int":
                register = RegisterPool.getTemp();
                code.append("lw ").append(register).append(", ").append(variable.address).append("\n");
                break;
            case "real":
                register = RegisterPool.getFloat();
                code.append("l.s ").append(register).append(", ").append(variable.address).append("\n");
                break;

            case "array":
                array = (ArrayType) variable;
            case "method":
                register = RegisterPool.getSavedTemp();
                code.append("la ").append(register).append(", ").append(variable.address).append("\n");
                break;

            case "string":
                register = RegisterPool.getSavedTemp();
                code.append("lw ").append(register).append(", ").append(variable.address).append("\n");
                break;

            default:
                throw new CoolCompileError("this variable in expresion is invalid");
        }
        System.out.println(register+"\n");
        semanticStack.push(register);
    }
    private void checkAndPushUsedID() throws CoolCompileError {
        String id;
        id = Lexer.Value;
        if (currentScope.symbolTable.containsKey(id)) {
            assignVarToReg(currentScope.symbolTable.get(id));
        } else if (currentScope.previousScope.symbolTable.containsKey(id)) {
            assignVarToReg(currentScope.previousScope.symbolTable.get(id));
        } else if (currentScope.previousScope.previousScope.symbolTable.containsKey(id)) {
            return;
        } else {
            throw new CoolCompileError("id not defined");
        }
    }
    private void ImmediateAssign(String numType) {
        String number = Lexer.Value;
        String reg = "";

        switch (numType) {
            case "int":
                reg = RegisterPool.getTemp();
                code.append("li ").append(reg).append(", ").append(number).append("\n");
                break;
            case "real":
                reg = RegisterPool.getFloat();
                code.append("li.s ").append(reg).append(", ").append(number).append("\n");
                break;
            case "hex":
                reg = RegisterPool.getTemp();
                code.append("li ").append(reg).append(", ").append(ConvertMethods.hexStringCorrection(number)).append("\n");
                break;
            case "sci":
                reg = RegisterPool.getFloat();
                code.append("li.s ").append(reg).append(", ").append(ConvertMethods.sciStringCorrection(number)).append("\n");
                break;
        }
        
        System.out.print(reg+"pushimmediate \n");
        semanticStack.push(reg);
    }
    private void setStringData() {
        String register;
        String literal = Lexer.Value;
        String addr = Integer.toString(literalCounter);
        literalCounter++;
        data.append("literal__").append(addr).append(" : ").append(".asciiz ")
                .append(literal).append("\n");
        register = RegisterPool.getSavedTemp();
        code.append("la ").append(register).append(", ")
                .append("literal__").append(addr).append("\n");
        semanticStack.push(register);
    }
    private void fetchArrVal() throws CoolCompileError {
        if (array == null) {
            throw new CoolCompileError("problem in array fetch");
        }
        String indexReg = semanticStack.pop();
        String arrayAddrReg = semanticStack.pop();

        String finalValueReg;
        String sizeAddrReg = RegisterPool.getTemp();
        String compResReg = RegisterPool.getTemp();

        code.append("lw ").append(sizeAddrReg).append(", ").append(array.address + "__" + "size").append("\n");
        code.append("addi ").append(sizeAddrReg).append(", ").append("-1").append("\n");
        code.append("sgt ").append(compResReg).append(", ").append(indexReg).append(", ").append(sizeAddrReg).append("\n");
        code.append("beq ").append(compResReg).append(", ").append("1").append(", ").append("exception").append("\n");
        code.append("slt ").append(compResReg).append(", ").append(indexReg).append(", ").append("0").append("\n");
        code.append("beq ").append(compResReg).append(", ").append("1").append(", ").append("exception").append("\n");

        RegisterPool.backTemp(sizeAddrReg);
        RegisterPool.backTemp(compResReg);

        code.append("mul ").append(indexReg).append(", ").append(indexReg).append(", ").append("4").append("\n");
        code.append("add ").append(arrayAddrReg).append(", ").append(arrayAddrReg).append(", ").append(indexReg).append("\n");

        if (array.type.equals("real")) {
            finalValueReg = RegisterPool.getFloat();
            code.append("l.s ").append(finalValueReg).append(", ").append("(").append(arrayAddrReg).append(")").append("\n");
        } else {
            finalValueReg = RegisterPool.getTemp();
            code.append("lw ").append(finalValueReg).append(", ").append("(").append(arrayAddrReg).append(")").append("\n");
        }

        semanticStack.push(finalValueReg);
        RegisterPool.backTemp(indexReg);
        RegisterPool.backSavedTemp(arrayAddrReg);
        array = null;
    }
    private void arrayLeftAssign() {
            String indexReg;
            String arrayAddrReg;
            indexReg = semanticStack.pop();
            arrayAddrReg = semanticStack.pop();

        String sizeAddrReg = RegisterPool.getTemp();
        String compResReg = RegisterPool.getTemp();

        code.append("lw ").append(sizeAddrReg).append(", ").append(array.address + "__" + "size").append("\n");
        code.append("addi ").append(sizeAddrReg).append(", ").append("-1").append("\n");
        code.append("sgt ").append(compResReg).append(", ").append(indexReg).append(", ").append(sizeAddrReg).append("\n");
        code.append("beq ").append(compResReg).append(", ").append("1").append(", ").append("exception").append("\n");
        code.append("slt ").append(compResReg).append(", ").append(indexReg).append(", ").append("0").append("\n");
        code.append("beq ").append(compResReg).append(", ").append("1").append(", ").append("exception").append("\n");

        RegisterPool.backTemp(sizeAddrReg);
        RegisterPool.backTemp(compResReg);

            code.append("mul ").append(indexReg).append(", ").append(indexReg).append(", ").append("4").append("\n");
            code.append("add ").append(arrayAddrReg).append(", ").append(arrayAddrReg).append(", ").append(indexReg).append("\n");

            semanticStack.push(arrayAddrReg);
            RegisterPool.backTemp(indexReg);

        }
        private void NewArray() throws CoolCompileError {
            String varType;
            varType = semanticStack.pop();
            if (array == null) {
                throw new CoolCompileError("problem in create array");
            }
            if (inArrayDCL) {
                throw new CoolCompileError("array can't be in array");
            }
            if (!varType.equals(array.arrayType)) {
                throw new CoolCompileError("array type not compatible with assign");
            }
            String arraySize = Lexer.Value;
            array.size = Integer.parseInt(arraySize);
    
            int begin = data.indexOf(array.address + ": .space 0");
            int size = (array.address + ": .space 0").length();
            data.replace(begin, begin + size, array.address + ": .space " + Integer.parseInt(arraySize) * 4);
    
            begin = data.indexOf(array.address + "__size: .word 0");
            size = (array.address + "__size: .word 0").length();
            data.replace(begin, begin + size, array.address + "__size: .word " + arraySize);
    
    
            newArrayInRight = true;
            inArrayDCL = false;
            array = null;
        }
        private void PrintOut() throws CoolCompileError {
            String expr = semanticStack.pop();
            if (expr.contains("$t")) {
                code.append("move $a0, ").append(expr).append("\n");
                code.append("jal ").append("print_int").append("\n");
                RegisterPool.backTemp(expr);
            }
            else if (expr.contains("$f")) {
                code.append("mov.s $f12, ").append(expr).append("\n");
                code.append("jal ").append("print_float").append("\n");
                RegisterPool.backFloat(expr);
            }
            else if (expr.contains("$s")) {
                code.append("move $a0, ").append(expr).append("\n");
                code.append("jal ").append("print_string").append("\n");
                RegisterPool.backSavedTemp(expr);
            }
            else {
                throw new CoolCompileError("not able to print");
            }
        }
        private void Jumpzero() {
            String expr = semanticStack.pop();
            code.append("beqz ").append(expr).append(", ").append("\n");
            codeLabelingCounter++;
            String lastIndex = String.valueOf(code.lastIndexOf("beqz "));
            RegisterPool.backTemp(expr);
            semanticStack.push(lastIndex);
        }

        private void CompleteJZ() {
           
            int jump_index = Integer.parseInt(semanticStack.pop());
            String addrOfHere = "DJZ__IF" + codeLabelingCounter;

            code.append(addrOfHere).append(": \n");
            System.out.println("beqzvalue"+code.substring(jump_index, jump_index + 10));
            code.replace(jump_index, jump_index + 10, code.substring(jump_index, jump_index + 10) + addrOfHere);
            
        }
        private void ElseJmp() {
            String hereLabel = "DJZ__IF" + codeLabelingCounter;
            String Delete = String.valueOf(code.lastIndexOf(hereLabel));

            code.delete( Integer.parseInt(Delete), Integer.parseInt(Delete)+hereLabel.length()+4);
            codeLabelingCounter++;
            
            code.append("b ").append("\n");
            code.append(hereLabel).append(":").append("\n");
            String index = String.valueOf(code.lastIndexOf("b "));
            semanticStack.push(index);
        }
        private void CompleteElseJmp() {
            
            int index = Integer.parseInt(semanticStack.pop());
            String label = "DJP__ELSE" + codeLabelingCounter;
            codeLabelingCounter++;
            code.append(label).append(":").append("\n");
            code.replace(index, index + 2, code.substring(index, index + 2) + label);
        }



        
        private void compressionFunction(String [] value) throws CoolCompileError {
            String [] temp = new String [2];
            if (semanticStack.peek().charAt(1) == 't') {
                temp[0]=value[0];
                temp[1]=null;
                System.out.println("compressionFunction"+temp+"\n");
                expressionFunctions(temp);
            } else {
                String topRegister1 ;
                String topRegister2 ;
                if (value[1]== "c.gt.s"){
                    topRegister2 = semanticStack.pop();
                    topRegister1 = semanticStack.pop();   
                    value[1] = "c.lt.s";
            }
                else if (value[1]== "c.ge.s"){
                    topRegister2 = semanticStack.pop();
                    topRegister1 = semanticStack.pop();   
                    value[1] = "c.le.s";
            }
                
                else {
                    topRegister1 = semanticStack.pop();
                    topRegister2 = semanticStack.pop();   
            }
                
                if (topRegister1.charAt(1) != topRegister2.charAt(1)) {
                    throw new CoolCompileError("type of two operand is different");
                }
                if (topRegister1.charAt(1) == 'f') {
                    String tempRegister = RegisterPool.getTemp();
                    code.append("ori ").append(tempRegister).append(",  ")
                            .append(tempRegister).append(",  ").append(1).append("\n"); //create temp register is true
                   
                    
    
                    code.append(value[1]=="c.ne.s"?"c.eq.s":value[1]).append(" ")
                            .append(topRegister2).append(",  ").append(topRegister1).append("\n");
    
                    String lineLabel = "myLineLabel" + linesLabelNumber++;
                    code.append( value[1] != "c.ne.s"?"bc1t " :"bc1f " ).append(lineLabel).append(" ").append("\n");
                    
                    code.append("andi ").append(tempRegister).append(",  ")
                    .append(tempRegister).append(",  ").append(0).append("\n"); //make temp register is false
                    
                    code.append(lineLabel).append(": ");
    
                    semanticStack.push(tempRegister);
                } else {
                    throw new CoolCompileError("otherwise f type are illegal");
                }
    
            }
        }

        private void expressionFunctions(String [] value) throws CoolCompileError {
           
            String topRegister1 = semanticStack.pop();
            String topRegister2 = semanticStack.pop();
            
            if (topRegister1.charAt(1) != topRegister2.charAt(1)) {
                throw new CoolCompileError("type of two operand is different");
            }
    
            if (value[1] != null && topRegister1.charAt(1) == 'f') {
                code.append(value[1]).append(" ").append(topRegister1).append(",  ")
                        .append(topRegister2).append(",  ").append(topRegister1).append("\n");
    
            } else {
                if (topRegister1.charAt(1) == 't') {
                    code.append(value[0]).append(" ").append(topRegister1).append(",  ")
                            .append(topRegister2).append(",  ").append(topRegister1).append("\n");
                } else {
                    throw new CoolCompileError("otherwise f,t type are illegal");
                }
            }
            RegisterPool.backTemp(topRegister2);
            semanticStack.push(topRegister1);
        }
        private void SignApply () {
           
            String topRegister1 = semanticStack.pop();
            String topRegister2 = semanticStack.pop();
            if ( topRegister1.charAt(1) == 'f'){
                String reg=RegisterPool.getFloat();
                code.append("li.s ").append(reg).append(", ").append(topRegister2).append("\n");
                code.append("mul.s").append(" ").append(topRegister1).append(",  ")
                                .append(reg).append(",  ").append(topRegister1).append("\n");
                semanticStack.push(topRegister1);
                 RegisterPool.backFloat(reg);
                 RegisterPool.backTemp(topRegister2);
                
                        }
            else {
                String reg=RegisterPool.getTemp();
                code.append("li ").append(reg).append(", ").append(topRegister2).append("\n");
                code.append("mulo").append(" ").append(topRegister1).append(",  ")
                                .append(reg).append(",  ").append(topRegister1).append("\n");
                semanticStack.push(topRegister1);
                RegisterPool.backTemp(reg);
                 RegisterPool.backTemp(topRegister2);
                System.out.println("finished!");
            }
        }
        private void ForLabelPush() {
            String label = "LOOP" + codeLabelingCounter;
            codeLabelingCounter++;
            code.append(label).append(": \n");
            semanticStack.push(label);
        }
        private void For_Complete_JZ() {
            String temp = semanticStack.pop();
            int index=0;
            System.out.println(temp+"\n");
            if(temp.contains("$")) {
                index = Integer.parseInt(semanticStack.pop());
            }
            else {
                index=Integer.parseInt(temp);
            }
            
            String loopLabel = semanticStack.pop();

            String endLabel = "END_LOOP" + codeLabelingCounter;
            codeLabelingCounter++;
            if(temp.contains("$")) {
                semanticStack.push(temp);
            }
            
            code.replace(index, index + 10, code.substring(index, index + 10) + endLabel);
            code.append("b ").append(loopLabel).append("\n");
            code.append(endLabel).append(": ").append("\n");

        }
    
        private void For_JZ_Condition() {
            String expr = semanticStack.pop();
    
            String beginUpdate = "BEGIN_UPDATE" + codeLabelingCounter;
            codeLabelingCounter++;
    
            code.append("beqz ").append(expr).append(", ").append("\n");
            code.append("b").append("\n");
            code.append(beginUpdate).append(":").append("\n");
            String lastIndex = String.valueOf(code.lastIndexOf("beqz "));
            String lastBranchIndex = String.valueOf(code.lastIndexOf("b"));
            RegisterPool.backTemp(expr);
            semanticStack.push(lastIndex);
            semanticStack.push(beginUpdate);
            semanticStack.push(lastBranchIndex);
        }
        private void For_Labeling_Update() {
            int indexBranchToHere = Integer.parseInt(semanticStack.pop());
            String updateLabel = semanticStack.pop();
            String index = semanticStack.pop();
            String forLabel = semanticStack.pop();
            String hereLabel = "BEGIN_STATEMENT" + codeLabelingCounter;
            codeLabelingCounter++;
            code.append("b ").append(forLabel).append("\n");
            code.append(hereLabel).append(":").append("\n");
            code.replace(indexBranchToHere, indexBranchToHere + 1, "b " + hereLabel);
            semanticStack.push(updateLabel);
            semanticStack.push(index);
        }
        private void While_Condition_Jump() {
            String expr = semanticStack.pop();
            code.append("beqz ").append(expr).append(", ").append("\n");
            String lastIndex = String.valueOf(code.lastIndexOf("beqz "));
            RegisterPool.backTemp(expr);
            semanticStack.push(lastIndex);
        }
        private void StatementINC1() throws CoolCompileError {
            String id = Lexer.Value;
            Data registerAddress;
            if (currentScope.symbolTable.containsKey(id)) {
                registerAddress = currentScope.symbolTable.get(id);
            } else if (currentScope.previousScope.symbolTable.containsKey(id)) {
                registerAddress = currentScope.previousScope.symbolTable.get(id);
            } else {
                throw new CoolCompileError("id not defined");
            }
            String newRegisterValue = RegisterPool.getTemp();
            code.append("lw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");
    
            code.append("addi ").append(newRegisterValue).append(",  ").append(newRegisterValue).append(",  ").append(1).append("\n");
    
            code.append("sw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");
        }
        private void StatementDEC1() throws CoolCompileError {
            String id = Lexer.Value;
            Data registerAddress;
            if (currentScope.symbolTable.containsKey(id)) {
                registerAddress = currentScope.symbolTable.get(id);
            } else if (currentScope.previousScope.symbolTable.containsKey(id)) {
                registerAddress = currentScope.previousScope.symbolTable.get(id);
            } else {
                throw new CoolCompileError("id not defined");
            }
            String newRegisterValue = RegisterPool.getTemp();
            code.append("lw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");
    
            code.append("addi ").append(newRegisterValue).append(",  ").append(newRegisterValue).append(",  ").append(-1).append("\n");
    
            code.append("sw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");
        }
        // private void Break_LOOP() {
        //     breakSeen=true;
        //     code.append("b").append("\n");
        //     String lastIndex = String.valueOf(code.lastIndexOf("b"));
        //     semanticStack.push(lastIndex);
        // }

        private void casting() {
            String numberRegister = semanticStack.pop();
            String typeString = semanticStack.pop();
    
            switch (typeString) {
                case "int":
                    if (numberRegister.charAt(1) == 't') { //current number is integer
                        semanticStack.push(numberRegister);
                    } else if (numberRegister.charAt(1) == 'f') { //current number is float
                        code.append("cvt.w.s ").append(numberRegister).append(" , ").append(numberRegister).append("\n");
    
                        String tempRegister = RegisterPool.getTemp();
                        code.append("mfc1 ").append(tempRegister).append(" , ").append(numberRegister).append("\n");
                        semanticStack.push(tempRegister);
                    }
                    break;
    
                case "real":
                    if (numberRegister.charAt(1) == 't') { //current number is integer
                        String floatRegister = RegisterPool.getFloat();
    
                        code.append("mtc1 ").append(numberRegister).append(" , ").append(floatRegister).append("\n");
    
                        code.append("cvt.s.w ").append(floatRegister).append(" , ").append(floatRegister).append("\n");
                        semanticStack.push(floatRegister);
                    } else if (numberRegister.charAt(1) == 'f') {
                        semanticStack.push(numberRegister);
                    }
                    break;
    
                case "bool":
                    semanticStack.push(numberRegister);
                    break;
            }
    
        }
        private void methodDCL() {
            String varID;
            varID = semanticStack.pop();
            Scope newMethod;
            if (varID.equals("main"))
                newMethod = new Method(varID, "method", varID, currentScope);
            else
                newMethod = new Method(varID, "method", currentScope.address + "__" + varID, currentScope);
            currentScope.symbolTable.put(varID, newMethod);
            currentScope = newMethod;
            inMethodInputDCL = true;
            System.out.println(newMethod.address+"METHOD\n");
            code.append(newMethod.address).append(":").append("\n");
        }
        private void SetReturnMethod() {
            String returnType = semanticStack.pop();
            if (inArrayDCL) {
                returnType = returnType + "[]";
                inArrayDCL = false;
            }
            Method method = (Method) currentScope;
            method.returnType = returnType;
        }
        private void findMethodName() {
            calleeMethodNameAddress = "Main__" + Lexer.Value;
            methodArgumentsCount = 0;
        }
        private void addMethodArgument() {
            methodArgumentsCount++;
        }
        private void MethodCall() {
            Method calleeMethod = (Method) currentScope.symbolTable.get(calleeMethodNameAddress);
    
            if (methodArgumentsCount > 0) {
                for (String key : calleeMethod.symbolTable.keySet()) {
                    VarType varType = (VarType) calleeMethod.symbolTable.get(key);
                    if (varType.isInput && methodArgumentsCount-- > 0) {
                        String topExpr = semanticStack.pop();
                        code.append("sw ").append(topExpr).append(", ").append(varType.address).append("\n");
    
                        if (topExpr.charAt(1) == 't') {
                            RegisterPool.backTemp(topExpr);
                        } else if (topExpr.charAt(1) == 'f') {
                            RegisterPool.backFloat(topExpr);
                        }
                    }
                }
            }
            code.append("jal ").append(calleeMethodNameAddress).append("\n");
        }
        private void checkVoidReturnMethod() throws CoolCompileError {
            Method calleeMethod = (Method) currentScope;
            if (!calleeMethod.returnType.equals("void")) {
                throw new CoolCompileError("return type of method should be void");
            }
    
            code.append("jr $ra \n");
        }
        private void checkReturnMethod() throws CoolCompileError {
            String top = semanticStack.peek();
            Method calleeMethod = (Method) currentScope;
            switch (calleeMethod.returnType) {
                case "int":
                    if (top.charAt(1) != 't') {
                        throw new CoolCompileError("Return type dosn't match expected return type");
                    }
                    break;
                case "bool":
                    if (top.charAt(1) != 't') {
                        throw new CoolCompileError("Return type dosn't match expected return type");
                    }
                    break;
    
                case "real":
                    if (top.charAt(1) != 'f') {
                        throw new CoolCompileError("Return type dosn't match expected return type");
                    }
                    break;
            }
    
            code.append("jr $ra \n");
        }
    private void read_string() {
        String address = "literal__" + literalCounter;
        literalCounter++;
        data.append(address).append(": .space ").append(STRING_MAX_SIZE).append("\n");
        code.append("la $a0, ").append(address).append("\n");
        code.append("li $a1, ").append(STRING_MAX_SIZE).append("\n");
        code.append("jal ").append("read_string").append("\n");
        String regAddr = RegisterPool.getSavedTemp();
        code.append("move ").append(regAddr).append(", ").append("$a0").append("\n");
        semanticStack.push(regAddr);
    }

    private void read_int() {
        code.append("jal ").append("read_int").append("\n");
        String reg = RegisterPool.getTemp();
        code.append("move ").append(reg).append(", ").append("$v0 ").append("\n");
        semanticStack.push(reg);
    }

    private void Add1BeforeUnary() throws CoolCompileError {
        String id1 = Lexer.Value;
        Data registerAddress;
        if (currentScope.symbolTable.containsKey(id1)) {
            registerAddress = currentScope.symbolTable.get(id1);
        } else if (currentScope.previousScope.symbolTable.containsKey(id1)) {
            registerAddress = currentScope.previousScope.symbolTable.get(id1);
        } else {
            throw new CoolCompileError("id not defined");
        }
        String newRegisterValue = RegisterPool.getTemp();
        code.append("lw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");

        code.append("addi ").append(newRegisterValue).append(",  ").append(newRegisterValue).append(",  ").append(1).append("\n");

        code.append("sw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");
        semanticStack.push(newRegisterValue);
    }

    private void Dec1BeforeUnary() throws CoolCompileError {
        String id2 = Lexer.Value;
        Data registerAddress;
        if (currentScope.symbolTable.containsKey(id2)) {
            registerAddress = currentScope.symbolTable.get(id2);
        } else if (currentScope.previousScope.symbolTable.containsKey(id2)) {
            registerAddress = currentScope.previousScope.symbolTable.get(id2);
        } else {
            throw new CoolCompileError("id not defined");
        }
        String newRegisterValue = RegisterPool.getTemp();
        code.append("lw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");

        code.append("addi ").append(newRegisterValue).append(",  ").append(newRegisterValue).append(",  ").append(-1).append("\n");

        code.append("sw ").append(newRegisterValue).append(",  ").append(registerAddress.address).append("\n");
        semanticStack.push(newRegisterValue);
    }

    private void PrintStringCode() {
        code.append("print_string").append(": ").append("\n");
        code.append("li $v0, 4").append("\n");
        code.append("syscall").append("\n");
        code.append("jr $ra").append("\n");
    }

    private void PrintIntCode() {
        code.append("print_int").append(": ").append("\n");
        code.append("li $v0, 1").append("\n");
        code.append("syscall").append("\n");
        code.append("jr $ra").append("\n");
    }

    private void PrintFloatCode() {
        code.append("print_float").append(": ").append("\n");
        code.append("li $v0, 2").append("\n");
        code.append("syscall").append("\n");
        code.append("jr $ra").append("\n");
    }

    private void ReadIntCode() {
        code.append("read_int").append(": ").append("\n");
        code.append("li $v0, 5").append("\n");
        code.append("syscall").append("\n");
        code.append("jr $ra").append("\n");
    }

    private void ReadStringCode() {
        code.append("read_string").append(": ").append("\n");
        code.append("li $v0, 8").append("\n");
        code.append("syscall").append("\n");
        code.append("jr $ra").append("\n");

    }

    private void ExceptionhandlingCode() {
        code.append("exception").append(": ").append("\n");
        code.append("la $a0, " + "EXPCEP").append("\n");
        code.append("li $v0, 4").append("\n");
        code.append("syscall").append("\n");
        code.append("jr $ra").append("\n");
        code.append("b termination\n");

    }

    private void TerminationCode() {
        code.append("termination: ").append("\n");
        code.append("li $v0, 10").append("\n");
        code.append("li $t0, 0").append("\n");
        code.append("move $a0, $t0").append("\n");
        code.append("syscall").append("\n");
    }
    private void GlobalRoutines() {
        PrintStringCode();
        PrintIntCode();
        PrintFloatCode();
        ReadIntCode();
        ReadStringCode();
        ExceptionhandlingCode();
    }
    private void Final() throws FileNotFoundException {
        TerminationCode();

        Formatter formatter = new Formatter(new FileOutputStream("./out.s"));
        formatter.format(data.toString());
        formatter.flush();
        formatter.format("\n");
        formatter.flush();
        formatter.format(code.toString());
        formatter.flush();
    }
}