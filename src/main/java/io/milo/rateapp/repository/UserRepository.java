package io.milo.rateapp.repository;

import io.milo.rateapp.model.User;

import java.io.IOException;

public interface UserRepository {
    User getById(String id) throws IOException;
    void create(User user) throws IOException;
}
