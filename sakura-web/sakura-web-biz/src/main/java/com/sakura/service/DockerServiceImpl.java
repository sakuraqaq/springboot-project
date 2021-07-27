package com.sakura.service;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.jaxrs.JerseyDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.sakura.dto.DockerClineDTO;
import com.sakura.enums.DockerStatus;
import com.sakura.websocket.MyChannelHandlerPool;
import com.sakura.websocket.SockerServer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class DockerServiceImpl implements DockerService{


    private final String prefix = "正在执行:";
    @Override
    public void buildImages(DockerClineDTO dockerClineDTO) throws Exception {
        SockerServer sockerServer = new SockerServer(8081);

        String baseDir = "D:\\";
        //1204251807
        //sakuraqaq123
        CredentialsProvider provider = new UsernamePasswordCredentialsProvider(dockerClineDTO.getGitUserName(), dockerClineDTO.getGitPassword());
        try {
            //git下载代码
            //https://codeup.aliyun.com/dnic/core/cim-dynamic.git
            //master
            File file = new File(baseDir);
            Git git = Git.cloneRepository()
                    .setURI(dockerClineDTO.getGitUrl())
                    .setDirectory(file)
                    .setCredentialsProvider(provider)
                    .setCloneSubmodules(true)
                    .setBranch(dockerClineDTO.getBranch())
                    .call();
            git.getRepository().close();
            git.close();//关闭
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        //用于push验证
        //"registry.cn-hangzhou.aliyuncs.com"
        //得林可科技
        //Dnic%123
        //tcp://localhost:2375
        AuthConfig authConfig = new AuthConfig();
        authConfig.withRegistryAddress(dockerClineDTO.getDockerUrl())
                .withUsername(dockerClineDTO.getDockerUserName())
                .withPassword(dockerClineDTO.getDockerPassWord());

        //根据host创建dockerClient
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerClineDTO.getDockerHost())
                .withRegistryUrl(dockerClineDTO.getDockerUrl())
                .withRegistryUsername(dockerClineDTO.getDockerUserName())
                .withRegistryPassword(dockerClineDTO.getGitPassword())
                .build();

        //创建DocketHttpClient
        DockerHttpClient httpClient = new JerseyDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();

        //最后初始化DocketClient
        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);

        //根据文件路径构建镜像
        String dockerfile = baseDir + File.separator + "docker" + File.separator + "pom" + File.separator + "Dockerfile";
        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd();
        AuthConfigurations authConfigurations = new AuthConfigurations(); //添加账号认证
        authConfigurations.addConfig(authConfig);
        buildImageCmd.withBuildAuthConfigs(authConfigurations);
        buildImageCmd.withBaseDirectory(new File(baseDir));
        buildImageCmd.withDockerfile(new File(dockerfile));

        //registry.cn-hangzhou.aliyuncs.com/cim-registry/cim-dynamic:stable
        Set<String> tags = new HashSet<>();
        tags.add(dockerClineDTO.getTagName());
        buildImageCmd.withTags(tags); //设置tag
        File file = new File("./docker/DockerLog.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BuildImageResultCallback callback = new BuildImageResultCallback() {

            private String stream = "";
            private final List<String> status = new ArrayList<>();

            @Override
            public void onStart(Closeable stream) {
            }

            public void onNext(BuildResponseItem item) {
                if (item.getStatus() != null) {
                    try {
                        String text = getStatus(item.getStatus()) + "Id:" + item.getId();
                        if (!status.contains(text)) {
                            String status = prefix + text + "----------------" + simpleDateFormat.format(Calendar.getInstance().getTime());
                            fileOutputStream.write(status.getBytes(StandardCharsets.UTF_8));
                            //websocket发送信息
                            MyChannelHandlerPool.sendAllMessage(status);
                        }
                        status.add(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (item.getErrorDetail() != null) {
                    try {
                        System.out.print("error" + item.getErrorDetail().getMessage());
                        fileOutputStream.write(("error" + item.getErrorDetail().getMessage()).getBytes(StandardCharsets.UTF_8));
                        //websocket发送信息
                        MyChannelHandlerPool.sendAllMessage("error" + item.getErrorDetail().getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (item.getStream() != null && !item.getStream().equals("")) {
                    try {
                        stream += item.getStream();
                        if (stream.contains("\n")) {
                            stream = stream.substring(0, stream.length() - 1);
                            String suffix = "";
                            if(!stream.contains(DockerStatus.MAVEN_INFO.getCode())){
                                suffix = (stream.substring(stream.lastIndexOf(".") + 1));
                            }
                            if (!stream.contains(DockerStatus.MAVEN_PROGRESS.getCode())
                                    && !stream.contains(DockerStatus.MAVEN_DOWNLOADING.getCode())
                                    && !stream.contains(DockerStatus.MAVEN_DOWNLOADED.getCode()) && !suffix.equals("jar")) {
                                fileOutputStream.write(stream.getBytes(StandardCharsets.UTF_8));
                                //websocket发送信息
                                MyChannelHandlerPool.sendAllMessage(stream);
                            }
                            stream = "";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                super.onNext(item);
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    System.out.print("error" + throwable.getMessage());
                    fileOutputStream.write(("error" + throwable.getMessage()).getBytes(StandardCharsets.UTF_8));
                    //websocket发送信息
                    MyChannelHandlerPool.sendAllMessage("error" + throwable.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.onError(throwable);
            }

            @Override
            public void onComplete() {
                try {
                    fileOutputStream.write(("构建完成").getBytes(StandardCharsets.UTF_8));
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    //websocket发送信息
                    MyChannelHandlerPool.sendAllMessage(dockerClineDTO.getTagName() + "构建完成");

//                    PushImageCmd pushImageCmd = dockerClient.pushImageCmd(tags.toArray(new String[0])[0]);
//                    pushImageCmd.withAuthConfig(authConfig);
//                    pushImageCmd.exec(new ResultCallback<PushResponseItem>() {
//                        @Override
//                        public void onStart(Closeable closeable) {
//
//                        }
//
//                        @Override
//                        public void onNext(PushResponseItem object) {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            System.out.println(throwable.getMessage());
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            System.out.println("push success");
//                        }
//
//                        @Override
//                        public void close() throws IOException {
//
//                        }
//                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.onComplete();
            }

        };
        buildImageCmd.exec(callback);
        sockerServer.start();
    }

    public String getStatus(String status) {

        for (DockerStatus value : DockerStatus.values()) {
            if (value.getCode().contains(status)) {
                return value.getMessage();
            }
        }
        return status;
    }
}
