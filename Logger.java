
import java.io.*;
public class Logger {
	static BufferedWriter bw;
	static {
		try{
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("log.txt")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String txt)
	{
		try{
			bw.write(txt);
			bw.newLine();
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readFile(String file)
	{
		String tmp = "";
		try {
			
		FileInputStream fis = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s = "";
        tmp = br.readLine();
        while ((s = br.readLine()) != null) {
            tmp += "\n" + s;
        }
		fis.close();
		return tmp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
