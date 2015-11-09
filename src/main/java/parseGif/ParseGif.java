package parseGif;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * The MIT License (MIT)

 Copyright (c) 2015 Ocean

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

/**
 *
 */
public class ParseGif {

    static final public String DEFAULT_CONFIG_PATH = "resource/config.properties";
    static final public String MAIN = "main";
    static final public String EXTENSION = "extension";


    private Properties propRead = null;
    private String sourcePath   = null;
    private String destination  = null;

    /**
     * 建構子
     */
    public ParseGif()throws IOException{
        super();
        readConfig();
    }

    /**
     * parse file name
     */
    public HashMap<String, String> parseFileName(String fullName){
        int startIndex = fullName.lastIndexOf(46) + 1;
        int endIndex = fullName.length();
        HashMap<String, String> fileName = new HashMap<String, String>();
        fileName.put(MAIN , fullName.substring(0, startIndex-1));
        fileName.put(EXTENSION, fullName.substring(startIndex, endIndex));
        return fileName;
    }

    /**
     * 讀取 config.property
     */
    protected void readConfig()throws IOException{
        InputStream content = null;
        try{
            propRead = new Properties();
            content = new FileInputStream(DEFAULT_CONFIG_PATH);
            propRead.load(content);
            content.close();
            sourcePath = propRead.getProperty("sourcePath");
            destination = propRead.getProperty("destination");
        }catch (IOException ex){
            System.out.print(ex.getMessage());
            if(content != null){
                content.close();
            }
        }
    }

    /**
     *
     */
    public void convertGif(String sourceFullName, String destinationFullName)throws IOException{
            System.out.println("source = " + sourceFullName);
            System.out.println("dest = " + destinationFullName);
            BufferedImage img = null;
            File sourceFile = new File(sourcePath, sourceFullName);
            File outFile = new File(destination, destinationFullName);
            BufferedImage sourceImg = null;
            try {
                sourceImg = ImageIO.read(sourceFile);
                int width = sourceImg.getWidth();
                int height = sourceImg.getHeight();
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics g = img.getGraphics();
                g.drawImage(sourceImg, 0, 0, null);
                ImageIO.write(img, "gif", outFile);
                sourceImg.flush();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                if (sourceImg != null) {
                    sourceImg.flush();
                }
            }
            System.out.println("-----------");
    }

    public void parseGif(String sourceFullName, String destinationName)throws IOException{
        HashMap<String, String> nameMap = parseFileName(sourceFullName);
        String extendName = nameMap.get(EXTENSION);
        if(extendName.equals("png") || extendName.equals("jpg") || extendName.equals("gif")) {
            convertGif(sourceFullName, destinationName);
        }

    }

    /**
     * 指定轉換圖片
     */
    public void parseGif(String sourceFullName)throws IOException{
        HashMap<String, String> nameMap =  parseFileName(sourceFullName);
        String fileName = nameMap.get(MAIN);
        parseGif(sourceFullName, fileName.concat(".gif"));
    }

    /**
     * 不指定，轉換所有圖片
     */
    public void parseGif()throws IOException{
        File folder = new File(sourcePath);
        String list[] = folder.list();
        for(int i=0; i < list.length ; i++){
            String fullName = list[i];
            HashMap<String, String> nameMap =  parseFileName(fullName);
            String fileName = nameMap.get(MAIN);
            parseGif(fullName, fileName.concat(".gif"));
        }
    }

    /**
     *
     */
    public static void main( String[] args ) throws IOException{
        ParseGif parse = new ParseGif();
        if(args.length >= 2) {
            System.out.println("parse git start ....");
            System.out.println("........................");
            String sourceFullName = args[0].trim();
            String destinationFullName = args[1].trim();
            parse.parseGif(sourceFullName, destinationFullName);
            System.out.println("........................");
            System.out.println("parse gif end ....");
        }else if (args.length >= 1){
            System.out.println("parse git start ....");
            System.out.println("........................");
            String sourceFullName = args[0].trim();
            parse.parseGif(sourceFullName);
            System.out.println("........................");
            System.out.println("parse gif end ....");
        }else{
            System.out.println("parse git start ....");
            System.out.println("........................");
            parse.parseGif();
            System.out.println("........................");
            System.out.println("parse gif end ....");
        }
    }
}
