package thesis;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import model.Point;
import javax.imageio.ImageIO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;

public class FeatureExtraction {
	
	public List<File> listFiles (File folder){
		List<File> queuedFiles = new ArrayList<File>();
		
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
				queuedFiles.add(file);
			}
		}
		return queuedFiles;
	}
	
	public void extractfeatures (File folder, List<File> queuedFiles) throws IOException{
		Features features = new Features();
		for (File inputFile : queuedFiles) {
			// T_Binerisasi_dan_chaincode_1.binerisasi("D:\\lam.PNG", 127);
			System.out.println("Processing " + inputFile + "...");
			Segment segment = new Segment();
			segment.setName(inputFile.getName());
			int[][] chaincode = T_Binerisasi_dan_chaincode_1.binerisasi(inputFile.getPath(), 127);
			ChainCode c = new ChainCode();
			List<ChainInfo> chains = c.chain3(chaincode);
			segment.getChains().addAll(chains);
			System.out.println(chains);
			int dotPos = 0;
			int dotCount = 0;
			String bodyChain = null;
			for (int i = 0; i < chains.size(); i++) {
				if (chains.get(i).chain.length() >= 17 &&
						(null == bodyChain || chains.get(i).chain.length() > bodyChain.length())) {
					bodyChain = chains.get(i).chain;
				} else {
					dotCount++;
					dotPos = chains.get(i).yPos;
				}
			}
			segment.setDotPos(dotPos);
			segment.setDotCount(dotCount);
			segment.setBodyChain(bodyChain);
			System.out.println("Dot count: " + dotCount);
			System.out.println("Dot position: " + dotPos);
			System.out.println("Body chain code: " + bodyChain);
			
			Normalisasi normalisasi = new Normalisasi();
			segment.setNormalizedBodyChain(normalisasi.normalizedFinish(bodyChain));
			
			String[] splitted = inputFile.getName().split("[_.]");
			if (splitted.length == 5) {
				int labelId = LABELS.indexOf(splitted[1]);
				if (-1 != labelId) {
					segment.setLabelId(labelId);
					segment.setLabel(LABELS.get(labelId));
				}
			}
			
			features.getSegments().add(segment);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(features);
		System.out.println("Features.json:");
		System.out.println(json);
		File outputFile = new File(folder, folder.getName() + ".Features.json");
		System.out.println("Writing output to " + outputFile);
		mapper.writeValue(outputFile, features);
	}
	
	public void extractFeatures(File folder) throws IOException{
		List<File> queuedFiles = listFiles(folder);
		extractfeatures(folder, queuedFiles);
	}
	
	static final List<String> LABELS = ImmutableList.of(
			"ain", "alif", "ba", "dal", "dhad", 
			"dzal", "dzo", "fa", "ghoin", "hamzah",
			"ha", "habesar", "jim", "kaf", "kha",
			"lam", "mim", "nun", "qaf", "ra", "sad",
			"sheen", "sin", "tamarbuto", "ta", "tho",
			"tsa", "wau", "ya", "za"
			);			

	static class Features {
		List<Segment> segments = new ArrayList<>();

		public List<Segment> getSegments() {
			return segments;
		}
	}
	
	static class Segment {
		String name;
		List<ChainInfo> chains = new ArrayList<>();
		String bodyChain;
		int[] normalizedBodyChain;
		int dotPos;
		int dotCount;
		String label;
		int labelId;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public List<ChainInfo> getChains() {
			return chains;
		}

		public String getBodyChain() {
			return bodyChain;
		}

		public void setBodyChain(String bodyChain) {
			this.bodyChain = bodyChain;
		}

		public int[] getNormalizedBodyChain() {
			return normalizedBodyChain;
		}

		public void setNormalizedBodyChain(int[] normalizedBodyChain) {
			this.normalizedBodyChain = normalizedBodyChain;
		}

		public int getDotPos() {
			return dotPos;
		}

		public void setDotPos(int dotPos) {
			this.dotPos = dotPos;
		}

		public int getDotCount() {
			return dotCount;
		}

		public void setDotCount(int dotCount) {
			this.dotCount = dotCount;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getLabelId() {
			return labelId;
		}

		public void setLabelId(int labelId) {
			this.labelId = labelId;
		}
		
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

		public String getChain() {
			return chain;
		}

		public void setChain(String chain) {
			this.chain = chain;
		}

		public int getyPos() {
			return yPos;
		}

		public void setyPos(int yPos) {
			this.yPos = yPos;
		}

		@Override
		public String toString() {
			return "ChainInfo [chain=" + chain + ", yPos=" + yPos + "]";
		}
	}
	
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
		File folder = new File("D:\\filetesting\\zhangsuen");
		FeatureExtraction featureextraction = new FeatureExtraction();
		featureextraction.extractFeatures(folder);
	}
	
	static class ChainCode {

		String getString(int[][] input) {
			String vis = "";
			for (int y = 0; y < input.length; y++) {
				for (int x = 0; x < input[y].length; x++) {
					vis += input[y][x] == 1 ? '1' : ' ';
				}
				vis += "\n";
			}
			return vis;
		}

		static class Fork {
			String backwardChain = "";
			int x, y;
			
			public Fork(int x, int y) {
				super();
				this.x = x;
				this.y = y;
			}
		}

		List<ChainInfo> chain3(int[][] input) {
			List<ChainInfo> results = new ArrayList<>();
			// copy input array ke working array
			int[][] working = new int[input.length][input[0].length];
			for (int y = 0; y < input.length; y++) {
				for (int x = 0; x < input[y].length; x++) {
					working[y][x] = input[y][x];
				}
			}
			while (true) {
				System.out.println("Working State " + results.size() + ":");
				String vis = getString(working);
				System.out.print(vis);
				
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
				System.out.println("First pixel: ["+ p.getY() + "," + p.getX() +"]");

				if (p.getY() < working.length * 2 / 5) {
					yPos = 0;
				} else if (p.getY() < working.length * 3 / 5) {
					yPos = 1;
				} else {
					yPos = 2;
				}
				Point next = p; // bikin titik namanya next isinya sama dengan p
				int x = p.getX(), y = p.getY();// ambil titik koordinat p

				Deque<Fork> forks = new ArrayDeque<>();
				while (!done) { // ketika done true maka berhenti
					vis = getString(working);
					
					// System.out.println(x + " " + y);
					int[] n = neighbors(working, next);
					int total = sumIntArray(n);

					if (total == 0) {
						working[y][x] = 0; // hapus pixel
						// bila ada fork, maka kembali
						if (!forks.isEmpty()) {
							Fork fork = forks.pop();
							System.out.println("Joining from [" + y + "," + x +
									"] to [" + fork.y + "," + fork.x + "] chain: " + 
									result + " + " + fork.backwardChain);
							result += fork.backwardChain;
							x = fork.x;
							y = fork.y;
							next = new Point(x, y);
						} else {
							// bila tidak ada fork, maka chaincode ini selesai
							result += "0";
							done = true;
						}
					} else {
						Deque<Integer> nextDirections = new ArrayDeque<Integer>();
						for (int i = 0; i < n.length; i++) {
							if (n[i] == 1) {
								// tambahkan semua neighbor yang ada
								nextDirections.addLast(i + 1);
							}
						}
						int direction = nextDirections.pop();
						
						// bila bercabang, maka ingat Fork baru ke dalam stack
						if (!nextDirections.isEmpty()) {
							System.out.println("Forking at [" + y + "," + x + "] chain: " + result);
							forks.push(new Fork(x, y));
						}

						result += "" + direction;
						// bila ada fork, maka tambahkan chain mundur
						if (!forks.isEmpty()) {
							int backwardDir = (((direction - 1) + 4) % 8) + 1;
							forks.peekLast().backwardChain = backwardDir + forks.peekLast().backwardChain;
						}

						working[y][x] = 0; // hapus pixel
						next = decider(next, direction); // fungsi decider untuk
															// menggerakkan
						x = next.getX();
						y = next.getY();
					}
				}
				
				// add chaincode to results
				System.out.println("Detected chain " + results.size() + ": " + result);
				results.add(new ChainInfo(result, yPos));
			}
			return results;
		}

		// finds the first foreground pixel that has only one neighbor
		// if there is none, take the first foreground pixel you meet
		private Point findFirstPixel(int[][] input) {
			Point result = null;
			boolean firstPixelFound = false;

			for (int y = 0; y < input.length; y++) {
				for (int x = 0; x < input[y].length; x++) {
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
		
		private int safeGet(int[][] input, int y, int x) {
			if (0 <= y && y < input.length && 0 <= x && x < input[y].length) {
				return input[y][x];
			} else {
				return 0;
			}
		}

		private int[] neighbors(int[][] input, Point p) { // mencari tetangga
			int x = p.getX();
			int y = p.getY();
			int[] result = new int[] { // arah tetangga dijelaskan dicatatan
										// aina
					safeGet(input, y - 1, x - 1),
					safeGet(input, y - 1, x),
					safeGet(input, y - 1, x + 1),
					safeGet(input, y, x + 1),
					safeGet(input, y + 1, x + 1),
					safeGet(input, y + 1, x),
					safeGet(input, y + 1, x - 1),
					safeGet(input, y, x - 1) 
				};

			return result;
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
