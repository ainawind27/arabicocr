

public class Antrian_1 {

	static int ukuran;
	static int [] antrian;
	static int top;
	
	static void inisialisasi(int i){
		
		ukuran = i;
		antrian = new int [ukuran];
		top =0;
	}
	
	static boolean isempty(){
		return top==0;
	}
	
	static boolean isfully(){
		return top==ukuran;
	}
	
	static void insert(int i){
		
		if (isfully()){
			System.out.println("Maaf antrian penuh");
		}else {
			//top = top+1;
			//antrian[top]=i;
			System.out.println("nilai " +i+ " masuk antrian");
			antrian [top++]=i;
		}
	}
	
	static void pop(){
		if (isempty()){
			System.out.println("maaf antrian kosong");
		}else{
			int pop = antrian[0];
			int i =1;
			System.out.println("Nilai " +pop+ " keluar dari antrian");
			while(i<top){
				antrian[i-1]= antrian[i];
				i++;
			}
			top--;
		}
	}
	
	static void display(){
		
		int i = 0;
		while (i<top){
			System.out.println("Antrian ke ["+(i+1)+"] adalah : " + antrian[i]);		
			i++;
		}
	}
	
	static void find(int i){
		
		int temp=0;
		boolean hasil = false;
		
		while (temp<top){
			if (i==antrian[temp]){
				hasil = true;
				break;
			}	
			temp++;
		}
		if (hasil){
			System.out.println("Nilai " +i+ " ditemukan pada indeks ke "+(temp+1));
		}else {
			System.out.println("Nilai " +i+ " tidak ditemukan");
		}
	}
	
	public static void main(String[]args){
		Antrian_1 a=new Antrian_1();
        a.inisialisasi(4);
        a.pop();
        a.insert(12);
        a.insert(5);
        a.insert(2);
        a.insert(3);
        a.display();
        a.insert(9);
        a.pop();
        a.display();
        a.find(3);
        a.find(13);
	}
}
