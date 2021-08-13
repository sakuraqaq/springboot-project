package com.sakura.oss;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class OssService {


    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private final String endpoint = "";
    private final String bucketName = "";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    private final String accessKeyId = "";
    //只有在创建子用户Accesskey的时候查看到
    private final String accessKeySecret = "";

    private String objectName;
    private String applicationName;

    private OSSClient ossClient;


    public Map<String, String> getUploadSTS(String roleSessionName, String fileName, boolean isTemp) {
        setObjectName(fileName, isTemp);
        //获取的角色ARN 在RAM角色管理中
        String roleArn = "";
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:PutObject\",\n" +
                "                 \"oss:GetObject\",\n" +
                "                  \"oss:DeleteObject\",\n" +
                "                   \"oss:ListPart\",\n" +
                "                   \"oss:AbortMultipartUpload\",\n" +
                "                   \"oss:ListObjects\",\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:" + bucketName + "/" + objectName + "\", \n" +
                "                 \"acs:oss:*:*:" + bucketName + "\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile(endpoint, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            request.setDurationSeconds(3600L);//设置临时访问凭证的有效时间为3600秒
            final AssumeRoleResponse assumeRoleResponse = client.getAcsResponse(request);
            AssumeRoleResponse.Credentials credentials = assumeRoleResponse.getCredentials();
            Map<String, String> map = new HashMap<>();
            map.put("accessKeyId", credentials.getAccessKeyId());
            map.put("accessKeySecret", credentials.getAccessKeySecret());
            map.put("securityToken", credentials.getSecurityToken());
            map.put("Expiration", credentials.getExpiration());
            return map;
        } catch (Exception ignored) {
            return null;
        }
    }


    /**
     * 上传文件
     *
     * @param file     本地上传文件
     * @param fileName md5+后缀(无后缀不加)
     */
    public void uploadFile(File file, String fileName, boolean isTemp) {
        getUploadSTS("", fileName, isTemp);
        initOssClient();
        setObjectName(fileName, isTemp);
        try {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);
            //初始化分片
            InitiateMultipartUploadResult result = this.ossClient.initiateMultipartUpload(request);
            //返回uploadId
            String uploadId = result.getUploadId();

            //partETag的集合
            List<PartETag> partETags = new ArrayList<>();
            //每个分片大小，用于计算文件有多少个分片
            final long partSize = 1024 * 1024L; //1mb
            long fileLength = file.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            //遍历分片上传
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                InputStream inputStream = new FileInputStream(file);
                //跳过已经上传的分片
                inputStream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setInputStream(inputStream);
                //设置分片大小 除了最后一个分片没有限制大小，其他的分片最小为100kb
                uploadPartRequest.setPartSize(curPartSize);
                //设置分片号，每一个上传的分片都是一个分片号，取值范围为1-10000
                uploadPartRequest.setPartNumber(i + 1);
                //每个分片不需要按顺序上传，甚至可以在不同客户端上传，oss 会按照分片号排序组成完整的文件
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                //每次上传分片之后 OSS的返回结果包含PartETag 将保存在PartETags中
                partETags.add(uploadPartResult.getPartETag());
            }

            //在执行分片上传后 ，提供所有有效的partETag 然后会验证所有分片没问题就组合成一个完整文件
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);
            ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 断电续传下载
     *
     * @param filePath 文件路径
     */
    public void downloadFile(String filePath, String fileName, boolean isTemp) {
        initOssClient();
        setObjectName(fileName, isTemp);
        try {
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, objectName);
            downloadFileRequest.setDownloadFile(filePath);
            downloadFileRequest.setPartSize(1024 * 1024);
            downloadFileRequest.setTaskNum(10);
            downloadFileRequest.setEnableCheckpoint(true);
            DownloadFileResult downloadFileResult = ossClient.downloadFile(downloadFileRequest);
            downloadFileResult.getObjectMetadata();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 获取文件连接
     *
     * @param fileName 文件名
     * @param isTemp   是否临时文件
     */
    public String getFileUrl(String fileName, boolean isTemp) {
        initOssClient();
        setObjectName(fileName, isTemp);
        try {
            //创建请求
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName);
            // 设置签名URL过期时间为3600秒（1小时）。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000);
            request.setMethod(HttpMethod.PUT);
            request.setExpiration(expiration);
            //生成URL
            URL url = ossClient.generatePresignedUrl(request);
            return url.toString();
        } finally {
            ossClient.shutdown();
        }
    }

    public void deleteFile(String fileName, boolean isTemp) {
        initOssClient();
        setObjectName(fileName, isTemp);
        try {
            ossClient.deleteObject(bucketName, objectName);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 设置文件目录
     *
     * @param fileName 文件名
     * @param isTemp   是否是临时文件
     */
    public void setObjectName(String fileName, boolean isTemp) {
        this.objectName = (isTemp ? "temp" : this.applicationName) + File.separator + getFileSuffix(fileName) + File.separator + fileName;
    }

    public String getFileSuffix(String fileName) {
        if (fileName.equals("")) {
            return fileName;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //使用OSS域名新建OSSClient
    private void initOssClient() {
        this.ossClient = (OSSClient) new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
