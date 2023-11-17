package codeGenerator.data;

public class Method extends Scope{
    public String returnType;

    public Method(String name, String type, String addr, Scope previousScopeTable) {
        super(name, type, addr, previousScopeTable);
        this.returnType = returnType;
    }
}
