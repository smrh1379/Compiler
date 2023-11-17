package codeGenerator.data;

public class StringCool extends VarType {
    public String value;

    public StringCool(String name, String type, String address, boolean isInput, String value) {
        super(name, type, address, isInput);
        this.value = value;
    }
}
