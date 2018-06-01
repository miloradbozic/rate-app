package io.milo.rateapp.repository.user;

import io.milo.rateapp.model.User;

import java.io.IOException;
import java.util.Map;

public interface UserRepository {
    User create(User user) throws IOException;
    User getById(String id) throws IOException;
}
