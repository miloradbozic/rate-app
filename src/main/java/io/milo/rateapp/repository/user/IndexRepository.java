package io.milo.rateapp.repository.user;

import io.milo.rateapp.model.User;

import java.io.IOException;

public interface IndexRepository {
    void create() throws IOException;
    void delete() throws IOException;
}
