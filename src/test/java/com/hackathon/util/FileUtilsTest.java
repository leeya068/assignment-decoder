package com.hackathon.util;

import com.hackathon.exception.FileProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileUtils utility class.
 * Tests file operations, validation, and PDF extraction.
 */
@DisplayName("File Utils Tests")
class FileUtilsTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    @DisplayName("Should validate file size correctly")
    void testValidateFileSize_Success() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "test.pdf", 
            "application/pdf", 
            new byte[1024] // 1KB
        );
        
        // When/Then - should not throw
        assertDoesNotThrow(() -> 
            FileUtils.validateFileSize(file, 10)
        );
    }
    
    @Test
    @DisplayName("Should reject file exceeding size limit")
    void testValidateFileSize_TooLarge() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "large.pdf", 
            "application/pdf", 
            new byte[11 * 1024 * 1024] // 11MB
        );
        
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            FileUtils.validateFileSize(file, 10)
        );
    }
    
    @Test
    @DisplayName("Should validate file extension correctly")
    void testValidateFileExtension_Success() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "document.pdf", 
            "application/pdf", 
            new byte[100]
        );
        
        // When/Then - should not throw
        assertDoesNotThrow(() -> 
            FileUtils.validateFileExtension(file, ".pdf", ".doc")
        );
    }
    
    @Test
    @DisplayName("Should reject invalid file extension")
    void testValidateFileExtension_Invalid() {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "script.exe", 
            "application/octet-stream", 
            new byte[100]
        );
        
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            FileUtils.validateFileExtension(file, ".pdf", ".zip")
        );
    }
    
    @Test
    @DisplayName("Should create temporary directory")
    void testCreateTempDirectory() throws IOException {
        // When
        Path tempPath = FileUtils.createTempDirectory();
        
        // Then
        assertNotNull(tempPath);
        assertTrue(Files.exists(tempPath));
        assertTrue(Files.isDirectory(tempPath));
        
        // Cleanup
        FileUtils.deleteDirectory(tempPath.toFile());
    }
    
    @Test
    @DisplayName("Should delete directory recursively")
    void testDeleteDirectory() throws IOException {
        // Given
        Path testDir = tempDir.resolve("test-delete");
        Files.createDirectories(testDir);
        Files.createFile(testDir.resolve("file1.txt"));
        Files.createFile(testDir.resolve("file2.txt"));
        
        // When
        FileUtils.deleteDirectory(testDir.toFile());
        
        // Then
        assertFalse(Files.exists(testDir));
    }
    
    @Test
    @DisplayName("Should read Java files from directory")
    void testReadJavaFiles() throws IOException {
        // Given
        Path javaDir = tempDir.resolve("java-files");
        Files.createDirectories(javaDir);
        Files.writeString(javaDir.resolve("Test.java"), "public class Test {}");
        Files.writeString(javaDir.resolve("Main.java"), "public class Main {}");
        
        // When
        String content = FileUtils.readJavaFiles(javaDir.toFile());
        
        // Then
        assertNotNull(content);
        assertTrue(content.contains("Test.java"));
        assertTrue(content.contains("Main.java"));
        assertTrue(content.contains("public class"));
    }
    
    @Test
    @DisplayName("Should handle empty directory")
    void testReadJavaFiles_EmptyDirectory() throws IOException {
        // Given
        Path emptyDir = tempDir.resolve("empty");
        Files.createDirectories(emptyDir);
        
        // When
        String content = FileUtils.readJavaFiles(emptyDir.toFile());
        
        // Then
        assertNotNull(content);
        assertTrue(content.contains("No Java files found"));
    }
    
    @Test
    @DisplayName("Should save uploaded file")
    void testSaveUploadedFile() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
            "file", 
            "upload.txt", 
            "text/plain", 
            "Test content".getBytes()
        );
        
        // When
        File savedFile = FileUtils.saveUploadedFile(file, tempDir);
        
        // Then
        assertNotNull(savedFile);
        assertTrue(savedFile.exists());
        assertEquals("upload.txt", savedFile.getName());
        
        String content = Files.readString(savedFile.toPath());
        assertEquals("Test content", content);
    }
    
    @Test
    @DisplayName("Should throw exception for null file")
    void testSaveUploadedFile_NullFile() {
        assertThrows(IllegalArgumentException.class, () -> 
            FileUtils.saveUploadedFile(null, tempDir)
        );
    }
}

// Made with Bob
