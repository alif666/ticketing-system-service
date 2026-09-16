package com.pridesys.ticketing.attachment.service;
import org.springframework.web.multipart.MultipartFile; import java.io.*;
public interface FileStorage { String store(MultipartFile file) throws IOException; InputStream load(String key) throws IOException; void delete(String key) throws IOException; }
