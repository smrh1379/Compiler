package codeGenerator.data;

import java.util.HashMap;
import java.util.Map;

public class Scope extends Data {
    public Map<String, Data> symbolTable = new HashMap<>();
    public Scope previousScope;

    public Scope(String name, String type, String addr,  Scope previousScopeTable) {
        super(name, type, addr);
        this.previousScope = previousScopeTable;
    }
}
