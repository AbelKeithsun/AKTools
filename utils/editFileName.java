
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 用来批量修改文件名称
 * @author 自阳
 *
 */
public class editFileName{
	public static void main(String[] args) {
		String mkPath = "F:\\my\\mouse\\0716生产";
		List<String> readFileList = getFileName(mkPath, "");
		for(String selectInfo:readFileList) {
			int index = selectInfo.indexOf('.');
//			System.out.println(selectInfo);
			String meat = selectInfo.substring(0, index);
			String[] meats = meat.split(" ");
			String firstEle = meats[0];
			String secondEles = meats[1];
			String date = secondEles.substring(0, 8);
			String threeEle = meats[2];
//			System.out.println(date);
			String newFileName = "政企-"+firstEle+"-"+date+"-"+readFileList.size()/2+"-x-"+threeEle+".txt";
			File oldFile = new File(mkPath+"\\"+selectInfo);
			File newFileName1 = new File(mkPath+"\\"+newFileName);
			System.out.println(oldFile.getPath()+"\n");
			if(oldFile.renameTo(newFileName1)) {
				System.out.println("execute success");;
			}else {
				System.out.println("execute fail");
			}
			System.out.println(newFileName);
		}
	}
	
	/**
	 * 指定文件夹地址,获取要进行解析的文件名称
	 * @param path
	 * @return 
	 */
	private static List<String> getFileName(String mkdirPath,String matchRule){
		File file = new File(mkdirPath);
		String[] fileList = file.list();
		List<String> readFileList = new ArrayList<String>();
		if(!matchRule.isEmpty()) {
			for (String fileName : fileList) {
				if (fileName.matches(matchRule)) {
					readFileList.add(fileName);
				}
			}
		}else {
			for (String fileName : fileList) {
				readFileList.add(fileName);
			}
		}
		return readFileList;
	}
}
