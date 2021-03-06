package com.company.repositories;

import com.company.models.FileInfo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Optional;

public class FileJdbcTemplateImpl implements FileRepository{
    //language=sql
    private final String SQL_SELECT_BY_STORAGE_FILENAME = "SELECT * FROM files_urls WHERE storagefilename = ?";
    //language=sql
    private final String SQL_INSERT = "INSERT INTO files_urls (\"originalFilename\", id, storagefilename, type, size) VALUES (?,?,?,?,?,?)";

    private JdbcTemplate jdbcTemplate;

    private RowMapper<FileInfo> fileFindRowMapper = (row, rowNumber) -> {
        Long id = row.getLong("id");
        String storageFileName = row.getString("storageFilename");
        String originalFileName = row.getString("originalFilename");
        Long size = row.getLong("size");
        String type = row.getString("type");
        return new FileInfo(id, storageFileName, originalFileName, size, type);
    };

    public FileJdbcTemplateImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    public Optional<FileInfo> findByUrl(String url) {
        try {
            FileInfo item = jdbcTemplate.queryForObject(SQL_SELECT_BY_STORAGE_FILENAME, new Object[]{url}, fileFindRowMapper);
            return Optional.ofNullable(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(FileInfo entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT);
            statement.setString(1, entity.getOriginalFileName());
            statement.setLong(2, entity.getId());
            statement.setString(3, entity.getStorageFileName());
            statement.setString(4, entity.getType());
            statement.setLong(5, entity.getSize());
            return statement;
        }, keyHolder);
    }
}
