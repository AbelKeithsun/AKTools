

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 从文件中筛选除特定数据，将统计结果汇总输出到规定的excel表格中
 * @author 自阳
 *
 */
public class selectSpecified {
	public static void main(String[] args) throws DocumentException {
		String matchRule = "control-(\\w+).xml";//输入文件名匹配规则
		String mkdirPath = "D:\\company\\asiainfo\\mavenlibs\\git\\ngesop\\src\\main\\resources\\config";//输入文件地址
		String resultFileName = "F:\\my\\result.xls";
		List<String> readFileList = getFileName(mkdirPath,matchRule);//获得需要转化的文件名列表
		List<Map<String,Object>> pathUrlList = null;
		try {
			pathUrlList = getDataOfXML(readFileList, mkdirPath);//获得目标数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeExcel(pathUrlList,resultFileName);//将数据写入目标文档
		
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
		for (String fileName : fileList) {
			if (fileName.matches(matchRule)) {
				readFileList.add(fileName);
			}
		}
		return readFileList;
	}

	/**
	 * 通过文件地址，获取解析 XML 文件，获取 XML 文件中指定的元素文本
	 * @param readFileList
	 * @param mkdirPath
	 * @return
	 * @throws DocumentException 
	 */
	private static List<Map<String,Object>> getDataOfXML(List<String> readFileList,String mkdirPath) throws DocumentException{
		List<Map<String,Object>> pathUrlList = new ArrayList<Map<String,Object>>();
		SAXReader reader = new SAXReader();
		for (String readFile : readFileList) {
			Document document = reader
					.read(new File(mkdirPath+"\\"+readFile));
			Element root = document.getRootElement();
			System.out.println("根节点是：" + root.getName());
			List<Element> list = root.elements();
			// 遍历子节点
			for (Element element : list) {
				System.out.println("子节点是：" + element.getName());
				Element sencond = element;
				List<Element> third = sencond.elements();
				String path = sencond.attributeValue("path");
				System.out.println("path:" + path);
				
				for (Element element2 : third) {
					System.out.println(element.getName() +"的子节点是："+element2.getName());
					String uid = element2.attributeValue("uid");
					String serviceClass = element2.attributeValue("service");
					String describe = element2.attributeValue("desc");
					System.out.println("uid:"+ uid
					+","+"service:"+element2.attributeValue("service")+","+"method:"+element2.attributeValue("method"));
					System.err.println(path+"?uid="+uid);
					String urlOrgin = path+"?uid="+uid;
					Map<String, Object> urlEnd = new HashMap<String, Object>();
					urlEnd.put("serviceClass", serviceClass);
					urlEnd.put("path", urlOrgin);
					urlEnd.put("describe", describe);
					pathUrlList.add(urlEnd);
				}
			}
		}
		return pathUrlList;
	}
	
	/**
	 * 将数据写入excel表格中
	 * @param data
	 */
	private static void writeExcel(List<Map<String, Object>> data,String resultFileName) {
		//创建一个 workbook 对应一个excel
		HSSFWorkbook workbook = new HSSFWorkbook();
		//在 workbook 中创建一个 sheet 对应 excel 中的sheet
		HSSFSheet sheet1 = workbook.createSheet("firstPage");
		//在 sheet 表中添加表头第0行，老版本的poi对sheet的行列有限制
		HSSFRow row = sheet1.createRow(0);
		//创建单元格，设置表头
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("sevice");
		cell = row.createCell(1);
		cell.setCellValue("URL");
		cell = row.createCell(2);
		cell.setCellValue("功能描述");
		// 写入实体数据
		for(int j=0;j<data.size();j++) {
			HSSFRow row1 = sheet1.createRow(j + 1);
            Map<String, Object> map=data.get(j);
            row1.createCell(0).setCellValue((String)map.get("serviceClass"));
            row1.createCell(1).setCellValue((String)map.get("path"));
            row1.createCell(2).setCellValue((String)map.get("describe"));
		}
		//将文件保存到指定的位置
        try {
            FileOutputStream fos = new FileOutputStream(resultFileName);
            workbook.write(fos);
            System.out.println("写入成功");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 通过字节的方式直接解析一切种类文件需要配置匹配规则，解码类型等
	 * @param readFileList
	 * @param mkdirPath
	 * @param EncodingType
	 * @return
	 * @throws Exception
	 */
	private static List<Map<String, Object>> getDataforAllfile(List<String> readFileList, String mkdirPath,String EncodingType) throws Exception {
		List<Map<String, Object>> resultCollection = null;
		for(String readFile:readFileList) {
			FileInputStream fis = new FileInputStream(mkdirPath+File.separator+readFile);
			InputStreamReader isr = new InputStreamReader(fis, EncodingType);
			String meat = "";
			int changePace = 0;
			int pace;
			int endPace;
			int d = -1;
			while ((d = isr.read()) != -1) {
				meat += (char) d;
			}
			int lastEnd = meat.lastIndexOf("path=");
			do {
				pace = meat.indexOf("path=\"", changePace);
				endPace = meat.indexOf("\"", pace + 6);
				changePace = endPace + 6;
				System.out.println(meat.substring(pace + 6, endPace));
				String path = meat.substring(pace + 6, endPace);
				Map<String, Object> element = new HashMap<String, Object>(); 
				element.put("path", path);
				resultCollection.add(element);
			} while (pace != lastEnd);
			isr.close();
		}
		return resultCollection;
	}
}
