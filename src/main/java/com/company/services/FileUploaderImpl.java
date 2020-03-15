package com.company.services;

import com.company.repositories.FileRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploaderImpl implements FileUploader {
    private String directory;
    private FileRepository fileRepository;
    private StorageFilenameGenerator storageFilenameGenerator;

    public FileUploaderImpl(String directory, FileRepository fileRepository, StorageFilenameGenerator storageFilenameGenerator) {
        this.directory = directory;
        this.fileRepository = fileRepository;
        this.storageFilenameGenerator = storageFilenameGenerator;
    }

    @Override
    public void uploadAndSaveToDb(MultipartFile multipartFile) {
        try {
            System.out.println("uploading");
            String storageFilename = storageFilenameGenerator.generateStorageFilename();
            String type = multipartFile.getContentType();
            Long size = multipartFile.getSize();
            multipartFile.transferTo(new File(directory + "/" + storageFilename + "." + type));
            System.out.println(directory + "/" + storageFilename + "." + type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
