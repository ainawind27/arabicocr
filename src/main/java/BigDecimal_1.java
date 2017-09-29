import java.math.BigDecimal;

public class BigDecimal_1 {
  public static void main(String[] argv) throws Exception {
    // Create via a string
    BigDecimal bd1 = new BigDecimal("123456789.0123456890");

    // Create via a long
    BigDecimal bd2 = BigDecimal.valueOf(123L);

    bd1 = bd1.add(bd2);
    System.out.println(bd1);
  }
}