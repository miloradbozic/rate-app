package io.milo.rateapp.repository;

import io.milo.rateapp.model.User;

import java.io.IOException;

public interface UserRepository {
    void create(User user) throws IOException;
    User getById(String id) throws IOException;
}
