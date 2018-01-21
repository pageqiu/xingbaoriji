package cn.pageqiu.util;

import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;

public class ImageTest {

	public static void main(String[] args) {
	      try {
	            // zoomImage(100,80,"/Users/pageqiu/Desktop/src.jpg","/Users/pageqiu/Desktop/desc.jpg");
	            addWatermarksByText("/Users/pageqiu/Desktop/src.jpg","/Users/pageqiu/Desktop/desc.jpg");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	}
	
    public static void zoomImage(Integer width, Integer height, String srcPath, String newPath) throws Exception {   
    	ProcessStarter.setGlobalSearchPath("/usr/local/bin/");
    	IMOperation op = new IMOperation();   
        op.addImage(srcPath);
        if(width == null){//根据高度缩放图片 
            op.resize(null, height);     
        }else if(height == null){//根据宽度缩放图片 
            op.resize(width, null); 
        }else { 
            op.resize(width, height); 
        }
        op.addImage(newPath);
        //这里不加参数或者参数为false是使用ImageMagick，true是使用GraphicsMagick
        ConvertCmd convert = new ConvertCmd();
//        convert.setSearchPath("g:/tool/graphicsmagick-1.3.20-q16");
//        convert.setSearchPath("G:/tool/ImageMagick-6.9.0-Q16");
        convert.run(op);   
    }
    
    /** 
     * 图片水印 
     * 
     * @param srcImagePath   源图片 
     * @param waterImagePath 水印 
     * @param destImagePath  生成图片 
     * @param gravity  图片位置 
     * @param dissolve 水印透明度 
     */  
    public static void waterMark(String waterImagePath, String srcImagePath, String destImagePath, String gravity, int dissolve) {  
        IMOperation op = new IMOperation();  
        op.gravity(gravity); //位置center：中心;northwest：左上;southeast：右下 
        op.dissolve(dissolve); //水印清晰度 ，0-100  最好设置高点要不看起来没效果 
        op.addImage(waterImagePath);  
        op.addImage(srcImagePath);  
        op.addImage(destImagePath);  
        CompositeCmd cmd = new CompositeCmd();  
        try {  
            cmd.run(op);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
    }  
    
    private static void addWatermarksByText(String srcPath, String tarPath) throws Exception {  
    	ProcessStarter.setGlobalSearchPath("/usr/local/bin/");
    	IMOperation op= new IMOperation();  
    	// op.font("/Library/Fonts/Songti.ttc").gravity("southeast").pointsize(18).fill("#BCBFC8").draw("text 5,5 '中文不乱码'");  
    	 op.font("宋体").gravity("southeast").pointsize(18).fill("#BCBFC8").draw("text 5,5 '中文不乱码'");  
    	op.addImage();  
    	op.addImage();  
    	ConvertCmd convert = new ConvertCmd(false);  
    	// linux下不要设置此值，不然会报错  
    	// convert.setSearchPath("D:\\Program Files\\ImageMagick-6.8.8-Q8\\");  
    	convert.run(op, srcPath, tarPath);  
    	} 

}
