package com.company.repositories;

import com.company.models.FileInfo;

public interface FileRepository {
    void save(FileInfo fileInfo);
}
