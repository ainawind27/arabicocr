class Boolean_2 {
  public static void main(String[] args) {  

    boolean b = true;  
    
  //  b = true;  
    System.out.println("Nilai b = " + b);
  
    if (b) {
      System.out.println("Statemen ke-1 dieksekusi");
    }
  
    b = false;
    System.out.println("Nilai b = " + b);
  
    if (b) {
      System.out.println("Statemen ke-2 tidak akan dieksekusi");
    }
  
    if (!b) {
      System.out.println("Statemen ke-3 dieksekusi");
    }
 
    System.out.println("5 <= 10 mengembalikan nilai " +
      (5 <= 10));
    System.out.println("4 > 6 mengembalikan nilai " + (4 > 6));
  }
}