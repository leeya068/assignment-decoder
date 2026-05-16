package com.hackathon.util;

import com.hackathon.exception.FileProcessingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Utility class for file operations including PDF extraction and cleanup.
 */
public final class FileUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    
    private FileUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Extracts text content from a PDF file using Apache PDFBox.
     * 
     * @param pdfFile the PDF file to extract text from
     * @return extracted text content
     * @throws FileProcessingException if extraction fails
     */
    public static String extractTextFromPdf(MultipartFile pdfFile) {
        if (pdfFile == null || pdfFile.isEmpty()) {
            throw new IllegalArgumentException("PDF file cannot be null or empty");
        }
        
        String fileName = pdfFile.getOriginalFilename();
        logger.info("Extracting text from PDF: {}", fileName);
        
        try (InputStream inputStream = pdfFile.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            if (text == null || text.trim().isEmpty()) {
                throw new FileProcessingException(
                    "PDF appears to be empty or contains no extractable text", 
                    fileName
                );
            }
            
            logger.info("Successfully extracted {} characters from PDF", text.length());
            return text.trim();
            
        } catch (IOException e) {
            logger.error("Failed to extract text from PDF: {}", fileName, e);
            throw new FileProcessingException(
                "Failed to extract text from PDF: " + e.getMessage(), 
                fileName, 
                e
            );
        }
    }
    
    /**
     * Validates file size against maximum allowed size.
     * 
     * @param file the file to validate
     * @param maxSizeMB maximum allowed size in megabytes
     * @throws IllegalArgumentException if file exceeds size limit
     */
    public static void validateFileSize(MultipartFile file, long maxSizeMB) {
        if (file == null) {
            return;
        }
        
        long maxSizeBytes = maxSizeMB * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            throw new IllegalArgumentException(
                String.format("File size (%d bytes) exceeds maximum allowed (%d MB)", 
                    file.getSize(), maxSizeMB)
            );
        }
    }
    
    /**
     * Validates file extension.
     * 
     * @param file the file to validate
     * @param allowedExtensions allowed file extensions (e.g., ".pdf", ".zip")
     * @throws IllegalArgumentException if file extension is not allowed
     */
    public static void validateFileExtension(MultipartFile file, String... allowedExtensions) {
        if (file == null || file.getOriginalFilename() == null) {
            return;
        }
        
        String fileName = file.getOriginalFilename().toLowerCase();
        for (String ext : allowedExtensions) {
            if (fileName.endsWith(ext.toLowerCase())) {
                return;
            }
        }
        
        throw new IllegalArgumentException(
            String.format("Invalid file type. Expected: %s, Got: %s", 
                String.join(", ", allowedExtensions), 
                fileName)
        );
    }
    
    /**
     * Creates a temporary directory with timestamp.
     * 
     * @return the created directory path
     * @throws IOException if directory creation fails
     */
    public static Path createTempDirectory() throws IOException {
        String tempDir = AppConstants.TEMP_DIR_PREFIX + System.currentTimeMillis();
        Path path = Paths.get(tempDir);
        Files.createDirectories(path);
        logger.debug("Created temporary directory: {}", path);
        return path;
    }
    
    /**
     * Recursively deletes a directory and all its contents.
     * 
     * @param directory the directory to delete
     */
    public static void deleteDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            return;
        }
        
        try {
            Path path = directory.toPath();
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            logger.warn("Failed to delete file: {}", file.getAbsolutePath());
                        }
                    });
            }
            logger.debug("Deleted directory: {}", directory.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to delete directory: {}", directory.getAbsolutePath(), e);
        }
    }
    
    /**
     * Reads all Java files from a directory recursively.
     * 
     * @param directory the directory to read from
     * @return concatenated content of all Java files
     */
    public static String readJavaFiles(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return "// No existing code found";
        }
        
        StringBuilder content = new StringBuilder();
        File[] javaFiles = directory.listFiles((dir, name) -> name.endsWith(AppConstants.JAVA_EXTENSION));
        
        if (javaFiles != null && javaFiles.length > 0) {
            for (File file : javaFiles) {
                try {
                    content.append("// FILE: ").append(file.getName()).append("\n");
                    content.append(Files.readString(file.toPath())).append("\n\n");
                } catch (IOException e) {
                    logger.warn("Could not read file: {}", file.getName(), e);
                    content.append("// Could not read: ").append(file.getName()).append("\n");
                }
            }
        }
        
        return content.length() > 0 ? content.toString() : "// No Java files found";
    }
    
    /**
     * Saves uploaded file to specified directory.
     * 
     * @param file the file to save
     * @param targetDirectory the target directory
     * @return the saved file
     * @throws IOException if save operation fails
     */
    public static File saveUploadedFile(MultipartFile file, Path targetDirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        
        Files.createDirectories(targetDirectory);
        String fileName = file.getOriginalFilename();
        Path targetPath = targetDirectory.resolve(fileName);
        
        file.transferTo(targetPath.toFile());
        logger.info("Saved uploaded file: {}", targetPath);
        
        return targetPath.toFile();
    }
    
    /**
     * Extracts a ZIP file to a target directory.
     *
     * @param zipFile the ZIP file to extract
     * @param targetDirectory the directory to extract to
     * @throws IOException if extraction fails
     */
    public static void extractZipFile(File zipFile, File targetDirectory) throws IOException {
        if (zipFile == null || !zipFile.exists()) {
            throw new IllegalArgumentException("ZIP file does not exist");
        }
        
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }
        
        try {
            net.lingala.zip4j.ZipFile zip = new net.lingala.zip4j.ZipFile(zipFile);
            zip.extractAll(targetDirectory.getAbsolutePath());
            logger.info("Extracted ZIP file to: {}", targetDirectory.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to extract ZIP file: {}", zipFile.getName(), e);
            throw new IOException("Failed to extract ZIP file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Copies a directory and all its contents to a target location.
     *
     * @param source the source directory
     * @param target the target directory
     * @throws IOException if copy operation fails
     */
    public static void copyDirectory(Path source, Path target) throws IOException {
        if (!Files.exists(source)) {
            throw new IllegalArgumentException("Source directory does not exist: " + source);
        }
        
        Files.createDirectories(target);
        
        try (Stream<Path> walk = Files.walk(source)) {
            walk.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath,
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    logger.error("Failed to copy: {}", sourcePath, e);
                }
            });
        }
        
        logger.info("Copied directory from {} to {}", source, target);
    }
}

// Made with Bob
