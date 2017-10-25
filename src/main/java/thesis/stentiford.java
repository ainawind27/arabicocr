package thesis;

public class stentiford {

	public static int boolToInt (boolean b){
		return b ? 1:0;
	}
	public static void main (String[] args){
		boolean [] matrix = {false, true, false,
							false, true, false,
							true, false, false				
							};
		System.out.println(boolToInt(!matrix[1]&&(matrix[2] || matrix[3]))+
		boolToInt(!matrix[3]&&(matrix[4] || matrix[5]))+
		boolToInt(!matrix[5]&&(matrix[6] || matrix[7]))+
		boolToInt(!matrix[7]&&(matrix[8] || matrix[1])));
		}
	}

