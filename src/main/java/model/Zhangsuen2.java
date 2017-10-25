package model;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import image.BinaryImageShell;
import image.MainbodySOSet;
import image.segmentator.SegmentatorChar;
import image.segmentator.SegmentatorSubword;

public class Zhangsuen2 {

	// Atribure
	Point[] Pos;
	List<Point> TandaiHapus;
	int[][] binaryImage;
	int widthImg;
	int heightImg;
	BufferedImage imgSrc;

	public Zhangsuen2(BufferedImage imgSrc, String name) {
		// create binaryImage
		this.imgSrc = imgSrc;
		widthImg = imgSrc.getWidth();
		heightImg = imgSrc.getHeight();
		// int threshold = OtsuThreshold.getThreshold(imgSrc);

		binaryImage = new int[widthImg][heightImg];
		System.out.println("Binarized " + name + "...");
		for (int i = 0; i < widthImg; i++) {
			for (int j = 0; j < heightImg; j++) {
				if (new Color(imgSrc.getRGB(i, j)).getRed() <= 150 || new Color(imgSrc.getRGB(i, j)).getGreen() <= 150
						|| new Color(imgSrc.getRGB(i, j)).getBlue() <= 150) {
					binaryImage[i][j] = 1;
				} else {
					binaryImage[i][j] = 0;
				}
				// System.out.print(binaryImage[i][j]);
			}
			// System.out.println();
		}
//		try {
//			String fileLocation = "D:\\filetestingsegmentation\\hasilthinning\\a\\";
//			File folder = new File(fileLocation);
//			File targetFolder = new File(folder, "binerz");
//			File thinnedFile = new File(fileLocation, name);
//
//			BufferedImage image = new BufferedImage(binaryImage.length, binaryImage[0].length,
//					BufferedImage.TYPE_3BYTE_BGR);
//
//			for (int i = 0; i < binaryImage.length; i++) {
//				for (int j = 0; j < binaryImage[i].length; j++) {
//					System.out.print(binaryImage[i][j]);
//					if (binaryImage[i][j] == 0) {
//						image.setRGB(i, j, Color.white.getRGB());
//					} else {
//						image.setRGB(i, j, Color.black.getRGB());
//					}
//				}
//				System.out.println();
//			}
//			ImageIO.write(image, "png", thinnedFile);
//		} catch (IOException e) {
//
//		}

		// create LokasiTetangga

		Pos = new Point[8];
		Pos[0] = new Point(1, 0);
		Pos[1] = new Point(1, -1);
		Pos[2] = new Point(0, -1);
		Pos[3] = new Point(-1, -1);
		Pos[4] = new Point(-1, 0);
		Pos[5] = new Point(-1, 1);
		Pos[6] = new Point(0, 1);
		Pos[7] = new Point(1, 1);
	}

	public boolean endPoint(Point a) {
		// Cek Jumlah Piksel sekitar Piksel
		int TetanggaHitam = 0;
		for (int i = 0; i < Pos.length; i++) {
			if (safeGet(binaryImage, a.x + (int) Pos[i].getX(), a.y + (int) Pos[i].getY()) == 1)
				TetanggaHitam += 1;
			if (TetanggaHitam > 1)
				return false;
		}
		if (TetanggaHitam == 1)
			return true;
		return false;
	}

