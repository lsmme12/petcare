package com.pet.util;

import java.io.File;

/**
 * 📁 FileUtil
 * - 표준 Servlet API 기반 (cos.jar 필요 없음)
 * - 파일 삭제 기능만 유지
 */
public class FileUtil {

    /** ✅ 파일 삭제 */
    public static void deleteFile(String saveDir, String fileName) {
        if (fileName == null || fileName.isEmpty()) return;

        File file = new File(saveDir, fileName);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("🗑️ 파일 삭제 성공: " + fileName);
            } else {
                System.out.println("⚠️ 파일 삭제 실패: " + fileName);
            }
        }
    }
}
