
import java.io.*;
import java.util.*;

public class OpeningBook {
	
	FileInputStream fis;
	BufferedReader br;
	
	public OpeningBook()
	{
		try{
			
		fis = new FileInputStream("JChessBook.txt");
		br = new BufferedReader(new InputStreamReader(fis));
		br.mark(100000);
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	public String start(String str)
	{
        Vector<String> moves = new Vector<String>();
		try{
		br.reset();
		br.mark(10000000);
		String s="";
		while ((s = br.readLine()) != null)
		{
			if (s.startsWith(str))
			{
                moves.add(s.substring(str.length(), str.length()+4));
				//return s.substring(str.length(), str.length()+4);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
        if (moves.size() == 0) {
            return null;
        } else {
            Random rand = new Random();
            return moves.get(rand.nextInt(moves.size()));
        }
	}
}