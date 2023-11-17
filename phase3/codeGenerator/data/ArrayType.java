package codeGenerator.data;

public class ArrayType extends VarType {
    public int size = 0;
    public String arrayType;

    public ArrayType(String name, String type, String arrayType, String address, boolean isInput) {
        super(name, type, address, isInput);
        this.isInput = isInput;
        this.arrayType = arrayType;
    }
}