	public int InterCon(Point a) {
		int[] tmp = new int[8];
		for (int i = 0; i < Pos.length; i++)
			tmp[i] = safeGet(binaryImage, a.x + (int) Pos[i].getX(),a.y + (int) Pos[i].getY());

		return (tmp[0] - (tmp[0] * tmp[1] * tmp[2])) + (tmp[2] - (tmp[2] * tmp[3] * tmp[4]))
				+ (tmp[4] - (tmp[4] * tmp[5] * tmp[6])) + (tmp[6] - (tmp[6] * tmp[7] * tmp[0]));
	}
	
//	public int boolToInt (boolean b){
//		return b ? 1:0;
//	}
//	public int InterCon (Point a){
//		boolean [] matrix = new boolean [8];
//		for (int i = 0; i < Pos.length; i++)
//			if(safeGet(binaryImage, a.x + (int) Pos[i].getX(),a.y + (int) Pos[i].getY())==1)
//				matrix[i]=true;
//			else 
//				matrix[i]=false;
//		return boolToInt(!matrix[0]&&(matrix[1] || matrix[2]))+
//			 boolToInt(!matrix[2]&&(matrix[3] || matrix[4]))+
//			 boolToInt(!matrix[4]&&(matrix[5] || matrix[6]))+
//			 boolToInt(!matrix[6]&&(matrix[7] || matrix[0]));
//	}
	
	/**
	 * Return image[x][y] if within boundaries, else return 0.
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @return
	 */
	private int safeGet(int[][] image, double x, double y) {
		if (x >= 0 && y >= 0 && x < image.length && y < image[(int) x].length) {
			return image[(int) x][(int) y];
		} else {
			return 0;
		}
	}

