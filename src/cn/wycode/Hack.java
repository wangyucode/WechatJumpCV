package cn.wycode;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Hack extends Thread {

    private Controller controller;
    public boolean isStop;

    private static Random RANDOM = new Random();

    private static final float RATIO = 3.7f;

    public Hack(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        String root = Hack.class.getResource("/").getPath();
        String adbPath = root + "platform-tools/adb";
        File srcDir = new File(root, "imgs/input");
        srcDir.mkdirs();

        int count = 0;
        while (!isStop) {

            count++;
            try {
                File file = new File(srcDir, count + ".png");

                controller.appendText("正在拉取截图...\n");

//                Process process = Runtime.getRuntime().exec(adbPath + " shell /system/bin/screencap -p /sdcard/screenshot.png");
//                process.waitFor();
//                process = Runtime.getRuntime().exec(adbPath + " pull /sdcard/screenshot.png " + file.getAbsolutePath());
//                process.waitFor();


                if (file.exists()) {
                    controller.appendText("截图获取成功.\n");
                    Mat mat = Imgcodecs.imread(file.getAbsolutePath());
                    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.Canny(mat,mat,20,40);
                    Image img = Utils.mat2Image(mat);
                    controller.setImage(img);


//                    controller.appendText("正在计算人物中心点...\n");



//                    String adbCommand = adbPath + String.format(" shell input swipe %d %d %d %d %d", pressX, pressY, pressX, pressY, time);
//                    Runtime.getRuntime().exec(adbCommand);
                }else{
                    count=0;
                }
                int sleepTime = 500;
                controller.appendText("等待" + sleepTime + "ms后继续\n");
                if (isStop) {
                    break;
                }
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
                controller.appendText("出现异常："+e.getMessage()+"\n");
                break;
            }

        }
        controller.appendText("已停止!\n");
        controller.setStopped();
    }
}
