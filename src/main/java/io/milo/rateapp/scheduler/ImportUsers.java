package io.milo.rateapp.scheduler;

import java.io.IOException;
import java.text.SimpleDateFormat;

import io.milo.rateapp.client.UinamesUserClient;
import io.milo.rateapp.model.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.milo.rateapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("users.scheduledTasks.run")
public class ImportUsers {

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ImportUsers.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void importSingle() {
        User user = UinamesUserClient.getClient().getSingle(); // @todo use dependency injection
        try {
            this.userRepository.create(user);
        } catch (IOException e) {
            log.error("Error while inserting user {} to ES:\n {}", user.toString(), e.getMessage());
        }
    }
}