	public BufferedImage doZhangSuen() {
		TandaiHapus = new ArrayList<Point>();
		Point Titik_Sekarang;
		boolean stop;
		int sl = 0;

		do {
			// T1 dari atas kiri kebawah sebelah kanan 
			stop = true;
			for (int y = 0; y < imgSrc.getHeight() ; y++) {
				for (int x = 0; x < imgSrc.getWidth() ; x++) {

					Titik_Sekarang = new Point(x, y);
					// cek titik sekarang apakah hitam
					if (binaryImage[(int) Titik_Sekarang.getX()][(int) Titik_Sekarang.getY()] == 1) {

						if (safeGet(binaryImage, Titik_Sekarang.getX() + 1, Titik_Sekarang.getY()) == 1
								&& safeGet(binaryImage, Titik_Sekarang.getX() - 1, Titik_Sekarang.getY()) == 0) {
							//
							if (InterCon(Titik_Sekarang) == 1 && !endPoint(Titik_Sekarang)) {
								TandaiHapus.add(new Point(Titik_Sekarang));
								stop = false;
							}
						}

					}
				}
			}
			for (int i = 0; i < TandaiHapus.size(); i++) {
				binaryImage[(int) TandaiHapus.get(i).getX()][(int) TandaiHapus.get(i).getY()] = 0;

			}
			TandaiHapus.clear();
			// T2 kanan atas kebawah sebelah kiri
			for (int x = imgSrc.getWidth() - 1; x >= 0; x--) {
				for (int y = 1; y < imgSrc.getHeight(); y++) {

					Titik_Sekarang = new Point(x, y);
					// cek apakah titik sekarng hitam
					if (binaryImage[(int) Titik_Sekarang.getX()][(int) Titik_Sekarang.getY()] == 1) {
						// template T2
						if (safeGet(binaryImage, Titik_Sekarang.getX(), Titik_Sekarang.getY() - 1) == 0
								&& safeGet(binaryImage, Titik_Sekarang.getX(), Titik_Sekarang.getY() + 1) == 1) {

							if (InterCon(Titik_Sekarang) == 1 && !endPoint(Titik_Sekarang)) {
								TandaiHapus.add(new Point(Titik_Sekarang));
								stop = false;
							}
						}

					}
				}
			}
			for (int i = 0; i < TandaiHapus.size(); i++) {
				binaryImage[(int) TandaiHapus.get(i).getX()][(int) TandaiHapus.get(i).getY()] = 0;
			}
			TandaiHapus.clear();

			// T3 kanan bawah keatas
			for (int x = imgSrc.getWidth() - 1; x >= 0; x--) {
				for (int y = imgSrc.getHeight() - 1; y >= 0; y--) {

					Titik_Sekarang = new Point(x, y);
					// Cek titik sekarang apakah hitam
					if (safeGet(binaryImage, Titik_Sekarang.getX(), Titik_Sekarang.getY()) == 1) {
						// Cek Template T3
						if (safeGet(binaryImage, Titik_Sekarang.getX() - 1, Titik_Sekarang.getY()) == 1
								&& safeGet(binaryImage, Titik_Sekarang.getX() + 1, Titik_Sekarang.getY()) == 0) {

							if (InterCon(Titik_Sekarang) == 1 && !endPoint(Titik_Sekarang)) {
								// menandai titik yang akan dihapus
								TandaiHapus.add(new Point(Titik_Sekarang));
								stop = false;
							}
						}

					}
				}
			}
			// hapus semua titik yang telah ditandai untuk di hapus
			for (int i = 0; i < TandaiHapus.size(); i++) {
				binaryImage[(int) TandaiHapus.get(i).getX()][(int) TandaiHapus.get(i).getY()] = 0;

			}
			TandaiHapus.clear();

			// T4 kiri bawah keatas
			for (int x = 0; x < imgSrc.getWidth() ; x++) {
				for (int y = imgSrc.getHeight() - 1; y >= 0; y--) {
					Titik_Sekarang = new Point(x, y);
					// cek apakah titik sekang titik hitam
					if (safeGet(binaryImage, Titik_Sekarang.getX(), Titik_Sekarang.getY()) == 1) {
						// cek template T4
						if (safeGet(binaryImage, Titik_Sekarang.getX(), Titik_Sekarang.getY() - 1) == 1
								&& safeGet(binaryImage, Titik_Sekarang.getX(), Titik_Sekarang.getY() + 1) == 0) {

							if (InterCon(Titik_Sekarang) == 1 && !endPoint(Titik_Sekarang)) {
								TandaiHapus.add(new Point(Titik_Sekarang));
								stop = false;
							}
						}

					}
				}
			}
			for (int i = 0; i < TandaiHapus.size(); i++) {
				binaryImage[(int) TandaiHapus.get(i).getX()][(int) TandaiHapus.get(i).getY()] = 0;
			}
			TandaiHapus.clear();

		} while (!stop);

		// Post-processing, delete all branching pixels
		for (int i = 0; i < imgSrc.getWidth(); i++) {
			for (int j = 0; j < imgSrc.getHeight(); j++) {
				int P1 = safeGet(binaryImage, i, j - 1);
				int P2 = safeGet(binaryImage, i + 1, j - 1);
				int P3 = safeGet(binaryImage, i + 1, j);
				int P4 = safeGet(binaryImage, i + 1, j + 1);
				int P5 = safeGet(binaryImage, i, j + 1);
				int P6 = safeGet(binaryImage, i - 1, j + 1);
				int P7 = safeGet(binaryImage, i - 1, j);
				int P8 = safeGet(binaryImage, i - 1, j - 1);

				if ((P1 == 1 && P3 == 1 && P6 != 1) || (P3 == 1 && P5 == 1 && P8 != 1)
						|| (P5 == 1 && P7 == 1 && P2 != 1) || (P7 == 1 && P1 == 1 && P4 != 1)) {
					// TandaiHapus.add(new Point(i, j));
					binaryImage[i][j] = 0;
				}
			}
		}

		// Delete all queued pixels for deletion
		for (int i = 0; i < TandaiHapus.size(); i++) {
			binaryImage[(int) TandaiHapus.get(i).getX()][(int) TandaiHapus.get(i).getY()] = 0;
		}
		TandaiHapus.clear();

		// Set thinning result to source image
		for (int i = 0; i < imgSrc.getWidth(); i++) {
			for (int j = 0; j < imgSrc.getHeight(); j++) {
				if (binaryImage[i][j] == 0) {
					imgSrc.setRGB(i, j, new Color(255, 255, 255).getRGB());
				} else {
					imgSrc.setRGB(i, j, new Color(0, 0, 0).getRGB());
				}
			}
		}

		return imgSrc;
	}
}
