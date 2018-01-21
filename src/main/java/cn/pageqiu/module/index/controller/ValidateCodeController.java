package cn.pageqiu.module.index.controller;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import cn.pageqiu.util.IdentifyingCode;

@Controller
@SessionAttributes("codeImge")
public class ValidateCodeController {
	
	private Random random = new Random();  
	
//	static {
//		UPLOAD_PATH = JournalingController.class.getClassLoader().getResource("").getPath();
//		log.error("==========path=========="+UPLOAD_PATH);
//	}
    
    @RequestMapping(value="/getCode", method=RequestMethod.GET)
    public @ResponseBody void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	
    	//设置不缓存图片  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "No-cache");  
        response.setDateHeader("Expires", 0) ;  
        //指定生成的相应图片  
        response.setContentType("image/jpeg") ; 
        
    	IdentifyingCode idCode = new IdentifyingCode();  
        BufferedImage image =new BufferedImage(idCode.getWidth() , idCode.getHeight() , BufferedImage.TYPE_INT_BGR) ;  
        Graphics2D g = image.createGraphics() ;  
        //定义字体样式  
        Font myFont = new Font("黑体" , Font.BOLD , 16) ;  
        //设置字体  
        g.setFont(myFont) ;  
          
        g.setColor(idCode.getRandomColor(200 , 250)) ;  
        //绘制背景  
        g.fillRect(0, 0, idCode.getWidth() , idCode.getHeight()) ;  
          
        g.setColor(idCode.getRandomColor(180, 200)) ;  
        
        idCode.drawRandomLines(g, 3) ;  
        String code = idCode.drawRandomString(4, g) ; 
        request.getSession().setAttribute("checkCode", code);
        g.dispose() ;  
        ImageIO.write(image, "JPEG", response.getOutputStream()) ;

    }
    
    
    @RequestMapping(value="/checkCode", method=RequestMethod.GET)
    public @ResponseBody String checkCode(HttpServletRequest request,@RequestParam("code") String checkCode) throws IOException{
    	String valiedCode = (String)request.getSession().getAttribute("checkCode");
    	

    	if (valiedCode.equalsIgnoreCase(checkCode)) {
    		request.getSession().setAttribute("codeflg", "0");
    		return "{\"flg\":\"1\"}";
    	} else {
    		return "{\"flg\":\"0\"}";
    	}

    }
    
    private int checkImageType(String name) {
    	
    	if(name ==null) {
    		return -1;
    	}
    	
    	if (name.endsWith(".jpg")||name.endsWith(".jpeg")||name.endsWith(".png")){
    		return name.lastIndexOf(".");
    	}
    	return -1;
    }
    
//    @RequestMapping(value="/journaling", method=RequestMethod.POST)
//    public @ResponseBody String journaling(HttpServletRequest request,
//            @RequestParam("file") MultipartFile file,@RequestParam("journaling") String text,HttpSession session){
//    	
//    	String codeflg = (String)request.getSession().getAttribute("codeflg");
//    	
//    	if(!"0".equals(codeflg)) {
//    		return "请输入正确的验证码";
//    	}
//    	
//    	String name = null;
//    	String orginName = null;
//    	String type = null;
//    	
//    	Diary diary = new Diary();
//    	
//    	diary.setContext(text);
//    	diary.setUserId(((User)session.getAttribute("user")).getId());
//
//    	BaseValueUtil.setCreateBaseEntityValue(diary);
//    	int num = diaryService.addDiary(diary);
//    	
//    	int diaryId= diary.getDiaryId();
//    	
//    	log.error("----diaryId----"+diaryId);
//
//        if (!file.isEmpty()) {
//            try {
//            	orginName = file.getOriginalFilename();
//            	
//            	int index = checkImageType(orginName);
//            	
//            	if(index<0){
//            		return "图片类型不正确（jpg，jpeg,png）";
//            	}
//            	
//            	type = file.getContentType();
//                byte[] bytes = file.getBytes();
//                //String name= ((User)session.getAttribute("user")).getId()
//                BufferedOutputStream stream =
//                        new BufferedOutputStream(new FileOutputStream(new File(UPLOAD_PATH+diaryId+orginName.substring(index))));
//                stream.write(bytes);
//                stream.close();
//                return "You successfully uploaded  "+orginName+"--type-"+type;
//            } catch (Exception e) {
//                return "You failed to upload " + name + " => " + e.getMessage();
//            }
//        } else {
//            return "You failed to upload " + name + " because the file was empty.";
//        }
//    }

    

//
//    @RequestMapping(value="/upload", method=RequestMethod.POST)
//    public @ResponseBody String handleFileUpload(
//            @RequestParam("file") MultipartFile file){
//    	String name = null;
//    	String orginName = null;
//    	String type = null;
//        if (!file.isEmpty()) {
//            try {
//            	orginName = file.getOriginalFilename();
//            	type = file.getContentType();
//                byte[] bytes = file.getBytes();
//               // log.error("-----------path-----------"+UPLOAD_PATH+orginName);
//                BufferedOutputStream stream =
//                        new BufferedOutputStream(new FileOutputStream(new File(UPLOAD_PATH+orginName)));
//                stream.write(bytes);
//                stream.close();
//                return "You successfully uploaded  "+orginName+"--type-"+type;
//            } catch (Exception e) {
//                return "You failed to upload " + name + " => " + e.getMessage();
//            }
//        } else {
//            return "You failed to upload " + name + " because the file was empty.";
//        }
//    }
    
    @RequestMapping(value="/gotoJournaling", method=RequestMethod.GET)
    public String gotoJournaling() {
        return "journaling";
    }

}
