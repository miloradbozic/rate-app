package io.milo.rateapp.core.repository;

import java.io.IOException;

public interface IndexRepository {
    void create() throws IOException;
    void delete() throws IOException;
}
