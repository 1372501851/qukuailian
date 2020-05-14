package com.saiyun.util;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;


public class QRCodeUtil {
private static final String CHARSET = "utf-8";
private static final String FORMAT_NAME = "JPG";
// 二维码尺寸
private static final int QRCODE_SIZE = 300;
// LOGO宽度
private static final int WIDTH = 60;
// LOGO高度
private static final int HEIGHT = 60;

private static BufferedImage createImage(String content, String imgPath,
        boolean needCompress) throws Exception {
    Hashtable hints = new Hashtable();
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
    hints.put(EncodeHintType.MARGIN, 1);
    BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
            BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
    int width = bitMatrix.getWidth();
    int height = bitMatrix.getHeight();
    BufferedImage image = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                    : 0xFFFFFFFF);
        }
    }
    if (imgPath == null || "".equals(imgPath)) {
        return image;
    }
    // 插入图片
    QRCodeUtil.insertImage(image, imgPath, needCompress);
    return image;
}


private static void insertImage(BufferedImage source, String imgPath,
        boolean needCompress) throws Exception {
    File file = new File(imgPath);
    if (!file.exists()) {
        System.err.println(""+imgPath+"   该文件不存在！");
        return;
    }
    Image src = ImageIO.read(new File(imgPath));
    int width = src.getWidth(null);
    int height = src.getHeight(null);
    if (needCompress) { // 压缩LOGO
        if (width > WIDTH) {
            width = WIDTH;
        }
        if (height > HEIGHT) {
            height = HEIGHT;
        }
        Image image = src.getScaledInstance(width, height,
                Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        src = image;
    }
    // 插入LOGO
    Graphics2D graph = source.createGraphics();
    int x = (QRCODE_SIZE - width) / 2;
    int y = (QRCODE_SIZE - height) / 2;
    graph.drawImage(src, x, y, width, height, null);
    Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
    graph.setStroke(new BasicStroke(3f));
    graph.draw(shape);
    graph.dispose();
}

//生产二维码

    /**
     *
     * @param content 图片文字类容
     * @param faceImg 嵌入图片路径
     * @param userid 用户的uid防止图片名字重复
     * @param destPath 保存路径
     * @param urlpath
     * @param needCompress
     * @return
     */
    public static String encode(String content, String faceImg,String userid, String destPath,String urlpath,
        boolean needCompress){
	try{

	    BufferedImage image = QRCodeUtil.createImage(content, faceImg,
	            needCompress);
	    mkdirs(destPath+urlpath);
	    String file = new Random().nextInt(99999999)+".png";
	    if(!(userid==null||userid.equals("")))
	    	file = userid+".png";
	    ImageIO.write(image, FORMAT_NAME, new File(destPath+urlpath+file));
	    return "/static/img"+urlpath+file;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}


public static void mkdirs(String destPath) {
    File file =new File(destPath);   
    //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
    if (!file.exists() && !file.isDirectory()) {
        file.mkdirs();
    }
}
public static void encode(String content, String faceImg,String userid, String destPath,String urlpath)
        throws Exception {
    QRCodeUtil.encode(content, faceImg,userid, destPath,urlpath,false);
}


public static void encode(String content, String destPath,String urlpath,
        boolean needCompress) throws Exception {
    QRCodeUtil.encode(content, null,"", destPath,urlpath, needCompress);
}


public static void encode(String content, String destPath) throws Exception {
    QRCodeUtil.encode(content, null, destPath, false);
}


public static void encode(String content, String imgPath,
        OutputStream output, boolean needCompress) throws Exception {
    BufferedImage image = QRCodeUtil.createImage(content, imgPath,
            needCompress);
    ImageIO.write(image, FORMAT_NAME, output);
}


public static void encode(String content, OutputStream output)
        throws Exception {
    QRCodeUtil.encode(content, null, output, false);
}


public static String decode(File file) throws Exception {
    BufferedImage image;
    image = ImageIO.read(file);
    if (image == null) {
        return null;
    }
    BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
            image);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    Result result;
    Hashtable hints = new Hashtable();
    hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
    result = new MultiFormatReader().decode(bitmap, hints);
    String resultStr = result.getText();
    return resultStr;
}


public static String decode(String path) throws Exception {
    return QRCodeUtil.decode(new File(path));
}


public static void main(String[] args) throws Exception {
//    String text = "https://minapps.letiqiu.com/html5/loginuser/18/10000084";
    String text = "http://www.cskc.com/?userid=57264943682302004";
   
	//生成二维码
	
    System.out.println(QRCodeUtil.encode(text, "D:\\tool\\qrcode\\2.jpg","57264943682302004", "d:/tool/","/qrcode/", true));
}



}