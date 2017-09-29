import java.util.Scanner;

/**
 *
 * @author Febri Hidayan
 */
public class Boolean_1 {
    public static void main(String[]args){
        boolean a, b;
        a = true;
        b = false;
        Scanner scanner = new Scanner (System.in);
        
        System.out.println("Masukkan huruf a atau b :");
        String huruf = scanner.next();
        if(a){
            System.out.println("boolean bernilai true");
        }if(b){
            System.out.println("boolean bernilai false");
        }
        System.out.println("Copyright Sekolah Program");
    }
}