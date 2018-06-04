package io.milo.rateapp.core.repository;

import io.milo.rateapp.core.model.User;
import java.io.IOException;

public interface UserRepository {
    User create(User user) throws IOException;
    User getById(String id) throws IOException;
}
