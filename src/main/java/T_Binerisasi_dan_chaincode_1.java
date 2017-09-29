import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Point;
import javax.imageio.ImageIO;

public class T_Binerisasi_dan_chaincode_1 {

	static int[][] binerisasi(String fileInput, int threshold) {
		try {
			File input = new File(fileInput);
			BufferedImage image = ImageIO.read(input);
			int width = image.getWidth();
			int height = image.getHeight();
			int[][] gambar = new int[height][width];

			for (int j = 0; j < height; j++) {

				for (int i = 0; i < width; i++) {

					Color c = new Color(image.getRGB(i, j));
					int grayscale = (int) (c.getRed() * 0.299) + (int) (c.getBlue() * 0.114)
							+ (int) (c.getGreen() * 0.587);
					if (grayscale < threshold) {
						gambar[j][i] = 1; // 1 adalah nilai hitam
					} else {
						gambar[j][i] = 0; // 0 adalah nilai putih
					}
					System.out.print(gambar[j][i]);
				}
				System.out.println();
			}

			System.out.println();

			return gambar;

		} catch (Exception e) {
			return null;
		}
	}

	static public void main(String args[]) throws Exception {
		// T_Binerisasi_dan_chaincode_1.binerisasi("D:\\lam.PNG", 127);
		int[][] chaincode = T_Binerisasi_dan_chaincode_1.binerisasi("D:\\Thin\\dzo_terpisah2.PNG", 127);
		ChainCode c = new ChainCode();
		List<ChainInfo> chains = c.chain2(chaincode);
		System.out.println(chains);
		int dotPos = 0;
		int dotCount = 0;
		String bodyChain = null;
		for (int i = 0; i < chains.size(); i++) {
			if (chains.get(i).chain.length() >= 5) {
				bodyChain = chains.get(i).chain;
			} else {
				dotCount++;
				dotPos = chains.get(i).yPos;
			}
		}
		System.out.println("Dot count: " + dotCount);
		System.out.println("Dot position: " + dotPos);
		System.out.println("Body chain code: " + bodyChain);
		
		
	}
	
	static class ChainInfo {
		String chain;
		// 0 = di atas
		// 1 = di tengah
		// 2 = di bawah
		int yPos;
		
		public ChainInfo(String chain, int yPos) {
			super();
			this.chain = chain;
			this.yPos = yPos;
		}

		@Override
		public String toString() {
			return "ChainInfo [chain=" + chain + ", yPos=" + yPos + "]";
		}
		
	}

	static class ChainCode {

		@Deprecated
		String chain(int[][] input) {
			String result = "";
			boolean done = false; // selagi masih ada yg ditelusuri dia lanjut
									// terus
			Point p = findFirstPixel(input); // mencari titik hitam pertama,
												// buat objek p
												// yang isinya hasil dari fungsi
												// findFirstPixel

			if (p != null) {
				Point next = p; // bikin titik namanya next isinya sama dengan p
				int x = p.getX(), y = p.getY();// ambil titik koordinat p

				while (!done) { // ketika done true maka berhenti

					// System.out.println(x + " " + y);
					int[] n = neighbors(input, next);
					int total = sumIntArray(n);

					if (total == 0) {
						input[y][x] = 0;
						result += "0";
						done = true;
					} else {
						int direction = 0;

						for (int i = 0; i < n.length; i++) {
							if (n[i] == 1) {
								direction = i + 1;
								break;
							}
						}

						result += "" + direction;

						input[y][x] = 0;
						next = decider(next, direction); // fungsi decider untuk
															// menggerakkan
						x = next.getX();
						y = next.getY();
					}
				}
			}

			return result;
		}

