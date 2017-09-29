

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Zhangsuen2 {

	//Atribure
	Point [] Pos;
	List<Point> TandaiHapus;
	int[][] binaryImage;
	int widthImg;
	int heightImg;
	BufferedImage imgSrc;
	
	public Zhangsuen2(BufferedImage imgSrc) {
		//create binaryImage
		this.imgSrc = imgSrc;
		widthImg = imgSrc.getWidth();
        heightImg = imgSrc.getHeight();
        binaryImage = new int[widthImg][heightImg];
        System.out.println("Binarized:");
		 for (int i = 0; i < widthImg; i++) {
	            for (int j = 0; j < heightImg; j++) {
	                if (new Color(imgSrc.getRGB(i, j)).getRed() <= 225 ||
	                		new Color(imgSrc.getRGB(i, j)).getGreen() <= 225 ||
	                				new Color(imgSrc.getRGB(i, j)).getBlue() <= 225)  {
	                    binaryImage[i][j] = 1;
	                } else {
	                    binaryImage[i][j] = 0;
	                }
	                System.out.print(binaryImage[i][j]);
	            }
	            System.out.println();
		 }
		 //create LokasiTetangga
		
		Pos = new Point[8];
		Pos[0] = new Point(1,0);
		Pos[1] = new Point(1,-1);
		Pos[2] = new Point(0,-1);
		Pos[3] = new Point(-1,-1);
		Pos[4] = new Point(-1,0);
		Pos[5] = new Point(-1,1);
		Pos[6] = new Point(0,1);
		Pos[7] = new Point(1,1);
	}
		 
	public boolean endPoint(Point a){
		//Cek Jumlah Piksel sekitar Piksel
		int TetanggaHitam = 0;
		for(int i = 0;i<Pos.length;i++){
			if(binaryImage [a.x + (int)Pos[i].getX()][ a.y+ (int)Pos[i].getY()] == 1)
				TetanggaHitam+=1;
			if(TetanggaHitam > 1)
				return false;
			}
		if(TetanggaHitam == 1)
			return true;
		return false;
		}
	
	public int InterCon(Point a){
	int [] tmp = new int[8];
	for(int i = 0; i<Pos.length;i++)
		tmp[i] = binaryImage [a.x +(int)Pos[i].getX()][ a.y+ (int)Pos[i].getY()];
	
	return (tmp[0] - (tmp[0] * tmp[1] * tmp[2])) +
			(tmp[2] - (tmp[2] * tmp[3] * tmp[4])) +
			(tmp[4] - (tmp[4] * tmp[5] * tmp[6])) +
			(tmp[6] - (tmp[6] * tmp[7] * tmp[0]));
	}
	public BufferedImage doZhangSuen() {
		TandaiHapus = new ArrayList<Point>();
		Point Titik_Sekarang;
		 boolean stop;
		 
		 do{
			 //T1
			 stop = true;
			 for (int y = 1; y < imgSrc.getHeight()-1; y++) {
		            for (int x = 1; x < imgSrc.getWidth()-1; x++) {
		            	
		            	Titik_Sekarang = new Point(x,y);
		            	//cek titik sekarang apakah hitam
		            	if(binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()]==1){
		            		
		            		if(binaryImage[(int)Titik_Sekarang.getX()+1][(int)Titik_Sekarang.getY()]==1 &&
		            				binaryImage[(int)Titik_Sekarang.getX()-1][(int)Titik_Sekarang.getY()]==0 ){
		            			//
		            			if(InterCon(Titik_Sekarang)==1 && !endPoint(Titik_Sekarang)){
		            				TandaiHapus.add(new Point(Titik_Sekarang));
		            				stop=false;
		            			}
		            		}
		            		
		            	}
		            }
			 }
			 for(int i=0;i<TandaiHapus.size();i++){
				 binaryImage[(int)TandaiHapus.get(i).getX()][(int)TandaiHapus.get(i).getY()] = 0;
			 
			 }
			 TandaiHapus.clear();
			//T2
			 for (int x = imgSrc.getWidth()-2; x > 0 ; x--) {
		            for (int y = 1; y < imgSrc.getHeight(); y++) {
		            	
		            	Titik_Sekarang = new Point(x,y);
		            	//cek apakah titik sekarng hitam
		            	if(binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()]==1){
		            		// template T2
		            		if(binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()-1]==0 &&
		            				binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()+1]==1 ){
		            			
		            			if(InterCon(Titik_Sekarang)==1 && !endPoint(Titik_Sekarang)){
		            				TandaiHapus.add(new Point(Titik_Sekarang));
		            				stop=false;
		            			}
		            		}
		            		
		            	}
		            }
			 }
			 for(int i=0;i<TandaiHapus.size();i++){
				 binaryImage[(int)TandaiHapus.get(i).getX()][(int)TandaiHapus.get(i).getY()] = 0;				 
			 }
			 TandaiHapus.clear();
			//T3
			 for (int x = imgSrc.getWidth()-2;x>0; x--) {
		            for (int y = imgSrc.getHeight()-2; y > 0;  y--) {
		            	
		            	Titik_Sekarang = new Point(x,y);
		            	//Cek titik sekarang apakah hitam
		            	if(binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()]==1){
		            		//Cek Template T3
		            		if(binaryImage[(int)Titik_Sekarang.getX()-1][(int)Titik_Sekarang.getY()]==1 &&
		            				binaryImage[(int)Titik_Sekarang.getX()+1][(int)Titik_Sekarang.getY()]==0 ){
		            			
		            			if(InterCon(Titik_Sekarang)==1 && !endPoint(Titik_Sekarang)){
		            				//menandai titik yang akan dihapus
		            				TandaiHapus.add(new Point(Titik_Sekarang));
		            				stop=false;
		            			}
		            		}
		            		
		            	}
		            }
			 }
			 //hapus semua titik yang telah ditandai untuk di hapus
			 for(int i=0;i<TandaiHapus.size();i++){
				 binaryImage[(int)TandaiHapus.get(i).getX()][(int)TandaiHapus.get(i).getY()] = 0;
	
			 }
			 TandaiHapus.clear();
			//T4
			 for (int x = 1; x < imgSrc.getWidth()-1;  x++) {
		            for (int y = imgSrc.getHeight()-2; y > 0;  y--) {
		            	Titik_Sekarang = new Point(x,y);
		            	//cek apakah titik sekang titik hitam
		            	if(binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()]==1){
		            		//cek template T4
		            		if(binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()-1]==1 &&
		            				binaryImage[(int)Titik_Sekarang.getX()][(int)Titik_Sekarang.getY()+1]==0 ){
		            			
		            			if(InterCon(Titik_Sekarang)==1 && !endPoint(Titik_Sekarang)){
		            				TandaiHapus.add(new Point(Titik_Sekarang));
		            				stop=false;
		            			}
		            		}
		            		
		            	}
		            }
			 }
			 for(int i=0;i<TandaiHapus.size();i++){
				 binaryImage[(int)TandaiHapus.get(i).getX()][(int)TandaiHapus.get(i).getY()] = 0;
			 }
			 TandaiHapus.clear();
		 }while(!stop);
		 
		 // Post-processing, delete all branching pixels
		 for(int i = 1; i < imgSrc.getWidth() - 1; i++) {
			 for(int j = 1; j < imgSrc.getHeight() - 1; j++) {
				 int P1 = binaryImage[i][j-1];
				 int P2 = binaryImage[i+1][j-1];
				 int P3 = binaryImage[i+1][j];
				 int P4 = binaryImage[i+1][j+1];
				 int P5 = binaryImage[i][j+1];
				 int P6 = binaryImage[i-1][j+1];
				 int P7 = binaryImage[i-1][j];
				 int P8 = binaryImage[i-1][j-1];
				 
				 int sum = P1 + P2 + P3 + P4 + P5 + P6 + P7 + P8;
				 
				 if((P1 == 1 && P3 == 1 && P6 != 1) ||
					(P3 == 1 && P5 == 1 && P8 != 1) ||
					(P5 == 1 && P7 == 1 && P2 != 1) ||
					(P7 == 1 && P1 == 1 && P4 != 1)) {
//					 TandaiHapus.add(new Point(i, j));
					 binaryImage[i][j] = 0;
				 }
			 }
		 }
		 
		 // Delete all queued pixels for deletion
//		 for(int i=0; i<TandaiHapus.size(); i++){
//			 binaryImage[(int)TandaiHapus.get(i).getX()][(int)TandaiHapus.get(i).getY()] = 0;
//		 }
//		 TandaiHapus.clear();
		 
		 // Set thinning result to source image
		 for (int i = 0; i < imgSrc.getWidth(); i++) {
	            for (int j = 0; j < imgSrc.getHeight(); j++) {
	                if (binaryImage[i][j] == 0){ 
	                	imgSrc.setRGB(i, j, new Color(255,255,255).getRGB());
	                } else {
	                	imgSrc.setRGB(i, j, new Color(0,0,0).getRGB());
	                }
	            }
		 }
		
	 return imgSrc;
	}
}
	




