package codeGenerator;

public class ConvertMethods {

    //  changing E to lower case
    public static String sciStringCorrection(String numberStr) {
        String result = numberStr;
        if (result.contains("E"))
            result.replace('E', 'e');
        
        return result;
    }

//  change x to lower case
    public static String hexStringCorrection(String numberStr) {
        String result = numberStr;
        if (result.contains("X"))
            result.replace('X', 'x');
        
        return result;
    }
}
