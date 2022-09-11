package cn.itsource.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.oss
 * @Author: Director
 * @CreateTime: 2022-09-05  10:26
 * @Description: TODO
 * @Version: 1.0
 */
public class UploadTest {

    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-chengdu.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tHC1wBEt4RZVY7bhXGx";
        String accessKeySecret = "YeqQrW6FfrRoNBWH3RXeJnn3VXKX25";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "java0412-itsource";
        // 就是上传过去之后文件的名称
        String objectName = "img.png";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // C:\Users\blank\Desktop\aaaaaa.jpg
        try {
            //String content = "Hello OSS";
            //ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
            ossClient.putObject(bucketName, objectName, new File("C:\\Users\\blank\\Desktop\\aaaaaa.jpg"));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
