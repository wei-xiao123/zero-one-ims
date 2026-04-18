package io.github.bluemiaomiao.autoconfigure;

import io.github.bluemiaomiao.service.FastdfsClientService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastdfsClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FastdfsClientService.class)
    public FastdfsClientService fastdfsClientService() {
        return new InMemoryFastdfsClientService();
    }
}
