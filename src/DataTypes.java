public class DataTypes {
    
    public static void main(String[] args) {
        int a = 10;
        Integer b = (Integer) 10;
        float f1 = 10.0f; //35e3F;
        double d1 = 12e4d;
        long l1 = 9223372036854775807L;
        System.out.print(b + "\n");
        System.out.println(f1);
        System.out.println(d1);
        System.out.println(l1);

        boolean isTrue = true;
        boolean isFalse = false;
        System.out.println(isTrue);
        System.out.println(isFalse);

        int myInt = 42;
        double myDouble = myInt;
        Integer myInt2 = (Integer) myInt;
        Integer wrappedInt = new Integer("5");
        Integer wrappedInt2 = Integer.valueOf("5");
        Float wrappedFloat = Float.valueOf(5.0f);

        System.out.println(myInt);
        System.out.println(myDouble);
        System.out.println(wrappedInt);
        System.out.println(wrappedFloat);

        String bin = Integer.toBinaryString(12345);
        String oct = Integer.toOctalString(12345);
        String hex = Integer.toHexString(12345);
        System.out.print("Binary representation: " + bin +
                "\nOctal representation: " + oct +
                "\nHexadecimal representation: " + hex + "\n");
    }
}