		List<ChainInfo> chain2(int[][] input) {
			List<ChainInfo> results = new ArrayList<>();
			// copy input array ke working array
			int[][] working = new int[input.length][input[0].length];
			for (int y = 0; y < input.length; y++) {
				for (int x = 0; x < input[y].length; x++) {
					working[y][x] = input[y][x];
				}
			}
			while (true) {
				String result = "";
				int yPos = 0;
				boolean done = false; // selagi masih ada yg ditelusuri dia lanjut
										// terus
				Point p = findFirstPixel(working); // mencari titik hitam pertama,
													// buat objek p
													// yang isinya hasil dari fungsi
													// findFirstPixel
				if (null == p) {
					break;
				}

				if (p.getY() < working.length * 2 / 5) {
					yPos = 0;
				} else if (p.getY() < working.length * 3 / 5) {
					yPos = 1;
				} else {
					yPos = 2;
				}
				Point next = p; // bikin titik namanya next isinya sama dengan p
				int x = p.getX(), y = p.getY();// ambil titik koordinat p

				while (!done) { // ketika done true maka berhenti
					// System.out.println(x + " " + y);
					int[] n = neighbors(working, next);
					int total = sumIntArray(n);

					if (total == 0) {
						working[y][x] = 0; // hapus pixel
						result += "0";
						done = true;
					} else {
						int direction = 0;

						for (int i = 0; i < n.length; i++) {
							if (n[i] == 1) {
								direction = i + 1;
								break;
							}
						}

						result += "" + direction;

						working[y][x] = 0; // hapus pixel
						next = decider(next, direction); // fungsi decider untuk
															// menggerakkan
						x = next.getX();
						y = next.getY();
					}
				}
				
				// add chaincode to results
				results.add(new ChainInfo(result, yPos));
			}
			return results;
		}

		// finds the first foreground pixel that has only one neighbor
		// if there is none, take the first foreground pixel you meet
		private Point findFirstPixel(int[][] input) {
			Point result = null;
			boolean firstPixelFound = false;

			for (int y = 1; y < input.length - 1; y++) {
				for (int x = 1; x < input[y].length - 1; x++) {
					if (input[y][x] == 1) {
						// System.out.println(input[x][y]);
						int[] n = neighbors(input, new Point(x, y));
						int total = sumIntArray(n);
						// System.out.println(total);

						if (total == 1) {
							result = new Point(x, y);
							// System.out.println(x+ " " +y);
							firstPixelFound = true;

							break;
						}
					}
				}
				if (firstPixelFound) {
					break;
				}
			}

			if (!firstPixelFound) {
				for (int y = 1; y < input.length - 1; y++) {
					for (int x = 1; x < input[y].length - 1; x++) {
						if (input[y][x] == 1) {
							result = new Point(x, y);
							break;
						}
					}
					if (result != null) {
						break;
					}
				}
			}

			return result;
		}

		private int[] neighbors(int[][] input, Point p) { // mencari tetangga
			try {
				int x = p.getX();
				int y = p.getY();
				int[] result = new int[] { // arah tetangga dijelaskan dicatatan
											// aina
						input[y - 1][x - 1], input[y - 1][x], input[y - 1][x + 1], input[y][x + 1], input[y + 1][x + 1],
						input[y + 1][x], input[y + 1][x - 1], input[y][x - 1] };

				return result;
			} catch (Exception ex) {
				return new int[] {};
			}

		}

		private int sumIntArray(int[] input) { // menjumlahkan tetangga
			int result = 0;

			for (int i : input) {
				result += i;
			}

			return result;
		}

		private Point decider(Point p, int input) { // untuk menggerakkan
			Point result;
			int x = p.getX();
			int y = p.getY();

			switch (input) {
			case 1:
				x--;
				y--;

				break;
			case 2:
				y--;
				break;
			case 3:
				x++;
				y--;

				break;
			case 4:
				x++;
				break;
			case 5:
				x++;
				y++;

				break;
			case 6:
				y++;
				break;
			case 7:
				x--;
				y++;

				break;
			case 8:
				x--;
				break;
			}

			result = new Point(x, y);
			return result;
		}
	}
}
