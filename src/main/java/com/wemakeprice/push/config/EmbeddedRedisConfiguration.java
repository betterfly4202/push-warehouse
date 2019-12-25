package com.wemakeprice.push.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.IntStream;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */

@Slf4j
@Profile("dev")
@Configuration
public class EmbeddedRedisConfiguration {
    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedisServer() throws Exception {
        final int port = isRedisRunning() ? findByAvailablePort() : redisPort;
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedisServer(){
        if(redisServer != null){
            redisServer.stop();
        }
    }

    public int findByAvailablePort(){
        return IntStream.range(10_000, 63_535)
                .filter(port -> {
                            try {
                                return isRunning(executeGrepProcessCommandLine(port));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return false;
                        })
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommandLine(redisPort));
    }

    private Process executeGrepProcessCommandLine(Integer port) throws IOException {
        String command = String.format("netstat -nat grep LISTEN|grep %d", port);
        String [] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private boolean isRunning(Process process) throws IOException {
        String line;
        StringBuilder processBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            while((line = br.readLine()) != null){
                processBuilder.append(line);
            }
        }

        return !StringUtils.isEmpty(processBuilder.toString());
    }

}
