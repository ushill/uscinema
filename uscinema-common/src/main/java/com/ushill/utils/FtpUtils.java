package com.ushill.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/3/31 下午11:33
 */

@Component
public class FtpUtils {

    /**
     * FTP 登录用户名
     */
    private static String userName;

    @Value("${ftp.username}")
    public void setUserName(String userName) {
        FtpUtils.userName = userName;
    }

    /**
     * FTP 登录密码
     */
    private static String passWord;

    @Value("${ftp.password}")
    public void setPassWord(String passWord) {
        FtpUtils.passWord = passWord;
    }

    /**
     * FTP 服务器地址IP地址
     */
    private static String hostName;

    @Value("${ftp.host}")
    public void setHostName(String hostName) {
        FtpUtils.hostName = hostName;
    }

    /**
     * FTP 端口
     */
    private static int port;

    @Value("${ftp.port}")
    public void setPort(int port) {
        FtpUtils.port = port;
    }

    /**
     * 文件服务器路径
     */
    private static String path;

    @Value("${ftp.path}")
    public void setPath(String path) {
        FtpUtils.path = path;
    }

    /**
     * 文件服务器绝对路径头
     */
    private static String header;

    @Value("${ftp.header}")
    public void setHeader(String header) {
        FtpUtils.header = header;
    }


    /**
     * 关闭连接-FTP方式
     *
     * @param ftp FTPClient对象
     * @return boolean
     */
    public static boolean closeFTP(FTPClient ftp) {
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 上传文件-FTP方式
     *
     * @return boolean
     * @throws Exception
     */
    public static FtpRetCode uploadFile(String partPath, byte[] decoderBytes, String imgName) throws Exception {
        FTPClient ftp = new FTPClient();
        try {
            //FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_L8);
            //conf.setServerLanguageCode("zh");
            //ftp.setControlEncoding("UTF-8");

            ftp.connect(hostName, port);
            ftp.enterLocalPassiveMode();  // 被动模式
            ftp.login(userName, passWord);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                return FtpRetCode.CONNECTION_FAILED;
            }

            String realPath = path + partPath + "/";
            //realPath = new String(realPath.getBytes("UTF-8"),"iso-8859-1"); //中文路径支持
            //imgName = new String(imgName.getBytes("UTF-8"),"iso-8859-1");  //中文文件名支持

            boolean changeWD = ftp.changeWorkingDirectory(realPath); // cd 到待存目录 不存在则创建
            if (!changeWD) {
                if (!CreateDirecroty(realPath, ftp)) {
                    return FtpRetCode.CREATE_DIRECTORY_FAILED;
                }
            }

            //同名文件更名，不使用，返回列表过大影响性能
            //FTPFile[] fs = ftp.listFiles(); // 得到远端目录的相应文件列表
            //imgName = FtpUtils.changeName(fileName, fs);

            if(!ftp.setFileType(FTP.BINARY_FILE_TYPE)){
                return FtpRetCode.SET_FILE_TYPE_FAILED;
            }

            //上传文件
            if(!ftp.storeFile(imgName, new ByteArrayInputStream(decoderBytes))){
                return FtpRetCode.UPLOAD_FILE_FAILED;
            }

            if(!ftp.logout()){
//                return FtpRetCode.LOGOUT_FAILED;
                System.out.println(FtpRetCode.LOGOUT_FAILED.getMessage());
            }

            return FtpRetCode.OK;

        } catch (Exception e) {
            throw e;
        } finally {
            if(!closeFTP(ftp)){
                System.out.println(FtpRetCode.DISCONNECT_FAILED.getMessage());
            }
        }
    }

    /**
     * 判断是否有重名文件
     *
     * @param fileName
     * @param fs
     * @return
     */
    public static boolean isFileExist(String fileName, FTPFile[] fs) {
        for (int i = 0; i < fs.length; i++) {
            FTPFile ff = fs[i];
            if (ff.getName().equals(fileName)) {
                return true; // 如果存在返回 正确信号
            }
        }
        return false; // 如果不存在返回错误信号
    }

    /**
     * 根据重名判断的结果 生成新的文件的名称
     *
     * @param fileName
     * @param fs
     * @return
     */
    public static String changeName(String fileName, FTPFile[] fs) {
        int n = 0;
        // fileName = fileName.append(fileName);
        while (isFileExist(fileName.toString(), fs)) {
            n++;
            String a = "[" + n + "]";
            int b = fileName.lastIndexOf(".");// 最后一出现小数点的位置
            int c = fileName.lastIndexOf("[");// 最后一次"["出现的位置
            if (c < 0) {
                c = b;
            }
            StringBuffer name = new StringBuffer(fileName.substring(0, c));// 文件的名字
            StringBuffer suffix = new StringBuffer(fileName.substring(b + 1));// 后缀的名称
            fileName = name.append(a) + "." + suffix;
        }
        return fileName.toString();
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public static boolean CreateDirecroty(String remote, FTPClient ftp) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !ftp.changeWorkingDirectory(new String(directory))) {

            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end));
                if (!ftp.changeWorkingDirectory(subDirectory)) {
                    if (ftp.makeDirectory(subDirectory)) {
                        ftp.changeWorkingDirectory(subDirectory);
                    } else {
                        System.out.println("创建目录失败");
                        success = false;
                        return success;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    public enum FtpRetCode {
        OK(0, "操作成功"),
        CONNECTION_FAILED(1, "连接FTP服务器失败"),
        CREATE_DIRECTORY_FAILED(2, "创建远程文件夹失败"),
        SET_FILE_TYPE_FAILED(3, "文件格式设置失败"),
        UPLOAD_FILE_FAILED(4, "上传文件失败"),
        LOGOUT_FAILED(5, "登出失败"),
        DISCONNECT_FAILED(6, "断连失败"),
        MAX(100, "-");

        private int code;
        private String message;

        FtpRetCode(int code, String message){
            this.code = code;
            this.message = message;
        }

        public String getMessage(){
            return this.message;
        }

        public int getCode(){
            return this.code;
        }
    }
}
