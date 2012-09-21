package org.isma.web.common.tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class RefreshWebFilesTool {
    public static final String ROOT_PROJECT = "E:/dev/projects/isma-j2ee/";

    public static void main(String[] args) throws IOException {
        RefreshWebFilesTool tool = new RefreshWebFilesTool();
        tool.refreshModule("isma-troc");
        tool.refreshModule("isma-fridge");
        tool.refreshModule("isma-versus");
        tool.refreshModule("isma-ws-server-samples");
        tool.refreshModule("isma-ajax-samples");
        tool.refreshModule("isma-poker-web");
    }

    private void refreshModule(String moduleName) throws IOException {
        System.out.println("------------------------------------------------------------");
        System.out.println("REFRESHING MODULE " + moduleName);
        System.out.println("------------------------------------------------------------");
        String webModulePath = String.format("%s/%s/", ROOT_PROJECT, moduleName);
        String sourcePath = String.format("%s/src/main/webapp/", webModulePath);
        String targetPath = String.format("/%s/target/%s/", webModulePath, moduleName);
        refresh(new File(sourcePath), new File(targetPath));
    }

    private void refresh(File sourceDir, File targetDir) throws IOException {
        for (File sourceFile : sourceDir.listFiles()) {
            copyRecursively(sourceFile, targetDir);
        }
    }

    private void copyRecursively(File sourceFile, File targetDir) throws IOException {
        System.out.printf("copyRecursively src=%s, dst=%s\n", sourceFile.getAbsolutePath(), targetDir.getAbsolutePath());
        if (!targetDir.isDirectory()) {
            throw new IOException("targetDir '" + targetDir.getAbsolutePath() + "' is not a valid directory");
        }
        if (sourceFile.isFile()) {
            //System.out.printf("\tcopyFile src=%s, dst=%s\n", sourceFile.getAbsolutePath(), targetDir.getAbsolutePath());
            File newTargetFile = new File(targetDir, sourceFile.getName());
            if (newTargetFile.exists()) {
                FileUtils.forceDelete(newTargetFile);
            }
            FileUtils.copyFile(sourceFile, newTargetFile);
        } else {
            File newTargetSubDir = new File(targetDir, sourceFile.getName());
            if (!newTargetSubDir.exists() && !newTargetSubDir.mkdir()) {
                throw new IOException("cannot create directory '" + newTargetSubDir.getAbsolutePath() + "'");
            }
            //System.out.printf("\tcreate directory %s\n", newTargetSubDir.getAbsolutePath());
            for (File sourceSubFile : sourceFile.listFiles()) {
                copyRecursively(sourceSubFile, newTargetSubDir);
            }
        }
    }
}
